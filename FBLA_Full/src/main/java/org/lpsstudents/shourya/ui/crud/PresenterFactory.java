/**
 *
 */
package org.lpsstudents.shourya.ui.crud;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import org.lpsstudents.shourya.app.security.CurrentUser;
import org.lpsstudents.shourya.backend.example.data.entity.Order;
import org.lpsstudents.shourya.backend.example.service.OrderService;
import org.lpsstudents.shourya.ui.views.storefront.StorefrontView;

@Configuration
public class PresenterFactory {

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public EntityPresenter<Order, StorefrontView> orderEntityPresenter(OrderService crudService, CurrentUser currentUser) {
		return new EntityPresenter<>(crudService, currentUser);
	}

}
