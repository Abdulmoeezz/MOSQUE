package com.example.mymosque.Model;

import com.google.gson.annotations.SerializedName;

public class AskImam {


    public String getQuestiion() {
        return questiion;
    }

    public void setQuestiion(String questiion) {
        this.questiion = questiion;
    }

    public String getM_id() {
        return m_id;
    }

    public void setM_id(String m_id) {
        this.m_id = m_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getAnwer() {
        return anwer;
    }

    public void setAnwer(String anwer) {
        this.anwer = anwer;
    }

    public int getQ_id() {
        return q_id;
    }

    public void setQ_id(int q_id) {
        this.q_id = q_id;
    }

    @SerializedName("questiion")
    private String questiion;

    @SerializedName("m_id")
    private String m_id;

    @SerializedName("user_id")
    private String user_id;

    public AskImam(String questiion, String m_id, String user_id, String anwer, int q_id) {
        this.questiion = questiion;
        this.m_id = m_id;
        this.user_id = user_id;
        this.anwer = anwer;
        this.q_id = q_id;
    }

    @SerializedName("anwer")
    private String anwer;


    @SerializedName("q_id")
    private int q_id;



}
