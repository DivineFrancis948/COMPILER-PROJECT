import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AddPRGQuestionComponent } from './add-prgquestion.component';

describe('AddPRGQuestionComponent', () => {
  let component: AddPRGQuestionComponent;
  let fixture: ComponentFixture<AddPRGQuestionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AddPRGQuestionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddPRGQuestionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
