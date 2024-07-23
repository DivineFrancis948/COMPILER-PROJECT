import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
// import * as CryptoJS from 'crypto-js';
import Swal from 'sweetalert2';
import { ActivatedRoute, Router } from '@angular/router';
import { McqService } from 'src/app/service/mcq.service';
import { McqQuestion } from 'src/app/models/mcq-question-model';
import { QuestionDTO } from 'src/app/models/question-dto';

// import { ToastrModule, ToastrService } from 'ngx-toastr';
declare var $: any;
declare var jQuery: any;
@Component({
  selector: 'app-mcq',
  templateUrl: './mcq.component.html',
  styleUrls: ['./mcq.component.css']
})
export class McqComponent implements OnInit {

  questionid: string;
  question:McqQuestion = new McqQuestion();
  mcqQuestWithoutOption:QuestionDTO = new QuestionDTO();
  validationMessage: any;

  constructor(private route: ActivatedRoute,private service: McqService,private router:Router)
  {
    this.questionid = window.atob(this.route.snapshot.paramMap.get('questionid'));
    this.question.questionId=this.questionid;

  }
  ngOnInit(): void
  {
    this.getQuestion();
    this.validationMessage = {};

  }
  add()
  {
    console.log(this.question);
    this.service.addQusetion(this.question).subscribe((item: any)=>
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
        this.router.navigate(['./question/addquestion'],{skipLocationChange:true});
      }
      else
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

  clear(){
    $('#questionId').val('');
    $('#question').val('');
    $('#option1').val('');
    $('#option2').val('');
    $('#option3').val('');
    $('#option4').val('');
    $('#answer').val('').trigger('change');

    this.question.questionId=null;
    this.question.question=null;
    this.question.option1=null;
    this.question.option2=null;
    this.question.option3=null;
    this.question.option4=null;

    this.question.answer=null;


  }

  getQuestion()
  {
    this.service.getMcqQusetionWithoutOption(this.questionid).subscribe((item: any)=>
    {
      if (item)
      {
        this.mcqQuestWithoutOption=item;
        this.question.question=item.question
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
}
