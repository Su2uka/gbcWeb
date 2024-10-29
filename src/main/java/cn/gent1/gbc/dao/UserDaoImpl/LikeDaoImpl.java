package cn.gent1.gbc.dao.UserDaoImpl;

import cn.gent1.gbc.dao.LikeDao;
import cn.gent1.gbc.domain.Like;
import cn.gent1.gbc.domain.Song;
import cn.gent1.gbc.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LikeDaoImpl implements LikeDao {

    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public List<Like> queryByUid(int uid) {
        List<Like> list = null;
        try {
            String sql = "select * from like_song where uid = ?";
            list = template.query(sql, new BeanPropertyRowMapper<Like>(Like.class), uid);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public void deleteByUidAndSid(int uid, int sid) {
        String sql = "delete from like_song where uid = ? and sid = ?";

        template.update(sql, uid, sid);
    }

    @Override
    public void insertLike(int uid, int sid, Date date) {
        String sql = "insert into like_song (sid,DATE,uid) values(?,?,?) ON DUPLICATE KEY UPDATE DATE = VALUES(DATE);";

        template.update(sql, sid, date, uid);

    }

    /*@Override
    public Like findByUidAndSid(int uid, int sid) {
        Like like = null;
        try {
            String sql = "select * from like_song where uid = ? and sid = ?";

            like = template.queryForObject(sql, new BeanPropertyRowMapper<Like>(Like.class), uid, sid);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        return like;
    }*/
    @Override
    public Like findByUidAndSid(int uid, int sid) {
        Like like = null;
        try {
            String sql = "SELECT * FROM like_song WHERE uid = ? AND sid = ?";
            like = template.queryForObject(sql, new BeanPropertyRowMapper<>(Like.class), uid, sid);
        } catch (EmptyResultDataAccessException e) {
            // 没有找到对应的记录，返回 null
            /*System.out.println("未找到喜欢记录：uid = " + uid + ", sid = " + sid);*/
        } catch (DataAccessException e) {
            // 捕获其他数据库异常
            e.printStackTrace();
        }

        return like;
    }

    @Override
    public int findTotalCountByUid(int uid, String search) {
        String sql = "select count(*) " +
                "from song " +
                "join like_song on song.sid = like_song.sid " +
                "where like_song.uid = ?";

        StringBuilder sb = new StringBuilder(sql);

        List<Object> params = new ArrayList<>(); // 使用泛型类型
        params.add(uid);

        if (search != null && search.length() > 0) {
            String searchPattern = "%" + search + "%";
            sb.append(" and (title LIKE ? OR artist LIKE ? OR album LIKE ?) ");
            params.add(searchPattern);
            params.add(searchPattern);
            params.add(searchPattern);
        }

        sql = sb.toString();

        return template.queryForObject(sql, Integer.class, params.toArray());
    }

    @Override
    public List<Song> findByPage(int start, int pageSize, String search, int uid) {
        // 基础 SQL 语句，分页查询特定用户收藏的歌曲
        String sql = "SELECT * " +
                "FROM song " +
                "JOIN like_song ON song.sid = like_song.sid " +
                "WHERE like_song.uid = ?";

        // 构建 StringBuilder 来扩展 SQL 查询
        StringBuilder sb = new StringBuilder(sql);

        // 参数列表
        List<Object> params = new ArrayList<>();
        params.add(uid);

        // 如果有搜索关键字，则添加过滤条件
        if (search != null && !search.isEmpty()) {
            String searchPattern = "%" + search + "%";
            sb.append(" AND (song.title LIKE ? OR song.artist LIKE ? OR song.album LIKE ?)");
            params.add(searchPattern);
            params.add(searchPattern);
            params.add(searchPattern);
        }

        // 添加分页逻辑
        sb.append(" ORDER BY song.sid LIMIT ? , ?");
        params.add(start);
        params.add(pageSize);

        // 将最终的 SQL 语句转为字符串
        sql = sb.toString();

        // 使用 RowMapper 将查询结果映射到 Song 对象
        return template.query(sql, new BeanPropertyRowMapper<>(Song.class), params.toArray());
    }

}
