package org.lpsstudents.shourya.app.security;

import org.lpsstudents.shourya.backend.example.data.entity.User;

@FunctionalInterface
public interface CurrentUser {

	User getUser();
}
