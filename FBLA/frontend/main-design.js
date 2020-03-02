import {html, PolymerElement} from '@polymer/polymer/polymer-element.js';
import '@vaadin/vaadin-app-layout/src/vaadin-app-layout.js';

class MainDesign extends PolymerElement {

    static get template() {
        return html`
<style include="shared-styles">
                :host {
                    display: block;
                    height: 100%;
                }
            </style>
<vaadin-app-layout></vaadin-app-layout>
`;
    }

    static get is() {
        return 'main-design';
    }

    static get properties() {
        return {
            // Declare your properties here.
        };
    }
}

customElements.define(MainDesign.is, MainDesign);
