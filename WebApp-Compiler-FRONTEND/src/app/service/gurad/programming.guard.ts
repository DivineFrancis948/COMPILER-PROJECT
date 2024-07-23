import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { TokenserviceService } from '../token.service';
import Swal from 'sweetalert2'

@Injectable({
  providedIn: 'root'
})
export class ProgrammingGuard implements CanActivate {
  constructor(private tokenservice: TokenserviceService,private router: Router){}

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
      const ismcq = this.tokenservice.getIsMcq();
      console.log(ismcq)
      if (ismcq === 'NO') {
        Swal.fire("Complete MCQ First");
        return false;
      } else {
        Swal.fire("Alr  eady Attended");
        return true;
      }
  }
  
}
