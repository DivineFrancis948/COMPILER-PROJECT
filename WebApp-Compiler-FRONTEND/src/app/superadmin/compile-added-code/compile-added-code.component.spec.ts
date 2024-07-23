import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CompileAddedCodeComponent } from './compile-added-code.component';

describe('CompileAddedCodeComponent', () => {
  let component: CompileAddedCodeComponent;
  let fixture: ComponentFixture<CompileAddedCodeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CompileAddedCodeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CompileAddedCodeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
