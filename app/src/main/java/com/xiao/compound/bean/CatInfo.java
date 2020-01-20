package com.xiao.compound.bean;

/**
 * Desc:
 * <p> @Author: ZhouTao
 * <p>Date: 2020/1/18
 * <p>Copyright: Copyright (c) 2016-2020
 * <p>Company: @小牛科技
 * <p>Email:zhoutao@xiaoniuhy.com
 * <p>Update Comments:
 */
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
