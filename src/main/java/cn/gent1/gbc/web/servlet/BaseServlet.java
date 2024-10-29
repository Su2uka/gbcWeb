package cn.gent1.gbc.web.servlet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BaseServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        System.out.println("baseServlet的service方法被执行了...");

        //完成方法分发
        //1.获取请求路径
        String uri = req.getRequestURI();  //  /gbc/user/add
            //System.out.println("请求uri"+uri);
        //2.获取方法名称
        String methodName = uri.substring(uri.lastIndexOf('/')+1);
            //System.out.println("方法名称"+methodName);
        //3.获取方法对象Method
            //System.out.println(this);  //UserServlet的对象
        try {
            Method method = this.getClass().getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
            //4.执行方法
            method.invoke(this,req,resp);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将传入的对象序列化为json，并且写回客户端
     * @param obj
     * @param response
     * @throws IOException
     */
    public void writeValue(Object obj ,HttpServletResponse response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json;charset=utf-8");
        mapper.writeValue(response.getOutputStream(),obj);
    }

    /**
     *
     * @param obj
     * @return
     */
    public String writeValueAsString(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(obj);
    }
}
