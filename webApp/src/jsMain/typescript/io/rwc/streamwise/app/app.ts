import {Component} from '@angular/core';
import {RouterOutlet} from '@angular/router';

import * as streamwise from '@streamwise';
import {TestComponent} from "./components/test.component";
import {AuthComponent} from "./components/auth.component";

streamwise.checkAuthRedirectResult()

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, AuthComponent, TestComponent],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected title = 'streamwise';

  constructor() {
  }
}
