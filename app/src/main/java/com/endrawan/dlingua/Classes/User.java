package com.endrawan.dlingua.Classes;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Endrawan made this on 02/12/2016.
 */

@IgnoreExtraProperties
public class User {
    private int Rupiah;
    private int Level;
    private int Exp;
    private String Username;

    public User() {

    }

    public User(int Rupiah, int Level, int Exp) {
        this.Rupiah = Rupiah;
        this.Level = Level;
        this.Exp = Exp;
    }

    public User(int Rupiah, int Level, int Exp, String username) {
        this.Rupiah = Rupiah;
        this.Level = Level;
        this.Exp = Exp;
        this.Username = username;
    }

    public int getRupiah() {
        return Rupiah;
    }

    public void setRupiah(int rupiah) {
        Rupiah = rupiah;
    }

    public int getLevel() {
        return Level;
    }

    public void setLevel(int level) {
        Level = level;
    }

    public int getExp() {
        return Exp;
    }

    public void setExp(int exp) {
        Exp = exp;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }
}
