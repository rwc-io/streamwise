import {Component} from "@angular/core";

import * as streamwise from '@streamwise';

@Component({
  templateUrl: './auth.component.html',
  standalone: true,
  imports: [],
  selector: 'auth-component',
})
class AuthComponent extends streamwise.AuthComponent {
  constructor() {
    super()
  }

  protected readonly JSON = JSON;
}

export {AuthComponent};
