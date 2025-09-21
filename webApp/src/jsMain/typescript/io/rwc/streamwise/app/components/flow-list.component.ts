import {Component, inject, Signal} from "@angular/core";

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
    super();
  }

  flows: Signal<Array<any>> = this.flowsService.flows;
}

export {FlowListComponent};
