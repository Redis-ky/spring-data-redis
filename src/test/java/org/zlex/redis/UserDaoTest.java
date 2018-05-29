package org.zlex.redis;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.zlex.common.JsonUtils;
import org.zlex.redis.dao.UserDao;
import org.zlex.redis.domain.User;

public class UserDaoTest {
	private ApplicationContext app;
	private UserDao userDao;

	@Before
	public void before() throws Exception {
		app = new ClassPathXmlApplicationContext("applicationContext.xml");
		userDao = (UserDao) app.getBean("userDao");
	}

	@Test
	public void save() {
		// -------------- Create ---------------
		String uid = "u123456";
		String address1 = "上海";
		User user = new User();
		user.setAddress(address1);
		user.setUid(uid);
		userDao.save(user);
	}
	
	@Test
	public void read() {
		// ---------------Read ---------------
		String uid = "u123456";
		User user = userDao.read(uid);
		System.out.println(JsonUtils.objectToJson(user));
	}
	
	@Test
	public void update() {
		// --------------Update ------------
		String uid = "u123456";
		String address1 = "北京";
		User user = new User();
		user.setAddress(address1);
		user.setUid(uid);
		userDao.save(user);
		user = userDao.read(uid);
		System.out.println(JsonUtils.objectToJson(user));
	}
	
	@Test
	public void delete() {
		// --------------Delete ------------
		String uid = "u123456";
		userDao.delete(uid);
		User user = userDao.read(uid);
		assertNull(user);
	}
}
