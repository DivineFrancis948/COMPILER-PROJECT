import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { RegistrationDTO } from 'src/app/models/registration-dto';
import { HttpService } from 'src/app/service/http.service';
import Swal from 'sweetalert2'
declare var $: any;
@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {

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
    this.httpservice.postdata("http://localhost:8085/register/student",this.regDTO).subscribe((item: any)=>
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
          title: "Added Succesfully",
          iconColor: "#fff"
        })        
        this.router.navigate(['./auth/login'],{skipLocationChange:true});

      }
      else 
      {
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

        if (item.details) 
        {
          // this.clear()
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
          title: "Error",
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
   $("#branch").val("");
   $("#ktuid").val("");
   $("#year").val("");
  }

}
