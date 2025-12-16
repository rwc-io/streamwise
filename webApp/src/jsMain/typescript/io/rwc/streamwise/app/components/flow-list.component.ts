import {Component, effect, input, output, signal} from "@angular/core";

import * as streamwise from '@streamwise';
import {MatIconModule} from '@angular/material/icon';

@Component({
  templateUrl: './flow-list.component.html',
  styleUrls: ['./flow-list.component.scss'],
  standalone: true,
  imports: [
    MatIconModule
  ],
  selector: 'flow-list',
})
class FlowListComponent extends streamwise.FlowListComponent {
  flows = input.required<Array<any>>();
  outputExcludedFlows = output<any>();

  constructor() {
    const internalExcludedFlows = signal<any>(null)
    super(internalExcludedFlows);

    // Emit through Angular output whenever the signal changes
    // We can't just write to the signal, because we need to pass that
    // output ref to Kotlin … but … we can't initialize the output in
    // the constructor, only in the property initializer. Because we
    // can't refer to 'this' until super, we can't pass it to kotlin.
    effect(() => {
      this.outputExcludedFlows.emit(internalExcludedFlows());
    });
  }
}

export {FlowListComponent};
