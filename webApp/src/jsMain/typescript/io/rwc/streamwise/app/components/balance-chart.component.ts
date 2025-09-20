import {Component, computed, effect, inject, signal, WritableSignal} from "@angular/core";
import {BaseChartDirective} from "ng2-charts";

import * as streamwise from '@streamwise';
import {FlowsService} from "../flows/flows-service";

@Component({
  templateUrl: './balance-chart.component.html',
  standalone: true,
  imports: [BaseChartDirective],
  selector: 'balance-chart',
})
class BalanceChartComponent extends streamwise.BalanceChartComponent {
  readonly flowsService = inject(FlowsService);

  constructor() {
    const theBalances: WritableSignal<Array<any>> = signal([]);
    super(theBalances);
    this.balances = theBalances;

    effect(() => {
      this.listenToBalances(this.flowsService.flowBundles())
    });
  }

  balances: WritableSignal<Array<any>>;
  chartData = computed(() => this.computeChartData(this.balances()));
}

export {BalanceChartComponent};
