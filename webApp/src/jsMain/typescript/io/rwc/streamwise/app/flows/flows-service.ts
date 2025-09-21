import {Injectable, Signal, signal, WritableSignal} from '@angular/core';

import * as streamwise from '@streamwise';

@Injectable({
  providedIn: 'root',
})
export class FlowsService extends streamwise.FlowsService {
  constructor() {
    const ngFlowBundlesSignal: WritableSignal<Array<any>> = signal([])
    const ngFlowsSignal: WritableSignal<Array<any>> = signal([])
    super(ngFlowBundlesSignal, ngFlowsSignal)
    this.flowBundles = ngFlowBundlesSignal;
    this.flows = ngFlowsSignal;
  }

  readonly flowBundles: Signal<Array<any>>;
  readonly flows: Signal<Array<any>>;
}
