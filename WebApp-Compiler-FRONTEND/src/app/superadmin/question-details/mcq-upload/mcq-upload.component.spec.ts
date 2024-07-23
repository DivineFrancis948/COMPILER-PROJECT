import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { McqUploadComponent } from './mcq-upload.component';

describe('McqUploadComponent', () => {
  let component: McqUploadComponent;
  let fixture: ComponentFixture<McqUploadComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ McqUploadComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(McqUploadComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
