package cn.gent1.gbc.web.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;

@WebServlet("/checkCode")
public class CheckCodeServlet extends HttpServlet {

    // 定义验证码图片的宽度和高度
    private static final int WIDTH = 120;
    private static final int HEIGHT = 40;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //服务器通知浏览器不要缓存
        response.setHeader("pragma","no-cache");
        response.setHeader("cache-control","no-cache");
        response.setHeader("expires","0");

        // 设置响应的内容类型为图片
        response.setContentType("image/png");

        // 创建一个指定宽度和高度的BufferedImage对象
        BufferedImage bufferedImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

        // 获取绘图对象
        Graphics2D g2d = bufferedImage.createGraphics();

        // 设置背景色并填充矩形
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, WIDTH, HEIGHT);

        // 生成随机的验证码字符串
        String checkCode = generateCheckCodeText(4); // 生成6位长度的验证码
        request.getSession().setAttribute("checkCode_session", checkCode); // 将验证码存储在session中

        // 用随机颜色和位置绘制验证码字符
        Random random = new Random();
        for (int i = 0; i < checkCode.length(); i++) {
            g2d.setFont(new Font("Arial", Font.BOLD, 28));
            g2d.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
            g2d.drawString(String.valueOf(checkCode.charAt(i)), 20 * i + 20, 30);
        }

        // 可选：添加一些随机的干扰线
        for (int i = 0; i < 5; i++) {
            g2d.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
            g2d.drawLine(random.nextInt(WIDTH), random.nextInt(HEIGHT), random.nextInt(WIDTH), random.nextInt(HEIGHT));
        }

        // 释放图形对象资源
        g2d.dispose();

        // 将生成的图片写入响应输出流
        ImageIO.write(bufferedImage, "png", response.getOutputStream());

        // 关闭输出流
        response.getOutputStream().close();
    }
    // 生成指定长度的随机验证码文本
    private String generateCheckCodeText(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder checkCode = new StringBuilder(length);
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            checkCode.append(chars.charAt(random.nextInt(chars.length())));
        }
        return checkCode.toString();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
