import {Component, computed, input} from "@angular/core";
import {BaseChartDirective} from "ng2-charts";

import * as streamwise from '@streamwise';

@Component({
  templateUrl: './balance-chart.component.html',
  standalone: true,
  imports: [BaseChartDirective],
  selector: 'balance-chart',
})
class BalanceChartComponent extends streamwise.BalanceChartComponent {
  balances = input.required<Array<any>>()

  constructor() {
    super();
  }

  chartData = computed(() => this.computeChartData(this.balances()));
}

export {BalanceChartComponent};
