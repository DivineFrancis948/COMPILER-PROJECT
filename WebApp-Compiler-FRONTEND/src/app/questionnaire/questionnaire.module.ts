import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { QuestionnaireRoutingModule } from './questionnaire-routing.module';
import { AddComponent } from './add/add.component';
import { FormsModule } from '@angular/forms';
import { ProgrammingComponent } from './programming/programming.component';
import { McqComponent } from './mcq/mcq.component';
import { QuestionSidebarComponent } from './question-sidebar/question-sidebar.component';


@NgModule({
  declarations: [AddComponent, ProgrammingComponent, McqComponent, QuestionSidebarComponent],
  imports: [
    CommonModule,
    QuestionnaireRoutingModule,
    FormsModule
  ]
})
export class QuestionnaireModule { }
