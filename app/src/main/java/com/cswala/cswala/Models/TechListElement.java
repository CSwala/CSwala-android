package com.cswala.cswala.Models;

public class TechListElement {
    private String techName,techTag;

    public TechListElement(String techName, String techTag) {
        this.techName = techName;
        this.techTag = techTag;
    }

    public String getTechName() {
        return techName;
    }

    public void setTechName(String techName) {
        this.techName = techName;
    }

    public String getTechTag() {
        return techTag;
    }

    public void setTechTag(String techTag) {
        this.techTag = techTag;
    }
}
