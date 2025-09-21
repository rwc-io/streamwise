import {Component, effect, inject, signal, WritableSignal} from '@angular/core';
import {RouterOutlet} from '@angular/router';

import * as streamwise from '@streamwise';
import {BalanceChartComponent} from "./components/balance-chart.component";
import {AuthComponent} from "./components/auth.component";
import {MatDrawer, MatDrawerContainer} from "@angular/material/sidenav";
import {MatIconButton} from "@angular/material/button";
import {MatToolbar} from "@angular/material/toolbar";
import {MatIcon} from "@angular/material/icon";
import {FlowListComponent} from "./components/flow-list.component";
import {FlowsService} from "./flows/flows-service";

streamwise.checkAuthRedirectResult()

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, AuthComponent, BalanceChartComponent, MatDrawerContainer, MatDrawer, MatToolbar, MatIcon, MatIconButton, FlowListComponent, BalanceChartComponent],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App extends streamwise.App {
  protected title = 'streamwise';

  readonly flowsService = inject(FlowsService);
  readonly balances: WritableSignal<Array<any>> = signal([]);

  constructor() {
    super()

    effect(() => {
      this.realizeFlowsToBalances(this.balances, this.flowsService.flows())
    });
  }
}
