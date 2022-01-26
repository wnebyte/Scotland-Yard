import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms'
import { Router } from '@angular/router';

import {MatFormField} from '@angular/material/form-field'; 

import {Board} from './board';
import {MatchConfiguration} from './match-configuration';
import {BoardService} from '../board.service';
import {MatchManagementService} from '../match-management.service';

@Component({
  selector: 'app-matchconfig',
  templateUrl: './matchconfig.component.html',
  styleUrls: ['./matchconfig.component.css']
})
export class MatchconfigComponent implements OnInit {

  boards : Board[];
  selectedBoard : Board;
  createdMatchUUID = '';
  configForm = this.fb.group({
	boardId: [''],
	startingPositions: [''],
	surfacingTurns: [''],
	nrOfDetectives: [''],
	nrOfTurns: [''],
	taxiPerDetective: [''],
	busPerDetective: [''],
	undergroundPerDetective: [''],
	blackPerDetective: [''],
	taxiForMrX: [''],
	busForMrX: [''],
	undergroundForMrX: [''],
	blackForMrX: ['']
  });
  nameForm = this.fb.group({
	  boardId: [''],
	  name: ['']
  });

  constructor(
	private boardService: BoardService,
	private mmService: MatchManagementService,
	private fb: FormBuilder,
	private router: Router) { }

  ngOnInit(): void {
	  this.getBoards();
  }

  onSelect(board : Board) : void {
	  this.selectedBoard = board;
	  this.getDefaultConfiguration();
  }
  
  onSubmit() : void {
	  var config: MatchConfiguration = this.configForm.getRawValue();
	  this.mmService.postCreateMatch(config)
		.subscribe(
			data => {
				this.createdMatchUUID = JSON.stringify(data);
				this.setName(data);
				this.router.navigate(['/']);
			}
		);
  }
  
  setName(id : String) : void {
	  this.mmService.postMatchName(
		id, this.nameForm.controls['name'].value)
			.subscribe(
				data => {}
			);
  }
  
  getBoards(): void { 
	  this.boardService.getBoards()
		.subscribe(
			data => {
				this.boards = data;
			}
		);
  }
  
  getDefaultConfiguration() : void {
	  this.boardService.getDefaultConfiguration(this.selectedBoard.id)
		.subscribe(
			data => {
				this.configForm.setValue(data);
			}
		);
  }
}
