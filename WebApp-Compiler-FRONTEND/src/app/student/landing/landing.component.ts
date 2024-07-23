import { Component, HostListener, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FullScreenService } from 'src/app/service/full-screen.service';
import { HttpService } from 'src/app/service/http.service';
import { SharedVariablesService } from 'src/app/service/shared-variables.service';
import { TokenserviceService } from 'src/app/service/token.service';
import Swal from 'sweetalert2'


@Component({
  selector: 'app-landing',
  templateUrl: './landing.component.html',
  styleUrls: ['./landing.component.css']
})
export class LandingComponent implements OnInit {
  num: number = 0;
  url: string;
  userName: any;


  constructor(private router: Router,private sharedVAriables:SharedVariablesService,private tokenservice: TokenserviceService,private httpservice: HttpService,private fullscreenservice:FullScreenService) { }

  ngOnInit(): void {
    this.tokenservice.setheading("INSTRUCTIONS")
    this.checkIsMcq();
    this.setupPageVisibilityListener();
  }

  mcqPage()
  {
    this.fullscreenservice.fullScreenMethod()
      this.router.navigate(['./student/mcq'],{skipLocationChange:true});  
  }

  prgLandingPage(){
    this.fullscreenservice.fullScreenMethod()
    this.router.navigate(['./student/programmingLanding'],{skipLocationChange:true});
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
          icon: "warning", 
          title: "Tab is Not Hidded",
        })
      } else 
      {
        Swal.fire({
          icon: 'warning',
          title: 'Warning!',
          text: 'Cannot Switch Tab/Window. It will be considered as malpractice and Always Recorded',
          showCancelButton: false,
          confirmButtonText: 'OK',
          confirmButtonColor: '#d33'
        });
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
/////////////////////////////////////////////TAB SWITCHING FINISHES HERE ////////////////////////////////////
  checkIsMcq()
  {
    this.url="http://localhost:8085/register"
    this.userName=this.tokenservice.getUsername()
      this.httpservice.getDetailsByUserName<any>(this.userName,this.url).subscribe(
        (response) => {
          this.tokenservice.setIsMcq(response.ismcq)
        },
        (error) => {
          console.error('Error:', error);
        }
      );
  }


}
