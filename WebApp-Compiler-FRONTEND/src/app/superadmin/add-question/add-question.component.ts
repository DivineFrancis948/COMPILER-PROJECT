import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { QuestionDTO } from 'src/app/models/question-dto';
import { HttpService } from 'src/app/service/http.service';
import { TokenserviceService } from 'src/app/service/token.service';
import Swal from 'sweetalert2'

@Component({
  selector: 'app-add-question',
  templateUrl: './add-question.component.html',
  styleUrls: ['./add-question.component.css']
})
export class AddQuestionComponent implements OnInit {

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
        Swal.fire("Registered Successfully!");
        this.questionid=window.btoa(item.message);
        if (this.questiondto.questiontype=="MCQ") {
          this.router.navigate(['./supadmin/mcq',this.questionid,],{skipLocationChange:true});
        } else {
          this.router.navigate(['./supadmin/program',this.questionid],{skipLocationChange:true});
        }
      }
      else 
      {
        Swal.fire("Fail!");
        if (item.details) 
        {
          Swal.fire("Validation Error!");
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
        Swal.fire("Error!");
      }
    })
  }

}

