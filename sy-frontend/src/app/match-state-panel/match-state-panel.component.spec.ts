import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MatchStatePanelComponent } from './match-state-panel.component';

describe('MatchStatePanelComponent', () => {
  let component: MatchStatePanelComponent;
  let fixture: ComponentFixture<MatchStatePanelComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MatchStatePanelComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MatchStatePanelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
