import {Component, input} from "@angular/core";

import * as streamwise from '@streamwise';

@Component({
  templateUrl: './flow-list.component.html',
  standalone: true,
  imports: [],
  selector: 'flow-list',
})
class FlowListComponent extends streamwise.FlowListComponent {
  flows = input.required<Array<any>>();

  constructor() {
    super();
  }
}

export {FlowListComponent};
