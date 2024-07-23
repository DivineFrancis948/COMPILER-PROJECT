import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AuthRoutingModule } from './auth-routing.module';
import { LoginComponent } from './login/login.component';
import { TemplandingComponent } from './templanding/templanding.component';
import { FormsModule } from '@angular/forms';


@NgModule({
  declarations: [LoginComponent, TemplandingComponent],
  imports: [
    CommonModule,
    AuthRoutingModule,
    FormsModule
  ]
})
export class AuthModule { }
