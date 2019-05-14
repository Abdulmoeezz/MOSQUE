package com.example.mymosque.Model;

public class DuaList {

    private int d_id,duration ;
    private String name,file_path,file_name,is_active,timestamp,discription,discriptionArbic;

    public DuaList() {
    }

    public DuaList(int d_id, int duration, String name, String file_path, String file_name, String is_active, String timestamp, String discription, String discriptionArbic) {
        this.d_id = d_id;
        this.duration = duration;
        this.name = name;
        this.file_path = file_path;
        this.file_name = file_name;
        this.is_active = is_active;
        this.timestamp = timestamp;
        this.discription = discription;
        this.discriptionArbic = discriptionArbic;
    }

    public int getD_id() {
        return d_id;
    }

    public void setD_id(int d_id) {
        this.d_id = d_id;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getIs_active() {
        return is_active;
    }

    public void setIs_active(String is_active) {
        this.is_active = is_active;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public String getDiscriptionArbic() {
        return discriptionArbic;
    }

    public void setDiscriptionArbic(String discriptionArbic) {
        this.discriptionArbic = discriptionArbic;
    }
}
