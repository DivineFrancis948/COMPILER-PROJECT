import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PrgDetailsComponent } from './prg-details.component';

describe('PrgDetailsComponent', () => {
  let component: PrgDetailsComponent;
  let fixture: ComponentFixture<PrgDetailsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PrgDetailsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PrgDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
