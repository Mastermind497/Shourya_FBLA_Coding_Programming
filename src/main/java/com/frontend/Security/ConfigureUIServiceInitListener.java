package com.frontend.Security;

import com.frontend.Login.LoginView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import org.springframework.stereotype.Component;

/**
 * A Class which forces redirection to LoginView while someone is not logged in
 * <p>
 * This keeps anyone from accessing any part of the app without being validated
 */
@Component
public class ConfigureUIServiceInitListener implements VaadinServiceInitListener {

    @Override
    public void serviceInit(ServiceInitEvent event) {
        event.getSource().addUIInitListener(uiEvent -> { //
            final UI ui = uiEvent.getUI();
            ui.addBeforeEnterListener(this::authenticateNavigation);
        });
    }
    
    /**
     * Authenticates whether movement between classes is allowed and reroutes back to LoginView.
     * <p>
     * If movement is not allowed, it reroutes back to LoginView. On the Other Hand, if it is allowed, this method does
     * nothing
     *
     * @param event A Lambda-reference event directed at navigation
     */
    private void authenticateNavigation(BeforeEnterEvent event) {
        if (!LoginView.class.equals(event.getNavigationTarget())
            && !SecurityUtils.isUserLoggedIn()) { //
            event.rerouteTo(LoginView.class);
        }
    }
}