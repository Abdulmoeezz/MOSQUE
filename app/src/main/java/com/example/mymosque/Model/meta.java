package com.example.mymosque.Model;

public class meta {

  /*        "meta": {
        "current_page": 1,
                "from": 1,
                "last_page": 7,
                "path": "http://masjidi.co.uk/api/getMosquesList/33",
                "per_page": 2,
                "to": 2,
                "total": 13
*/










    private int  current_page;
    private int  from;
    private int  last_page;
    private int  per_page;
    private int  to;
    private int  total;
    private String  path;


    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getLast_page() {
        return last_page;
    }

    public void setLast_page(int last_page) {
        this.last_page = last_page;
    }

    public int getPer_page() {
        return per_page;
    }

    public void setPer_page(int per_page) {
        this.per_page = per_page;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }






}
