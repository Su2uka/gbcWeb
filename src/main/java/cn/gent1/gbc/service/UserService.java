package cn.gent1.gbc.service;

import cn.gent1.gbc.domain.User;

public interface UserService {
    /**
     * 用户注册
     * @param user
     * @return
     */
    boolean register(User user);

    /**
     * 激活账户
     * @param code
     * @return
     */
    boolean activate(String code);

    /**
     * 用户登录
     * @param user
     * @return
     */
    User login(User user);

    /**
     * 记住我，将token存入数据库
     * @param u
     * @param token
     */
    void rememberMe(User u, String token);

    /**
     * 检查token
     * @param token
     * @return
     */
    User checkToken(String token);

    /**
     * 清除token
     * @param user
     */
    void clearRememberMeToken(User user);
}
