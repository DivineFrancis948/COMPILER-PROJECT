import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { McqQuestion } from 'src/app/models/mcq-question-model';
import { QuestionDTO } from 'src/app/models/question-dto';
import { McqService } from 'src/app/service/mcq.service';
import { SuperAdminService } from 'src/app/service/super-admin.service';
import Swal from 'sweetalert2';

declare var $: any;
declare var jQuery: any;

@Component({
  selector: 'app-mcq-update',
  templateUrl: './mcq-update.component.html',
  styleUrls: ['./mcq-update.component.css']
})
export class McqUpdateComponent implements OnInit {

  questionId:any;
  question:QuestionDTO = new QuestionDTO();

  mcqQuestionOption:McqQuestion = new McqQuestion();
  

  constructor(private superAdminService: SuperAdminService, private router: Router,private mcqService:McqService,
  private route: ActivatedRoute) {
    this.questionId = window.atob(this.route.snapshot.paramMap.get('questionId'));
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
  update(){
    $('#mcqupdate-question-modal').modal('show');
  }
  confirmUpdate(){
    this.updateQuestion();
    this.updateOptionAndAnswer();
    $('#mcqupdate-question-modal').modal('hide');
    this.router.navigate(['./supadmin/questionDetails'],{skipLocationChange:true});
  }
  cancelUpdate(){
    $('#mcqupdate-question-modal').modal('hide');
  }
  updateQuestion()
  {
    console.log(this.question);
    this.mcqService.updateQuestion(this.question).subscribe((item: any)=>
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
  updateOptionAndAnswer()
  {
    console.log(this.mcqQuestionOption);
    this.mcqService.updateOptionAndAnswer(this.mcqQuestionOption).subscribe((item: any)=>
    {
      if (item.code.toLowerCase() == "success")
      {
        Swal.fire(item.message);
        // this.router.navigate(['./question/addquestion'],{skipLocationChange:true});
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
