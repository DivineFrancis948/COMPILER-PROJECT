import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import Swal from 'sweetalert2'
import { RegistrationDTO } from 'src/app/models/registration-dto';
import { SuperAdminService } from 'src/app/service/super-admin.service';

declare var $: any;
@Component({
  selector: 'app-all-details',
  templateUrl: './all-details.component.html',
  styleUrls: ['./all-details.component.css']
})
export class AllDetailsComponent implements OnInit {

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
 

        $("#userNameD").val(this.userName).trigger('change');
        $("#fullnameD").val(this.registrationObj.fullname).trigger('change');
        $("#emailD").val(this.registrationObj.email).trigger('change');
        $("#phoneNumberD").val(this.registrationObj.phoneNumber).trigger('change');
        $("#collegeD").val(this.registrationObj.college).trigger('change');
        $("#branchD").val(this.registrationObj.branch).trigger('change');
        $("#genderD").val(this.registrationObj.gender).trigger('change');
        $("#userTypeD").val(this.registrationObj.userType).trigger('change');
        $("#statusD").val(this.registrationObj.status).trigger('change');

        $("#ktuIdD").val(this.registrationObj.ktuId).trigger('change');
        $("#totalMarksD").val(this.registrationObj.totalMarks).trigger('change');
        $("#mdateD").val(this.registrationObj.mdate).trigger('change');
        $("#cdateD").val(this.registrationObj.cdate).trigger('change');


    }, error => {
        console.log("error")
    }, () => {
        // console.log("finally")
    });
}

backToUser() {
  this.router.navigate(['./supadmin/details'],{skipLocationChange:true});
}



}
