import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, Subject } from 'rxjs';
import { LogoutService } from './logout.service';

@Injectable({
  providedIn: 'root',
})
export class TimerService {
  constructor(private router: Router,private logout:LogoutService){}
  private timer: any;
  private minutes: number=0;
  private seconds: number = 0;
  private timerSubject = new Subject<string>();

  settime(time:number)
  {
    this.minutes=time;
  }
  settimesecond(seconds:number)
  {
    this.seconds=seconds;
  }
  gettime()
  {
    return this.minutes
  }
//gettimer function

  getTimer(): Observable<string> 
  {
    return this.timerSubject.asObservable();
  }

  startTimer(): void 
  {
    this.timer = setInterval(() => {
      if (this.minutes === 0 && this.seconds === 0) {
        this.stopTimer();
        this.timerSubject.next('00:00');
        this.logout.logoutt()
        this.router.navigate(['./auth/login'],{skipLocationChange:true});
      } else {
        this.updateTimer();
        this.timerSubject.next(this.formatTime());
      }
    }, 1000);
  }

  startwithoutupdationTimer(): void 
  {
    this.timer = setInterval(() => {
      if (this.minutes === 0 && this.seconds === 0) {
        this.stopTimer();
        this.timerSubject.next('00:00');
        this.logout.logoutt()
        this.router.navigate(['./auth/login'],{skipLocationChange:true});
      }
    }, 1000);
  }

  stopTimer(): void 
  {
    clearInterval(this.timer);
  }

  private updateTimer(): void {
    if (this.seconds === 0) {
      this.minutes--;
      this.seconds = 59;
    } else {
      this.seconds--;
    }
  }

  private formatTime(): string {
    return `${this.minutes}:${this.seconds < 10 ? '0' + this.seconds : this.seconds}`;
  }
}
