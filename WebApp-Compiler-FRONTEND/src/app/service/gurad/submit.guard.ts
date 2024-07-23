import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { TokenserviceService } from '../token.service';
import Swal from 'sweetalert2'

@Injectable({
  providedIn: 'root'
})
export class SubmitGuard implements CanActivate {
  constructor(private tokenservice: TokenserviceService,private router: Router){}

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
      const issubmit = this.tokenservice.getSubmit();
      if (issubmit === 'YES') {
        Swal.fire({
          toast: true,
          position: "top-end",
          showConfirmButton: false,
          timer: 1000,
          icon: "success",
          title: "Exam Submitted",
        })
        return false;
      } else {
        return true;
      }
  }
  
}
