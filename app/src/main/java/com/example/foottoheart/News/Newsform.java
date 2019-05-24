package com.example.foottoheart.News;

public class Newsform {
    private int imagenumber;
    private String title;
    private String url;

    public Newsform(int imagenumber, String title,String url){
        this.imagenumber = imagenumber;
        this.title = title;
        this.url = url;
    }

    public int getImagenumber() {
        return imagenumber;
    }

    public String getTitle(){
        return title;
    }
    public String getUrl() {
        return url;
    }
    public void setImagenumber(int imagenumber) {
        this.imagenumber = imagenumber;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
