package cn.gent1.gbc.web.servlet;

import cn.gent1.gbc.domain.ResultInfo;
import cn.gent1.gbc.domain.User;
import cn.gent1.gbc.service.UserService;
import cn.gent1.gbc.service.impl.UserServiceImpl;
import cn.gent1.gbc.util.UuidUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/user/*")
public class UserServlet extends BaseServlet {
    private UserService userService = new UserServiceImpl();

    /**
     * 注册
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void register(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.获取验证码,进行验证码校验
        String check = request.getParameter("check");
        //2.从session中获取验证码
        HttpSession session = request.getSession();
        String checkCode_session = (String) session.getAttribute("checkCode_session");
        session.removeAttribute("checkCode_session");
        //3.判断
        if (checkCode_session == null || !checkCode_session.equalsIgnoreCase(check)) {
            //验证码错误
            ResultInfo info = new ResultInfo();
            info.setFlag(false);
            info.setErrorMsg("验证码错误!");

            //将info序列化为json，并返回客户端
            writeValue(info, response);   //自定义方法
            return;
        }


        //1.获取数据
        Map<String, String[]> map = request.getParameterMap();
        //2.封装数据
        User user = new User();
        try {
            BeanUtils.populate(user, map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        //3.调用Service完成注册
        boolean flag = userService.register(user);
        ResultInfo info = new ResultInfo();
        //4.响应结果
        if (flag) {
            //注册成功
            info.setFlag(true);
        } else {
            //注册失败
            info.setFlag(false);
            info.setErrorMsg("注册失败!");
        }

        //将info序列化为json，并写回客户端
        writeValue(info, response);
    }

    /**
     * 用户激活
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void activate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.获取激活码
        String code = request.getParameter("code");
        if (code != null) {
            //2.调用service完成激活
            boolean flag = userService.activate(code);

            //3.判断
            String msg = null;
            if (flag) {
                //激活成功
                //msg = "激活成功，请<a href='http://localhost:80/gbc/login.html'>登录</a>";
                msg = "激活成功，请<a href='https://gent1.cn/gbc/login.html'>登录</a>";
            } else {
                //激活失败
                msg = "激活失败，请联系管理员";
            }

            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write(msg);
        }
    }

    /**
     * 用户登录
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //记住我
        String rememberMe = request.getParameter("rememberMe");

        //1.获取验证码
        String check = request.getParameter("check");
        //2.从session中获取验证码
        HttpSession session = request.getSession();
        String checkCode_session = (String) session.getAttribute("checkCode_session");
        session.removeAttribute("checkCode_session");
        //3.判断
        if (checkCode_session == null || !checkCode_session.equalsIgnoreCase(check)) {
            //验证码错误
            ResultInfo info = new ResultInfo();
            info.setFlag(false);
            info.setErrorMsg("验证码错误!");

            //将info序列化为json，并返回客户端
            writeValue(info, response);   //自定义方法
            return;
        }


        //1.获取数据
        Map<String, String[]> map = request.getParameterMap();
        //2.封装数据
        User user = new User();
        try {
            BeanUtils.populate(user, map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        //3.调用service
        User u = userService.login(user);
        ResultInfo info = new ResultInfo();

        //4.判断
        if (u == null) {
            //账户不存在
            info.setFlag(false);
            info.setErrorMsg("邮箱或密码错误");
        } else {
            //账户存在
            if (!"Y".equals(u.getStatus())) {
                //未激活
                info.setFlag(false);
                info.setErrorMsg("尚未激活，请在邮箱中完成激活");
            } else {
                //登录成功
                info.setFlag(true);

                //判断是否勾选记住我
                if ("on".equals(rememberMe)) {
                    // 用户勾选了“记住我”,调用Service完成接下来的操作
                    //生成 Token 并存入 Cookie
                    String token = UuidUtil.getUuid();
                    Cookie cookie = new Cookie("rememberMe", token);
                    cookie.setMaxAge(60 * 60 * 24 * 7); // 7 天有效
                    cookie.setHttpOnly(true); // 防止 JavaScript 访问 Cookie
                    cookie.setSecure(true);   // 确保 Cookie 只通过 HTTPS 发送
                    response.addCookie(cookie);

                    userService.rememberMe(u, token);    // 将 token 存入数据库
                }

                //用户信息存入session
                request.getSession().setAttribute("user", u);

            }
        }

        //5.数据写回客户端
        writeValue(info, response);
    }

    /**
     * 寻找一个用户
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //从Session中获取登录用户
        Object user = request.getSession().getAttribute("user");

        if (user == null) {
            String token = null;
            // 检查 Cookie 中是否有 rememberMe
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("rememberMe".equals(cookie.getName())) {
                        token = cookie.getValue();
                        break;
                    }
                }
            }

            if (token != null) {
                User u = userService.checkToken(token);

                if (u != null) {
                    //存入session
                    request.getSession().setAttribute("user", u);
                }

                //写回客户端
                writeValue(u, response);
            }
        }

        //写回客户端
        writeValue(user, response);
    }

    /**
     * 退出登录
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void exit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //1. 清除数据库中的 remember_me_token
        User user = (User) request.getSession().getAttribute("user");
        if (user != null) {
            userService.clearRememberMeToken(user);
        }

        // 2. 清除 rememberMe Cookie
        Cookie cookie = new Cookie("rememberMe", null);
        cookie.setMaxAge(0);  // 设置过期时间为0，表示删除该 Cookie
        response.addCookie(cookie);

        //3.销毁session
        request.getSession().invalidate();

        //4.跳转
        response.sendRedirect(request.getContextPath() + "/login.html");
    }



}



