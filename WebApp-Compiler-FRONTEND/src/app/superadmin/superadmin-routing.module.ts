import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { DetailsComponent } from './details/details.component';
import { LandingComponent } from './landing/landing.component';
import { McqComponent } from './mcq/mcq.component';
import { SetAdminComponent } from './details/set-admin/set-admin.component';
import { VerifyComponent } from './details/verify/verify.component';
import { UpdateComponent } from './details/update/update.component';
import { AllDetailsComponent } from './details/all-details/all-details.component';

import { ExcelUploadComponent } from './details/excel-upload/excel-upload.component';

import { PrglandingComponent } from './prglanding/prglanding.component';
import { CompileAddedCodeComponent } from './compile-added-code/compile-added-code.component';
import { QuestionDetailsComponent } from './question-details/question-details.component';
import { McqUpdateComponent } from './question-details/mcq-update/mcq-update.component';
import { PrgUpdateComponent } from './question-details/prg-update/prg-update.component';
import { McqVerifyComponent } from './question-details/mcq-verify/mcq-verify.component';
import { PrgVerifyComponent } from './question-details/prg-verify/prg-verify.component';

import { McqUploadComponent } from './question-details/mcq-upload/mcq-upload.component';
import { PrgUploadComponent } from './question-details/prg-upload/prg-upload.component';

import { UploadComponent } from './upload/upload.component';
import { DownloadComponent } from './download/download.component';
import { AddQuestionComponent } from './add-question/add-question.component';
import { AddPRGQuestionComponent } from './add-prgquestion/add-prgquestion.component';
import { AddMCQQuestionComponent } from './add-mcqquestion/add-mcqquestion.component';
import { AuthGuard } from '../service/gurad/auth.guard';




const routes: Routes = 
[
  {path:"landing",component:LandingComponent},
  {path:"details",component:DetailsComponent},
  {path:"addquestion",component:AddQuestionComponent},

  {path:"mcq",component:McqComponent},
  {path:"programmigquestionlist",component:PrglandingComponent},
  {path:"details/verify/:userName",component:VerifyComponent},
  {path:"details/setAdmin/:userName",component:SetAdminComponent},
  {path:"details/update/:userName",component:UpdateComponent},
  {path:"details/allDetails/:userName",component:AllDetailsComponent},

  {path:"details/excelUpload",component:ExcelUploadComponent},
  {path:"upload",component:UploadComponent},
  {path:"download",component:DownloadComponent},


  {path:"questionDetails",component:QuestionDetailsComponent,canActivate: [AuthGuard] },
  {path:"questionDetails/updateMcq/:questionId",component:McqUpdateComponent,canActivate: [AuthGuard] },
  {path:"questionDetails/updatePrg/:questionId",component:PrgUpdateComponent,canActivate: [AuthGuard] },
  {path:"questionDetails/verifyMcq/:questionId",component:McqVerifyComponent,canActivate: [AuthGuard] },
  {path:"questionDetails/verifyPrg/:questionId",component:PrgVerifyComponent,canActivate: [AuthGuard] },
  {path:"addtestcaseoutput/:selectedQuestionId/:selectedQuestion/:selectedQuestionHeading",component:CompileAddedCodeComponent,canActivate: [AuthGuard] },
  
  {path:"questionDetails/mcqUpload",component:McqUploadComponent,canActivate: [AuthGuard] },
  {path:"questionDetails/prgUpload",component:PrgUploadComponent,canActivate: [AuthGuard] },
  {
    path:"program/:questionid",component:AddPRGQuestionComponent,canActivate: [AuthGuard] 
  },
  {
    path:"mcq/:questionid",component:AddMCQQuestionComponent,canActivate: [AuthGuard] 
  }

];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class SuperadminRoutingModule { }
