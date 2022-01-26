import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {MatchconfigComponent} from './matchconfig/matchconfig.component';
import {MatchListComponent} from './match-list/match-list.component';
import {MatchComponent} from './match/match.component';

const routes: Routes = [
	{path: 'newmatch', component: MatchconfigComponent},
	{path: '', component: MatchListComponent},
	{path: 'match', component: MatchComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
