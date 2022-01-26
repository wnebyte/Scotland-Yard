import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule }from '@angular/forms';

import {MaterialModule} from './material.module';


import { AppComponent } from './app.component';
import { MatchconfigComponent } from './matchconfig/matchconfig.component';
import { AppRoutingModule } from './app-routing.module';
import { MatchListComponent, MatchListNameDialog } from './match-list/match-list.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatchComponent } from './match/match.component';
import { BoardComponent } from './board/board.component';
import { AvailableMovesComponent } from './available-moves/available-moves.component';
import { MatchStatePanelComponent } from './match-state-panel/match-state-panel.component';
import { MoveHistoryPanelComponent } from './move-history-panel/move-history-panel.component';
import { DetectivesPanelComponent } from './detectives-panel/detectives-panel.component';

@NgModule({
  declarations: [
    AppComponent,
    MatchconfigComponent,
    MatchListComponent,
	MatchListNameDialog,
	MatchComponent,
	BoardComponent,
	AvailableMovesComponent,
	MatchStatePanelComponent,
	MoveHistoryPanelComponent,
	DetectivesPanelComponent
  ],
  imports: [
  	MaterialModule,
    BrowserModule,
	HttpClientModule,
	FormsModule,
	ReactiveFormsModule,
	AppRoutingModule,
	BrowserAnimationsModule
  ],
  exports: [
/*	MaterialModule,
    BrowserModule,
	HttpClientModule,
	FormsModule,
	ReactiveFormsModule,
	AppRoutingModule,
	BrowserAnimationsModule*/
	],
  providers: [{ provide: Window, useValue: window }],
  bootstrap: [AppComponent]
})
export class AppModule { }
