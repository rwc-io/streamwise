import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
import { App } from './app/app';

// Anchor Luxon to the window so Kotlin can find the EXACT same constructor
import { DateTime } from 'luxon';
(window as any).luxon = { DateTime };

import 'chartjs-adapter-luxon';

bootstrapApplication(App, appConfig)
  .catch((err) => console.error(err));
