import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Custominput } from 'src/app/models/custominput';
import { Editor, Embeddedid } from 'src/app/models/editor.model';
import { Testcase } from 'src/app/models/testcase';
import { EditorService } from 'src/app/service/editor.service';
import { HttpService } from 'src/app/service/http.service';
import { TokenserviceService } from 'src/app/service/token.service';
import Swal from 'sweetalert2'


declare var runCode: any;
declare var setEditorValue: any;
declare var $: any;
declare var jQuery: any;
@Component({
  selector: 'app-compile-added-code',
  templateUrl: './compile-added-code.component.html',
  styleUrls: ['./compile-added-code.component.css']
})
export class CompileAddedCodeComponent implements OnInit {

  editorObj: Editor = new Editor();
  embid: Embeddedid = new Embeddedid();
  tescase: Testcase = new Testcase();

  selectedLanguage: string = 'Choose Language';
  inputValue: string = '';
  outputValue: string = '';
  questionid: any;
  testCasePassed: number = 0;
  condition: boolean = true; // Set your condition based on your logic
  error: String;
  icon: any;
  customRun:Custominput = new Custominput();
  testCaseResultt: any;
  num: any;
showLoadingPopup: any;


  updateTestCases(passedCount: number): void 
  {
    this.testCasePassed = passedCount;
  }

  selectedQuestionId: any;
  selectedQuestion: any;
  selectedQuestionHeading: any;

  constructor(private editorService:EditorService,private httpservice: HttpService, private route: ActivatedRoute,private tokenservice: TokenserviceService,private router: Router) { 
    this.selectedQuestionId = window.atob(this.route.snapshot.paramMap.get('selectedQuestionId'));
    this.selectedQuestion = window.atob(this.route.snapshot.paramMap.get('selectedQuestion'));
    this.selectedQuestionHeading = window.atob(this.route.snapshot.paramMap.get('selectedQuestionHeading'));
  }

  ngOnInit(): void {
    this.showLoadingPopup=false;

    this.gettestcases();
    // this.setupPageVisibilityListener();
    this.condition=false;
        // externalScript.changeLanguage();
        const script = document.createElement('script');
        script.src = '../../../assets/ext-script.js';
        document.body.appendChild(script);
        console.log(this.selectedQuestion);
        $("#question").val(this.selectedQuestion);

  }

  runUserCode(): void 
  {
    this.condition=false;
    const editorContent = runCode();
    this.customRun.program=editorContent;
    this.customRun.programname="Main"
    console.log(this.customRun)
    this.httpservice.postdata("http://localhost:8087/student/run",this.customRun).subscribe((item: any)=>
    {
      this.testCasePassed=item.output
      if (item.passed== "YES")
      {
        this.testCaseResultt = item.result.Result;
        Swal.fire({
          toast: true,
          position: "top-end",
          showConfirmButton: false,
          timer: 1000,
          icon: "success",
          title: "Compiled Successfully",
        })
      }
      else 
      {
        Swal.fire({
          toast: true,
          position: "top-end",
          showConfirmButton: false,
          timer: 1000,
          icon: "error",
          title: "Compiled Error",
        })
        this.error=item.compileError;
        this.condition=true;
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

  saveUserCode()
  {
    this.condition=false;
    this.editorObj.questionid=this.selectedQuestionId;
    this.editorObj.username=this.tokenservice.getUsername();
    const editorContent = runCode();
    this.editorObj.programmingLanguage = this.selectedLanguage;
    this.editorObj.solution=editorContent;
    this.editorObj.input=this.inputValue;
    this.editorObj.programname="JavaClass"
    // console.log(this.editorObj)

    console.log(this.editorObj)
    this.httpservice.postdata("http://localhost:8087/student/add/prgSol",this.editorObj).subscribe((item: any)=>
    {
      if (item.code.toLowerCase() == "success") 
      {
        Swal.fire({
          toast: true,
          position: "top-end",
          showConfirmButton: false,
          timer: 1000,
          icon: "success",
          title: "Code Saved Succefully",
        })
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

  submit()
  {
    this.showLoadingPopup=true
    this.condition=false;
    this.embid.questionid=this.editorObj.questionid;
    this.embid.username=this.tokenservice.getUsername();
    this.embid.programmingLanguage=this.customRun.language;
    console.log(this.embid)
    this.httpservice.postdata("http://localhost:8087/student/runtestcase",this.embid).subscribe((item: any)=>
    {
      this.showLoadingPopup=false
      console.log(item)
      this.testCasePassed=item.output
      if (item.passed== "YES")
      {
        this.showLoadingPopup=false
        for (const testCase of item.result.Result) {
          // Loop through each object in the array
          for (const key in testCase) {
            if (testCase.hasOwnProperty(key)) {
              const value = testCase[key];
            }
          }
        }

        
        Swal.fire({
          toast: true,
          position: "top-end",
          showConfirmButton: false,
          timer: 1000,
          icon: "success",
          title: "Compiled Successfully",
        })
        
      }
      else 
      {
        Swal.fire("Error Occured");
        this.error=item.compileError;
        this.condition=true;
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
  closemodal()
  {
    this.condition=false;
  }
  private setupPageVisibilityListener(): void 
  {
    document.addEventListener('visibilitychange', () => {
      if (document.hidden) {
        Swal.fire("Registered Successfully!");
      } else {
        this.num=this.num+1
        if(this.num>3)
        {
          Swal.fire("You will be terminated");

        }
        Swal.fire("Cannot Switch Tab");
      }
    });
  }

  // @HostListener('window:beforeunload', ['$event'])
  // unloadNotification($event: any): void {
  //   // Prevent the window from being minimized
  //   $event.returnValue = true;
  // }

  // @HostListener('window:keydown', ['$event'])
  // onKeyDown(event: KeyboardEvent): void {
  //   // Disable Ctrl+C (copy) and Ctrl+V (paste)
  //   if ((event.ctrlKey || event.metaKey) && (event.key === 'c' || event.key === 'v')) {
  //     Swal.fire("Don't be smart");
  //     event.preventDefault();
  //   }
  // }

  gettestcases()
  {
 
      this.httpservice.getAllQuestions("http://localhost:8086/question/gettestcases/",this.selectedQuestionId).subscribe((item: any)=>
      {
  
        if (item) 
        {
          this.tescase.testcase01=item.Testcase01;
          this.tescase.testcase02=item.Testcase02;
          this.tescase.testcase03=item.Testcase03;
          this.tescase.testcase04=item.Testcase04;
          this.tescase.testcase05=item.Testcase05;
          this.tescase.testcase06=item.Testcase06;
          this.tescase.testcase07=item.Testcase07;
          this.tescase.testcase08=item.Testcase08;
          this.tescase.testcase09=item.Testcase09;
          this.tescase.testcase10=item.Testcase10;
        }
        else 
        {
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
  
  backtomenu()
  {
    this.router.navigate(['./supadmin/programmigquestionlist'],{skipLocationChange:true});

  }


}