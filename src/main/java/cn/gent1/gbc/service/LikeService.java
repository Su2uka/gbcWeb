package cn.gent1.gbc.service;

import cn.gent1.gbc.domain.Like;
import cn.gent1.gbc.domain.User;

import java.util.List;

public interface LikeService {
    List<Like> queryLike(User user);

    void cancelLike(String uid, String sid);

    void addLike(String uid, String sid);

    Like checkLike(String uid, String sid);
}
