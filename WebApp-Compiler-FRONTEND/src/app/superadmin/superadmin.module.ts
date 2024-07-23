import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { SuperadminRoutingModule } from './superadmin-routing.module';
import { DetailsComponent } from './details/details.component';
import { LandingComponent } from './landing/landing.component';
import { McqComponent } from './mcq/mcq.component';
import { SidebarComponent } from './sidebar/sidebar.component';
import { SetAdminComponent } from './details/set-admin/set-admin.component';
import { VerifyComponent } from './details/verify/verify.component';
import { UpdateComponent } from './details/update/update.component';
import { AllDetailsComponent } from './details/all-details/all-details.component';
import { ExcelUploadComponent } from './details/excel-upload/excel-upload.component';
import { PrglandingComponent } from './prglanding/prglanding.component';
import { CompileAddedCodeComponent } from './compile-added-code/compile-added-code.component';
import { QuestionDetailsComponent } from './question-details/question-details.component';
import { FormsModule } from '@angular/forms';
import { PrgVerifyComponent } from './question-details/prg-verify/prg-verify.component';
import { PrgUpdateComponent } from './question-details/prg-update/prg-update.component';
import { McqUpdateComponent } from './question-details/mcq-update/mcq-update.component';
import { McqVerifyComponent } from './question-details/mcq-verify/mcq-verify.component';

import { McqUploadComponent } from './question-details/mcq-upload/mcq-upload.component';
import { PrgUploadComponent } from './question-details/prg-upload/prg-upload.component';

import { UploadComponent } from './upload/upload.component';
import { DownloadComponent } from './download/download.component';
import { AddQuestionComponent } from './add-question/add-question.component';
import { AddMCQQuestionComponent } from './add-mcqquestion/add-mcqquestion.component';
import { AddPRGQuestionComponent } from './add-prgquestion/add-prgquestion.component';


@NgModule({
  declarations:
    [DetailsComponent,
      LandingComponent,
      McqComponent, SidebarComponent, SetAdminComponent, VerifyComponent, UpdateComponent, AllDetailsComponent,ExcelUploadComponent,

       PrglandingComponent, CompileAddedCodeComponent, QuestionDetailsComponent,PrgVerifyComponent,PrgUpdateComponent,McqUpdateComponent,McqVerifyComponent, McqUploadComponent, PrgUploadComponent, UploadComponent, DownloadComponent, AddQuestionComponent, AddMCQQuestionComponent, AddPRGQuestionComponent],


  imports: [
    CommonModule,
    SuperadminRoutingModule,
    FormsModule
  ]
})
export class SuperadminModule { }
