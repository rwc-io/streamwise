import {Component} from '@angular/core';
import {RouterOutlet} from '@angular/router';

import * as streamwise from '@streamwise';
import {TestComponent} from "./components/test.component";

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, TestComponent],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected title = 'streamwise';
  protected num = "";

  constructor() {
    this.num = streamwise.testFunction()
  }
}
