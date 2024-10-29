package cn.gent1.gbc.web.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 解决全站乱码问题，处理所有的请求
 */
@WebFilter("/*")
public class CharacterFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse rep, FilterChain filterChain) throws IOException, ServletException {
        //将父接口转为子接口
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) rep;
        //获取请求方法
        String method = request.getMethod();
        String uri = request.getRequestURI();

        // 根据文件扩展名设置不同的 Content-Type
        if (uri.endsWith(".css")) {
            response.setContentType("text/css;charset=UTF-8");
        } else if (uri.endsWith(".js")) {
            response.setContentType("application/javascript;charset=UTF-8");
        } else if (uri.endsWith(".jpg") || uri.endsWith(".jpeg")) {
            response.setContentType("image/jpeg");
        } else if (uri.endsWith(".png")) {
            response.setContentType("image/png");
        } else if (uri.endsWith(".gif")) {
            response.setContentType("image/gif");
        } else if (uri.endsWith(".mp3")) {
            response.setContentType("audio/mpeg");
        } else if (uri.endsWith(".webm")) {
            response.setContentType("video/webm");
        } else if (uri.endsWith(".webp")) {
            response.setContentType("image/webp");
        }else {
            // 处理动态内容（如HTML或其他非静态资源）的请求和响应
            // 解决POST请求中文数据乱码问题
            if (method.equalsIgnoreCase("post")) {
                request.setCharacterEncoding("utf-8");
            }
            // 仅对动态内容（HTML等）设置响应的 Content-Type
            response.setContentType("text/html;charset=utf-8");
        }

        // 继续过滤链
        filterChain.doFilter(request, response);

    }

    @Override
    public void destroy() {

    }
}
