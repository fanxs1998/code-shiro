package com.code.session;

import com.code.util.JedisUtil;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.util.SerializationUtils;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author fxs
 * @Date 2022/2/8 15:31
 * @Description:
 */
public class RedisSessionDao extends AbstractSessionDAO {
    
    @Resource
    private JedisUtil jedisUtil;
    
    /**
     * 前缀
     */
    private final String SHIRO_SESSION_PREFIX = "shiro_session";
    
    /**
     * 设置固定前缀,redis中通过存储二进制的方式存储
     * @param key
     * @return
     */
    private byte[] getKey(String key) {
        return (SHIRO_SESSION_PREFIX + key).getBytes();
    }
    
    /**
     * 提取保存session的共同方法
     * @param session
     */
    private void saveSession(Session session) {
        if (session != null && session.getId() != null) {
            byte[] key = getKey(session.getId().toString());
            //序列号session对象
            byte[] value = SerializationUtils.serialize(session);
            //存储到缓存中
            jedisUtil.set(key, value);
            jedisUtil.expire(key, 600);
        }
    }
    
    @Override
    protected Serializable doCreate(Session session) {
        System.out.println("create session");
        Serializable sessionId = generateSessionId(session);
        // 将sessionID和session进行捆绑
        assignSessionId(session, sessionId);
        saveSession(session);
        return sessionId;
    }
    
    @Override
    protected Session doReadSession(Serializable sessionId) {
        System.out.println("read session");
        if (sessionId == null) {
            return null;
        }
        byte[] key = getKey(sessionId.toString());
        byte[] value = jedisUtil.get(key);
        
        Session session = null;
        
        try {
            //将byte数组反序列化为session对象
            session = (Session) SerializationUtils.deserialize(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return session;
    }
    
    @Override
    public void update(Session session) throws UnknownSessionException {
        System.out.println("update session");
        saveSession(session);
    }
    
    @Override
    public void delete(Session session) {
        System.out.println("delete session");
        if (session == null || session.getId() == null) {
            return;
        }
        byte[] key = getKey(session.getId().toString());
        jedisUtil.del(key);
    }
    
    /**
     * 获取存活的session
     * @return
     */
    @Override
    public Collection<Session> getActiveSessions() {
        Set<byte[]> keys = jedisUtil.keys(SHIRO_SESSION_PREFIX);
        Set<Session> sessions = new HashSet<>();
        if (CollectionUtils.isEmpty(keys)) {
            return sessions;
        }
        for (byte[] key : keys) {
            //通过jedis获得key之后将它反序列化为session对象
            Session session = (Session) SerializationUtils.deserialize(jedisUtil.get(key));
            sessions.add(session);
        }
        return sessions;
    }
}
