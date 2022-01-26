import { Component, OnInit, Inject } from '@angular/core';
import { Router } from '@angular/router';
import {MatDialog, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material/dialog';
import {MatFormField} from '@angular/material/form-field'; 
import {MatTableDataSource} from '@angular/material/table';

import {filter} from 'rxjs/operators';

import {Match} from './match';
import {MatchManagementService} from '../match-management.service';

@Component({
  selector: 'app-match-list',
  templateUrl: './match-list.component.html',
  styleUrls: ['./match-list.component.css']
})
export class MatchListComponent implements OnInit {

  displayedColumns = ['match', 'board', 'detectives', 'actions'];	
  matches: Match[];
  dataSource = new MatTableDataSource<Match>([]);
  registrations: MatchRegistration[] = [];

  constructor(private mmService: MatchManagementService,
			  private router: Router,
			  private dialog: MatDialog) { }

  ngOnInit(): void {
	  this.getOpenMatches();
  }

  openNameDialog(match: Match): void {
	  const dialogRef = this.dialog.open(MatchListNameDialog, {
		width: '250px',
		data: {name: "Anonymous"}
	  });
	  dialogRef.afterClosed()
	  .pipe(
		filter(result => result != "")
	  )
	  .subscribe(
		result => this.joinMatch(match, result));
  }

  getOpenMatches(): void {
	  this.mmService.getOpenMatches()
		.subscribe(
			data => (this.dataSource.data = data)
		);
  }	 

  joinMatch(match: Match, name: String): void {
	  this.mmService.postJoinMatch(match.matchId, name)
		.pipe(filter(item => item != ""))
		.subscribe(data => this.onSuccessfulJoin(match, name, data));
  }
  
  onSuccessfulJoin(match: Match, playerName: String, pId: String) : void {
	  var reg: MatchRegistration = {
		  matchId: match.matchId,
		  playerId: pId
	  };		  
	  this.registrations.push(reg);
	  this.getOpenMatches();
	  
  }
  
  onGotoMatch(match: Match) : void {
	  if (this.hasRegistrationFor(match.matchId)) {
		  var playerId = this.registrations
			.filter(e => e.matchId == match.matchId)
			.map(e => e.playerId)[0];
		  this.router.navigate(['/match', {'matchId': match.matchId, 'playerId': playerId}]);
	  }
  }
  
  hasRegistrationFor(matchId: String) : boolean {
	  return this.registrations.map(r => r.matchId).includes(matchId);
  }

  onGotoNewMatch() : void {
	  this.router.navigate(['/newmatch', {}]);
  }
}

export interface MatchRegistration {
	matchId: String;
	playerId: String;
}

export interface DialogData {
	name: String;
}

@Component({
	selector: 'match-list-name-dialog',
	templateUrl: './match-list-name-dialog.html',
	styleUrls: ['./match-list-name-dialog.css']
})
export class MatchListNameDialog {
	
	constructor (
		public dialogRef: MatDialogRef<MatchListNameDialog>,
		@Inject(MAT_DIALOG_DATA) public data: DialogData
	) {}
	
	onCancelClick(): void {
		this.dialogRef.close("");
	}
}
