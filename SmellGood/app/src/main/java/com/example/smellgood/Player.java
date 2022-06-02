package com.example.smellgood;

import java.io.Serializable;

public class Player implements Serializable {
    private String name,nickname,score,robo,ballance;

    public Player(String name, String nickname, String score, String robo, String ballance) {
        this.name = name;
        this.nickname = nickname;
        this.score = score;
        this.robo = robo;
        this.ballance = ballance;
    }

    public Player(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getRobo() {
        return robo;
    }

    public void setRobo(String robo) {
        this.robo = robo;
    }

    public String getBallance() {
        return ballance;
    }

    public void setBallance(String ballance) {
        this.ballance = ballance;
    }
}
