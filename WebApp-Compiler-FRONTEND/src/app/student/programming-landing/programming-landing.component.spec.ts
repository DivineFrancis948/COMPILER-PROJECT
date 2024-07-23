import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProgrammingLandingComponent } from './programming-landing.component';

describe('ProgrammingLandingComponent', () => {
  let component: ProgrammingLandingComponent;
  let fixture: ComponentFixture<ProgrammingLandingComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProgrammingLandingComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProgrammingLandingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

});
