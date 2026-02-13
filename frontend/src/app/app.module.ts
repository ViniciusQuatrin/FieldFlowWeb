import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';

// Components
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { MaterialFormComponent } from './components/material-form/material-form.component';
import { MovementsComponent } from './components/movements/movements.component';

@NgModule({
  declarations: [
    AppComponent,
    DashboardComponent,
    MaterialFormComponent,
    MovementsComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
