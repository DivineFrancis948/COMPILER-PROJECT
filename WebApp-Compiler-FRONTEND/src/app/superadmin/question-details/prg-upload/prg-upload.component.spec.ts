import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PrgUploadComponent } from './prg-upload.component';

describe('PrgUploadComponent', () => {
  let component: PrgUploadComponent;
  let fixture: ComponentFixture<PrgUploadComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PrgUploadComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PrgUploadComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
