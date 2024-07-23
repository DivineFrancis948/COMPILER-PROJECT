import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class TokenserviceService 
{
  private TOKEN_KEY = 'auth-token';
  private USERNAME = 'username';
  private IsMCQ='YES';
  private SUBMIT='NO';
  heading:String;
  constructor() 
  {

  }
  getToken(): string | null 
  {
    return localStorage.getItem(this.TOKEN_KEY);
  } //gettoken ends here *************************************************

  getUsername(): string | null 
  {
    return localStorage.getItem(this.USERNAME);
  } //getusername ends here *************************************************

  setToken(token: string): void 
  {
    localStorage.setItem(this.TOKEN_KEY, token);
  } //set token ends here ***********************************************

  setUsername(username: string): void 
  {
    localStorage.setItem(this.USERNAME, username);
  } //set username ends here ***********************************************

  removeToken(): void 
  {
    localStorage.removeItem(this.TOKEN_KEY);
  } //remove token ends here *********************************************

  removeUsername(): void 
  {
    localStorage.removeItem(this.USERNAME);
  } //remove user ends here *********************************************
  
  getheading()
  {
    return this.heading;
  }
  setheading(head:String)
  {
    return this.heading=head;
  }
  getIsMcq(): string | null 
  {
    return localStorage.getItem(this.IsMCQ);
  } //gettoken ends here *************************************************

  setIsMcq(Ismcq:string): void
  {
    localStorage.setItem(this.IsMCQ, Ismcq);
  } //getusername ends here *************************************************

  setSubmit(submit: string): void 
  {
    localStorage.setItem(this.SUBMIT, submit);
  } //set submit ends here ***********************************************

  getSubmit(): string | null 
  {
    return localStorage.getItem(this.SUBMIT);
  } //getsubmit ends here *************************************************
}
