import {Component, computed, effect, inject, signal, WritableSignal} from "@angular/core";
import {BaseChartDirective} from "ng2-charts";

import * as streamwise from '@streamwise';
import {FlowsService} from "../flows/flows-service";

@Component({
  templateUrl: './flow-list.component.html',
  standalone: true,
  imports: [],
  selector: 'flow-list',
})
class FlowListComponent extends streamwise.FlowListComponent {
  readonly flowsService = inject(FlowsService);

  constructor() {
    const theFlows: WritableSignal<Array<any>> = signal([]);
    super(theFlows);
    this.flows = theFlows;

    effect(() => {
      this.listenToFlows(this.flowsService.flowBundles())
    });
  }

  flows ;
}

export {FlowListComponent};
