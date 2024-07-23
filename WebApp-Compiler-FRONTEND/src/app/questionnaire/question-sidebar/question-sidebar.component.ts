import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { LogoutService } from 'src/app/service/logout.service';
import { TokenserviceService } from 'src/app/service/token.service';

@Component({
  selector: 'app-question-sidebar',
  templateUrl: './question-sidebar.component.html',
  styleUrls: ['./question-sidebar.component.css']
})
export class QuestionSidebarComponent implements OnInit {
  constructor(private router: Router,private token:TokenserviceService,private logoutt:LogoutService) { }
  isSidebarOpen: boolean = false;
  username: any;


downloadFile() 
{

}
uploadFile() {
}
listQuestions() {
}

goToDashboard() 
{
  this.router.navigate(['./supadmin/landing'],{skipLocationChange:true});

  
}


  ngOnInit(): void 
  {
    this.username=this.token.getUsername()
  }
  toggleSidebar() 
  {
    this.isSidebarOpen = !this.isSidebarOpen;
  }
  logOut()
  {
    this.logoutt.logoutt();

  }
  addQuestion()
  {
    this.router.navigate(['./question/addquestion'],{skipLocationChange:true});

  }
  codeBase()
  {
    this.router.navigate(['./student/programmingLanding'],{skipLocationChange:true});

  }


}
