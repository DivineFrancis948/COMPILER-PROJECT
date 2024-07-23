import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { PrgQuestionModel } from 'src/app/models/prg-question-model';
import { QuestionDTO } from 'src/app/models/question-dto';
import { McqService } from 'src/app/service/mcq.service';
import Swal from 'sweetalert2';
import {  ViewChild, ElementRef } from '@angular/core';
declare var CodeMirror: any;
declare var $: any;
declare var jQuery: any;


@Component({
  selector: 'app-prg-update',
  templateUrl: './prg-update.component.html',
  styleUrls: ['./prg-update.component.css']
})
export class PrgUpdateComponent implements OnInit {
  @ViewChild('editor', { static: true }) editorTextArea: ElementRef;
  private editor: any;
  selectedLanguage: string = 'java';

  questionId:any;
  question:QuestionDTO = new QuestionDTO();
  prgmodel:PrgQuestionModel = new PrgQuestionModel();

  constructor( private router: Router,private mcqService:McqService,
    private route: ActivatedRoute) {
      this.questionId = window.atob(this.route.snapshot.paramMap.get('questionId'));
     }

  ngOnInit(): void {
    this.getQuestion();
    this.getPRGQuestionDetails();
  }

  //getting question from backend
  getQuestion(){
    
    this.mcqService.getQusetionWithStatus(this.questionId).subscribe((item: any)=>
    {
      if (item)
      {
        this.question = item
        console.log(this.question)
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
  //getting question details from backend
  getPRGQuestionDetails(){
    this.mcqService.getPRGQusetionDetails(this.questionId).subscribe((item: any)=>
    {
      if (item)
      {
        this.prgmodel = item
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

  update(){
    $('#prgupdate-question-modal').modal('show');
  }
  confirmUpdate(){
    this.updateQuestion();
    this.updatePRGDetails();
    $('#prgupdate-question-modal').modal('hide');
    this.router.navigate(['./supadmin/questionDetails'],{skipLocationChange:true});
  }
  cancelUpdate(){
    $('#prgupdate-question-modal').modal('hide');
  }
  //updating Question
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
  //updating prg details
  updatePRGDetails()
  {
    this.mcqService.updatePRGDetails(this.prgmodel).subscribe((item: any)=>
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
