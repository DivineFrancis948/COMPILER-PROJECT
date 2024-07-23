import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpService } from './http.service';

@Injectable({
  providedIn: 'root'
})
export class EditorService {

  constructor(private httpService : HttpService) { }

  public ExecuteCode(editorObj: any): Observable<any> {
    return Observable.create((observer) => {
      const apiUrl ="http://localhost:8089/api/executeCode";
      this.httpService.execute(apiUrl,editorObj)
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
