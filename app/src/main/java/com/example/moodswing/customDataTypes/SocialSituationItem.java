package com.example.moodswing.customDataTypes;

//The social situation item blueprint to put in the spinner
// this class is not used yet. since the feature is not in release.

public class SocialSituationItem {

    private String situation;
    private int sitImage;

    /**
     * a social situation is intialized with a string describing the social situation(ie. alone), and a
     * corresponding image
     * @param situation the situation string
     * @param sitImage the situation image associated with the string
     */
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
