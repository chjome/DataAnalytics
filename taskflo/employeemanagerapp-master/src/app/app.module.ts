import { BrowserModule, Title } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { TaskService } from './task.service';
import { HttpClientModule } from '@angular/common/http';import { FormsModule } from '@angular/forms';



@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule, FormsModule
  ],
  providers: [TaskService, Title],
  bootstrap: [AppComponent]
})
export class AppModule { }
