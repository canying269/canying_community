package com.cmz.community.service;

import com.cmz.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

@Service
public class LikeService {
    @Autowired
    private RedisTemplate redisTemplate;

    //点赞
//    public void like(int userId, int entityType, int entityId){
//        //获取key
//        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType,entityId);
//        String userLikeKey = RedisKeyUtil.getUserLikeKey(userId);
//
//        //用户是否已点赞 用来获取 操作是点赞还是取消点赞
//        Boolean isMember = redisTemplate.opsForSet().isMember(entityLikeKey, userId);
//        if (isMember) {
//            redisTemplate.opsForSet().remove(entityLikeKey,userId);
//            redisTemplate.opsForSet().remove(userLikeKey, userId);//应放在事务中
//        }else {
//            redisTemplate.opsForSet().add(entityLikeKey,userId);
//            redisTemplate.opsForSet().add(userLikeKey,userId);
//      }
//    }
    public void like(int userId, int entityType,int entityId,int entityUserId){
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType,entityId);
                //userId为点赞的人 需要entityUserId被点赞的人
                String userLikeKey = RedisKeyUtil.getUserLikeKey(entityUserId);

                Boolean isMember = operations.opsForSet().isMember(entityLikeKey, userId);
                operations.multi();

                if(isMember){
                    operations.opsForSet().remove(entityLikeKey,userId);
                    operations.opsForValue().decrement(userLikeKey);
                }else {
                    operations.opsForSet().add(entityLikeKey,userId);
                    operations.opsForValue().increment(userLikeKey);
                }

                return operations.exec();
            }
        });
    }

    //查询某实体点赞的数量
    public long findEntityLikeCount(int entityType, int entityId){
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType,entityId);
        return redisTemplate.opsForSet().size(entityLikeKey);
    }

    //查询某人对某实体是否点赞 点赞状态 返回值为int方便将来添加新的互动（踩）
    public int findEntityLikeStatus(int userId, int entityType, int entityId){
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType,entityId);
        return redisTemplate.opsForSet().isMember(entityLikeKey,userId) ? 1 : 0;
    }

    //查询当前用户收到的赞
    public long findUserLikeCount(int userId){
        String userLikeKey = RedisKeyUtil.getUserLikeKey(userId);
        Integer integer = (Integer)redisTemplate.opsForValue().get(userLikeKey);
        return integer == null ? 0 : integer.intValue();
    }

}
