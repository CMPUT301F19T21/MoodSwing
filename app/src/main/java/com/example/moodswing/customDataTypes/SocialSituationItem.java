package com.example.moodswing.customDataTypes;

public class SocialSituationItem {

    private String situation;
    private int sitImage;

    public SocialSituationItem(String situation, int sitImage) {
        this.situation = situation;
        this.sitImage = sitImage;
    }

    public String getSituation() {
        return situation;
    }

    public int getSitImage() {
        return sitImage;
    }
}
