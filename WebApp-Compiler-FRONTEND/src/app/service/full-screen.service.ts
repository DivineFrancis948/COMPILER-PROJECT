import { Injectable,Component } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class FullScreenService {

  constructor() { }

  ele=document.documentElement
  fullScreenMethod()
  {
    if(this.ele.requestFullscreen)
    {
      this.ele.requestFullscreen();
    }
  }
}
