package com.endrawan.dlingua.Classes;

/**
 * Endrawan made this on 25/11/2016.
 */
public class LocationItem {
    private String Name;
    private String ImgUrl;
    private String FileBranch;

    public LocationItem() {

    }

    public LocationItem(String Name, String ImgUrl, String FileBranch) {
        this.Name = Name;
        this.ImgUrl = ImgUrl;
        this.FileBranch = FileBranch;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImgUrl() {
        return ImgUrl;
    }

    public void setImgUrl(String imgUrl) {
        ImgUrl = imgUrl;
    }

    public String getFileBranch() {
        return FileBranch;
    }

    public void setFileBranch(String fileBranch) {
        FileBranch = fileBranch;
    }
}
