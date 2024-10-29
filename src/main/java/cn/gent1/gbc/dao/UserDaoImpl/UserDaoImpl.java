package cn.gent1.gbc.dao.UserDaoImpl;

import cn.gent1.gbc.dao.UserDao;
import cn.gent1.gbc.domain.User;
import cn.gent1.gbc.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

public class UserDaoImpl implements UserDao {

    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    /**
     * 根据email查询用户信息
     *
     * @param email
     * @return
     */
    @Override
    public User findByEmail(String email) {
        User user = null;
        try {
            //1.定义sql
            String sql = "select * from user where email = ?";
            //2.执行sql
            user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), email);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        return user;
    }

    /**
     * 用户保存
     *
     * @param user
     */
    @Override
    public void save(User user) {
        //1.定义sql
        String sql = "insert into user(email,password,status,code) value(?,?,?,?)";
        //2.执行sql
        template.update(sql, user.getEmail(), user.getPassword(), user.getStatus(), user.getCode());
    }

    /**
     * 根据激活码查询用户
     * @param code
     * @return
     */
    @Override
    public User findByCode(String code) {
        User user = null;
        try {
            //1.定义sql
            String sql = "select * from user where code = ?";
            //2.执行sql
            user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), code);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        return user;
    }

    /**
     * 修改指定用户的激活状态
     * @param user
     */
    @Override
    public void updateStatus(User user) {
        String sql = "update user set status = 'Y' where uid = ?";
        template.update(sql, user.getUid());
    }

    /**
     * 根据邮箱和密码查询用户
     * @param email
     * @param password
     * @return
     */
    @Override
    public User findByEmailAndPassword(String email, String password) {
        User user = null;
        try {
            String sql = "select * from user where email = ? and password = ?";

            user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), email, password);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        return user;
    }

    /**
     * 保存对应用户的token
     * @param uid
     * @param token
     */
    @Override
    public void saveToken(int uid, String token) {
        String sql = "update user set remember_me_token = ? where uid = ?";

        template.update(sql,token,uid);
    }

    /**
     * 根据token查询用户
     * @param token
     * @return
     */
    @Override
    public User findUserByRememberMeToken(String token) {
        User user = null;
        try {
            String sql = "select * from user where remember_me_token = ?";
            user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), token);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        return user;
    }
}
