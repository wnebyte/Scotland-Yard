import { TestBed } from '@angular/core/testing';

import { MatchManagementService } from './match-management.service';

describe('MatchManagementServiceService', () => {
  let service: MatchManagementService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MatchManagementService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
