import { Component, OnInit } from '@angular/core';
import { TimerService } from 'src/app/service/timer.service';
import { TokenserviceService } from 'src/app/service/token.service';

@Component({
  selector: 'app-thankyou',
  templateUrl: './thankyou.component.html',
  styleUrls: ['./thankyou.component.css']
})
export class ThankyouComponent implements OnInit {

  constructor(private tokenservice: TokenserviceService,private timer:TimerService) { }

  ngOnInit(): void 
  {

    setTimeout(() => {
      this.tokenservice.setToken("null");
      this.timer.settime(0);
      this.timer.settimesecond(0);
      this.tokenservice.setSubmit("NO")
      // Add more functions if needed
    }, 10000); 
  }

}
