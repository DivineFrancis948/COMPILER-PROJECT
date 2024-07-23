import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import Swal from 'sweetalert2'
import { RegistrationDTO } from 'src/app/models/registration-dto';
import { SuperAdminService } from 'src/app/service/super-admin.service';

declare var $: any;
@Component({
  selector: 'app-set-admin',
  templateUrl: './set-admin.component.html',
  styleUrls: ['./set-admin.component.css']
})
export class SetAdminComponent implements OnInit {

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
 

        $("#userNameA").val(this.userName).trigger('change');
        $("#fullnameA").val(this.registrationObj.fullname).trigger('change');
        $("#emailA").val(this.registrationObj.email).trigger('change');
        $("#phoneNumberA").val(this.registrationObj.phoneNumber).trigger('change');
        $("#collegeA").val(this.registrationObj.college).trigger('change');
        $("#branchA").val(this.registrationObj.branch).trigger('change');
        $("#genderA").val(this.registrationObj.gender).trigger('change');
        $("#userTypeA").val(this.registrationObj.userType).trigger('change');
        $("#statusA").val(this.registrationObj.status).trigger('change');

    }, error => {
        console.log("error")
    }, () => {
        // console.log("finally")
    });
}



setAsAdmin(){
  this.superAdminService.setAdmin(this.registrationObj).subscribe((item: any) => {
    this.router.navigate(['./supadmin/details'],{skipLocationChange:true});
    if (item.code.toLowerCase() == "success") {
      Swal.fire({
        background: "#2ecc71",
        color:"#fff",
        toast: true,
        position: "center",
        showConfirmButton: false,
        timer: 2000,
        icon: "success",
        title: "Success",
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
        title: "Failed",
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
    title: "Failed",
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
