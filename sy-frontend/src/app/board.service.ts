import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import {map} from 'rxjs/operators';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import {DomSanitizer}from '@angular/platform-browser';

import {Board} from './matchconfig/board';
import {MatchConfiguration} from './matchconfig/match-configuration';

@Injectable({
  providedIn: 'root'
})
export class BoardService {
  
  private hostServer  = window.location.hostname;
  private hostPort = '7000';
  		
  constructor(private http: HttpClient,
			  private domSanitizer: DomSanitizer) { }
  
  getBoards() : Observable<Board[]> {
	  var serviceUrl = 'http://' + this.hostServer + ':' + this.hostPort + '/boards';
	  return this.http.get<Board[]>(serviceUrl);
  }
  
  getDefaultConfiguration(boardId: String) : Observable<MatchConfiguration> {
	  var serviceUrl = 'http://' + this.hostServer + ':' + this.hostPort + '/config';
	  return this.http.get<MatchConfiguration>(serviceUrl);
  }
  
  getBoardImage() : Observable<Blob> {
	  var serviceUrl = 'http://' + this.hostServer + ':' + this.hostPort + '/map';
	  return this.http.get(serviceUrl, {responseType: 'blob'});
  }
  
  getLocation(node: number) : Observable<Location> {
	  var serviceUrl = 'http://' + this.hostServer + ':' + this.hostPort + '/location/' + node;
	  return this.http.get<Location>(serviceUrl);
  }
}

export interface Location {
	x: number;
	y: number;
}