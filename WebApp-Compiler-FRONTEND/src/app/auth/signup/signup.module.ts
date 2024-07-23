import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { SignupRoutingModule } from './signup-routing.module';
import { UserComponent } from './user/user.component';
import { QuestionairComponent } from './questionair/questionair.component';
import { FormsModule } from '@angular/forms';


@NgModule({
  declarations: [UserComponent, QuestionairComponent],
  imports: [
    CommonModule,
    SignupRoutingModule,
    FormsModule
  ]
})
export class SignupModule { }
