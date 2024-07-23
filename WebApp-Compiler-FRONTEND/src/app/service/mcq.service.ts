import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
@Injectable({
  providedIn: 'root'
})
export class McqService {
 

  baseUrlForAdmin:any = "http://localhost:8086/question";
  baseUrlForUser:any ="http://localhost:8087/student"

  constructor(private http:HttpClient) { }

  // public addQusetion(question:any){
  //   return this.http.post(`${this.baseUrlForAdmin}/question/create`,question)
  // }


  //getting mcq question from question table(question,questiontype)
  public getMcqQusetionWithoutOption(questionid:any): Observable<any> {
    return Observable.create((observer) => {
      this.http.get(`${this.baseUrlForAdmin}/getMcqWithoutOption/${questionid}`)
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
//addquestion to question table by questioniar
  public addQusetion(question:any): Observable<any> {
    return Observable.create((observer) => {
      this.http.post(`${this.baseUrlForAdmin}/add/mcq`,question)
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

//getting random number of question to a student
public getMcqListQuestion(studentId:any): Observable<any> {
  return Observable.create((observer) => {
    this.http.get(`${this.baseUrlForAdmin}/getRandomMcqQuestion/${studentId}`)
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

//getting mcq options from mcq question table (there is no question)
public getMcqFullWithOptions(questionId:any): Observable<any> {
  return Observable.create((observer) => {
    this.http.get(`${this.baseUrlForAdmin}/getMcqFullWithOptions/${questionId}`)
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

//save the mcq selected by student
public saveMcqAnswer(mcqAnswerDto): Observable<any> {
    return Observable.create((observer) => {
      this.http.post(`${this.baseUrlForUser}/answerCreation`,mcqAnswerDto)
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

//getting the selected answer from saved
public getSavedAnswer(userName:any,questionId:any): Observable<any> {
    return Observable.create((observer) => {
      this.http.get(`${this.baseUrlForUser}/getSelectedMcqAnswer/${userName}/${questionId}`)
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

  //updateTotalMcqMark
  public updateTotalMcqMark(userName:any): Observable<any> {
    return Observable.create((observer) => {
      this.http.get(`${this.baseUrlForUser}/updatemark/${userName}`)
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




public addQuiz(): Observable<any> {
  return Observable.create((observer) => {
    this.http.get(`${this.baseUrlForAdmin}/retriveQuiz`)
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

public addQuestionToQuiz(limit:any): Observable<any> {
  console.log(limit);
  return Observable.create((observer) => {
    this.http.get(`${this.baseUrlForAdmin}/addQuestionToQuiz/${limit}`)
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

//deleting question
public deleteQuestion(questionId:any): Observable<any> {
    return Observable.create((observer) => {
      this.http.get(`${this.baseUrlForAdmin}/deleteQuestion/${questionId}`)
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

public updateOptionAndAnswer(question:any): Observable<any> {
    return Observable.create((observer) => {
      this.http.post(`${this.baseUrlForAdmin}/update/mcq`,question)
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

public updateQuestion(question:any): Observable<any> {
    return Observable.create((observer) => {
      this.http.post(`${this.baseUrlForAdmin}/update/question`,question)
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

public getQusetionWithStatus(questionid:any): Observable<any> {
    return Observable.create((observer) => {
      this.http.get(`${this.baseUrlForAdmin}/getQusetionWithStatus/${questionid}`)
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
  //getting answer and option for admin -question update
  public getMcqFullWithOptionsAndAnswer(questionId:any): Observable<any> {
    return Observable.create((observer) => {
      this.http.get(`${this.baseUrlForAdmin}/getMcqFullWithOptionsAndAnswer/${questionId}`)
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

  //getting PRG deatils
  public getPRGQusetionDetails(questionId:any): Observable<any> {
    return Observable.create((observer) => {
      this.http.get(`${this.baseUrlForAdmin}/getPRGQuestionDetals/${questionId}`)
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

  public verifyQuestion(questionId:any): Observable<any> {
    return Observable.create((observer) => {
      this.http.get(`${this.baseUrlForAdmin}/verifyQuestion/${questionId}`)
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
public updatePRGDetails(question:any): Observable<any> {
    return Observable.create((observer) => {
      this.http.post(`${this.baseUrlForAdmin}/update/prg`,question)
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

//getting mcqStatus
public getMcqStatus(userName:any): Observable<any> {
    return Observable.create((observer) => {
      this.http.get(`${this.baseUrlForUser}/getMcqStatus/${userName}`)
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
  public getAtetndedPRGQuestionsStatus(userName:any): Observable<any> {
    return Observable.create((observer) => {
      this.http.get(`${this.baseUrlForUser}/getprgquestionattended/${userName}`)
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


  //updated mcq attended
public updatedMcqAttended(userName:any): Observable<any> {
    return Observable.create((observer) => {
      this.http.get(`${this.baseUrlForAdmin}/updatemcqattendedstatus/${userName}`)
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

    //updated prg attended
public updatedPrgAttended(userName:any): Observable<any> {
    return Observable.create((observer) => {
      this.http.get(`${this.baseUrlForAdmin}/updateprgattendedstatus/${userName}`)
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
