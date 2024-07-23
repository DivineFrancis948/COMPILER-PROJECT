import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { McqDetailsComponent } from './mcq-details.component';

describe('McqDetailsComponent', () => {
  let component: McqDetailsComponent;
  let fixture: ComponentFixture<McqDetailsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ McqDetailsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(McqDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
