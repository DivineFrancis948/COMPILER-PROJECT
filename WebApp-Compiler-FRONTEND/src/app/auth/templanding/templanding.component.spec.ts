import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TemplandingComponent } from './templanding.component';

describe('TemplandingComponent', () => {
  let component: TemplandingComponent;
  let fixture: ComponentFixture<TemplandingComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TemplandingComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TemplandingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
