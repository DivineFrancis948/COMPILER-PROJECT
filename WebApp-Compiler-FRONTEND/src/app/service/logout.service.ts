import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpService } from './http.service';
import { TokenserviceService } from './token.service';
import { TimerService } from './timer.service';

@Injectable({
  providedIn: 'root'
})
export class LogoutService 
{
  constructor(private tokenservice: TokenserviceService,private route:Router) 
  { }

  logoutt()
  {
    this.tokenservice.setToken("null")
    this.tokenservice.setUsername("username")
    this.tokenservice.setheading("null")
    this.route.navigate(['./auth/login'],{skipLocationChange:true});
  }

}
