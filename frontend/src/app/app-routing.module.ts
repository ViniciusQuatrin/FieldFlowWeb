import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { MaterialFormComponent } from './components/material-form/material-form.component';
import { MovementsComponent } from './components/movements/movements.component';

const routes: Routes = [
  { path: '', component: DashboardComponent },
  { path: 'novo', component: MaterialFormComponent },
  { path: 'editar/:id', component: MaterialFormComponent },
  { path: 'movimentacoes', component: MovementsComponent },
  { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
