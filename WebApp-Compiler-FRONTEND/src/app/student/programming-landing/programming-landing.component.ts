import { Component, HostListener, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Embeddedid } from 'src/app/models/editor.model';
import { SelectedQues } from 'src/app/models/selected-ques.model';
import { Timerdto } from 'src/app/models/timerdto';
import { HttpService } from 'src/app/service/http.service';
import { McqService } from 'src/app/service/mcq.service';
import { TimerService } from 'src/app/service/timer.service';
import { TokenserviceService } from 'src/app/service/token.service';
import Swal from 'sweetalert2'

declare var setEditorValue: any;
declare var $: any;
declare var jQuery: any;
@Component({
  selector: 'app-programming-landing',
  templateUrl: './programming-landing.component.html',
  styleUrls: ['./programming-landing.component.css']
})
export class ProgrammingLandingComponent implements OnInit {

 selectedQuesObj: SelectedQues = new SelectedQues();
 value: any;
  userName: string;
  url: string;
  embid: Embeddedid = new Embeddedid();
  content:any = '';
  mcqStatus: any;

  constructor(private timerservice:TimerService,private httpservice: HttpService, private router: Router,private tokenservice: TokenserviceService,private mcqservice:McqService) { }

  
  ngOnInit(): void {
    this.getQuestionDetails();
    this.tokenservice.setheading("PROGRAMMING QUESTIONS");
    // const script = document.createElement('script');
    // script.src = '../../../assets/ext-script.js';
    // document.body.appendChild(script);
    this.prgAttended();
    this.updateTimer();

    this.setupPageVisibilityListener();
  }

  getQuestionDetails(): void{

    this.httpservice.getAllQuestions("http://localhost:8086/question/getfivequestions/",this.tokenservice.getUsername()).subscribe((item: any)=>
    {
      // this.testCasePassed=item.output
      this.selectedQuesObj.question1=item.aaData[0].Question;
      this.selectedQuesObj.question2=item.aaData[1].Question;
      this.selectedQuesObj.question3=item.aaData[2].Question;
      this.selectedQuesObj.question4=item.aaData[3].Question;
      this.selectedQuesObj.question5=item.aaData[4].Question;
      
      this.selectedQuesObj.questionHeading1=item.aaData[0].QuestionHeading;
      this.selectedQuesObj.questionHeading2=item.aaData[1].QuestionHeading;
      this.selectedQuesObj.questionHeading3=item.aaData[2].QuestionHeading;
      this.selectedQuesObj.questionHeading4=item.aaData[3].QuestionHeading;
      this.selectedQuesObj.questionHeading5=item.aaData[4].QuestionHeading;

      this.selectedQuesObj.questionId1=item.aaData[0].Questionid;
      this.selectedQuesObj.questionId2=item.aaData[1].Questionid;
      this.selectedQuesObj.questionId3=item.aaData[2].Questionid;
      this.selectedQuesObj.questionId4=item.aaData[3].Questionid;
      this.selectedQuesObj.questionId5=item.aaData[4].Questionid;



      if (item.aaData) 
      {
        Swal.fire({
          toast: true,
          position: "top-end",
          showConfirmButton: false,
          timer: 100,
          icon: "success",
          title: "Welcome to Programming",
        })
      }
      else 
      {
        // Swal.fire("Fail!");
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
        Swal.fire("Error!");
      }
    })
  }


  studentPrgmPage(event: any){
    let clickedBtn = event.target.id;
    console.log(clickedBtn);

    let selectedQuestionId: string;
    let selectedQuestion: string;
    let selectedQuestionHeading: string;

    switch (clickedBtn) {
      case "q1":
        selectedQuestionId = window.btoa(this.selectedQuesObj.questionId1);
        selectedQuestion = window.btoa(this.selectedQuesObj.question1);
        selectedQuestionHeading = window.btoa(this.selectedQuesObj.questionHeading1);
        break;
      case "q2":
        selectedQuestionId = window.btoa(this.selectedQuesObj.questionId2);
        selectedQuestion = window.btoa(this.selectedQuesObj.question2);
        selectedQuestionHeading = window.btoa(this.selectedQuesObj.questionHeading2);
        break;
      case "q3":
        selectedQuestionId = window.btoa(this.selectedQuesObj.questionId3);
        selectedQuestion = window.btoa(this.selectedQuesObj.question3);
        selectedQuestionHeading = window.btoa(this.selectedQuesObj.questionHeading3);
        break;
      case "q4":
        selectedQuestionId = window.btoa(this.selectedQuesObj.questionId4);
        selectedQuestion = window.btoa(this.selectedQuesObj.question4);
        selectedQuestionHeading = window.btoa(this.selectedQuesObj.questionHeading4);
        break;
      case "q5":
        selectedQuestionId = window.btoa(this.selectedQuesObj.questionId5);
        selectedQuestion = window.btoa(this.selectedQuesObj.question5);
        selectedQuestionHeading = window.btoa(this.selectedQuesObj.questionHeading5);
        break;
      default:
        break;
    }

    console.log(selectedQuestion);
    this.value=this.value+20;
    this.router.navigate(['./student/programming', selectedQuestionId, selectedQuestion,selectedQuestionHeading],{skipLocationChange:true});
  }  
  backToMcq()
  {

  }
  
  Submit()
  {
    this.tokenservice.setSubmit("YES");
    this.embid.username=this.tokenservice.getUsername();
    this.embid.questionid=this.tokenservice.getUsername();
    this.httpservice.postdata("http://localhost:8087/student/submitprgquestions",this.embid).subscribe((item: any)=>
    {
      if(item.code=="SUCCESS")
      {
        Swal.fire({
          toast: true,
          position: "top-end",
          showConfirmButton: false,
          timer: 100,
          icon: "success",
          title: "Program Attended",
        })
      }
      else{
        Swal.fire({
          toast: true,
          position: "top-end",
          showConfirmButton: false,
          timer: 100,
          icon: "error",
          title: "No Program Attended",
        })
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
          timer: 100,
          icon: "error",
          title: "Error",
        })      
      }
    })
    this.router.navigate(['./student/submitpage'],{skipLocationChange:true});

  }



  getAttendedPrgQuestion()
  {
    this.mcqservice.getAtetndedPRGQuestionsStatus(this.tokenservice.getUsername()).subscribe((item: any)=>
    {
      console.log(item.code)
      console.log(item)

      if (item.code ==  "SUCCESS")
      {
          item.details.forEach(detail => 
          {
            this.content += `${detail.questionid}<br> TestCasePassed: ${detail.testCasePassed}<br><br>`;
          }); 
          Swal.fire({
            title: 'Submited the Exam!',
            html: this.content,
            icon: 'success',
            confirmButtonText: 'OK',
          });
      }
      else
      {
        Swal.fire({
          toast: true,
          position: "top-end",
          showConfirmButton: false,
          timer: 100,
          icon: "error",
          title: "Error",
        })        
        if (item.details)
        {
          Swal.fire({
            toast: true,
            position: "top-end",
            showConfirmButton: false,
            timer: 100,
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
      let msg = "";
      error.error.details.forEach(element =>
        {
          msg = msg + element + "<br>"
        });
        Swal.fire({
          toast: true,
          position: "top-end",
          showConfirmButton: false,
          timer: 100,
          icon: "error",
          title: "Error",
        })      
      }
    })
    this.content=''
  }
  

  //////////////////////////////////////////////////////////TAB SWITCHING API CODE//////////////////////////////////////////////////////////
  private setupPageVisibilityListener(): void 
  {
    document.addEventListener('visibilitychange', () => {
      if (document.hidden) 
      { 
        Swal.fire({
          toast: true,
          position: "center",
          showConfirmButton: false,
          timer: 1000,
          icon: "success",
          title: "Tab if Not Hidden",
        })
      } else 
      {
            this.httpservice.updatedTabSwitching(this.tokenservice.getUsername()).subscribe((item: any)=>
            {
              if (item.code.toLowerCase() == "success")
              {
                Swal.fire({
                  icon: 'warning',
                  title: 'Warning!',
                  text: 'Cannot Switch Tab',
                  showCancelButton: false,
                  confirmButtonText: 'OK',
                  confirmButtonColor: '#d33'
                });              
              } // if ends here 
              else
              {
                Swal.fire("Fail Tab Switching!");
                if (item.details)
                {
                  Swal.fire({
                    toast: true,
                    position: "top-end",
                    showConfirmButton: false,
                    timer: 100,
                    icon: "error",
                    title: "Validation Error",
                  })                  
                  item.details.forEach(element =>
                    {
                    var key = Object.keys(element)[0];
                  });
                } //if ends here
              } //else ends here
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
                  toast: true,
                  position: "top-end",
                  showConfirmButton: false,
                  timer: 100,
                  icon: "error",
                  title: "Error",
                })              
              } //if ends here
            })
      }
    });
  }
  
  @HostListener('window:beforeunload', ['$event'])
  unloadNotification($event: any): void {
    // Prevent the window from being minimized
    $event.returnValue = true;
  }

  @HostListener('window:keydown', ['$event'])
  onKeyDown(event: KeyboardEvent): void {
    // Disable Ctrl+C (copy) and Ctrl+V (paste)
    if ((event.ctrlKey || event.metaKey) && (event.key === 'c' || event.key === 'v')) {
      Swal.fire("Don't be smart Copy Paste Not Allowed");
      event.preventDefault();
    }
  }

////////////////////////////TAB SWITCHING ENDS HERE ////////////////////////////////////////////////////////////////
prgAttended()
{
  this.mcqservice.updatedPrgAttended(this.tokenservice.getUsername()).subscribe((item: any)=>
  {
    if (item.message.toLowerCase() == "success")
    {
      this.mcqStatus=item
      Swal.fire({
        toast: true,
        position: "top-end",
        showConfirmButton: false,
        timer: 1000,
        icon: "success",
        title: "Program Question Attended",
      })

    }
    else
    {
      Swal.fire({
        toast: true,
        position: "top-end",
        showConfirmButton: false,
        timer: 2000,
        icon: "warning",
        title: "Program Question Attended Not Updated",
      });
      if (item.details)
      {
        Swal.fire({
          toast: true,
          position: "top-end",
          showConfirmButton: false,
          timer: 2000,
          icon: "error",
          title: "Validation Error",
        });
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
        toast: true,
        position: "top-end",
        showConfirmButton: false,
        timer: 1000,
        icon: "error",
        title: "Error",
      });
    }
  })
}

/////////////////////////////////UPDATING TIMER STARTS HERE/////////////////////////////////
timerdto:Timerdto = new Timerdto()
updateTimer()
{
this.timerdto.timer=this.timerservice.gettime();
console.log(this.timerdto.timer);
this.timerdto.username=this.tokenservice.getUsername();
  this.httpservice.updatedata("http://localhost:8087/student/updatetimer",this.timerdto).subscribe((item: any)=>
  {
    if(item.code=="SUCCESS")
    {
      Swal.fire({
        toast: true,
        position: "top-end",
        showConfirmButton: false,
        timer: 2000,
        icon: "success",
        title: "Time Updated",
      });
    }
    else{
      Swal.fire({
        toast: true,
        position: "top-end",
        showConfirmButton: false,
        timer: 2000,
        icon: "error",
        title: "Time Updation Error",
      });

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
        timer: 2000,
        icon: "error",
        title: "Error",
      });
    }
  })
}

}
