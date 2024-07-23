import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import Swal from 'sweetalert2'
import { RegistrationDTO } from 'src/app/models/registration-dto';
import { StatusCountModel } from 'src/app/models/status-count-model';
import { StatusCountService } from 'src/app/service/status-count.service';
import { SuperAdminService } from 'src/app/service/super-admin.service';
import { QuestionDTO } from 'src/app/models/question-dto';
import { McqService } from 'src/app/service/mcq.service';


declare var $: any;
declare var jQuery: any;


@Component({
  selector: 'app-question-details',
  templateUrl: './question-details.component.html',
  styleUrls: ['./question-details.component.css']
})
export class QuestionDetailsComponent implements OnInit {


  static obj: QuestionDetailsComponent;
  iTotalDisplayRecords: any
  userDatatable: any;
  statusCountList: StatusCountModel = new StatusCountModel();
  initialHead: string[] = ['Question ID', 'Username', 'Question Type','Question','Created','Status'];
  constructor(private router: Router,private statusCountService: StatusCountService,private superAdminService: SuperAdminService,private mcqService : McqService) {
  }
  ngOnInit(): void
  {
    QuestionDetailsComponent.obj = this;
    this.getUsers();
  }
  refreshByStatus(status: string)
  {
    //to clear all search when all is clicked
    // if(status == ""){
    //   $("#userType").val("").trigger("change");
    // }
    $("#status").val(status).trigger("change");
    //to clear usertype if status is clicked
    // $("#userType").val("").trigger("change");
    this.search();
  }
  refreshByType(questionType: string)
  {
    //to clear all search when all is clicked
    // if(status == ""){
    //   $("#userType").val("").trigger("change");
    // }
    $("#questionType").val(questionType).trigger("change");
    //to clear usertype if status is clicked
    // $("#userType").val("").trigger("change");
    this.search();
  }
  // refreshByUserType(status: string)
  // {
  //   $("#userType").val(status).trigger("change");
  //   this.search();
  // }
  getUsers() {

    this.userDatatable = $('#questionList').DataTable({
      "bProcessing": false,
      "bDeferRender": true,
      "ordering": true,
      bAutoWidth: false,
      bServerSide: true,
      sAjaxSource: "http://localhost:8085/register/searchQuestion",
      "iDisplayLength": 5,
      "aLengthMenu": [[5,10, 25, 50, 100], [5,10, 25, 50, 100]],
      "sPaginationType": "full_numbers",
      "paging": true,
      "fnServerParams": function (aoData) {
        var dataString = QuestionDetailsComponent.obj.getSearchInputs();
        aoData.push({ name: "searchParam", value: dataString });
      },
      
      "fnServerData": (sSource, aoData, fnCallback, oSettings) => {
        oSettings.jqXHR = $.ajax({
          "dataType": 'json',
          "type": "GET",
          "url": sSource,
          "data": aoData,
          "success": (data) => {
            this.iTotalDisplayRecords=data.iTotalDisplayRecords;
            console.log(this.iTotalDisplayRecords)
            console.log(data.countByStatus)
            console.log(data.countByType)
            this.statusCountService.setStatusCount(this.statusCountList, data.countByStatus);
            this.statusCountService.setStatusCount(this.statusCountList, data.countByType);
            fnCallback(data);
          },
          "error": (e) => {
            if (e.status == "403" || e.status == "401") {
            }
          }
        });
      },
      "createdRow": (row: Node, data: any[] | object, dataIndex: number) => {
        // Double the height of each row
        $(row).css("height", "110px");

      },
      // "width": "100%",
      // "bSort": true,
      "sDom": "<rt><'row border-top pt-2'<'col-sm-12 col-md-5'l><'col-sm-12 col-md-7'p>>",
      "aoColumns": [
        { "mDataProp": "questionId", "bSortable": true, "sTitle": "ID","sWidth": "80px" },
        { "mDataProp": "userName", "bSortable": true, "sTitle": "Reviewer" },
        { "mDataProp": "questionType", "bSortable": false, "sTitle": "Type" },
        // { "mDataProp": "questionHeading", "bSortable": false, },
        { "mDataProp": "question", "bSortable": true, "sTitle": "Question","sWidth": "700px"},
        // { "mDataProp": "createdDate", "bSortable": true, "sTitle": "Created On","sWidth": "130px" },

        {
          "mDataProp": "status", "bSortable": true, "sTitle": "Status",
          "mRender": function (data) {
            if (data == 'PROCESSD') {
              return '<i class="fa fa-plus-circle fa-2x" style="color:#8B8000" aria-hidden="true"></i>';
            } else if (data == 'DELETED') {
              return '<i class="fa fa-times fa-2x" style="color:red" aria-hidden="true"></i>';
            }  else if (data == 'VERIFIED') {
              return '<i class="fa fa-check-square-o fa-2x" style="color:green" aria-hidden="true"></i>';
            } else if (data == 'REJECT') {
              return '<span class="badge bg-danger ipsh-badge-reject">Rejected</span>'
            }
            return data;
          }
        }
      ]
    });
  // this.getCheckBoxesModal();
  $('#questionList tbody').on('click', 'tr', (event) => {
    // console.log(event);
    $(event.currentTarget).toggleClass('selected');
  });

}
  
  search(){
    // this.slect2 = $("#id-userType").val();
    this.userDatatable.draw();
  }
  clear(){
    $('#questionId').val('');
    $('#question').val('');
    $('#date').val('');
    $('#questionHeading').val('');
    $('#userName').val('');
    $("#status").val("").trigger("change");
    $("#questionType").val("").trigger("change");
    this.userDatatable.draw();
  }

  getSearchInputs() {
    let questionSearch: QuestionDTO = new QuestionDTO();
    questionSearch.username = $('#userName').val();
    questionSearch.questionid = $('#questionId').val();
    questionSearch.questiontype = $('#questionType').val();
    questionSearch.questionHeading = $('#questionHeading').val();
    questionSearch.question = $('#question').val();
    questionSearch.createdDate = $('#date').val();
    questionSearch.status = $('#status').val();
    if ( questionSearch.username == null ||  questionSearch.username == undefined) {
      questionSearch.username = '';
    }
    if (questionSearch.questionid == null || questionSearch.questionid == undefined) {
      questionSearch.questionid = '';
    }
    if (questionSearch.questiontype == null || questionSearch.questiontype == undefined) {
      questionSearch.questiontype = '';
    }
    if (questionSearch.questionHeading == null || questionSearch.questionHeading == undefined) {
      questionSearch.questionHeading= '';
    }
    if (questionSearch.question == null || questionSearch.question== undefined) {
      questionSearch.question = '';
    }
    if (questionSearch.createdDate== null || questionSearch.createdDate == undefined) {
      questionSearch.createdDate = '';
    }
    if (questionSearch.status== null || questionSearch.status == undefined) {
      questionSearch.status = '';
    }
    if (Object.values(questionSearch).some(value => value !== '')) {
      return JSON.stringify(questionSearch);
    }
    return "";
}
deleteUser(){
  {
    if (this.userDatatable.rows('.selected').data().length == 0) {
      Swal.fire("Select a record to delete")
    } else if (this.userDatatable.rows('.selected').data().length > 1) {
      Swal.fire("Multiple Records")
    } else {
      
      var status = this.userDatatable.rows('.selected').data()[0].status;
      if(status == "DELETED"){
        Swal.fire("Already deleted");
      }else{
        $('#delete-question-modal').modal('show');
    }
  }
  }
}
updateQuestion(){
 
  
  if (this.userDatatable.rows('.selected').data().length == 0) {
    Swal.fire("Please select a record to Update");
  } else if (this.userDatatable.rows('.selected').data().length > 1) {
    Swal.fire("Multiple record  can not Update");
  } else {
    
      var status: any;
      var questionId: any;
      var questionType:any;
     status = this.userDatatable.rows('.selected').data()[0].status;
     questionId = this.userDatatable.rows('.selected').data()[0].questionId;
     questionType = this.userDatatable.rows('.selected').data()[0].questionType;

      if (status == "VERIFIED") {
        Swal.fire("Details Already Verified !");
      } else if(status == "DELETED"){
        Swal.fire("Deleted user can't Update!");
      }else{
        
        if(questionType.toUpperCase() == "PRG"){
          let selectedIdForUpdate   = window.btoa(questionId);
          console.log("prg")
        this.router.navigate(['./supadmin/questionDetails/updatePrg', selectedIdForUpdate],{skipLocationChange:true});
        
        }else if(questionType.toUpperCase() == "MCQ"){
          let selectedIdForUpdate  = window.btoa(questionId);
          console.log("mcq")
        this.router.navigate(['./supadmin/questionDetails/updateMcq', selectedIdForUpdate],{skipLocationChange:true});
        }
      }
  }
}
verifyQuestion(){
  if (this.userDatatable.rows('.selected').data().length == 0) {
    Swal.fire("Please select a record to Verify");
  } else if (this.userDatatable.rows('.selected').data().length > 1) {
    Swal.fire("Multiple record  can not verify");
  } else {
      var status: any;
      var questionId: any;
      var questionType:any;
     status = this.userDatatable.rows('.selected').data()[0].status;
     questionId = this.userDatatable.rows('.selected').data()[0].questionId;
     questionType = this.userDatatable.rows('.selected').data()[0].questionType;
      if (status == "VERIFIED") {
        Swal.fire("Details Already Verified !");
      } else if(status == "DELETED"){
        Swal.fire("Deleted user can't verify!");
      }else{
        let selectedIdForVerify  = window.btoa(questionId);
        if(questionType.toUpperCase() == "PRG"){
          this.router.navigate(['./supadmin/questionDetails/verifyPrg', selectedIdForVerify],{skipLocationChange:true});
        }else if(questionType.toUpperCase() == "MCQ"){
          this.router.navigate(['./supadmin/questionDetails/verifyMcq', selectedIdForVerify],{skipLocationChange:true});
        }

        
        
      }
  }
}
confirmDelete(){
  var questionId = this.userDatatable.rows('.selected').data()[0].questionId;
  this.mcqService.deleteQuestion(questionId).subscribe((item: any) => {
    Swal.fire("Deleted Successfully")
    this.userDatatable.draw();
    $('#delete-question-modal').modal('hide');
  }, error => {
    Swal.fire("Cannot delete")
  }, () => {
    console.log("finally")
  });
}
cancelDelete(){
  $('#delete-question-modal').modal('hide');
}

//Upload mcq excel
uploadMcq(){
  this.router.navigate(['./supadmin/questionDetails/mcqUpload'],{skipLocationChange:true})
}

//upload prg excel
uploadPrg(){
  this.router.navigate(['./supadmin/questionDetails/prgUpload'],{skipLocationChange:true})
}

addQuestion() {
  this.router.navigate(['./question/addquestion'],{skipLocationChange:true})

  }


}
 