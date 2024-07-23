import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { UserComponent } from './user/user.component';
import { QuestionairComponent } from './questionair/questionair.component';


const routes: Routes = 
[
  {
    path:"user",component:UserComponent
  },
  {
    path:"questionnaire",component:QuestionairComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class SignupRoutingModule { }
