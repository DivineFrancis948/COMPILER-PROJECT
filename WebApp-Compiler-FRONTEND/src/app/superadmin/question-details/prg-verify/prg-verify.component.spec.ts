import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PrgVerifyComponent } from './prg-verify.component';

describe('PrgVerifyComponent', () => {
  let component: PrgVerifyComponent;
  let fixture: ComponentFixture<PrgVerifyComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PrgVerifyComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PrgVerifyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
