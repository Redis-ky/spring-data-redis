package org.zlex.redis.dao;

import java.util.List;

import org.zlex.redis.domain.UserModel;

public interface UserRedisDao {
	
	/** 
     * 新增 
     * <br>------------------------------<br> 
     * @param user
     * @return boolean 
     */
	boolean add(UserModel userModel);
	
	/** 
     * 批量新增 使用pipeline方式 
     * <br>------------------------------<br> 
     * @param userModelList
     * @return boolean
     */  
    boolean addBatch(List<UserModel> userModelList);
    
    /** 
     * 修改 
     * <br>------------------------------<br> 
     * @param userModel 
     * @return boolean
     */  
    boolean update(UserModel userModel);

    /** 
     * 通过key获取 
     * <br>------------------------------<br> 
     * @param uid 
     * @return UserModel 
     */
    UserModel select(String uid);

    /** 
     * 删除 
     * <br>------------------------------<br> 
     * @param uid 
     */
    Boolean delete(String uid);
	
	/** 
     * 删除多个 
     * <br>------------------------------<br> 
     * @param uidList 
     */
    Boolean deleteBatch(List<String> uidList);
	
	/**  
     * 校验key是否存在
     * <br>------------------------------<br> 
     * @param uid 
     * @return boolean
     */ 
    boolean check(String uid);
}
