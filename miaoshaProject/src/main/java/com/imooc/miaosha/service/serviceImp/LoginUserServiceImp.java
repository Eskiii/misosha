package com.imooc.miaosha.service.serviceImp;

import com.imooc.miaosha.dao.MiaoshaUserDao;
import com.imooc.miaosha.vo.LoginUser;
import com.imooc.miaosha.entity.MiaoshaUser;
import com.imooc.miaosha.exception.GlobalException;
import com.imooc.miaosha.redis.MiaoshaUserKey;
import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.service.LoginUserService;
import com.imooc.miaosha.utils.CodeMsg;
import com.imooc.miaosha.utils.MD5Util;
import com.imooc.miaosha.utils.UUIDUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @Classname LoginUserServiceImp
 * @Description TODO
 * @Date 2022/4/28 15:13
 * @Created by Eskii
 */

@Service
public class LoginUserServiceImp implements LoginUserService {

    private static final Logger logger = LoggerFactory.getLogger(LoginUserServiceImp.class);

    @Autowired
    RedisService redisService;
    @Autowired
    MiaoshaUserDao miaoshaUserDao;

    /**
     * 用户登录验证
     * 步骤：1、用户信息不为空
     *      2、系统中存在该用户
     *      3、密码是否一致
     * @param loginUser 登录的用户信息
     * @return  登录后状态码
     */
    @Override
    public String login(HttpServletResponse response, LoginUser loginUser) {
        if (loginUser == null) {
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        String mobile = loginUser.getMobile();
        String pwd = loginUser.getPassword();
        MiaoshaUser user = getUserById(Long.parseLong(mobile));
        if(user == null) {  // 用户不存在
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        // 校验密码是否与系统存储的密码相符
        String dbPwd = MD5Util.formPassToDBPass(pwd, user.getSalt());
        if (!dbPwd.equals(user.getPassword())) {
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);  // 密码错误
        }
        String token = UUIDUtil.getUUID();  // 生成用户的 token
        addCookie(response, token, user);
        return token; // 可以成功登录
    }

    /**
     * 根据 token 获取用户信息
     * 注意细节，当前 token 的有效时间应该是用户当前访问系统的时间 + token的最大存活时间
     * 所以应该更新redis缓存中的token有效时间
     * @param token
     * @return
     */
    @Override
    public MiaoshaUser getByToken(HttpServletResponse response, String token) {
        if(StringUtils.isEmpty(token)) {
            return null;
        }
        MiaoshaUser user = redisService.get(MiaoshaUserKey.token, token, MiaoshaUser.class);
        if (user != null) { // 如果 user 不为空才更新，否则表明当前的 token 已过期或者不存在，则用户不合法，需要重新登录
            addCookie(response, token, user);
        }
        return user;
    }

    /**
     * 校验用户是否在数据库中
     * 先在缓存中找，再去数据库中查找
     * @param id
     * @return 获取到相应 id 的用户信息
     */
    private MiaoshaUser getUserById(Long id) {
        MiaoshaUser user = null;
        // 先去 redis 缓存查找
        user = redisService.get(MiaoshaUserKey.getById, id + "", MiaoshaUser.class);
        if (user != null) return user;
        // 如果找不到，再去数据库查找
        user = miaoshaUserDao.getUserById(id);
        //并将用户信息加入到缓存中，方便下次登录
        if(user != null) {
            redisService.set(MiaoshaUserKey.getById, "" + id, user);
        }
        return user;
    }

    /**
     * 更新用户密码
     * 步骤：（1）先去库存或者数据库查找用户信息
     *      （2）更新数据库中相应的用户的密码
     *      （3）删除以用户id为key的用户信息缓存，因为这个一般情况下不能更新；更新以用户token为key的用户信息，因为token可以由访问自动更新，不会成为脏数据
     *
     * 可以将更新数据库和删除缓存这两个步骤调换顺序吗？
     * 不行。因为如果提前将缓存删了，还是有可能在向数据库更新数据之前重新将旧的用户数据重新加载到缓存中，这样就造成了用户数据不一致的情况了！
     * @param token
     * @param id
     * @param newPwd
     * @return
     */
    private boolean updateUserPwd(String token, Long id, String newPwd) {
        MiaoshaUser user = getUserById(id);
        if (user == null) {
            return false;
        }
        //更新数据库数据
        MiaoshaUser user1 = new MiaoshaUser();
        user1.setId(id);
        user1.setPassword(newPwd);
        miaoshaUserDao.update(user1);
        //删除之前的缓存数据
        redisService.delete(MiaoshaUserKey.getById, "" + id);
        //更新token数据，因为直接删除会导致用户修改完密码就要重新登录了
        user.setPassword(newPwd);
        redisService.set(MiaoshaUserKey.token, token, user);
        return true;
    }
    /**
     * 更新用户 cookie 的工具方法
     * @param response
     * @param token
     * @param user
     */
    public void addCookie(HttpServletResponse response, String token, MiaoshaUser user) {
        // 可以成功登录则将用户信息及相应的 token 插入 redis 中
        redisService.set(MiaoshaUserKey.token, token, user);
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        cookie.setPath("/");    // 指定可以获取 cookie 的目录，该路径和其子路径都可以获取到该 cookie
        cookie.setMaxAge(MiaoshaUserKey.TOKEN_EXPIRE);
        logger.info("用户" + user.getId() + "的cookie :" + cookie.getValue());
        response.addCookie(cookie);
    }
}
