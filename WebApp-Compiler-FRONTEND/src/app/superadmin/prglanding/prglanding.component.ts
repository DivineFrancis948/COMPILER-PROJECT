import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Embeddedid } from 'src/app/models/editor.model';
import { Programlist } from 'src/app/models/programlist';
import { SelectedQues } from 'src/app/models/selected-ques.model';
import { HttpService } from 'src/app/service/http.service';
import { TokenserviceService } from 'src/app/service/token.service';
import Swal from 'sweetalert2'


@Component({
  selector: 'app-prglanding',
  templateUrl: './prglanding.component.html',
  styleUrls: ['./prglanding.component.css']
})
export class PrglandingComponent implements OnInit {
  questions: Programlist[] = [];
  selectedQuesObj: SelectedQues = new SelectedQues();
  value: any;
   userName: string;
   url: string;
   embid: Embeddedid = new Embeddedid();
   constructor(private httpservice: HttpService, private router: Router,private tokenservice: TokenserviceService) { }
 
   
   ngOnInit(): void {
     this.getQuestionDetails();
     this.tokenservice.setheading("PROGRAMMING QUESTIONS");
     const script = document.createElement('script');
     script.src = '../../../assets/ext-script.js';
     document.body.appendChild(script);
   }
 
   getQuestionDetails(): void{
 
     this.httpservice.getAllQuestions("http://localhost:8086/question/getallquestions/",this.tokenservice.getUsername()).subscribe((item: any)=>
     {
 
       if (item.aaData) 
       {
        this.questions = item.aaData.map((data: any) => new Programlist(data));
        Swal.fire({
          toast: true,
          position: "top-end",
          showConfirmButton: false,
          timer: 1000,
          icon: "success",
          title: "Programming Questions",
        })       }
       else 
       {
         // Swal.fire("Fail!");
         if (item.details) 
         {
          Swal.fire({
            toast: true,
            position: "top-end",
            showConfirmButton: false,
            timer: 1000,
            icon: "error",
            title: "Validation Error",
          })
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
        Swal.fire({
          toast: true,
          position: "top-end",
          showConfirmButton: false,
          timer: 1000,
          icon: "error",
          title: "Error",
        })       
      }
     })
   }
 
 
   studentPrgmPage(event: any,i:any){
     let clickedBtn = event.target.id;
     console.log(clickedBtn);
 
     let selectedQuestionId: string;
     let selectedQuestion: string;
     let selectedQuestionHeading: string;
 
     switch (clickedBtn) {
       case "q1":
        selectedQuestionId = window.btoa(this.questions[i].Questionid);
        selectedQuestion = window.btoa(this.questions[i].Question);
        selectedQuestionHeading = window.btoa(this.questions[i].QuestionHeading);
         break;
       case "q2":
        selectedQuestionId = window.btoa(this.questions[i].Questionid);
        selectedQuestion = window.btoa(this.questions[i].Question);
        selectedQuestionHeading = window.btoa(this.questions[i].QuestionHeading);
         break;
       case "q3":
        selectedQuestionId = window.btoa(this.questions[i].Questionid);
        selectedQuestion = window.btoa(this.questions[i].Question);
        selectedQuestionHeading = window.btoa(this.questions[i].QuestionHeading);
         break;
       case "q4":
        selectedQuestionId = window.btoa(this.questions[i].Questionid);
        selectedQuestion = window.btoa(this.questions[i].Question);
        selectedQuestionHeading = window.btoa(this.questions[i].QuestionHeading);
         break;
       case "q5":
        selectedQuestionId = window.btoa(this.questions[i].Questionid);
        selectedQuestion = window.btoa(this.questions[i].Question);
        selectedQuestionHeading = window.btoa(this.questions[i].QuestionHeading);
         break;
       default:
         break;
     }
 
     console.log(selectedQuestion);
     this.value=this.value+20;
     this.router.navigate(['./supadmin/addtestcaseoutput', selectedQuestionId, selectedQuestion,selectedQuestionHeading],{skipLocationChange:true});
   }  
   backToMcq()
   {
 
   }
   
   Submit()
   {
     this.embid.username=this.tokenservice.getUsername();
     this.embid.questionid=this.tokenservice.getUsername();
     this.httpservice.postdata("http://localhost:8087/student/submitprgquestions",this.embid).subscribe((item: any)=>
     {
       if(item.message=="fail")
       {
         Swal.fire("Not UPdated Value");
       }
       else{
         Swal.fire("Exam Submitted");
       }
     },
     error=>
     {
       if(error.status == "400")
       {
         Swal.fire("Error!");
       }
     })
   }
 

 
 }
 
