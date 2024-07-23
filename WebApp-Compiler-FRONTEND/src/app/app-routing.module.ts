import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {AuthGuard} from './service/gurad/auth.guard';
import { VisitedroutesGuard } from './service/gurad/visitedroutes.guard';


const routes: Routes = 
[
  // PATH TO AUTHENTICATION MODULE
  { path: 'auth', loadChildren: () => import(`./auth/auth.module`).then(m => m.AuthModule)},
  { path: '', redirectTo: 'auth/login', pathMatch: 'full' },
  // PATH TO ADMIN MODULE
  { path: 'admin', loadChildren: () => import(`./admin/admin.module`).then(m => m.AdminModule),canActivate: [AuthGuard] },
  // PATH TO STUDENT MODULE
  { path: 'student', loadChildren: () => import(`./student/student.module`).then(m => m.StudentModule),canActivate: [AuthGuard] },
  // PATH TO QUESTION MODULE
  { path: 'question', loadChildren: () => import(`./questionnaire/questionnaire.module`).then(m => m.QuestionnaireModule),canActivate: [AuthGuard] },
  // PATH TO SUPERADMIN MODULE
  { path: 'supadmin', loadChildren: () => import(`./superadmin/superadmin.module`).then(m => m.SuperadminModule),canActivate: [AuthGuard] }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
