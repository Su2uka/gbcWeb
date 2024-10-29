package cn.gent1.gbc.service.impl;

import cn.gent1.gbc.dao.LikeDao;
import cn.gent1.gbc.dao.SongDao;
import cn.gent1.gbc.dao.UserDaoImpl.LikeDaoImpl;
import cn.gent1.gbc.dao.UserDaoImpl.SongDaoImpl;
import cn.gent1.gbc.domain.Like;
import cn.gent1.gbc.domain.PageBean;
import cn.gent1.gbc.domain.Song;
import cn.gent1.gbc.domain.User;
import cn.gent1.gbc.service.SongService;

import java.util.List;

public class SongServiceImpl implements SongService {

    private SongDao songDao = new SongDaoImpl();
    private LikeDao likeDao = new LikeDaoImpl();


    @Override
    public PageBean<Song> pageQuery(int currentPage, int pageSize, String search) {
        //封装PageBean
        PageBean<Song> pb = new PageBean<Song>();

        //当前页码
        pb.setCurrentPage(currentPage);
        //每页显示条数
        pb.setPageSize(pageSize);

        //设置总记录数TotalCount
        int totalCount = songDao.findTotalCount(search);
        pb.setTotalCount(totalCount);

        //设置当前页显示的集合
        int start = (currentPage - 1) * pageSize; //开始的记录数
        List<Song> list = songDao.findByPage(start, pageSize, search);
        pb.setList(list);

        //设置总页数
        int totalPage = (totalCount % pageSize == 0) ? totalCount / pageSize : (totalCount / pageSize) + 1;
        pb.setTotalPage(totalPage);


        return pb;
    }

    @Override
    public List<Song> queryAll(String search) {
        return songDao.queryAll(search);
    }

    @Override
    public String download(String sid) {
        Song song = new Song();

        song = songDao.queryBySid(Integer.parseInt(sid));

        return song.getDownload();
    }

    @Override
    public PageBean<Song> likePageQuery(int currentPage, int pageSize, String search, int uid) {
        //封装PageBean
        PageBean<Song> pb = new PageBean<Song>();

        //当前页码
        pb.setCurrentPage(currentPage);
        //每页显示条数
        pb.setPageSize(pageSize);

        //设置总记录数TotalCount
        int totalCount = likeDao.findTotalCountByUid(uid, search);
        pb.setTotalCount(totalCount);

        //设置当前页显示的集合
        int start = (currentPage - 1) * pageSize; //开始的记录数
        List<Song> list = likeDao.findByPage(start, pageSize, search ,uid);
        pb.setList(list);

        //设置总页数
        int totalPage = (totalCount % pageSize == 0) ? totalCount / pageSize : (totalCount / pageSize) + 1;
        pb.setTotalPage(totalPage);


        return pb;
    }

    @Override
    public List<Song> queryAllLike(String search, int uid) {
        return songDao.queryLikeSongByUid(search,uid);
    }


}
