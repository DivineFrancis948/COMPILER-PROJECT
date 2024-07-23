import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PrgUpdateComponent } from './prg-update.component';

describe('PrgUpdateComponent', () => {
  let component: PrgUpdateComponent;
  let fixture: ComponentFixture<PrgUpdateComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PrgUpdateComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PrgUpdateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
