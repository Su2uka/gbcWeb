package cn.gent1.gbc.service.impl;

import cn.gent1.gbc.dao.LikeDao;
import cn.gent1.gbc.dao.UserDaoImpl.LikeDaoImpl;
import cn.gent1.gbc.domain.Like;
import cn.gent1.gbc.domain.User;
import cn.gent1.gbc.service.LikeService;

import java.util.Date;
import java.util.List;

public class LikeServiceImpl implements LikeService {

    private LikeDao likeDao = new LikeDaoImpl();

    @Override
    public List<Like> queryLike(User user) {

        return likeDao.queryByUid(user.getUid());

    }

    @Override
    public void cancelLike(String uid, String sid) {
        likeDao.deleteByUidAndSid(Integer.parseInt(uid),Integer.parseInt(sid));
    }

    @Override
    public void addLike(String uid, String sid) {
        likeDao.insertLike(Integer.parseInt(uid),Integer.parseInt(sid),new Date());
    }

    @Override
    public Like checkLike(String uid, String sid) {
        return likeDao.findByUidAndSid(Integer.parseInt(uid),Integer.parseInt(sid));
    }
}
