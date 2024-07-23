import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { QuestionDTO } from 'src/app/models/question-dto';
import { HttpService } from 'src/app/service/http.service';
import { TokenserviceService } from 'src/app/service/token.service';
import Swal from 'sweetalert2'


@Component({
  selector: 'app-add',
  templateUrl: './add.component.html',
  styleUrls: ['./add.component.css']
})
export class AddComponent implements OnInit {

  questiondto: QuestionDTO =new  QuestionDTO();
  questionid: any;
  constructor(private httpservice: HttpService,private router: Router,private tokenservice: TokenserviceService) {
    this.questiondto.username=this.tokenservice.getUsername()
  }
  validationMessage: any;

  ngOnInit(): void 
  {
    this.validationMessage = {};

  }

  add()
  {
    console.log(this.questiondto)
    this.httpservice.postdata("http://localhost:8086/question/add",this.questiondto).subscribe((item: any)=>
    {
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
         this.questionid=window.btoa(item.message);
        if (this.questiondto.questiontype=="MCQ") {
          this.router.navigate(['./question/mcq',this.questionid,],{skipLocationChange:true});
        } else {
          this.router.navigate(['./question/prg',this.questionid],{skipLocationChange:true});
        }
      }
      else 
      {        

        if (item.details) 
        {
          Swal.fire({
            background: "#f3fa59",
            toast: true,
            position: "center",
            showConfirmButton: false,
            timer: 2000,
            icon: "warning",
            title: "Validation Error",
            iconColor: "orange"
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
      if(error.status == "400")
      {
      let msg = "";
      error.error.details.forEach(element => 
        {
          msg = msg + element + "<br>"
        });
        Swal.fire({
          background: "#f3fa59",
          toast: true,
          position: "center",
          showConfirmButton: false,
          timer: 2000,
          icon: "warning",
          title: "Error",
          iconColor: "red"
        })      
      }
    })
  }

}
