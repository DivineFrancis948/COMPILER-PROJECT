import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class SharedVariablesService {


  mcqAttendedFlag : boolean = false;

  constructor() { }

  public mcqAttended(mcqAttended:boolean){
   this.mcqAttendedFlag = mcqAttended
  }

}
