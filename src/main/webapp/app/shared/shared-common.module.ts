import { NgModule } from '@angular/core';

import { MedicSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';

@NgModule({
    imports: [MedicSharedLibsModule],
    declarations: [JhiAlertComponent, JhiAlertErrorComponent],
    exports: [MedicSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent]
})
export class MedicSharedCommonModule {}
