package cn.gent1.gbc.dao.UserDaoImpl;

import cn.gent1.gbc.dao.SongDao;
import cn.gent1.gbc.domain.Song;
import cn.gent1.gbc.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class SongDaoImpl implements SongDao {

    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public int findTotalCount(String search) {
        //定义sql
        String sql = "";

        int res = 0;

        if (search != null && search.length() > 0) {
            sql = "select count(*) from song WHERE title LIKE ? OR artist LIKE ? OR album LIKE ?";
            //模糊查询的关键字，添加 '%' 通配符
            String searchPattern = "%" + search + "%";
            res = template.queryForObject(sql, Integer.class, searchPattern, searchPattern, searchPattern);
        } else {
            sql = "select count(*) from song";
            res = template.queryForObject(sql, Integer.class);
        }

        return res;
    }

    @Override
    public List<Song> findByPage(int start, int pageSize, String search) {
        String sql = "select * from song where 1=1 ";
        StringBuilder sb = new StringBuilder(sql);

        List params = new ArrayList();//条件

        if (search != null && search.length() > 0) {
            String searchPattern = "%" + search + "%";
            sb.append(" and (title LIKE ? OR artist LIKE ? OR album LIKE ?) ");
            params.add(searchPattern);
            params.add(searchPattern);
            params.add(searchPattern);
        }

        sb.append("limit ? , ?");
        sql = sb.toString();

        /*System.out.println(sql);*/

        params.add(start);
        params.add(pageSize);

        return template.query(sql, new BeanPropertyRowMapper<Song>(Song.class), params.toArray());
    }

    @Override
    public List<Song> queryAll(String search) {
        String sql = "";

        List<Song> list;

        if (search != null && search.length() > 0) {
            sql = "select * from song WHERE title LIKE ? OR artist LIKE ? OR album LIKE ?";
            //模糊查询的关键字，添加 '%' 通配符
            String searchPattern = "%" + search + "%";
            list = template.query(sql, new BeanPropertyRowMapper<Song>(Song.class), searchPattern, searchPattern, searchPattern);
        } else {
            sql = "select * from song";
            list = template.query(sql,new BeanPropertyRowMapper<Song>(Song.class));
        }

        return list;
    }

    @Override
    public Song queryBySid(int sid) {

        Song song = null;
        try {
            String sql = "select * from song where sid = ?";

            song = template.queryForObject(sql, new BeanPropertyRowMapper<Song>(Song.class), sid);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        return song;
    }

    @Override
    public List<Song> queryLikeSongByUid(String search, int uid) {
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

        // 将最终的 SQL 语句转为字符串
        sql = sb.toString();

        // 使用 RowMapper 将查询结果映射到 Song 对象
        return template.query(sql, new BeanPropertyRowMapper<>(Song.class), params.toArray());
    }
}
