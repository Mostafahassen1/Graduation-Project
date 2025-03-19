import { ApplicationConfig, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';

import {getRoutes} from './app.routes';
import { provideHttpClient } from '@angular/common/http';

// Important Note to read:
// -----------------------
// We need to check authentication first and after the user is authenticated the
// getRoutes(isLoggedIn : boolean) will take a flag that indicates the authentication state and updates the routes.

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(getRoutes(true)),
    provideHttpClient()]
};

