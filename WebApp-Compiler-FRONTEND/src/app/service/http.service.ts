import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { RegistrationDTO } from '../models/registration-dto';
import { FileUpload } from '../models/file-upload.model';


@Injectable({
  providedIn: 'root'
})
export class HttpService 
{
  baseUrlForQuestion:any = "http://localhost:8086/question";

  constructor(private http: HttpClient) 
  { 

  }

  public postdata<T>(url: string, data: any): Observable<T> 
  {
    return this.http.post<T>(url,data);
  }
  public updatedata<T>(url: string, data: any): Observable<T> 
  {
    return this.http.put<T>(url,data);
  }

  public execute(url : string,obj:any):Observable<Object>{
    return this.http.post(url,obj);
  }

  public getAllQuestions<T>(url: string,id: any): Observable<T> {
    return this.http.get<T>(url+id);
}
public getbyurlOnly<T>(url: string): Observable<T> {
  return this.http.get<T>(url);
}

getDetailsByUserName<T>(userName: string,apiurl:string): Observable<T> {
  const url = `${apiurl}/checkismcq/${userName}`;
  return this.http.get<T>(url);
}
public updateByDto<T>(url: string, Dto: any): Observable<T> {
  return this.http.post<T>( url  , Dto);
}
public deleteByUserName<T>(url: string, id1: any): Observable<T> {
  return this.http.delete<T>( url + id1 );
}
public getByUserName<T>(url: string, userName: any): Observable<T> {

  return this.http.get<T>(url + userName);
}

public saveExcel<T>(url: string, fileUploadObj: FileUpload): Observable<T> {
  // return this.http.post<T>( url  , JSON.stringify(registrationObj));
  return this.http.post<T>( url  , fileUploadObj);
}

public saveQuestionExcel<T>(url: string, fileUploadObj: FileUpload): Observable<T> {
  // return this.http.post<T>( url  , JSON.stringify(registrationObj));
  return this.http.post<T>( url  , fileUploadObj);
}

public updatedTabSwitching(userName:any): Observable<any> {
  return Observable.create((observer) => {
    this.http.get(`${this.baseUrlForQuestion}/updateTabSwitchingStatus/${userName}`)
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

public downloadPdf<T>(url: string, params?: any): Observable<T> {
  return this.http.post<T>( url, JSON.stringify(params));
}

public getdata<T>(url: string): Observable<T> {
  return this.http.get<T>(url);
}


}
