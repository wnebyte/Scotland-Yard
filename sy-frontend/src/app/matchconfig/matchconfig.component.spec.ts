import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MatchconfigComponent } from './matchconfig.component';

describe('MatchconfigComponent', () => {
  let component: MatchconfigComponent;
  let fixture: ComponentFixture<MatchconfigComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MatchconfigComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MatchconfigComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
