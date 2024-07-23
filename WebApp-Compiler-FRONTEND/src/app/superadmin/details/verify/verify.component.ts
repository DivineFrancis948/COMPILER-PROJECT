import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import Swal from 'sweetalert2'
import { RegistrationDTO } from 'src/app/models/registration-dto';
import { SuperAdminService } from 'src/app/service/super-admin.service';

declare var $: any;
@Component({
  selector: 'app-verify',
  templateUrl: './verify.component.html',
  styleUrls: ['./verify.component.css']
})
export class VerifyComponent implements OnInit {

  registrationObj: RegistrationDTO = new RegistrationDTO();
 

  userName : any;

  constructor(private superAdminService: SuperAdminService, private router: Router,
    private route: ActivatedRoute) {
      this.userName = window.atob(this.route.snapshot.paramMap.get('userName'));
     }

  ngOnInit(): void {
    this.getUser();
  }

  getUser() {
    this.superAdminService.GetUser(this.userName).subscribe((item: any) => {
        this.registrationObj = item;
 

        $("#userNamev").val(this.userName).trigger('change');
        $("#fullnamev").val(this.registrationObj.fullname).trigger('change');
        $("#emailv").val(this.registrationObj.email).trigger('change');
        $("#phoneNumberv").val(this.registrationObj.phoneNumber).trigger('change');
        $("#collegev").val(this.registrationObj.college).trigger('change');
        $("#branchv").val(this.registrationObj.branch).trigger('change');
        $("#genderv").val(this.registrationObj.gender).trigger('change');
        $("#userTypev").val(this.registrationObj.userType).trigger('change');
        $("#statusv").val(this.registrationObj.status).trigger('change');

    }, error => {
        console.log("error")
    }, () => {
        // console.log("finally")
    });
}



verify(){
  this.superAdminService.verifyUser(this.registrationObj).subscribe((item: any) => {
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
        title: "Verified",
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
        title: "Verification Failed",
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
    title: "Verification Failed",
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
