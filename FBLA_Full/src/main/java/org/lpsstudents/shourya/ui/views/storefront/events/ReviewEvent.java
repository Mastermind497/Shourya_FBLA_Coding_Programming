package org.lpsstudents.shourya.ui.views.storefront.events;

import com.vaadin.flow.component.ComponentEvent;
import org.lpsstudents.shourya.ui.views.orderedit.OrderEditor;

public class ReviewEvent extends ComponentEvent<OrderEditor> {

	public ReviewEvent(OrderEditor component) {
		super(component, false);
	}
}