package com.endrawan.dlingua.Classes;

/**
 * Endrawan made this on 11/01/2017.
 */
public class SubCourseItem {
    private String CourseKey;
    private String FileBranch;
    private String Name;
    private String imgUrl;

    public SubCourseItem() {

    }

    public SubCourseItem(String CourseKey, String FileBranch, String Name, String imgUrl) {
        this.CourseKey = CourseKey;
        this.FileBranch = FileBranch;
        this.Name = Name;
        this.imgUrl = imgUrl;
    }

    public String getCourseKey() {
        return CourseKey;
    }

    public void setCourseKey(String courseKey) {
        CourseKey = courseKey;
    }

    public String getFileBranch() {
        return FileBranch;
    }

    public void setFileBranch(String fileBranch) {
        FileBranch = fileBranch;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
