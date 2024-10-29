package cn.gent1.gbc.dao;

import cn.gent1.gbc.domain.User;

public interface UserDao {
    /**
     * 根据邮箱查询用户信息
     * @param email
     * @return
     */
    public User findByEmail(String email);

    /**
     * 保存用户信息
     * @param user
     */
    void save(User user);

    /**
     * 根据激活码查询用户
     * @param code
     * @return
     */
    User findByCode(String code);

    /**
     * 更新指定用户的激活状态
     * @param user
     */
    void updateStatus(User user);

    /**
     * 根据邮箱和密码查询用户
     * @param email
     * @param password
     * @return
     */
    User findByEmailAndPassword(String email, String password);

    /**
     * 保存对应用户的token
     * @param uid
     * @param token
     */
    void saveToken(int uid, String token);

    /**
     * 根据token查询用户
     * @param token
     * @return
     */
    User findUserByRememberMeToken(String token);
}
