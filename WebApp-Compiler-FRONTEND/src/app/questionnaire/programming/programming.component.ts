import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { PrgQuestionModel } from 'src/app/models/prg-question-model';
import { HttpService } from 'src/app/service/http.service';
import {  ViewChild, ElementRef } from '@angular/core';
import Swal from 'sweetalert2'
import { CodeData } from 'src/app/models/admin-custom-output';
declare var CodeMirror: any;
declare var setEditorValue: any;

@Component({
  selector: 'app-programming',
  templateUrl: './programming.component.html',
  styleUrls: ['./programming.component.css']
})
export class ProgrammingComponent implements OnInit {

  @ViewChild('editor', { static: true }) editorTextArea: ElementRef;
  private editor: any;
  selectedLanguage: string = 'java';
  questionid: string;
  validationMessage: any;
  prgmodel:PrgQuestionModel = new PrgQuestionModel();CodeData
  codeData:CodeData = new CodeData();

  constructor(private route: ActivatedRoute,private httpservice: HttpService,private router: Router) 
  {
    this.questionid = window.atob(this.route.snapshot.paramMap.get('questionid'));
    this.prgmodel.questionId=this.questionid
  }

  ngOnInit(): void 
  {
    this.validationMessage = {};

  }


  add()
  {
    this.validationMessage = {};
    const editorContent = this.runCode();
    this.prgmodel.solution=editorContent;
    this.httpservice.postdata("http://localhost:8086/question/add/prg",this.prgmodel).subscribe((item: any)=>
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
          title: "Not Added",
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

Extractoutput()
{
  console.log(this.codeData)
  this.codeData.language=this.prgmodel.language;
  const editorContent = this.runCode();
  this.codeData.sourceCode=editorContent
  this.codeData.testCases.testCase1 = this.prgmodel.testcase1;
  this.codeData.testCases.testCase2 = this.prgmodel.testcase2;
  this.codeData.testCases.testCase3 = this.prgmodel.testcase3;
  this.codeData.testCases.testCase4 = this.prgmodel.testcase4;
  this.codeData.testCases.testCase5 = this.prgmodel.testcase5;
  this.codeData.testCases.testCase6 = this.prgmodel.testcase6;
  this.codeData.testCases.testCase7 = this.prgmodel.testcase7;
  this.codeData.testCases.testCase8 = this.prgmodel.testcase8;
  this.codeData.testCases.testCase9 = this.prgmodel.testcase9;
  this.codeData.testCases.testCase10 = this.prgmodel.testcase10; 
  this.httpservice.postdata("http://localhost:8086/admin/execute",this.codeData).subscribe((item: any)=>
  {
    if (item.response.passed == "YES") 
    {
      Swal.fire({
        background: "#2ecc71",
        color:"#fff",
        toast: true,
        position: "center",
        showConfirmButton: false,
        timer: 2000,
        icon: "success",
        title: "Output Extracted",
        iconColor: "#fff"
      })      
      item.response.result.Result.forEach((element, index) => {
        var testCase = element.TestCase;
        var output = element.Output;
        // Assuming prgmodel is an object with properties like testcase1A, testcase2A, etc.
        this.prgmodel["testcase" + (index + 1) + "A"] = output;
    });
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
        title: "Output Not Extracted",
        iconColor: "orange"
      })             
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
        iconColor: "Red"
      })    
    }
  })
}

  addNextQuestion()
  {
    this.router.navigate(['./question/addquestion'],{skipLocationChange:true});

  }

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
        const newEditorWidth = 0.590 * width;
        this.editor.setSize(newEditorWidth, 300);
      }
    
      onResize() {
        this.updateEditorSize();
      }
        ////////////////////////////CODE EDITOR ENDS  HERE ////////////////////////////////////////////////////////////////

}
