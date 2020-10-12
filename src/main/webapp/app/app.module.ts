import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { TeachPredictorSharedModule } from 'app/shared/shared.module';
import { TeachPredictorCoreModule } from 'app/core/core.module';
import { TeachPredictorAppRoutingModule } from './app-routing.module';
import { TeachPredictorHomeModule } from './home/home.module';
import { TeachPredictorEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ErrorComponent } from './layouts/error/error.component';

@NgModule({
  imports: [
    BrowserModule,
    TeachPredictorSharedModule,
    TeachPredictorCoreModule,
    TeachPredictorHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    TeachPredictorEntityModule,
    TeachPredictorAppRoutingModule,
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, FooterComponent],
  bootstrap: [MainComponent],
})
export class TeachPredictorAppModule {}
