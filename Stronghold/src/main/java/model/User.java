package model;

import utility.*;
import utility.SHA;

public class User {
    private String username;
    private String password;
    private String nickname;
    private String email;
    private String slogan;
    private int securityQuestionNumber;

    private int rank;
    private int score;
    private int highScore;
    private final String securityAnswer;

    private String avatarPath;

    public User(String username, String password, String nickname, String email, int securityQuestionNumber, String securityAnswer,String slogan) {
        this.username = username;
        this.nickname = nickname;
        this.email = email.toLowerCase();
        this.securityQuestionNumber = securityQuestionNumber;
        this.securityAnswer = securityAnswer;
        this.slogan = slogan;
        this.avatarPath = RandomGenerators.randomAvatar();
        setPassword(password);
        Database.addUser(this);
    }

    public String getUsername() {
        return username;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

    public String getSlogan() {
        return slogan;
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public String getAvatarPathSahand() {
        return "file:src/main/resources" + avatarPath;
    }

    public int getHighScore() {
        return highScore;
    }

    public int getSecurityQuestionNumber() {
        return securityQuestionNumber;
    }

    public void setUsername(String username) {
        this.username = username;
        DataManager.saver();
    }

    public void setPassword(String password) {
        this.password = SHA.shaString(password);
        DataManager.saver();
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
        DataManager.saver();
    }

    public void setEmail(String email) {
        this.email = email.toLowerCase();
        DataManager.saver();
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
        DataManager.saver();
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
        DataManager.saver();
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
        DataManager.saver();
    }

    public boolean checkPassword(String passwordToCheck) {
        return SHA.shaString(passwordToCheck).equals(password);
    }

    public boolean checkAnswer(String answerToCheck) {
        return securityAnswer.equalsIgnoreCase(answerToCheck);
    }

    public boolean checkEmail(String emailToCheck) {
        return email.equalsIgnoreCase(emailToCheck);
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
        DataManager.saver();
    }


    public int getScore() {
        return score;
    }

    @Override
    public String toString() {
        String s = username;
        s += "\n" + nickname;
        s += "\n" + email;
        return s;
    }
}
