import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PrglandingComponent } from './prglanding.component';

describe('PrglandingComponent', () => {
  let component: PrglandingComponent;
  let fixture: ComponentFixture<PrglandingComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PrglandingComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PrglandingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
