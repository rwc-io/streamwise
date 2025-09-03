import {Injectable, signal, WritableSignal} from '@angular/core';
import {User} from "@firebase/auth";

import * as streamwise from '@streamwise';

@Injectable({
  providedIn: 'root',
})
export class AuthService extends streamwise.AuthService {
  constructor() {
    const ngCurrentUserSignal: WritableSignal<User | null> = signal(null)
    super(ngCurrentUserSignal)
    this.currentUser = ngCurrentUserSignal;
  }

  readonly currentUser;
}
