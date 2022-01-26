import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MoveHistoryPanelComponent } from './move-history-panel.component';

describe('MoveHistoryPanelComponent', () => {
  let component: MoveHistoryPanelComponent;
  let fixture: ComponentFixture<MoveHistoryPanelComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MoveHistoryPanelComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MoveHistoryPanelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
