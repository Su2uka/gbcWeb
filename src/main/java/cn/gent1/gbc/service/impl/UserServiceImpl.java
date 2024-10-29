package cn.gent1.gbc.service.impl;

import cn.gent1.gbc.dao.UserDao;
import cn.gent1.gbc.dao.UserDaoImpl.UserDaoImpl;
import cn.gent1.gbc.domain.User;
import cn.gent1.gbc.service.UserService;
import cn.gent1.gbc.util.MailUtils;
import cn.gent1.gbc.util.UuidUtil;

public class UserServiceImpl implements UserService {

    UserDao userDao = new UserDaoImpl();

    @Override
    public boolean register(User user) {
        //1.根据email查询用户信息
        User u = userDao.findByEmail(user.getEmail());

        //判断
        if (u != null) {
            //该email已被注册，注册失败
            return false;
        }
        //2.保存用户信息
        //2.1设置激活码，唯一字符串
        user.setCode(UuidUtil.getUuid());
        //2.2设置激活状态
        user.setStatus("N");
        userDao.save(user);

        //3.发送激活邮箱
        String content = "<a href='https://gent1.cn/user/activate?code="+user.getCode()+"'>点击激活【GIRLS BAND CRY】</a>";
        //String content = "<a href='http://localhost/user/activate?code="+user.getCode()+"'>点击激活【GIRLS BAND CRY】</a>";

        MailUtils.sendMail(user.getEmail(),content,"【GIRLS BAND CRY】激活邮件");
        return true;
    }

    @Override
    public boolean activate(String code) {
        //1.根据激活码查询用户对象
        User user = userDao.findByCode(code);

        if (user != null) {
            //2.调用dao，修改激活状态
            userDao.updateStatus(user);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public User login(User user) {
        return userDao.findByEmailAndPassword(user.getEmail(), user.getPassword());
    }

    @Override
    public void rememberMe(User u, String token) {
        userDao.saveToken(u.getUid(),token);
    }

    @Override
    public User checkToken(String token) {
        return userDao.findUserByRememberMeToken(token);
    }

    @Override
    public void clearRememberMeToken(User user) {
        userDao.saveToken(user.getUid(),null);
    }


}
