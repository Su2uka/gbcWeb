package cn.gent1.gbc.web.filter;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*")
public class LoginFilter implements Filter {
    public void init(FilterConfig config) throws ServletException {
    }

    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        // 获取请求的 URL 路径
        String requestURI = httpRequest.getRequestURI();


        // 检查是否请求了 song_list_like.html 页面
        if (requestURI.endsWith("songs_list_like.html")) {
            // 检查用户是否已登录（假设登录信息保存在 session 中）
            if (session == null || session.getAttribute("user") == null) {
                // 用户未登录，重定向到登录页面
                httpResponse.sendRedirect("login.html"); //
                return;
            }
        }

        // 如果不是访问 song_list_like.html 或已登录用户，则继续处理请求
        chain.doFilter(request, response);
    }
}
