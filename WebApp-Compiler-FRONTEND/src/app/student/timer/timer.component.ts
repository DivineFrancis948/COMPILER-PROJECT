import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { TimerService } from 'src/app/service/timer.service';

@Component({
  selector: 'app-timer',
  templateUrl: './timer.component.html',
  styleUrls: ['./timer.component.css']
})
export class TimerComponent implements OnInit, OnDestroy {
  time: string;
  private timerSubscription: Subscription;

  constructor(private timerService: TimerService) {}

  ngOnInit(): void {
    this.timerSubscription = this.timerService.getTimer().subscribe(time => {
      this.time = time;
    });

    this.timerService.startwithoutupdationTimer();
  }

  ngOnDestroy(): void {
    this.timerSubscription.unsubscribe();
  }
}
