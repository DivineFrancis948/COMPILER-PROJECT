import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpService } from './http.service';

declare var $: any;
declare var jQuery: any;

@Injectable({
  providedIn: 'root'
})
export class SuperAdminService {

  constructor(private httpService : HttpService) { }

  public GetUser(userName: String): Observable<any> {
    return Observable.create((observer) => {
        const apiUrl ="http://localhost:8085/register/getUserByName/";
        this.httpService.getByUserName<any>(apiUrl, userName)
            .subscribe((tokenData: any) => {
                observer.next(tokenData);
            }, (error) => {
                // this.commonServiceProvider.GoToErrorHandling(error);
                observer.error(error)
            },
                () => {
                    observer.complete()
                });
    });
  }


  public verifyUser(registrationObj: any): Observable<any> {
    return Observable.create((observer) => {
      const apiUrl ="http://localhost:8085/register/verifyUser";
      this.httpService.updateByDto<any>(apiUrl, registrationObj)
        .subscribe((tokenData: any) => {
          console.log("gfgg");
          observer.next(tokenData);
        }, (error) => {
          console.log("41118484")
          observer.error(error)
        },
          () => {
            observer.complete()
          });
    });
  }

  public updateUser(registrationObj: any): Observable<any> {
    return Observable.create((observer) => {
      const apiUrl ="http://localhost:8085/register/updateUser";
      this.httpService.updateByDto<any>(apiUrl, registrationObj)
        .subscribe((tokenData: any) => {
          
          observer.next(tokenData);
        }, (error) => {
          
          observer.error(error)
        },
          () => {
            observer.complete()
          });
    });
  }

  public setAdmin(registrationObj: any): Observable<any> {
    return Observable.create((observer) => {
      const apiUrl ="http://localhost:8085/register/setAsAdmin";
      this.httpService.updateByDto<any>(apiUrl, registrationObj)
        .subscribe((tokenData: any) => {
          observer.next(tokenData);
        }, (error) => {
          observer.error(error)
        },
          () => {
            observer.complete()
          });
    });
  }

  public deleteUser(userName: any): Observable<any> {
    return Observable.create((observer) => {
      const apiUrl ="http://localhost:8085/register/deleteUser/";
        this.httpService.deleteByUserName<any>(apiUrl, userName)
            .subscribe((tokenData: any) => {
                observer.next(tokenData);
            }, (error) => {
                // this.commonServiceProvider.GoToErrorHandling(error);
                observer.error(error)
            },
                () => {
                    observer.complete()
                });
    });
  }

  //create using excel -registration table
  public saveExcelFile(fileUploadObj: any): Observable<any> {
    return Observable.create((observer) => {
      const apiUrl ="http://localhost:8085/register/createExcel";
        this.httpService.saveExcel<any>(apiUrl, fileUploadObj)
            .subscribe((tokenData: any) => {
                observer.next(tokenData);
            }, (error) => {
                observer.error(error)
            },
                () => {
                    observer.complete()
                });
    });
  }

    //create using excel -mcq
    public mcqExcelUpload(fileUploadObj: any): Observable<any> {
      return Observable.create((observer) => {
        const apiUrl ="http://localhost:8086/question/createExcelMcq";
          this.httpService.saveQuestionExcel<any>(apiUrl, fileUploadObj)
              .subscribe((tokenData: any) => {
                  observer.next(tokenData);
              }, (error) => {
                  observer.error(error)
              },
                  () => {
                      observer.complete()
                  });
      });
    }

    //create using excel prg
    public prgExcelUpload(fileUploadObj: any): Observable<any> {
      return Observable.create((observer) => {
        const apiUrl ="http://localhost:8086/question/createExcelPrg";
          this.httpService.saveQuestionExcel<any>(apiUrl, fileUploadObj)
              .subscribe((tokenData: any) => {
                  observer.next(tokenData);
              }, (error) => {
                  observer.error(error)
              },
                  () => {
                      observer.complete()
                  });
      });
    }

    //create using excel -registration table
    public saveExcelFileQuestionnaire(fileUploadObj: any): Observable<any> {
      return Observable.create((observer) => {
        const apiUrl ="http://localhost:8085/register/createExcelQuestionnaire";
          this.httpService.saveExcel<any>(apiUrl, fileUploadObj)
              .subscribe((tokenData: any) => {
                  observer.next(tokenData);
              }, (error) => {
                  observer.error(error)
              },
                  () => {
                      observer.complete()
                  });
      });
    }

    public pdfDownload(searchObj: any): Observable<any> {
      return Observable.create((observer) => {
          const apiUrl ="http://localhost:8085/register/student/pdfDownload";
          this.httpService.downloadPdf<any>(apiUrl, searchObj)
              .subscribe((tokenData: any) => {
                  observer.next(tokenData);
              }, (error) => {
                  // this.commonServiceProvider.GoToErrorHandling(error);
                  observer.error(error)
              },
                  () => {
                      observer.complete()
                  });
      });
    }
    public excelDownload(searchObj: any): Observable<any> {
      return Observable.create((observer) => {
          const apiUrl ="http://localhost:8085/register/student/excelDownload";
          this.httpService.downloadPdf<any>(apiUrl, searchObj)
              .subscribe((tokenData: any) => {
                  observer.next(tokenData);
              }, (error) => {
                  // this.commonServiceProvider.GoToErrorHandling(error);
                  observer.error(error)
              },
                  () => {
                      observer.complete()
                  });
      });
    }

}
