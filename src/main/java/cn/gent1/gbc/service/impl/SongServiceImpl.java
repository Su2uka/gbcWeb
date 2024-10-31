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
import cn.gent1.gbc.util.JedisUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import redis.clients.jedis.Jedis;

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
        /*return songDao.queryAll(search);*/

        // 获取 Redis 客户端
        Jedis jedis = JedisUtil.getJedis();

        // 定义缓存键前缀和 JSON 序列化工具
        final String REDIS_KEY_PREFIX = "songs:";
        final ObjectMapper objectMapper = new ObjectMapper();

        List<Song> songs;

        try {
            // 根据 search 参数生成 Redis 缓存键
            String redisKey = REDIS_KEY_PREFIX + (search == null || search.isEmpty() ? "all" : search);

            // 尝试从 Redis 获取缓存数据
            String cachedData = jedis.get(redisKey);
            if (cachedData != null) {
                // 如果缓存中存在数据，将 JSON 反序列化为 List<Song> 对象
                songs = objectMapper.readValue(cachedData, new TypeReference<List<Song>>() {});
                System.out.println("从redis中取出数据.....");
            } else {
                // 缓存中没有数据，从数据库中查询
                songs = songDao.queryAll(search);

                // 序列化为 JSON 字符串并存入 Redis，并设置过期时间
                jedis.setex(redisKey, 3600, objectMapper.writeValueAsString(songs));
                System.out.println("查询数据库并将结果存入redis.....");
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 出现异常时从数据库中查询
            songs = songDao.queryAll(search);
        } finally {
            // 关闭 Redis 连接
            jedis.close();
        }

        return songs;
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
