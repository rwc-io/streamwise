import {Component} from '@angular/core';
import {RouterOutlet} from '@angular/router';

import * as streamwise from '@streamwise';

@Component({
  templateUrl: './test.component.html',
  standalone: true,
  selector: 'test-component',
})
class TestComponent extends streamwise.TestComponent {
  constructor() {
    super()
  }
}

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
