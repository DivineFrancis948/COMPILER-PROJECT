import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AddComponent } from './add/add.component';
import { ProgrammingComponent } from './programming/programming.component';
import { McqComponent } from './mcq/mcq.component';


const routes: Routes = 
[
  {
    path:"addquestion",component:AddComponent
  },
  {
    path:"prg/:questionid",component:ProgrammingComponent
  },
  {
    path:"mcq/:questionid",component:McqComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class QuestionnaireRoutingModule { }
