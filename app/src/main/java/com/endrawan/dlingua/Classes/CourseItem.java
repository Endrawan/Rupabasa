package com.endrawan.dlingua.Classes;

/**
 * Endrawan made this on 08/01/2017.
 */
public class CourseItem {
    private String FileBranch;
    private String ImgUrl;
    private String LanguageKey;
    private String Name;

    public CourseItem() {

    }

    public CourseItem(String FileBranch, String ImgUrl, String LanguageKey, String Name) {
        this.FileBranch = FileBranch;
        this.ImgUrl = ImgUrl;
        this.LanguageKey = LanguageKey;
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

    public void setImgUrl(String ImgUrl) {
        this.ImgUrl = ImgUrl;
    }

    public String getLanguageKey() {
        return LanguageKey;
    }

    public void setLanguageKey(String languageKey) {
        LanguageKey = languageKey;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
