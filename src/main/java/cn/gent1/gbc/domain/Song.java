package cn.gent1.gbc.domain;

public class Song {
    private int sid; //歌曲id
    private String filepath; //MP3路径
    private String title; //歌曲标题
    private String artist;
    private String album; //专辑名
    private String duration; //总时长
    private String cover; //封面图片路径
    private String download; //下载路径

    public Song() {
    }

    public Song(int sid, String filepath, String title, String artist, String album, String duration, String cover, String download) {
        this.sid = sid;
        this.filepath = filepath;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.duration = duration;
        this.cover = cover;
        this.download = download;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getDownload() {
        return download;
    }

    public void setDownload(String download) {
        this.download = download;
    }

}
