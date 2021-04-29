package com.xiao.compound.bean;


public class CatInfo {
    /**
     * 等级
     */
    public int level = 1;
    /**
     * 是不是一只刚出生的猫
     */
    public boolean newCat;
    /**
     * 按下中
     */
    public boolean touching;

    public CatInfo(int level) {
        this.level = level;
    }
}
