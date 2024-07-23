// import { Component, Renderer2, ElementRef, OnInit,HostListener } from '@angular/core';

import { Component, HostListener, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Login } from 'src/app/models/login';
import { FullScreenService } from 'src/app/service/full-screen.service';
import { HttpService } from 'src/app/service/http.service';
import { RouteVisitService } from 'src/app/service/route-visit.service';
import { TimerService } from 'src/app/service/timer.service';
import { TokenserviceService } from 'src/app/service/token.service';
import { TimerComponent } from 'src/app/student/timer/timer.component';
import Swal from 'sweetalert2'

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  logindto:Login = new Login();
  constructor(private router: Router,private httpservice: HttpService,private tokenservice: TokenserviceService,private timer:TimerService,private visitedroutes:RouteVisitService,private fullscreenservice:FullScreenService) { }

  ngOnInit(): void 
  {
    // this.requestFullscreen();  
    this.tokenservice.setToken("null");
    this.timer.settime(0);
    this.timer.settimesecond(0);
    this.tokenservice.setSubmit("NO")
  }

  login()
  {
    this.httpservice.postdata("http://localhost:8085/register/login",this.logindto).subscribe((item: any)=>
    {
      if (item.token.toLowerCase() != "not found" && item.role=="ADMIN") 
      {
        this.timer.settime(10000);
        this.tokenservice.setUsername(item.username);
        this.tokenservice.setToken(item.token);

        Swal.fire({
          toast: true,
          position: "top-end",
          showConfirmButton: false,
          timer: 1000,
          icon: "success",
          title: "ADMIN LOGGED IN",
        })
        this.router.navigate(['./supadmin/landing'],{skipLocationChange:true});
      }
      else if((item.role=="QUESTIONNAIRE"))
      {
        this.timer.settime(10000);
        this.tokenservice.setUsername(item.username);
        this.tokenservice.setToken(item.token);
        Swal.fire({
          toast: true,
          position: "top-end",
          showConfirmButton: false,
          timer: 1000,
          icon: "success",
          title: "QUESTIONAIR LOGGED IN",
        }) 
        this.router.navigate(['./question/addquestion'],{skipLocationChange:true}); 
      }
      else if((item.role=="STUDENT"))
      {        
        this.fullscreenservice.fullScreenMethod();
        if(item.isSubmitted==="YES")
        {
          Swal.fire({
            toast: true,
            position: "top-end",
            showConfirmButton: false,
            timer: 1000,
            icon: "warning",
            title: "Exam Submitted ",
          })
        }
        else
        {
          this.timer.startTimer();
          this.timer.settime(item.timer);
          this.tokenservice.setIsMcq(item.ismcq);
          this.tokenservice.setSubmit(item.isSubmitted)
          console.log(this.tokenservice.getIsMcq())
          this.tokenservice.setUsername(item.username);
          this.tokenservice.setToken(item.token);
          Swal.fire({
            toast: true,
            position: "top-end",
            showConfirmButton: false,
            timer: 1000,
            icon: "success",
            title: "STUDENT LOGGED IN",
          })
          this.router.navigate(['./student/landing'],{skipLocationChange:true});
        }
 
      }
    },
    error=>
    {
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
    })
  }

signup()
{
  this.router.navigate(['./auth/signup/user'],{skipLocationChange:true});
}

@HostListener('contextmenu', ['$event'])
onRightClick(event: Event): void 
{
  event.preventDefault();
}

// private requestFullscreen() {
//   const elem = this.el.nativeElement;

//   if (elem.requestFullscreen) {
//     elem.requestFullscreen();
//   } else if (elem.mozRequestFullScreen) { // Firefox
//     elem.mozRequestFullScreen();
//   } else if (elem.webkitRequestFullscreen) { // Chrome, Safari, and Opera
//     elem.webkitRequestFullscreen();
//   } else if (elem.msRequestFullscreen) { // IE/Edge
//     elem.msRequestFullscreen();
//   }
// }

}
