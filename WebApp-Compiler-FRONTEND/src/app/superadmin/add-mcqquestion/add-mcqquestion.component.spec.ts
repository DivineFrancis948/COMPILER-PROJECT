import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AddMCQQuestionComponent } from './add-mcqquestion.component';

describe('AddMCQQuestionComponent', () => {
  let component: AddMCQQuestionComponent;
  let fixture: ComponentFixture<AddMCQQuestionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AddMCQQuestionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddMCQQuestionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
