package org.zlex.redis.dao;

import org.zlex.redis.domain.User;

public interface UserDao {
	
	void save(User user);

	User read(String uid);

	void delete(String uid);
}
