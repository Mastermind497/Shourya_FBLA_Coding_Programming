package org.lpsstudents.shourya.ui.views.storefront.events;

import com.vaadin.flow.component.ComponentEvent;
import org.lpsstudents.shourya.ui.views.orderedit.OrderItemsEditor;

public class ValueChangeEvent extends ComponentEvent<OrderItemsEditor> {

	public ValueChangeEvent(OrderItemsEditor component) {
		super(component, false);
	}
}