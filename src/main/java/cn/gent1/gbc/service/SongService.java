package cn.gent1.gbc.service;

import cn.gent1.gbc.domain.PageBean;
import cn.gent1.gbc.domain.Song;
import cn.gent1.gbc.domain.User;

import java.util.List;

public interface SongService {
    PageBean<Song> pageQuery(int currentPage, int pageSize, String search );

    List<Song> queryAll(String search);


    String download(String sid);

    PageBean<Song> likePageQuery(int currentPage, int pageSize, String search, int uid);

    List<Song> queryAllLike(String search, int uid);
}
