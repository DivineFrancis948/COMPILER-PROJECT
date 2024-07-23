import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { McqService } from 'src/app/service/mcq.service';
import { TokenserviceService } from 'src/app/service/token.service';
import Swal from 'sweetalert2'

@Component({
  selector: 'app-submitpage',
  templateUrl: './submitpage.component.html',
  styleUrls: ['./submitpage.component.css']
})
export class SubmitpageComponent implements OnInit {
studentPrgmPage($event: any) {
throw new Error('Method not implemented.');
}
  programQuestions: any;

  constructor(private mcqservice:McqService,private router: Router,private tokenservice: TokenserviceService) { }

  ngOnInit(): void 
  {
    this.getAttendedPrgQuestion()

  }

  getAttendedPrgQuestion()
  {
    this.mcqservice.getAtetndedPRGQuestionsStatus(this.tokenservice.getUsername()).subscribe((item: any)=>
    {
      this.programQuestions=item.details
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

  thankyou()
  {
    this.router.navigate(['./student/thankyou'],{skipLocationChange:true});

  }

}
