import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FileUpload } from 'src/app/models/file-upload.model';
import { RegistrationDTO } from 'src/app/models/registration-dto';
import { SuperAdminService } from 'src/app/service/super-admin.service';
import Swal from 'sweetalert2'

declare var $: any;
declare var jQuery: any;
@Component({
  selector: 'app-excel-upload',
  templateUrl: './excel-upload.component.html',
  styleUrls: ['./excel-upload.component.css']
})
export class ExcelUploadComponent implements OnInit {

  selectedFile: File | null = null;
  displayedFilePath: string = 'Choose file';
  fileList3: any;
  fileData: any;

  fileUploadObj: FileUpload = new FileUpload();

  constructor(private router: Router, private superAdminService: SuperAdminService) { }

  ngOnInit(): void {
    //to load placeholder
    this.clear();
  }


  backToList() {
    this.router.navigate(['./supadmin/details'],{skipLocationChange:true});
  }
  clear() {
    $("#file").val("");
    $(".tempFileLabel").css("display", "none");
    $(".originalFileLabel").css("display", "block");
  }
  onFileSelected(event: any): void {
    const fileInput = event.target;
    if (fileInput.files && fileInput.files.length > 0) {
      this.displayedFilePath = fileInput.files[0].name;
    } else {
      this.displayedFilePath = 'Choose file';
    }
    $(".tempFileLabel").css("display", "block");
    $(".originalFileLabel").css("display", "none");
    $(".tempFileLabel").text(this.displayedFilePath);
    this.selectedFile = event.target.files[0];
    // $(".originalFileLabel").css("display", "none");
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
        // this.toastr.warning("File is empty!!. Please upload another file", '', { positionClass: 'toast-bottom-right' });
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

  upload(): void {
    
    if (this.selectedFile) {
      const isExcelFile = this.isExcelFile(this.selectedFile.name);
      if(isExcelFile){
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
            });
            this.backToList();
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

  //checking file extension
  isExcelFile(fileName: string): boolean {
    const allowedExtensions = ["xls", "xlsx"]; 
    const fileExtension = fileName.split(".").pop()?.toLowerCase();
    return fileExtension && allowedExtensions.includes(fileExtension);
  }


}
