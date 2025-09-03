import {Component, inject} from "@angular/core";

import * as streamwise from '@streamwise';
import {AuthService} from "../auth/auth-service";

@Component({
  templateUrl: './auth.component.html',
  standalone: true,
  imports: [],
  selector: 'auth-component',
})
class AuthComponent extends streamwise.AuthComponent {
  private readonly authService = inject(AuthService);
  readonly authedUser = this.authService.currentUser;

  constructor() {
    super()
  }
}

export {AuthComponent};
