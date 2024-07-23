import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { RouteVisitService } from '../route-visit.service';
import Swal from 'sweetalert2'

@Injectable({
  providedIn: 'root'
})
export class VisitedroutesGuard implements CanActivate 
{
  constructor
  (
    private router: Router,
    private visitedRoutesService: RouteVisitService
  ) 
  {

  }
  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree 
    {
      const routeUrl = state.url;
      console.log(state.url)
      console.log(this.visitedRoutesService.hasVisited(routeUrl))
      if (this.visitedRoutesService.hasVisited(routeUrl)) 
      {
        // If the user has already visited the route, prevent access
        console.warn(`Route ${routeUrl} has already been visited.`);
        Swal.fire({
          toast: true,
          position: "center",
          showConfirmButton: false,
          timer: 1000,
          icon: "warning",
          title: "User Already Visited",
        })
        return false;
      } else {
        // Mark the route as visited and allow access
        this.visitedRoutesService.markAsVisited(routeUrl);
        Swal.fire({
          toast: true,
          position: "center",
          showConfirmButton: false,
          timer: 1000,
          icon: "warning",
          title: "User Not Visited",
        })
        return true;
      }
  }
  
}
