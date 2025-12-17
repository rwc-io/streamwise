import { ApplicationConfig, provideBrowserGlobalErrorListeners, provideZonelessChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { provideCharts, withDefaultRegisterables } from 'ng2-charts';
import { provideLuxonDateAdapter } from "@angular/material-luxon-adapter";

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideZonelessChangeDetection(),
    provideRouter(routes),
    provideLuxonDateAdapter(),
    // Can specify chart types here to reduce bundle size:
    provideCharts(withDefaultRegisterables()),
  ]
};
