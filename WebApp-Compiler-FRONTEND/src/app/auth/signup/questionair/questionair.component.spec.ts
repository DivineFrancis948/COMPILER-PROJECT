import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { QuestionairComponent } from './questionair.component';

describe('QuestionairComponent', () => {
  let component: QuestionairComponent;
  let fixture: ComponentFixture<QuestionairComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ QuestionairComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(QuestionairComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
