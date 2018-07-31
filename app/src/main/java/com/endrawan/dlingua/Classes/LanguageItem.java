package com.endrawan.dlingua.Classes;

/**
 * Endrawan made this on 02/12/2016.
 */
public class LanguageItem {
    private String FileBranch;
    private String ImgUrl;
    private String LocationKey;
    private String Name;

    public LanguageItem() {

    }

    public LanguageItem(String FileBranch, String ImgUrl, String LocationKey, String Name) {
        this.FileBranch = FileBranch;
        this.ImgUrl = ImgUrl;
        this.LocationKey = LocationKey;
        this.Name = Name;
    }

    public String getFileBranch() {
        return FileBranch;
    }

    public void setFileBranch(String fileBranch) {
        FileBranch = fileBranch;
    }

    public String getImgUrl() {
        return ImgUrl;
    }

    public void setImgUrl(String imgUrl) {
        ImgUrl = imgUrl;
    }

    public String getLocationKey() {
        return LocationKey;
    }

    public void setLocationKey(String locationKey) {
        LocationKey = locationKey;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
