import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { RegistrationDTO } from 'src/app/models/registration-dto';
import { HttpService } from 'src/app/service/http.service';
import Swal from 'sweetalert2'
declare var $: any;
@Component({
  selector: 'app-questionair',
  templateUrl: './questionair.component.html',
  styleUrls: ['./questionair.component.css']
})
export class QuestionairComponent implements OnInit {
  validationMessage: any;
  regDTO: RegistrationDTO=new RegistrationDTO();
  constructor(private httpservice: HttpService,private router: Router) { }

  ngOnInit(): void 
  {
    this.validationMessage = {};
  }



  register()
  {
    this.validationMessage = {};
    this.httpservice.postdata("http://localhost:8085/register/questionar",this.regDTO).subscribe((item: any)=>
    {
      this.clear()
      if (item.code.toLowerCase() == "success") 
      {
        Swal.fire({
          background: "#2ecc71",
          color:"#fff",
          toast: true,
          position: "center",
          showConfirmButton: false,
          timer: 2000,
          icon: "success",
          title: "Added Successfully",
          iconColor: "#fff"
        })        
        this.router.navigate(['./supadmin/landing'],{skipLocationChange:true});
      }
      else 
      {
        
        if (item.details) 
        {
          Swal.fire({
            background: "red",
            color: "#fff",
            toast: true,
            position: "center",
            showConfirmButton: false,
            timer: 2000,
            icon: "error",
            title: "Validation Error",
            iconColor: "#fff"
          })        

          item.details.forEach(element => 
            {
            var key = Object.keys(element)[0];
            this.validationMessage[key] = element[key];
          Swal.fire({
            background: "red",
            color: "#fff",
            toast: true,
            position: "center",
            showConfirmButton: false,
            timer: 2000,
            icon: "error",
            title: "Error",
            iconColor: "#fff"
          })       
              
        });
        }        
      }
    },
    error=>
    {
      this.clear()
      if(error.status == "400")
      {
      let msg = "";
      error.error.details.forEach(element => 
        {
          msg = msg + element + "<br>"
        });
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
    })

  }

  clear()
  {
   $("#fullname").val("");
   $("#username").val("");
   $("#phoneNumber").val("");
   $("#empid").val("");
   $("#email").val("");
   $("#password").val("");
   $("#password").val("");
  }

}
