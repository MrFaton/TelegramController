package com.mr_faton.core.entity;

import java.io.Serializable;

public class Telegram implements Serializable {
    private String header;
    private String digitalHeader;
    private int searchDepth;
    private String checkPattern;
    private boolean state;

    public String getHeader() {
        return header;
    }
    public void setHeader(String header) {
        this.header = header;
    }

    public String getDigitalHeader() {
        return digitalHeader;
    }
    public void setDigitalHeader(String digitalHeader) {
        this.digitalHeader = digitalHeader;
    }

    public int getSearchDepth() {
        return searchDepth;
    }
    public void setSearchDepth(int searchDepth) {
        this.searchDepth = searchDepth;
    }

    public String getCheckPattern() {
        return checkPattern;
    }
    public void setCheckPattern(String checkPattern) {
        this.checkPattern = checkPattern;
    }

    public boolean getState() {
        return state;
    }
    public void setState(boolean state) {
        this.state = state;
    }



    @Override
    public String toString() {
        return header;
    }

    @Override
    public int hashCode() {
        return header.hashCode() + digitalHeader.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        Telegram otherTelegram = (Telegram) obj;
        return this.header.equals(otherTelegram.header) && this.digitalHeader.equals(otherTelegram.digitalHeader);
    }
}
