import {Component, effect} from "@angular/core";
import {BaseChartDirective} from "ng2-charts";

import * as streamwise from '@streamwise';

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
