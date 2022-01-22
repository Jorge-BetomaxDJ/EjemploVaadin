package com.examen.jorge.app.security;

import com.examen.jorge.backend.data.entity.User;

@FunctionalInterface
public interface CurrentUser {

	User getUser();
}
