package com.pitstop.model;

public class Recipe {
    private int id;
    private String region;
    private String name;
    private int requiredStamps;
    private String content;
    private boolean isUnlocked;

    public Recipe(int id, String region, String name, int requiredStamps, String content) {
        this.id = id;
        this.region = region;
        this.name = name;
        this.requiredStamps = requiredStamps;
        this.content = content;
        this.isUnlocked = false;
    }

    public int getId() { return id; }
    public String getRegion() { return region; }
    public String getName() { return name; }
    public int getRequiredStamps() { return requiredStamps; }
    public String getContent() { return content; }
    public boolean isUnlocked() { return isUnlocked; }
    public void setUnlocked(boolean unlocked) { isUnlocked = unlocked; }
}
