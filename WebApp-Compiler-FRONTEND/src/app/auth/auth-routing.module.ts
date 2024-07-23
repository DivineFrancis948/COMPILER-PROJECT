import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { TemplandingComponent } from './templanding/templanding.component';
import { VisitedroutesGuard } from '../service/gurad/visitedroutes.guard';


const routes: Routes = 
[
 {path:"login",component:LoginComponent},
 {path: "signup", loadChildren: () => import(`./signup/signup.module`).then(m => m.SignupModule)},

];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AuthRoutingModule { }
