import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { McqVerifyComponent } from './mcq-verify.component';

describe('McqVerifyComponent', () => {
  let component: McqVerifyComponent;
  let fixture: ComponentFixture<McqVerifyComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ McqVerifyComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(McqVerifyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
