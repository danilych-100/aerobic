import { Component, Injectable, Input, Output, EventEmitter } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class SharedService {
    @Output()
    fire: EventEmitter<any> = new EventEmitter();

    constructor() {
        console.log('shared service started');
    }

    change() {
        console.log('change started');
        this.fire.emit(true);
    }

    changeBool(changeElem: boolean) {
        console.log('change started');
        this.fire.emit(changeElem);
    }

    getEmittedValue() {
        return this.fire;
    }
}
