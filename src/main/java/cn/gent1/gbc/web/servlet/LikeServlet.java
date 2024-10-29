package cn.gent1.gbc.web.servlet;

import cn.gent1.gbc.domain.Like;
import cn.gent1.gbc.domain.ResultInfo;
import cn.gent1.gbc.domain.User;
import cn.gent1.gbc.service.LikeService;
import cn.gent1.gbc.service.impl.LikeServiceImpl;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/like/*")
public class LikeServlet extends BaseServlet {

    private LikeService likeService = new LikeServiceImpl();

    public void queryLike(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //从session中获取user对象
        User user = (User) request.getSession().getAttribute("user");

        if (user != null) {
            List<Like> list =  likeService.queryLike(user);
            writeValue(list,response);
        }

    }

    public void cancelLike(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //1.获取参数
        String uid = request.getParameter("uid");
        String sid = request.getParameter("sid");

        //2.调用Service
        likeService.cancelLike(uid,sid);

    }

    public void addLike(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.获取参数
        String uid = request.getParameter("uid");
        String sid = request.getParameter("sid");

        //2.调用Service
        likeService.addLike(uid,sid);
    }


    public void checkLike(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.获取参数
        String uid = request.getParameter("uid");
        String sid = request.getParameter("sid");

        //2.调用Service
        Like like = likeService.checkLike(uid,sid);

        //3.判断
        ResultInfo info = new ResultInfo();

        if (like == null) {
            info.setFlag(false);
        } else {
            info.setFlag(true);
        }

        //写回客户端
        writeValue(info,response);
    }


}
