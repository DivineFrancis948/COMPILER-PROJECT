import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-templanding',
  templateUrl: './templanding.component.html',
  styleUrls: ['./templanding.component.css']
})
export class TemplandingComponent implements OnInit {

  constructor(private router: Router) { }

  ngOnInit(): void 
  {

  }
  question()
  {
    this.router.navigate(['./auth/signup/questionair']);
  }
  user()
  {
    this.router.navigate(['./auth/signup/user']);
  }
  admin()
  {
    this.router.navigate(['./supadmin/details']);
  }
  addquestion()
  {
    this.router.navigate(['./question/addquestion']);

  }
  codebase()
  {
    this.router.navigate(['./student/programmingLanding']);
  }

}
