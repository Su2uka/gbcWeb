package cn.gent1.gbc.web.servlet;

import cn.gent1.gbc.domain.PageBean;
import cn.gent1.gbc.domain.Song;
import cn.gent1.gbc.domain.User;
import cn.gent1.gbc.service.SongService;
import cn.gent1.gbc.service.impl.SongServiceImpl;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/song/*")
public class SongServlet extends BaseServlet {

    private SongService songService = new SongServiceImpl();

    /**
     * 分页查询
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void pageQuery(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.接收参数
        String currentPageStr = request.getParameter("currentPage");
        String pageSizeStr = request.getParameter("pageSize");

        String search = request.getParameter("search");


        //2.处理参数
        if (search != null && search.length() > 0 && !"null".equals(search)) {
            //search = new String(search.getBytes("iso-8859-1"), "utf-8");
        } else {
            search = "";
        }



        int currentPage = 0;
        if (currentPageStr != null && currentPageStr.length() > 0) {
            currentPage = Integer.parseInt(currentPageStr);
        } else {
            currentPage = 1;
        }

        int pageSize = 0;
        if (pageSizeStr != null && pageSizeStr.length() > 0) {
            pageSize = Integer.parseInt(pageSizeStr);
        } else {
            pageSize = 5;
        }

        //3.调用Service查询PageBean对象
        PageBean<Song> pb = songService.pageQuery(currentPage, pageSize, search);

        //4.将PageBean对象序列化为json
        writeValue(pb, response);

    }

    public void likePageQuery(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.接收参数
        String currentPageStr = request.getParameter("currentPage");
        String pageSizeStr = request.getParameter("pageSize");

        String search = request.getParameter("search");

        //从session取出user
        User user = (User) request.getSession().getAttribute("user");
        int uid;
        if (user != null) {
            uid = user.getUid();
        } else {
            return;
        }


        //2.处理参数
        if (search != null && search.length() > 0 && !"null".equals(search)) {
            //search = new String(search.getBytes("iso-8859-1"), "utf-8");
        } else {
            search = "";
        }

        int currentPage = 0;
        if (currentPageStr != null && currentPageStr.length() > 0) {
            currentPage = Integer.parseInt(currentPageStr);
        } else {
            currentPage = 1;
        }

        int pageSize = 0;
        if (pageSizeStr != null && pageSizeStr.length() > 0) {
            pageSize = Integer.parseInt(pageSizeStr);
        } else {
            pageSize = 5;
        }


        //3.调用Service查询PageBean对象
        PageBean<Song> pb = songService.likePageQuery(currentPage, pageSize, search, uid);

        //4.将PageBean对象序列化为json
        writeValue(pb, response);
    }

    /**
     * 查询所有
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void queryAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String search = request.getParameter("search");

        //处理参数
        if (search != null && search.length() > 0 && !"null".equals(search)) {
            //search = new String(search.getBytes("iso-8859-1"), "utf-8");
        } else {
            search = "";
        }

        List<Song> list = songService.queryAll(search);

        writeValue(list, response);
    }


    public void download(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.获取参数
        String sid = request.getParameter("sid");

        //2.调用Service
        String downloadLink = songService.download(sid);

        //3.写回客户端
        writeValue(downloadLink, response);
    }


    public void queryAllLike(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String search = request.getParameter("search");

        //从session取出user
        User user = (User) request.getSession().getAttribute("user");

        int uid;
        if (user != null) {
            uid = user.getUid();
        } else {
            return;
        }

        //处理参数
        if (search != null && search.length() > 0 && !"null".equals(search)) {
            //search = new String(search.getBytes("iso-8859-1"), "utf-8");
        } else {
            search = "";
        }


        List<Song> list = songService.queryAllLike(search,uid);

        writeValue(list, response);

    }


}
