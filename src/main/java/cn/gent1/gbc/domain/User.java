package cn.gent1.gbc.domain;


import java.io.Serializable;

/**
 * 用户实体类
 */
public class User implements Serializable {
    private int uid; //用户id
    private String email; //邮箱
    private String password; //密码
    private String status;//激活状态，Y代表激活，N代表未激活
    private String code;//激活码（要求唯一）
    private String token; //记住我token

    public User() {
    }

    public User(int uid, String email, String password, String status, String code, String token) {
        this.uid = uid;
        this.email = email;
        this.password = password;
        this.status = status;
        this.code = code;
        this.token = token;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid=" + uid +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", status='" + status + '\'' +
                ", code='" + code + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
