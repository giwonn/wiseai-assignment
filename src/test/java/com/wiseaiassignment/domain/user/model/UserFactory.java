package com.wiseaiassignment.domain.user.model;

public class UserFactory {

	public static User create(
			long id,
			String name,
			String email
	) {
		return new User(id, name, email);
	}
}
