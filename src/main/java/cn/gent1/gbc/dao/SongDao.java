package cn.gent1.gbc.dao;

import cn.gent1.gbc.domain.Song;

import java.util.List;

public interface SongDao {
    /**
     * 根据搜索查询总记录数
     * @param search
     * @return
     */
    int findTotalCount(String search);

    /**
     * 根据start，pageSize查询当前页的集合
     * @param start
     * @param pageSize
     * @param search
     * @return
     */
    List<Song> findByPage(int start, int pageSize, String search);


    List<Song> queryAll(String search);

    Song queryBySid(int parseInt);

    List<Song> queryLikeSongByUid(String search, int parseInt);
}
