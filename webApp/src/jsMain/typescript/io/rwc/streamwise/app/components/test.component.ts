import {Component, effect, inject} from "@angular/core";
import {BaseChartDirective} from "ng2-charts";

import * as streamwise from '@streamwise';
import {AuthService} from "../auth/auth-service";

@Component({
  templateUrl: './test.component.html',
  standalone: true,
  imports: [BaseChartDirective],
  selector: 'test-component',
})
class TestComponent extends streamwise.TestComponent {
  constructor() {
    super()

    effect(() => {
      this.effecter()
    });
  }
}

export {TestComponent};
