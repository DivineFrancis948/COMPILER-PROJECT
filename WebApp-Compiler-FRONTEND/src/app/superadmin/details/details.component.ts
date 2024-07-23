import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import Swal from 'sweetalert2'
import { RegistrationDTO } from 'src/app/models/registration-dto';
import { StatusCountModel } from 'src/app/models/status-count-model';
import { StatusCountService } from 'src/app/service/status-count.service';
import { SuperAdminService } from 'src/app/service/super-admin.service';
import * as fileSaver from 'file-saver';

declare var $: any;
declare var jQuery: any;
@Component({
  selector: 'app-details',
  templateUrl: './details.component.html',
  styleUrls: ['./details.component.css']
})
export class DetailsComponent implements OnInit {

  static obj: DetailsComponent;

  iTotalDisplayRecords: any
  userDatatable: any;
  statusCountList: StatusCountModel = new StatusCountModel();
  initialHead: string[] = ['USERNAME', 'FULL NAME', 'PHONE NUMBER','EMAIL','TOTAL MARKS','USERTYPE','STATUS','MCQ','PROGRAM'];
  constructor(private router: Router,private statusCountService: StatusCountService,private superAdminService: SuperAdminService,) { 
  
  }

  ngOnInit(): void 
  {
    DetailsComponent.obj = this;
    
    this.getUsers();
    
  }

  refreshByStatus(status: string)
  {
    //to clear all search when all is clicked
    if(status == ""){
      $("#userType").val("").trigger("change");
    }
    $("#idStatus").val(status).trigger("change");

    //to clear usertype if status is clicked
    // $("#userType").val("").trigger("change");
    this.search();
  }

  refreshByUserType(status: string)
  {
    $("#userType").val(status).trigger("change");
    this.search();
  }

  getUsers() {

    this.userDatatable = $('#userList').DataTable({
      "bProcessing": false,
      "bDeferRender": true,
      "ordering": false,
      bAutoWidth: false,
      bServerSide: true,
  
      sAjaxSource: "http://localhost:8085/register/search",
      // "iDisplayLength": 10,
      // "aLengthMenu": [[10, 25, 50, 100], [10, 25, 50, 100]],
      // "sPaginationType": "full_numbers",
      // "paging": true,
      "fnServerParams": function (aoData) {
        var dataString = DetailsComponent.obj.getSearchInputs();
        aoData.push({ name: "searchParam", value: dataString });
      },
      "fnRowCallback": (nRow, aData, iDisplayIndex, iDisplayIndexFull) => {
        $(nRow).on('click', (event) => {
          // Check if it's a double click
          if ($(this).data('lastClick') && (new Date().getTime() - $(this).data('lastClick') < 500)) {
            // It's a double click
            // Call your function here, passing relevant data
            // For example, you can get the userId from aData
            const userId = aData.userId;
            // Call your function with the userId
            // this.yourDoubleClickFunction();
          }
          // Update last click time
          $(this).data('lastClick', new Date().getTime());
        });
      },
      "fnServerData": (sSource, aoData, fnCallback, oSettings) => {
        oSettings.jqXHR = $.ajax({
          "dataType": 'json',
          "type": "GET",
          "url": sSource,
          "data": aoData,
    
          "success": (data) => { 
            this.iTotalDisplayRecords=data.iTotalDisplayRecords;
            
            this.statusCountService.setStatusCount(this.statusCountList, data.countByStatus);
            this.statusCountService.setUserType(this.statusCountList,data.countByType);
            fnCallback(data);
          },

          "error": (e) => {
            if (e.status == "403" || e.status == "401") {
              // this.commonServiceProvider.GoToErrorHandling(e);
            }
          }

        });
      },
      // "bSort": false,
      "sDom": "<rt><'row border-top pt-2'<'col-sm-12 col-md-5'l><'col-sm-12 col-md-7'p>>",
      "aoColumns": [/*{"mDataProp": "userId", "bSortable":  false,
                         "mRender": function(data){
                             return '<input type="checkbox">  ' ;
                         }
                     },*/
        { "mDataProp": "userName", "bSortable": false },
        { "mDataProp": "fullname", "bSortable": false },
        { "mDataProp": "phoneNumber", "bSortable": false, },
        { "mDataProp": "email", "bSortable": false, },
        { "mDataProp": "totalMarks", "bSortable": false, },
        {
          "mDataProp": "userType", "bSortable": false,
          "mRender": function (data) {

            if (data == 'STUDENT') {
              return '<span class="badge badge-warning" style="background-color: rgb(243, 70, 17)">STUDENT</span>';
            } else if (data == 'QUESTIONNAIRE') {
              return '<span class="badge badge-primary" style="background-color: rgb(9, 227, 230)">QUESTIONNAIRE</span>';
            } else if (data == 'ADMIN') {
              return '<span class="badge badge-success" style="background-color: rgb(53, 44, 225)">ADMIN</span>'
            }
            return data;
          }
        },
        {
          "mDataProp": "status", "bSortable": false,
          "mRender": function (data) {
      
            if (data == 'PROCESSD') {
              return '<span class="badge bg-warning ipsh-badge-pending">Pending Approval</span>';
            } else if (data == 'DELETED') {
              return '<span class="badge bg-danger ipsh-badge-delete">Deleted</span>';
             }  else if (data == 'VERIFIED') {
              return '<span class="badge ipsh-badge-approve" style="background-color: rgb(4, 194, 4)">Approved</span>'
            } else if (data == 'REJECT') {
              return '<span class="badge bg-danger ipsh-badge-reject">Rejected</span>'
            }
            return data;
          }
        },
        { "mDataProp": "isMCQAttended", "bSortable": false, 
        "mRender": function (data) {
          
          if (data === true) {
            return '<span class="badge" style="background-color: #33ce6c">Attended</span>';
          } else if (data == false) {
            return '<span class="badge" style="background-color: #ceb733">Not Attended</span>';
          }
          return data;
        }
        },
        { "mDataProp": "isPRGAttended", "bSortable": false, 
        "mRender": function (data) {
          
          if (data === true) {
            return '<span class="badge" style="background-color: #33ce6c">Attended</span>';
          } else if (data == false) {
            return '<span class="badge" style="background-color: #ceb733">Not Attended</span>';
          }
          return data;
        }
        },
      ]
    });

    // this.getCheckBoxesModal();
    $('#userList tbody').on('click', 'tr', (event) => {
      // console.log(event);
      $(event.currentTarget).toggleClass('selected');
    });

         //double click
         $("#userList tbody").on("dblclick", "tr", (event) => {
          $(event.currentTarget).addClass("selected");
          if (this.userDatatable.rows(".selected").data().length > 1) {
            Swal.fire({
              background: "#f3fa59",
              toast: true,
              position: "center",
              showConfirmButton: false,
              timer: 1000,
              icon: "warning",
              title: "Multiple records",
              iconColor: "orange"
            })
          } else {
            let userName: any = this.userDatatable.rows(".selected").data()[0].userName;

              // this.isPreserve = true;
              let selectedUserName = window.btoa(userName);
   
              this.router.navigate([
                "./supadmin/details/allDetails/" + selectedUserName
             
              ],{skipLocationChange:true});
            
          }
        });
         //double click

  }

  goToVerifyPage(){
    if (this.userDatatable.rows('.selected').data().length == 0) {
      Swal.fire({
        background: "#f3fa59",
        toast: true,
        position: "center",
        showConfirmButton: false,
        timer: 2000,
        icon: "warning",
        title: "Select a record to verify",
        iconColor: "orange"
      })
    } else if (this.userDatatable.rows('.selected').data().length > 1) {
      Swal.fire({
        background: "#f3fa59",
        toast: true,
        position: "center",
        showConfirmButton: false,
        timer: 2000,
        icon: "warning",
        title: "Multiple records",
        iconColor: "orange"
      })
    } else {
        var status: any;
        var userName: any;
       status = this.userDatatable.rows('.selected').data()[0].status;
       status = this.userDatatable.rows('.selected').data()[0].status;
       userName = this.userDatatable.rows('.selected').data()[0].userName;
       
        if (status == "VERIFIED") {
          Swal.fire({
            background: "#f3fa59",
            toast: true,
            position: "center",
            showConfirmButton: false,
            timer: 2000,
            icon: "warning",
            title: "Details Already Verified",
            iconColor: "orange"
          })
        } else if(status == "DELETED"){
          Swal.fire({
            background: "#f3fa59",
            toast: true,
            position: "center",
            showConfirmButton: false,
            timer: 2000,
            icon: "warning",
            title: "Deleted user can't verify",
            iconColor: "orange"
          })
        }else{
          let selectedIdForVerify  = window.btoa(userName);
          this.router.navigate(['./supadmin/details/verify', selectedIdForVerify],{skipLocationChange:true});
        }    
    }
  }

  goToUpdatePage(){
    if (this.userDatatable.rows('.selected').data().length == 0) {
      Swal.fire({
        background: "#f3fa59",
        toast: true,
        position: "center",
        showConfirmButton: false,
        timer: 2000,
        icon: "warning",
        title: "select a record to Update",
        iconColor: "orange"
      })
      
    } else if (this.userDatatable.rows('.selected').data().length > 1) {
      
      Swal.fire({
        background: "#f3fa59",
        toast: true,
        position: "center",
        showConfirmButton: false,
        timer: 2000,
        icon: "warning",
        title: "Multiple record  can not update",
        iconColor: "orange"
      })
    } else {
        var status: any;
        var userName: any;
        var userType: any;
       status = this.userDatatable.rows('.selected').data()[0].status;
       userType = this.userDatatable.rows('.selected').data()[0].userType;
       userName = this.userDatatable.rows('.selected').data()[0].userName;
       
        if(status == "DELETED"){
          Swal.fire({
            background: "#f3fa59",
            toast: true,
            position: "center",
            showConfirmButton: false,
            timer: 2000,
            icon: "warning",
            title: "Deleted user can't update",
            iconColor: "orange"
          })
        }else{
          if(userType == "ADMIN"){
            Swal.fire({
              background: "#f3fa59",
              toast: true,
              position: "center",
              showConfirmButton: false,
              timer: 2000,
              icon: "warning",
              title: "Permission Denied",
              iconColor: "orange"
            })
          }else{
            let selectedIdForUpdate  = window.btoa(userName);
            this.router.navigate(['./supadmin/details/update', selectedIdForUpdate],{skipLocationChange:true});
          }
        }    
    }

  }

  goToSetAdminPage(){
    if (this.userDatatable.rows('.selected').data().length == 0) {
      Swal.fire({
        background: "#f3fa59",
        toast: true,
        position: "center",
        showConfirmButton: false,
        timer: 2000,
        icon: "warning",
        title: "Please select a record",
        iconColor: "orange"
      })
    } else if (this.userDatatable.rows('.selected').data().length > 1) {
      Swal.fire({
        background: "#f3fa59",
        toast: true,
        position: "center",
        showConfirmButton: false,
        timer: 2000,
        icon: "warning",
        title: "Multiple records",
        iconColor: "orange"
      })
    } else {
        var userType: any;
        var userName: any;
        var status: any;
       userType = this.userDatatable.rows('.selected').data()[0].userType;
       userName = this.userDatatable.rows('.selected').data()[0].userName;
       status = this.userDatatable.rows('.selected').data()[0].status;
       
        if (userType == "ADMIN") {
          Swal.fire({
            background: "#f3fa59",
            toast: true,
            position: "center",
            showConfirmButton: false,
            timer: 2000,
            icon: "warning",
            title: "Already Admin",
            iconColor: "orange"
          })
        } else if(userType == "STUDENT"){
          Swal.fire({
            background: "#f3fa59",
            toast: true,
            position: "center",
            showConfirmButton: false,
            timer: 2000,
            icon: "warning",
            title: "Student cannot set as admin!",
            iconColor: "orange"
          })
        }else if(status == "DELETED"){
          Swal.fire({
            background: "#f3fa59",
            toast: true,
            position: "center",
            showConfirmButton: false,
            timer: 2000,
            icon: "warning",
            title: "Deleted User",
            iconColor: "orange"
          })
        }else{
          let selectedId  = window.btoa(userName);
          this.router.navigate(['./supadmin/details/setAdmin', selectedId],{skipLocationChange:true});
        }    
    }
  }

  deleteUser() {
        
    if (this.userDatatable.rows('.selected').data().length == 0) {
      Swal.fire({
        background: "#f3fa59",
        toast: true,
        position: "center",
        showConfirmButton: false,
        timer: 2000,
        icon: "warning",
        title: "Select a record to delete",
        iconColor: "orange"
      })
    } else if (this.userDatatable.rows('.selected').data().length > 1) {
      Swal.fire({
        background: "#f3fa59",
        toast: true,
        position: "center",
        showConfirmButton: false,
        timer: 2000,
        icon: "warning",
        title: "Multiple Records",
        iconColor: "orange"
      })
    } else {
      var userName = this.userDatatable.rows('.selected').data()[0].userName;
      var status = this.userDatatable.rows('.selected').data()[0].status;
      if(status == "DELETED"){
        Swal.fire({
          background: "#f3fa59",
          toast: true,
          position: "center",
          showConfirmButton: false,
          timer: 2000,
          icon: "warning",
          title: "Already deleted",
          iconColor: "orange"
        })
      }else{

        Swal.fire({
          title: "Do you want to delete the record?",
          icon: "question",
          showCancelButton: true,
          confirmButtonColor: "#3085d6",
          cancelButtonColor: "#d33",
          confirmButtonText: "OK",
          cancelButtonText: "Cancel",
        }).then((result) => {
          if (result.isConfirmed) {
            this.superAdminService.deleteUser(userName).subscribe((item: any) => {
              userName=""
              console.log(item);
              
            
              Swal.fire({
                background: "#2ecc71",
                color:"#fff",
                toast: true,
                position: "center",
                showConfirmButton: false,
                timer: 2000,
                icon: "success",
                title: "Deleted Successfully",
                iconColor: "#fff"
              })
              setTimeout(() => {
                this.userDatatable.draw();
                window.location.reload();
            }, 1000);
              


              
            }, error => {
              Swal.fire({
                background: "red",
                color: "#fff",
                toast: true,
                position: "center",
                showConfirmButton: false,
                timer: 2000,
                icon: "error",
                title: "Deletion Failed",
                iconColor: "#fff"
              })
            }, () => {
              console.log("finally")
            });
          } else if (result.dismiss === Swal.DismissReason.cancel) {
            Swal.fire({
              background: "#f3fa59",
              toast: true,
              position: "center",
              showConfirmButton: false,
              timer: 2000,
              icon: "warning",
              title: "Cancelled",
              iconColor: "orange"
            })
          }
        });
    

    }
  }
  }


  search(){
    // this.slect2 = $("#id-userType").val();
    this.userDatatable.draw();
  }

  clear(){
    $('#userName').val('');
    $('#phoneNumber').val('');
    $('#fullname').val('');
    $('#email').val('');
    $("#idStatus").val("").trigger("change");
    $("#userType").val("").trigger("change");
    this.userDatatable.draw();
    Swal.fire({
      background: "#2ecc71",
      color:"#fff",
      toast: true,
      position: "center",
      showConfirmButton: false,
      timer: 2000,
      icon: "success",
      title: "Search Cleared",
      iconColor: "#fff"
    })

  }
  getSearchInputs() {

    let userSearch: RegistrationDTO = new RegistrationDTO();
    userSearch.userName = $('#userName').val();

    userSearch.phoneNumber = $('#phoneNumber').val();
    userSearch.fullname = $('#fullname').val();
    userSearch.status = $('#idStatus').val();
    userSearch.email = $('#email').val();
    userSearch.userType = $('#userType').val();

    if (userSearch.userName == null || userSearch.userName == undefined) {
      userSearch.userName = '';
    }
    if (userSearch.status == null || userSearch.status == undefined) {
      userSearch.status = '';
    }
    if (userSearch.phoneNumber == null || userSearch.phoneNumber == undefined) {
      userSearch.phoneNumber = '';
    }
    if (userSearch.fullname == null || userSearch.fullname == undefined) {
      userSearch.fullname= '';
    }
    if (userSearch.email == null || userSearch.email== undefined) {
      userSearch.email = '';
    }
    if (userSearch.userType== null || userSearch.userType == undefined) {
      userSearch.userType = '';
    }
    if (Object.values(userSearch).some(value => value !== '')) {
      return JSON.stringify(userSearch);
      
    }
    return "";
}


uploadUsers(){
  this.router.navigate(['./supadmin/details/excelUpload'],{skipLocationChange:true})
}
  
excel(){
  let searchObj: RegistrationDTO = new RegistrationDTO();
  searchObj.userName = $('#userName').val();
    searchObj.phoneNumber = $('#phoneNumber').val();
    searchObj.fullname = $('#fullname').val();
    searchObj.status = $('#idStatus').val();
    searchObj.email = $('#email').val();
    searchObj.userType = $('#userType').val();
  console.log(searchObj);
  console.log(searchObj);
  this.superAdminService.excelDownload(searchObj).subscribe((item: any) => {
    console.log(item);
    this.saveToFileSystem(item);
    Swal.fire("Success");
 }, error => {
Swal.fire("Failed")}, () => {
    //console.log("finally")
});
}
pdf(){
  let searchObj: RegistrationDTO = new RegistrationDTO();
  searchObj.userName = $('#userName').val();
    searchObj.phoneNumber = $('#phoneNumber').val();
    searchObj.fullname = $('#fullname').val();
    searchObj.status = $('#idStatus').val();
    searchObj.email = $('#email').val();
    searchObj.userType = $('#userType').val();
  this.superAdminService.pdfDownload(searchObj).subscribe((item: any) => {
    console.log(item);
    this.saveToFileSystem(item);
    Swal.fire("Success");
 }, error => {
Swal.fire("Failed")}, () => {
    //console.log("finally")
});
}
private saveToFileSystem(response) {
  const contentType: string = response.contentType;
  const filename =  response.fileName;
  var byteString = atob(response.data);
  var ab = new ArrayBuffer(byteString.length);
  var ia = new Uint8Array(ab);
  for (var i = 0; i < byteString.length; i++) {
    ia[i] = byteString.charCodeAt(i);
   }
  console.log(contentType)
  const blob = new Blob([ab], { type: contentType });
  fileSaver.saveAs(blob, filename);
}












}
