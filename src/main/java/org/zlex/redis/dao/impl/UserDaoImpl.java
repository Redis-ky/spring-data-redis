package org.zlex.redis.dao.impl;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.zlex.common.JsonUtils;
import org.zlex.redis.dao.UserDao;
import org.zlex.redis.domain.User;

@Repository("userDao")
public class UserDaoImpl implements UserDao {

	@Autowired
	private RedisTemplate<Serializable, Serializable> redisTemplate;

	@Override
	public void save(final User user) {
		redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection connection)
					throws DataAccessException {
				connection.select(1);
				connection.set(
						redisTemplate.getStringSerializer().serialize("user.uid." + user.getUid()),
						redisTemplate.getStringSerializer().serialize(JsonUtils.object2Json(user)));
				return null;
			}
		});
	}

	@Override
	public User read(final String uid) {
		return redisTemplate.execute(new RedisCallback<User>() {
			@Override
			public User doInRedis(RedisConnection connection)
					throws DataAccessException {
				connection.select(1);
				byte[] key = redisTemplate.getStringSerializer().serialize("user.uid." + uid);
				if (connection.exists(key)) {
					byte[] value = connection.get(key);
					String objStr = redisTemplate.getStringSerializer().deserialize(value);
					return JsonUtils.json2Object(objStr, User.class);
				}
				return null;
			}
		});
	}

	@Override
	public void delete(final String uid) {
		redisTemplate.execute(new RedisCallback<Object>() {
			public Object doInRedis(RedisConnection connection) {
				connection.select(1);
				connection.del(redisTemplate.getStringSerializer().serialize("user.uid." + uid));
				return null;
			}
		});
	}
}
