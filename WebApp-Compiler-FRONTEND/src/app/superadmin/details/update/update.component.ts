import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import Swal from 'sweetalert2'
import { RegistrationDTO } from 'src/app/models/registration-dto';
import { SuperAdminService } from 'src/app/service/super-admin.service';

declare var $: any;
@Component({
  selector: 'app-update',
  templateUrl: './update.component.html',
  styleUrls: ['./update.component.css']
})
export class UpdateComponent implements OnInit {
  registrationObj: RegistrationDTO = new RegistrationDTO();
  userName : any;

  constructor(private superAdminService: SuperAdminService, private router: Router,private route: ActivatedRoute) { 
    this.userName = window.atob(this.route.snapshot.paramMap.get('userName'));
  }

  ngOnInit(): void {
    this.getUser();
  }

  getUser() {
    this.superAdminService.GetUser(this.userName).subscribe((item: any) => {
        this.registrationObj = item;
 

        $("#userNameu").val(this.userName).trigger('change');
        $("#fullnameu").val(this.registrationObj.fullname).trigger('change');
        $("#emailu").val(this.registrationObj.email).trigger('change');
        $("#phoneNumberu").val(this.registrationObj.phoneNumber).trigger('change');
        $("#collegeu").val(this.registrationObj.college).trigger('change');
        $("#branchu").val(this.registrationObj.branch).trigger('change');
        $("#genderu").val(this.registrationObj.gender).trigger('change');
        $("#userTypeu").val(this.registrationObj.userType).trigger('change');
        $("#statusu").val(this.registrationObj.status).trigger('change');
        $("#totalMarksu").val(this.registrationObj.totalMarks).trigger('change');

        $("#mcqu").val(this.registrationObj.mcqattended).trigger('change');
        $("#prgu").val(this.registrationObj.prgattended).trigger('change');
        

    }, error => {
        console.log("error")
    }, () => {
        // console.log("finally")
    });
}



update(){

  this.registrationObj.fullname = $("#fullnameu").val();
  this.registrationObj.email = $("#emailu").val();
  this.registrationObj.phoneNumber = $("#phoneNumberu").val();
  this.registrationObj.college= $("#collegeu").val();
  this.registrationObj.branch = $("#branchu").val();
  this.registrationObj.gender = $("#genderu").val();
  this.registrationObj.totalMarks = $("#totalMarksu").val();

  if( $("#mcqu").val()=="true"){
    this.registrationObj.mcqattended = true;
  }else{
    this.registrationObj.mcqattended = false;
  }

  if ($("#prgu").val()=="true"){
    this.registrationObj.prgattended = true;
  }else{
    this.registrationObj.prgattended = false;
  }


  this.superAdminService.updateUser(this.registrationObj).subscribe((item: any) => {
    // this.router.navigate(['./supadmin/details'],{skipLocationChange:true});
    if (item.code.toLowerCase() == "success") {
      Swal.fire({
        background: "#2ecc71",
        color:"#fff",
        toast: true,
        position: "center",
        showConfirmButton: false,
        timer: 2000,
        icon: "success",
        title: "Updated",
        iconColor: "#fff"
      })
        this.router.navigate(['./supadmin/details'],{skipLocationChange:true});   
    } else {
      Swal.fire({
        background: "red",
        color: "#fff",
        toast: true,
        position: "center",
        showConfirmButton: false,
        timer: 2000,
        icon: "error",
        title: "Updation Failed",
        iconColor: "#fff"
      })
    }
}, error => {
  Swal.fire({
    background: "red",
    color: "#fff",
    toast: true,
    position: "center",
    showConfirmButton: false,
    timer: 2000,
    icon: "error",
    title: "Updation Failed",
    iconColor: "#fff"
  })
}, () => {
    //console.log("finally")
});

}

backToUser() {
  this.router.navigate(['./supadmin/details'],{skipLocationChange:true});
}


}
