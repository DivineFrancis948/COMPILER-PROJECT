import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class RouteVisitService {

  constructor() { }

  private visitedRoutes: Set<string> = new Set();

  markAsVisited(routeUrl: string): void {
    this.visitedRoutes.add(routeUrl);
  }

  hasVisited(routeUrl: string): boolean {
    return this.visitedRoutes.has(routeUrl);
  }

  toNull()
  {
    this.visitedRoutes=null;
  }
  
}
