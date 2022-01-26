import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';

import {MatchConfiguration} from './matchconfig/match-configuration';
import {Match} from './match-list/match';
import {MatchState} from './match/match-state';
import {PlayerState} from './match/player-state';
import {Move} from './available-moves/move';

@Injectable({
  providedIn: 'root'
})
export class MatchManagementService {

  //private hostServer  = 'localhost';
  private hostServer  = window.location.hostname;
  private hostPort = '7001';
  		
  constructor(private http: HttpClient) { }
  
  postCreateMatch(config: MatchConfiguration) : Observable<String> {
	  var serviceUrl = 'http://' + this.hostServer + ':' + this.hostPort + '/matches';
	  return this.http.post<String>(serviceUrl, config);
  }
  
  postMatchName(id: String, name: string) : Observable<String> {
	  var serviceUrl = 'http://' + this.hostServer
		+ ':' + this.hostPort + '/matches/'
		+ id;
	  return this.http.post<String>(serviceUrl, name);
  }
  
  getOpenMatches() : Observable<Match[]> { 
	  var serviceUrl = 'http://' + this.hostServer + ':' + this.hostPort + '/matches';
	  return this.http.get<Match[]>(serviceUrl);
  } 
  
  postJoinMatch(matchId: String, name: String) : Observable<String> {
	  var serviceUrl = 'http://' + this.hostServer
		+ ':' + this.hostPort + '/matches/'
		+ matchId + '/join';
	  return this.http.post<String>(serviceUrl, name);
  }
  
  getMatchState(matchId: string, playerId: string) : Observable<MatchState> {
	var serviceUrl = 'http://' + this.hostServer
		+ ':' + this.hostPort + '/matches/'
		+ matchId + '/state'; 
	var params = new HttpParams().set('playerId', playerId);
	return this.http.get<MatchState>(serviceUrl, {params});
  }
  
  getAvailableMoves(matchId: string, playerId: string) : Observable<Move[]> {
	var serviceUrl = 'http://' + this.hostServer
		+ ':' + this.hostPort + '/matches/'
		+ matchId + '/possiblemoves'; 
	var params = new HttpParams().set('playerId', playerId);
	return this.http.get<Move[]>(serviceUrl, {params});
  }
  
  getMoveHistory(matchId: string) : Observable<string[]> {
	var serviceUrl = 'http://' + this.hostServer
		+ ':' + this.hostPort + '/matches/'
		+ matchId + '/mrxhistory'; 
	return this.http.get<string[]>(serviceUrl);
  }
  
  getSurfacingTurns(matchId: string) : Observable<number[]> {
	var serviceUrl = 'http://' + this.hostServer
		+ ':' + this.hostPort + '/matches/'
		+ matchId + '/surfacing'; 
	return this.http.get<number[]>(serviceUrl);
  }
}
