import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { McqUpdateComponent } from './mcq-update.component';

describe('McqUpdateComponent', () => {
  let component: McqUpdateComponent;
  let fixture: ComponentFixture<McqUpdateComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ McqUpdateComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(McqUpdateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
