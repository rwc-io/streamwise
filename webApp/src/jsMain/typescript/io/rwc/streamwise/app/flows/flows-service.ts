import {Injectable, signal, WritableSignal} from '@angular/core';

import * as streamwise from '@streamwise';

@Injectable({
  providedIn: 'root',
})
export class FlowsService extends streamwise.FlowsService {
  constructor() {
    const ngFlowBundlesSignal: WritableSignal<Array<any>> = signal([])
    super(ngFlowBundlesSignal)
    this.flowBundles = ngFlowBundlesSignal;
  }

  readonly flowBundles;
}
