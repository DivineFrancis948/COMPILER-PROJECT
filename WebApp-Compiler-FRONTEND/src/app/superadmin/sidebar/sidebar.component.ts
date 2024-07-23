import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { LogoutService } from 'src/app/service/logout.service';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent{
  constructor(private router: Router,private logoutt:LogoutService){}

  isSidebarOpen: boolean = false;

  toggleSidebar() 
  {
    this.isSidebarOpen = !this.isSidebarOpen;
  }
  goToDashboard() {
    this.router.navigate(['./supadmin/landing'],{skipLocationChange:true});
  }
  listUsers() {
    this.router.navigate(['./supadmin/details'],{skipLocationChange:true});
  }
  addQuestion() {
    this.router.navigate(['./supadmin/addquestion'],{skipLocationChange:true});
  }
  viewAnalytics() {
    this.router.navigate(['./auth/signup/questionnaire'],{skipLocationChange:true});
  }
  viewCodeBase() {
    this.router.navigate(['./supadmin/programmigquestionlist'],{skipLocationChange:true});
  }
  downloadFile() {
    this.router.navigate(['./supadmin/download'],{skipLocationChange:true});
  }
  uploadFile() {
    this.router.navigate(['./supadmin/upload'],{skipLocationChange:true});
  }
  listQuestions() {
    this.router.navigate(['./supadmin/questionDetails'],{skipLocationChange:true});
  }
  openSettings() {
    // Implementation to open settings
  }
  logout() {
    this.logoutt.logoutt();
  }

}