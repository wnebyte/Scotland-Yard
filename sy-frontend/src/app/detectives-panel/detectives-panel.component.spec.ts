import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DetectivesPanelComponent } from './detectives-panel.component';

describe('DetectivesPanelComponent', () => {
  let component: DetectivesPanelComponent;
  let fixture: ComponentFixture<DetectivesPanelComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DetectivesPanelComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DetectivesPanelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
