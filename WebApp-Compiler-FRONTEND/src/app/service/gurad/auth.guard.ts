import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { TokenserviceService } from '../token.service';
import Swal from 'sweetalert2'

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  constructor(private tokenservice: TokenserviceService,private router: Router){}

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
      const token = this.tokenservice.getToken() ?? null;
      if (token == 'null') {
        Swal.fire("Login First");
        this.router.navigate(['./auth/login']);
        return false;
      } else {
        return true;
      }

  }
  
}
