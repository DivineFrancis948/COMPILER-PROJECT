import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FileUpload } from 'src/app/models/file-upload.model';
import { SuperAdminService } from 'src/app/service/super-admin.service';
import { TokenserviceService } from 'src/app/service/token.service';
import Swal from 'sweetalert2'

declare var $: any;
@Component({
  selector: 'app-upload',
  templateUrl: './upload.component.html',
  styleUrls: ['./upload.component.css']
})
export class UploadComponent implements OnInit {

  selectedFile: File | null = null;
  displayedFilePath: string = 'Choose file';
  fileList3: any;
  fileData: any;

  fileUploadObj: FileUpload = new FileUpload();

constructor( private superAdminService: SuperAdminService,private router: Router,private tokenservice: TokenserviceService) { }

ngOnInit(): void 
{
  $("#file").val("");
  $(".tempFileLabel").css("display", "none");
  $(".originalFileLabel").css("display", "block");
}

importMCQQuestions() 
{
  if (this.selectedFile) {
    const isExcelFile = this.isExcelFile(this.selectedFile.name);
    if(isExcelFile){
      this.fileUploadObj.userName=this.tokenservice.getUsername();
      this.superAdminService.mcqExcelUpload(this.fileUploadObj).subscribe(
        (response) => {
          Swal.fire({
            background: "#2BD448",
            color: "#fff",
            toast: true,
            position: "center",
            showConfirmButton: true, 
            title: "File Uploaded",
            icon: "success",
            iconColor: "#fff",
            width: 'auto'
          }).then((result) => {
            if (result.isConfirmed) {
                // location.reload();
            }
        });
        },
        (error) => {
          Swal.fire({
            background: "red",
            color: "#fff",
            toast: true,
            position: "center",
            showConfirmButton: false,
            timer: 2000,
            icon: "error",
            title: "Error Uploading File",
            iconColor: "#fff"
          })
        }
      );
    }else{
      Swal.fire({
        background: "red", color: "#fff", toast: true, position: "center", showConfirmButton: false, 
        timer: 2000, icon: "error", title: "File type should be 'xls' or 'xlsx' ", iconColor: "#fff"
      })
    }
  }else{
    Swal.fire({
      background: "red", color: "#fff", toast: true, position: "center", showConfirmButton: false, 
      timer: 2000, icon: "error", title: "File Not Found", iconColor: "#fff"
    })
  }
}

importPRGQuestions() 
{
  if (this.selectedFile) {
    const isExcelFile = this.isExcelFile(this.selectedFile.name);
    if(isExcelFile){
      this.fileUploadObj.userName=this.tokenservice.getUsername();
      this.superAdminService.prgExcelUpload(this.fileUploadObj).subscribe(
        (response) => {
          Swal.fire({
            background: "#2BD448",
            color: "#fff",
            toast: true,
            position: "center",
            showConfirmButton: true, 
            // timer: 2000,
            title: "File Uploaded",
            icon: "success",
            iconColor: "#fff",
            width: 'auto'
          }).then((result) => {
            if (result.isConfirmed) {
                // location.reload();
            }
        });
        },
        (error) => {
          Swal.fire({
            background: "red",
            color: "#fff",
            toast: true,
            position: "center",
            showConfirmButton: false,
            timer: 2000,
            icon: "error",
            title: "Error Uploading File",
            iconColor: "#fff"
          })
        }
      );
    }else{
      Swal.fire({
        background: "red", color: "#fff", toast: true, position: "center", showConfirmButton: false, 
        timer: 2000, icon: "error", title: "File type should be 'xls' or 'xlsx' ", iconColor: "#fff"
      })
    }
  }else{
    Swal.fire({
      background: "red", color: "#fff", toast: true, position: "center", showConfirmButton: false, 
      timer: 2000, icon: "error", title: "File Not Found", iconColor: "#fff"
    })
  }
}
importStudentDetails() 
{
  if (this.selectedFile) {
    const isExcelFile = this.isExcelFile(this.selectedFile.name);
    if(isExcelFile){
      // this.fileUploadObj.userName="admin1";
      this.superAdminService.saveExcelFile(this.fileUploadObj).subscribe(
        (response) => {
          Swal.fire({
            background: "#2BD448",
            color: "#fff",
            toast: true,
            position: "center",
            showConfirmButton: true,
            width: '35%',
            html: this.generateDetailsHtml(response.details),
        }).then((result) => {
            if (result.isConfirmed) {
                // location.reload();
            }
        });
        },
        (error) => {
          Swal.fire({
            background: "red",
            color: "#fff",
            toast: true,
            position: "center",
            showConfirmButton: false,
            timer: 2000,
            icon: "error",
            title: "Error Uploading File",
            iconColor: "#fff"
          })
        }
      );
    }else{
      Swal.fire({
        background: "red", color: "#fff", toast: true, position: "center", showConfirmButton: false, 
        timer: 2000, icon: "error", title: "File type should be 'xls' or 'xlsx' ", iconColor: "#fff"
      })
    }
  }else{
    Swal.fire({
      background: "red", color: "#fff", toast: true, position: "center", showConfirmButton: false, 
      timer: 2000, icon: "error", title: "File Not Found", iconColor: "#fff"
    })
  }
}

importQuestionnair() 
{
  if (this.selectedFile) {
    const isExcelFile = this.isExcelFile(this.selectedFile.name);
    if(isExcelFile){
      this.superAdminService.saveExcelFileQuestionnaire(this.fileUploadObj).subscribe(
        (response) => {
          Swal.fire({
            background: "#2BD448",
            color: "#fff",
            toast: true,
            position: "center",
            showConfirmButton: true,
            width: '35%',
            html: this.generateDetailsHtml(response.details),
        }).then((result) => {
            if (result.isConfirmed) {
                // location.reload();
            }
        });
        },
        (error) => {
          Swal.fire({
            background: "red",
            color: "#fff",
            toast: true,
            position: "center",
            showConfirmButton: false,
            timer: 2000,
            icon: "error",
            title: "Error Uploading File",
            iconColor: "#fff"
          })
        }
      );
    }else{
      Swal.fire({
        background: "red", color: "#fff", toast: true, position: "center", showConfirmButton: false, 
        timer: 2000, icon: "error", title: "File type should be 'xls' or 'xlsx' ", iconColor: "#fff"
      })
    }
  }else{
    Swal.fire({
      background: "red", color: "#fff", toast: true, position: "center", showConfirmButton: false, 
      timer: 2000, icon: "error", title: "File Not Found", iconColor: "#fff"
    })
  }
}

/////////

  //display not created usernames using excel
  generateDetailsHtml(details: any[]): string {
    if (!details || details.length === 0) {
      let htm ='<h1 style="text-align:center; font-weight: bold">File Uploaded</h1>'
      return htm;
    }

    let html = '<div> <h1 style="text-align:center; font-weight: bold">File Uploaded</h1><h2 style="color: red">Records not created: </h2>';
    details.forEach((entry: any) => {
      html += `<p style="font-size: large; font-weight: bold; line-space:2">${entry.userName} <strong>:</strong> ${entry.message}</p>`;
    });
    html += '</div>';

    return html;
}

  //checking file extension
  isExcelFile(fileName: string): boolean {
    const allowedExtensions = ["xls", "xlsx"]; 
    const fileExtension = fileName.split(".").pop()?.toLowerCase();
    return fileExtension && allowedExtensions.includes(fileExtension);
  }
///////////////


onFileSelected(event: any,type: any): void {
  const fileInput = event.target;
  if (fileInput.files && fileInput.files.length > 0) {
    this.displayedFilePath = fileInput.files[0].name;
  } else {
    this.displayedFilePath = 'Choose file';
  }

  console.log(type);
  if(type=="mcq"){
    $(".tempFileLabel_MCQ").css("display", "block");
    $(".originalFileLabel_MCQ").css("display", "none");
    $(".tempFileLabel_MCQ").text(this.displayedFilePath);
  }else if(type=="prg"){
    $(".tempFileLabel_PRG").css("display", "block");
    $(".originalFileLabel_PRG").css("display", "none");
    $(".tempFileLabel_PRG").text(this.displayedFilePath);
  }else if(type=="student"){
    $(".tempFileLabel_STU").css("display", "block");
    $(".originalFileLabel_STU").css("display", "none");
    $(".tempFileLabel_STU").text(this.displayedFilePath);
  }else{
    $(".tempFileLabel_Q").css("display", "block");
    $(".originalFileLabel_Q").css("display", "none");
    $(".tempFileLabel_Q").text(this.displayedFilePath);
  }
  // $(".tempFileLabel").css("display", "block");
  // $(".originalFileLabel").css("display", "none");
  // $(".tempFileLabel").text(this.displayedFilePath);

  this.selectedFile = event.target.files[0];
  this.fileList3 = event.target.files;
  if (this.fileList3[0].size > 10485760) {
    Swal.fire("File Size Exceeds 10 MB Please Upload Another File.")
    this.fileList3 = null;
  } else {
    let reader = new FileReader();
    let file = this.fileList3[0];
    reader.readAsBinaryString(file);
    var byteString;
    reader.onload = (file) => {
      let temp = btoa(reader.result.toString());
     if(temp =="" || temp ==null || temp == undefined){
      Swal.fire("File Is Empty! Please Upload Another File.")
     }else{
      this.fileData = temp;
      this.fileUploadObj.fileData = temp;
      this.fileUploadObj.fileName = this.fileList3[0].name;
      if(this.fileList3[0].name.length>50){
        $("#fileUploader").text(this.fileList3[0].name.substring(0,30)+ "........");
      }else{
        $("#fileUploader").text(this.fileList3[0].name);
      }
     }
    };
  }
}

}
