package cn.gent1.gbc.dao;

import cn.gent1.gbc.domain.Like;
import cn.gent1.gbc.domain.Song;

import java.util.Date;
import java.util.List;

public interface LikeDao {
    List<Like> queryByUid(int uid);

    void deleteByUidAndSid(int parseInt, int parseInt1);

    void insertLike(int uid, int sid, Date date);

    Like findByUidAndSid(int uid, int sid);

    int findTotalCountByUid(int uid,String search);

    List<Song> findByPage(int start, int pageSize, String search, int uid);
}
