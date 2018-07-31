package com.endrawan.dlingua.Classes;

/**
 * Endrawan made this on 28/01/2017.
 */
public class ShopItem {

    private String name;
    private String information;
    private int drawable;
    private int price;

    public ShopItem(String name, String information, int drawable, int price) {
        this.name = name;
        this.information = information;
        this.drawable = drawable;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
