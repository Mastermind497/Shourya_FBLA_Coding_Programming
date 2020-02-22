package org.lpsstudents.shourya.testbench.elements.ui;

import com.vaadin.flow.component.applayout.testbench.AppLayoutElement;
import org.lpsstudents.shourya.testbench.elements.components.AppNavigationElement;

public class MainViewElement extends AppLayoutElement {

	public AppNavigationElement getMenu() {
		return $(AppNavigationElement.class).first();
	}

}
