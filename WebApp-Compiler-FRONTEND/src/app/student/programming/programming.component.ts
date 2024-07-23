// import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Component, HostListener, OnInit } from '@angular/core';
import { Editor, Embeddedid } from 'src/app/models/editor.model';
import { EditorService } from 'src/app/service/editor.service';
import { HttpService } from 'src/app/service/http.service';
import { TokenserviceService } from 'src/app/service/token.service';
import Swal from 'sweetalert2'
import { Custominput } from 'src/app/models/custominput';
import {  ViewChild, ElementRef } from '@angular/core';
import { Timerdto } from 'src/app/models/timerdto';
import { TimerService } from 'src/app/service/timer.service';

declare var CodeMirror: any;

declare var setEditorValue: any;
declare var $: any;
declare var jQuery: any;
@Component({
  selector: 'app-programming',
  templateUrl: './programming.component.html',
  styleUrls: ['./programming.component.css']
})
export class ProgrammingComponent implements OnInit {
  @ViewChild('editor', { static: true }) editorTextArea: ElementRef;

  private editor: any;
  editorObj: Editor = new Editor();
  embid: Embeddedid = new Embeddedid();

  selectedLanguage: string = 'java';
  inputValue: string = '';
  outputValue: string = '';
  questionid: any;
  testCasePassed: number = 0;
  condition: boolean = true; // Set your condition based on your logic
  error: String;
  icon: any;
  isConditionMet1: boolean;
  isConditionMet2: boolean;
  isConditionMet3: boolean;
  isConditionMet4: boolean;
  isConditionMet5: boolean;
  isConditionMet6: boolean;
  isConditionMet8: boolean;
  isConditionMet9: boolean;
  isConditionMet10: boolean;
  customRun:Custominput = new Custominput();
  isConditionMet7: boolean;
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

  constructor(private timerservice:TimerService,private editorService:EditorService,private httpservice: HttpService, private route: ActivatedRoute,private tokenservice: TokenserviceService, private router: Router) { 
    this.selectedQuestionId = window.atob(this.route.snapshot.paramMap.get('selectedQuestionId'));
    this.selectedQuestion = window.atob(this.route.snapshot.paramMap.get('selectedQuestion'));
    this.selectedQuestionHeading = window.atob(this.route.snapshot.paramMap.get('selectedQuestionHeading'));
  }

  ngOnInit(): void {
    this.getUserSavedCode(this.selectedQuestionId)
    this.showLoadingPopup=false;
    this.setupPageVisibilityListener();
    this.iscoditionfunction()
    this.condition=false;
    this.updateTimer()

        // externalScript.changeLanguage();
        // const script = document.createElement('script');
        // script.src = '../../../assets/ext-script.js';
        // document.body.appendChild(script);
        $("#question").val(this.selectedQuestion);

  }

  runUserCode(): void 
  {
    this.updateTimer()
    this.iscoditionfunction()
    this.condition=false;
    const editorContent = this.runCode();
    this.customRun.program=editorContent;
    this.customRun.programname="Main"
    this.customRun.language=this.selectedLanguage
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
        Swal.fire({
          toast: true,
          position: "top-end",
          showConfirmButton: false,
          timer: 1000,
          icon: "error",
          title: "Response is Not Sucess",
        })

      }
    })


  }

  saveUserCode()
  {
    this.updateTimer()
    this.iscoditionfunction()
    this.condition=false;
    this.editorObj.questionid=this.selectedQuestionId;
    this.editorObj.username=this.tokenservice.getUsername();
    const editorContent = this.runCode();
    console.log("Run Code")
    this.editorObj.programmingLanguage = this.selectedLanguage;
    console.log(this.selectedLanguage)
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
        Swal.fire({
          toast: true,
          position: "top-end",
          showConfirmButton: false,
          timer: 1000,
          icon: "error",
          title: "Response is Not Sucess",
        })
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
          title: "Response Error",
        })
      }
    })

  }

  submit()
  {
    this.iscoditionfunction()
    this.showLoadingPopup=true
    this.condition=false;
    this.embid.questionid=this.selectedQuestionId;
    this.embid.username=this.tokenservice.getUsername();
    this.embid.programmingLanguage=this.selectedLanguage;
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
              this.iconTest(key,value)
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
        Swal.fire({
          toast: true,
          position: "top-end",
          showConfirmButton: false,
          timer: 1000,
          icon: "error",
          title: "Error Ocuured",
        });
        this.error=item.compileError;
        this.condition=true;
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
          title: "Response Error",
        });

      }
    })
  }
  closemodal()
  {
    this.condition=false;
  }

  iconTest(value:String, testcase:String)
  {
    if((value=="TestCase1") && (testcase=="Passed"))
    {
      this.isConditionMet1=true;
    }
    if((value=="TestCase2") && (testcase=="Passed"))
    {
      this.isConditionMet2=true;

    }
    if((value=="TestCase3") && (testcase=="Passed"))
    {
      this.isConditionMet3=true;

    }
    if((value=="TestCase4") && (testcase=="Passed"))
    {
      this.isConditionMet4=true;

    }
    if((value=="TestCase5") && (testcase=="Passed"))
    {
      this.isConditionMet5=true;

    }
    if((value=="TestCase6") && (testcase=="Passed"))
    {
      this.isConditionMet6=true;

    }
    if((value=="TestCase7") && (testcase=="Passed"))
    {
      this.isConditionMet7=true;

    }
    if((value=="TestCase8") && (testcase=="Passed"))
    {
      this.isConditionMet8=true;

    }
    if((value=="TestCase9") && (testcase=="Passed"))
    {
      this.isConditionMet9=true;

    }
    if((value=="TestCase10") && (testcase=="Passed"))
    {
      this.isConditionMet10=true;

    }
  }

  iscoditionfunction()
  {
    this.isConditionMet1=false;
    this.isConditionMet2=false;
    this.isConditionMet3=false;
    this.isConditionMet4=false;
    this.isConditionMet5=false;
    this.isConditionMet6=false;
    this.isConditionMet7=false;
    this.isConditionMet8=false;
    this.isConditionMet9=false;
    this.isConditionMet10=false;
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
                    text: 'Malpractice is Resorded',
                    showCancelButton: false,
                    confirmButtonText: 'OK',
                    confirmButtonColor: '#d33'
                  });              
                } // if ends here 
                else
                {
                  Swal.fire({
                    toast: true,
                    position: "top-end",
                    showConfirmButton: false,
                    timer: 1000,
                    icon: "error",
                    title: "Tab Switching Respose Failed",
                  })
                  if (item.details)
                  {
                    Swal.fire({
                      toast: true,
                      position: "top-end",
                      showConfirmButton: false,
                      timer: 1000,
                      icon: "error",
                      title: "Validation Error",
                    });
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
                    timer: 1000,
                    icon: "error",
                    title: "Response Error",
                  });
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
        Swal.fire("Don't be smart");
        event.preventDefault();
      }
    }
  
  ////////////////////////////TAB SWITCHING ENDS HERE ////////////////////////////////////////////////////////////////

  
  ////////////////////////////CODE EDITOR STARTS  HERE ////////////////////////////////////////////////////////////////


  ngAfterViewInit() {
    this.initializeCodeMirror(this.selectedLanguage);
    this.updateEditorSize();  
  }

  public initializeCodeMirror(language: string) {
    this.editor = CodeMirror.fromTextArea(this.editorTextArea.nativeElement, {
      mode: this.changeLanguage(language),
      theme: 'dracula',
      lineNumbers: true,
      autoCloseBrackets: true,
    });
  }

  setEditorValue(value: string) {
    this.editor.setValue(value);
  }

  changeLanguage(language: string) {
    switch (language) {
      case 'c':
        console.log("c")
        return 'text/x-csrc';
      case 'cpp':
        console.log("cpp");
        return 'text/x-c++src';
      case 'java':
        console.log("java");
        return 'text/x-java';
      case 'python3':
        return 'text/x-python';
      default:
        return 'text/plain';
    }
  }

  setlanguage(language: string)
  {
    this.editor.setOption("mode", this.changeLanguage(language));

  }
  runCode() {
    const editorContent = this.editor.getValue();
    console.log("Run Code")
    console.log(editorContent)

    // You can perform additional actions with the editor content here
    return editorContent;
  }

  updateEditorSize() {
    const width = window.innerWidth;
    const newEditorWidth = 0.525 * width;
    this.editor.setSize(newEditorWidth, 570);
  }

  onResize() {
    this.updateEditorSize();
  }
    ////////////////////////////CODE EDITOR ENDS  HERE ////////////////////////////////////////////////////////////////
    
    getUserSavedCode(questionidselected:any)
    {
      this.embid.questionid=questionidselected
      this.embid.username=this.tokenservice.getUsername()
      this.httpservice.postdata("http://localhost:8087/student/getsavedanswer",this.embid).subscribe((item: any)=>
      {
        this.setEditorValue(item.message);
        this.selectedLanguage=item.code
        this.setlanguage(item.code)
      },
      error=>
      {
        if(error.status == "400")
        {
          Swal.fire("Error!");
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
    if(item.code=="success")
    {
      Swal.fire({
        toast: true,
        position: "top-end",
        showConfirmButton: false,
        timer: 5000,
        icon: "success",
        title: "Time Updated",
      });
    }
    else{
      Swal.fire({
        toast: true,
        position: "top-end",
        showConfirmButton: false,
        timer: 5000,
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

backtomenu()
{
  this.router.navigate(['./student/programmingLanding'],{skipLocationChange:true});

}

}
