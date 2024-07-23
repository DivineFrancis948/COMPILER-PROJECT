import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { McqQuestion } from 'src/app/models/mcq-question-model';
import { McqAnswer } from 'src/app/models/selected-mcq-question.model';
import { McqService } from 'src/app/service/mcq.service';
import { SharedVariablesService } from 'src/app/service/shared-variables.service';
import { TokenserviceService } from 'src/app/service/token.service';
import Swal from 'sweetalert2';


declare var $: any;
declare var jQuery: any;

@Component({
  selector: 'app-mcq',
  templateUrl: './mcq.component.html',
  styleUrls: ['./mcq.component.css']
})
export class McqComponent implements OnInit {

  questionNumber:number;

  selectedAnswer :string;
  //username refer student
  userName:any;
  saved:any;
  answered:any;
  unanswered:any;
  questions: any;

  questionToDisplay:any;

  mcqStatus:any;

  mcqQuestions:McqQuestion = new McqQuestion();
  mcqAnswer:McqAnswer = new McqAnswer();


  flags: string[] = Array(15);
  saveFlag:boolean[] = Array(15).fill(false)


  black:any ;
  yellow:any ;
  green:any ;

  constructor(
    private route: ActivatedRoute,private service: McqService,private router: Router,
    private sharedVariables:SharedVariablesService,private tokenservice: TokenserviceService) 
  { this.userName=tokenservice.getUsername()}

  ngOnInit(): void {

    this.tokenservice.setheading("MCQ QUESTIONS")
    this.tokenservice.getheading();
    this.getMcqListQuestion();  //first function
    // Introduce a time lag (e.g., 1000 milliseconds or 1 second)
    const delayInMilliseconds = 1000;
    // Use setTimeout to call the second method after the specified delay
    setTimeout(() => {
      this.getStatus(); //get status fucntion
      setTimeout(() => {
        this.updateQuestionStatusToUser(); 
      });
    }, delayInMilliseconds);
    
    this.mcqAttended();
  }



  getMcqListQuestion(){
    //get random 15 mcq question
    this.service.getMcqListQuestion(this.userName).subscribe((item: any)=>
    {
      if (item)
      {
        this.questions=item.aaData
        this.getQuestion(1);
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
          title: "Failed",
          iconColor: "#fff"
        });   
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
            title: "Validation Failed",
            iconColor: "#fff"
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
        });
            }
    })
}
getMcqFullWithOptions(questionId:any){
      this.service.getMcqFullWithOptions(questionId).subscribe((item: any)=>
      {
        if (item)
        {
          this.mcqQuestions = item;
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
            title: "Failed",
            iconColor: "#fff"
          })          
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
              title: "Validation Failed",
              iconColor: "#fff"
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
getQuestion(number:any){
    this.saveFlag = Array(15).fill(false);
    this.questionNumber=number;

    if(this.questions.length <number-1 && number<1){
      Swal.fire({
        background: "red",
        color: "#fff",
        toast: true,
        position: "center",
        showConfirmButton: false,
        timer: 2000,
        icon: "error",
        title: "No Question",
        iconColor: "#fff"
      })    
    }else{
      if(this.questions[number-1]){
        this.getMcqFullWithOptions(this.questions[number-1].Questionid);
        this.getQuestionToDisplay(this.questions[number-1].Questionid);
        this.updateQuestionStatusToUser();
        if(this.flags[this.questionNumber-1] == 'A' || (this.flags[this.questionNumber-1] == 'S')){
          this.getSavedAnswer(this.userName,this.questions[number-1].Questionid);
        }else{
          this.selectedAnswer = null;
        }
        
      }else{
        Swal.fire({
          background: "red",
          color: "#fff",
          toast: true,
          position: "center",
          showConfirmButton: false,
          timer: 2000,
          icon: "error",
          title: "No Question",
          iconColor: "#fff"
        })      
      }
    }

    
}
getQuestionToDisplay(questionId:any){
    
      this.service.getMcqQusetionWithoutOption(questionId).subscribe((item: any)=>
      {
        if (item)
        {
          this.questionToDisplay = item.question
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
            title: "Failed",
            iconColor: "#fff"
          })          
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
              title: "Validation Failed",
              iconColor: "#fff"
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
//on next answer saved to db
next(){
  
      this.mcqAnswer.questionId=this.questions[this.questionNumber-1].Questionid;
      this.mcqAnswer.userName = this.userName;
      this.mcqAnswer.selected = this.selectedAnswer;
      this.mcqAnswer.mcqStatus = this.flags[this.questionNumber-1];
    if(this.selectedAnswer == null){
      if(this.saveFlag[this.questionNumber-1]){
        this.flags[this.questionNumber-1] = 'S';
        this.saveFlag[this.questionNumber-1] = false;
      }
      if(this.questionNumber<this.questions.length){
        this.questionNumber++; 
      }
      this.saveMcqAnswer(this.mcqAnswer);
      this.getQuestion(this.questionNumber);
    }else{
      if(this.saveFlag[this.questionNumber-1]){
        this.flags[this.questionNumber-1] = 'S';
        this.saveFlag[this.questionNumber-1] = false;
      }else{
        this.flags[this.questionNumber-1] = 'A';
        this.mcqAnswer.mcqStatus = 'A';
      }  
      this.saveMcqAnswer(this.mcqAnswer);
        if(this.questionNumber<this.questions.length){
          this.questionNumber++; 
        }
      this.selectedAnswer = null;
      this.getQuestion(this.questionNumber);
    }
    
    
}
previous(){
      this.mcqAnswer.questionId=this.questions[this.questionNumber-1].Questionid;
      this.mcqAnswer.userName = this.userName;
      this.mcqAnswer.selected = this.selectedAnswer;
      this.mcqAnswer.mcqStatus = this.flags[this.questionNumber-1];
    if(this.selectedAnswer == null){
      if(this.questionNumber>1){
        this.questionNumber--; 
      }
      this.saveMcqAnswer(this.mcqAnswer);
      this.getQuestion(this.questionNumber);
    }else{
      if(this.saveFlag[this.questionNumber-1]){
        // this.flags[this.questionNumber-1] = 3;
        this.saveFlag[this.questionNumber-1] = false;
      }else{
        this.flags[this.questionNumber-1] = 'A';
      }
      this.saveMcqAnswer(this.mcqAnswer);
      if(this.questionNumber>1){
        this.questionNumber--; 
      }
      this.selectedAnswer = null;
      this.getQuestion(this.questionNumber);
    }
}

save(){
      this.mcqAnswer.questionId=this.questions[this.questionNumber-1].Questionid;
      this.mcqAnswer.userName = this.userName;
      this.mcqAnswer.selected = this.selectedAnswer;
      this.mcqAnswer.mcqStatus = this.flags[this.questionNumber-1];
      this.flags[this.questionNumber-1] = 'S';
      this.saveFlag[this.questionNumber-1] = true;
      this.mcqAnswer.mcqStatus = 'S'
      this.saveMcqAnswer(this.mcqAnswer);
}

submit(){
    $('#submit-user-modal').modal('show');
    this.getStatus();
    // TypeScript code for handling the modal
}
confirmSubmit(){
    this.updateTotalMcqMark(this.userName);
    this.router.navigate(['./student/landing'],{skipLocationChange:true});
    $('#submit-user-modal').modal('hide');
    // this.sharedVariables.mcqAttended(true);
    
}
cancelSubmit(){
    $('#submit-user-modal').modal('hide');
}
updateQuestionStatusToUser(){
    this.black=0;
    this.green=0;
    this.yellow=0;
    for (let i = 0; i < this.flags.length; i++) {
      if(this.flags[i] == 'U'){
        this.black++;
      }else if(this.flags[i] == 'A'){
        this.green++;
      }else if(this.flags[i] == 'S'){
        this.yellow++;
      }
    }
}
//save the answer to db when option selected
saveMcqAnswer(mcqAnswerDto:any){
 
    this.service.saveMcqAnswer(mcqAnswerDto).subscribe((item: any)=>
    {
      if (item.code.toLowerCase() == "success") 
      {
        Swal.fire({
          toast: true,
          position: "center",
          showConfirmButton: false,
          timer: 1000,
          icon: "success",
          title: "Successfully",
        })
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
        });  
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
            title: "Validation Failed",
            iconColor: "#fff"
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
getSavedAnswer(userName:any,questionId:any){
    this.service.getSavedAnswer(userName,questionId).subscribe((item: any)=>
    {
      if (item)
      {
        this.mcqAnswer = item;
        this.selectedAnswer=this.mcqAnswer.selected;
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

updateTotalMcqMark(userName:any){
    this.service.updateTotalMcqMark(userName).subscribe((item: any)=>
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
          title: "Success",
          iconColor: "#fff"
        })      
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
          title: "Failed",
          iconColor: "#fff"
        })        
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
            title: "Validation Failed",
            iconColor: "#fff"
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
//getting status of mcq questions
getStatus(){
  this.service.getMcqStatus(this.userName).subscribe((item: any)=>
    {
      if (item.message === "success")
      {
        this.saved = item.saved;
        this.answered = item.answered;
        this.unanswered = item.unanswered;
        this.flags = item.array;
        console.log(this.flags);
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
          title: "Failed",
          iconColor: "#fff"
        })        
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
            title: "Validation Failed",
            iconColor: "#fff"
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

mcqAttended()
{
  this.service.updatedMcqAttended(this.userName).subscribe((item: any)=>
  {
    if (item.code.toLowerCase() == "success")
    {
      this.mcqStatus=item
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
        title: "Deletion Failed",
        iconColor: "#fff"
      });    
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
          title: "Validation Failed",
          iconColor: "#fff"
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

}

