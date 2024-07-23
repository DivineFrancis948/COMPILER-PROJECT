import { Component, OnInit } from '@angular/core';
import { TokenserviceService } from 'src/app/service/token.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  username: String;
  heading:String;

  constructor(private tokenservice: TokenserviceService) { }

  ngOnInit(): void {
    this.heading=this.tokenservice.getheading();
    this.username=this.tokenservice.getUsername();
  }

}
