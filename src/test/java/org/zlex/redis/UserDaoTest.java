package org.zlex.redis;

import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.zlex.common.JsonUtils;
import org.zlex.redis.dao.UserRedisDao;
import org.zlex.redis.domain.UserModel;

public class UserDaoTest {
	private ApplicationContext app;
	private UserRedisDao userDao;

	@Before
	public void before() throws Exception {
		app = new ClassPathXmlApplicationContext("applicationContext.xml");
		userDao = (UserRedisDao) app.getBean("userDao");
	}

	@Test
	public void add() {
		// -------------- Create ---------------
		String uid = "u123456";
		String address1 = "上海";
		UserModel userModel = new UserModel();
		userModel.setAddress(address1);
		userModel.setUid(uid);
		System.out.println(userDao.add(userModel));
	}
	
	@Test
	public void addBatch() {
		// -------------- Create ---------------
		List<UserModel> userModelList = new ArrayList<UserModel>();
		for (int i=0;i<100;i++) {
			String uid = "u" + String.format("%0"+4+"d", i);
			String address = "上海" + i;
			UserModel userModel1 = new UserModel(uid, address);
			userModelList.add(userModel1);
		}
		
		System.out.println(userDao.addBatch(userModelList));
	}
	
	@Test
	public void select() {
		// ---------------Read ---------------
		String uid = "u123456";
		UserModel userModel = userDao.select(uid);
		System.out.println(JsonUtils.objectToJson(userModel));
	}
	
	@Test
	public void update() {
		// --------------Update ------------
		String uid = "u123456";
		String address1 = "北京";
		UserModel userModel = new UserModel();
		userModel.setAddress(address1);
		userModel.setUid(uid);
		userDao.add(userModel);
		userModel = userDao.select(uid);
		System.out.println(JsonUtils.objectToJson(userModel));
	}
	
	@Test
	public void delete() {
		// --------------Delete ------------
		String uid = "u123456";
		userDao.delete(uid);
		UserModel userModel = userDao.select(uid);
		assertNull(userModel);
	}
	
	@Test
	public void deleteBatch() {
		// --------------Delete ------------
		List<String> uidList = new ArrayList<String>();
		for (int i=0;i<100;i++) {
			String uid = "u" + String.format("%0"+4+"d", i);
			uidList.add(uid);
		}
		System.out.println(userDao.deleteBatch(uidList));
	}
}
