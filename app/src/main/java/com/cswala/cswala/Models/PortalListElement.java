package com.cswala.cswala.Models;

public class PortalListElement {
    String title, link;
    boolean isCompany;

    public PortalListElement(String title, String link, boolean isCompany) {
        this.title = title;
        this.link = link;
        this.isCompany = isCompany;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public boolean isCompany() {
        return isCompany;
    }

    public void setCompany(boolean company) {
        isCompany = company;
    }

    @Override
    public String toString() {
        return "PortalListElement{" +
                "title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", isCompany=" + isCompany +
                '}';
    }
}
