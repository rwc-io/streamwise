import {Component} from '@angular/core';
import {RouterOutlet} from '@angular/router';

import * as streamwise from '@streamwise';
import {TestComponent} from "./components/test.component";
import {AuthComponent} from "./components/auth.component";
import {MatDrawer, MatDrawerContainer} from "@angular/material/sidenav";
import {MatIconButton} from "@angular/material/button";
import {MatToolbar} from "@angular/material/toolbar";
import {MatIcon} from "@angular/material/icon";
import {FlowListComponent} from "./components/flow-list.component";

streamwise.checkAuthRedirectResult()

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, AuthComponent, TestComponent, MatDrawerContainer, MatDrawer, MatToolbar, MatIcon, MatIconButton, FlowListComponent],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected title = 'streamwise';

  constructor() {
  }
}
