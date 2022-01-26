import { Component, OnInit, OnChanges, Input, ViewChild, ElementRef, SimpleChange } from '@angular/core'; 

import {BoardService, Location} from '../board.service';
import {MatchState} from '../match/match-state';
import {PlayerState} from '../match/player-state';

@Component({
  selector: 'app-board',
  templateUrl: './board.component.html',
  styleUrls: ['./board.component.css']
})
export class BoardComponent implements OnInit, OnChanges {

	@ViewChild('boardCanvas', {static: true})
	boardCanvas: ElementRef<HTMLCanvasElement>;
	private ctx: CanvasRenderingContext2D;
	private labelFill = "#99ffbc";
  	private map = new Image();
  	private badge = new Image();
  	private mrx = new Image();
  
 @Input()
 state: MatchState

  constructor(private window: Window,
			  private boardService: BoardService) { }

  ngOnInit(): void {
	this.ctx = this.boardCanvas.nativeElement.getContext('2d');
	this.boardCanvas.nativeElement.width =
		this.boardCanvas.nativeElement.offsetWidth;
	this.boardCanvas.nativeElement.height = 
		this.boardCanvas.nativeElement.offsetHeight;
	
	this.map.onload = () => {
		this.updateBoard();
	};
	this.badge.src = 'assets/helmet.png';
	this.mrx.src = 'assets/mrx.png';
 	let img = this.boardService.getBoardImage();
	img.subscribe(data => {
		this.map.src = URL.createObjectURL(data);
	});	
	this.ctx.font = "12px sans-serif";
  }

	ngOnChanges(changes: {[propKey: string]: SimpleChange}) {
		this.updateBoard();
	}
	
	updateBoard(): void {
		this.ctx.clearRect(0, 0, this.ctx.canvas.width, this.ctx.canvas.height);
		this.ctx.drawImage(this.map, 0, 0);
		var ps;
		for (ps of this.state.playerStates) {
			if (ps.role == "DETECTIVE") {
				this.drawDetective(ps.position, ps.name);
			}
			else if (ps.role == "MR_X") {
				this.drawMrX(ps.position);
			}
		}
	}
	
	drawDetective(position: number, name: string): void {
		let loc: Location;
		this.boardService.getLocation(position)
			.subscribe(data =>  {
				this.ctx.drawImage(this.badge, data['x'] - 20, data['y'] - 20);
				//draw name
				let text = this.ctx.measureText(name);
				this.ctx.fillStyle = this.labelFill;
				this.ctx.fillRect(data['x'] - 20, data['y'] + 20, text.width, 15);
				this.ctx.fillStyle = 'black';
				this.ctx.fillText(name, data['x'] - 20, data['y'] + 32);
			});
	}
	
	drawMrX(position: number): void {
		let loc: Location;
		this.boardService.getLocation(position)
			.subscribe(data =>  {
				this.ctx.drawImage(this.mrx, data['x'] - 20, data['y'] - 20);
			});
	}
}