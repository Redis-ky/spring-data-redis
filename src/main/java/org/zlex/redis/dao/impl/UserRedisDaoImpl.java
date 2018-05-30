package org.zlex.redis.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import org.zlex.common.JsonUtils;
import org.zlex.redis.dao.UserRedisDao;
import org.zlex.redis.domain.UserModel;

@Repository("userDao")
public class UserRedisDaoImpl implements UserRedisDao {

	@Autowired
	private RedisTemplate<Serializable, Serializable> redisTemplate;
	

	@Override
	public boolean add(final UserModel userModel) {
		boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
//				connection.select(1);//选择数据库
				RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
				byte[] key  = serializer.serialize("user.uid." + userModel.getUid());  
                byte[] name = serializer.serialize(JsonUtils.object2Json(userModel));  
				boolean bl = connection.setNX(key, name);
//				if(bl){//设置键的有效时间
//                	connection.expire(key, 1800L);
//                }
                return bl;
			}
		});
		return result; 
	}
	
	@Override
	public boolean addBatch(List<UserModel> userModelList) {
		 Assert.notEmpty(userModelList);  
	        boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {  
	            public Boolean doInRedis(RedisConnection connection) throws DataAccessException { 
	                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();  
	                for (UserModel userModel : userModelList) {  
	                    byte[] key  = serializer.serialize("user.uid." + userModel.getUid());  
	                    byte[] name = serializer.serialize(JsonUtils.object2Json(userModel));  
	                    connection.setNX(key, name);  
	                }  
	                return true;  
	            }  
	        }, false, true);  
	        return result;  
	}
	
	@Override
	public boolean update(UserModel userModel) {
		boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {  
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {  
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();  
                byte[] key  = serializer.serialize("user.uid." + userModel.getUid());  
                byte[] name = serializer.serialize(JsonUtils.object2Json(userModel));  
                connection.set(key, name);  
//                //设置键的有效时间
//                connection.expire(key, Long.parseLong(parameterConfig.getParameterValue()) );
                return true;  
            }  
        });  
        return result;  
	}

	@Override
	public UserModel select(final String uid) {
		return redisTemplate.execute(new RedisCallback<UserModel>() {
			@Override
			public UserModel doInRedis(RedisConnection connection) throws DataAccessException {
				byte[] key = redisTemplate.getStringSerializer().serialize("user.uid." + uid);
				if (connection.exists(key)) {
					byte[] value = connection.get(key);
					String objStr = redisTemplate.getStringSerializer().deserialize(value);
					return JsonUtils.json2Object(objStr, UserModel.class);
				}
				return null;
			}
		});
	}

	@Override
	public Boolean delete(final String uid) {
		boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {  
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {  
                byte[] key = redisTemplate.getStringSerializer().serialize("user.uid." + uid); 
                if (connection.del(key)>0) {
                	return true;
                }
                return false;
            }  
        });  
		return result ;
	}
	
	@Override
	public Boolean deleteBatch(List<String> uidList) {
		boolean result = false;
		for (String str : uidList) {
			result = this.delete(str);
		}
		return result ;
	}

	@Override
	public boolean check(String uid) {
		boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {  
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {  
                byte[] key = redisTemplate.getStringSerializer().serialize("user.uid." + uid); 
                byte[] value = connection.get(key);
                if (value == null) {  
                    return false;  
                } 
//                //若键存在，则延长键的有效时间
//                connection.expire(key, Long.parseLong(parameterConfig.getParameterValue()));
                return true;
            }  
        });  
        return result; 
	}

}
