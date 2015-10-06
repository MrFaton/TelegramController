package com.mr_faton.core.entity;

import java.io.Serializable;

public class DBServer implements Serializable {
    private String name;
    private String url;
    private String user;
    private String userPassword;
    private boolean state;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }
    public void setUser(String user) {
        this.user = user;
    }

    public String getUserPassword() {
        return userPassword;
    }
    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public boolean getState() {
        return state;
    }
    public void setState(boolean state) {
        this.state = state;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        DBServer other = (DBServer) obj;

        return this.name.equals(other.getName()) && this.url.equals(other.getUrl()) &&
                this.user.equals(other.getUser()) && this.userPassword.equals(other.getUserPassword());
    }

    @Override
    public String toString() {
        return name;
    }
}
