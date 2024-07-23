import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { McqQuestion } from 'src/app/models/mcq-question-model';
import { QuestionDTO } from 'src/app/models/question-dto';
import { McqService } from 'src/app/service/mcq.service';
import { SuperAdminService } from 'src/app/service/super-admin.service';
import Swal from 'sweetalert2';

declare var $: any;
declare var jQuery: any;

@Component({
  selector: 'app-mcq-verify',
  templateUrl: './mcq-verify.component.html',
  styleUrls: ['./mcq-verify.component.css']
})
export class McqVerifyComponent implements OnInit{

questionId:any;
question:QuestionDTO = new QuestionDTO();

mcqQuestionOption:McqQuestion = new McqQuestion();


constructor(private superAdminService: SuperAdminService, private router: Router,private mcqService:McqService,
private route: ActivatedRoute) {
  this.questionId = window.atob(this.route.snapshot.paramMap.get('questionId'));console.log(this.questionId)
 }
ngOnInit(): void {
  this.getQuestion(this.questionId);
  this.getMcqFullWithOptionsAndAnswer(this.questionId);
}

getMcqFullWithOptionsAndAnswer(questionId:any){
  this.mcqService.getMcqFullWithOptionsAndAnswer(questionId).subscribe((item: any)=>
  {
    if (item)
    {
      this.mcqQuestionOption = item;
      console.log(this.mcqQuestionOption);
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
getQuestion(questionId:any){
  
  this.mcqService.getQusetionWithStatus(questionId).subscribe((item: any)=>
  {
    if (item)
    {
      this.question = item
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
Verify(){
  $('#mcqverify-question-modal').modal('show');
}
confirmVerify(){
  this.verifyQuestion();
  $('#mcqverify-question-modal').modal('hide');
  this.router.navigate(['./supadmin/questionDetails'],{skipLocationChange:true});
}
cancelVerify(){
  $('#mcqverify-question-modal').modal('hide');
}
verifyQuestion()
{
  this.mcqService.verifyQuestion(this.questionId).subscribe((item: any)=>
  {
    if (item.code.toLowerCase() == "success")
    {
      Swal.fire(item.message);
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
back(){
  this.router.navigate(['./supadmin/questionDetails'],{skipLocationChange:true});
}

}