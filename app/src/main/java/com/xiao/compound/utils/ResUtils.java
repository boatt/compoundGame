package com.xiao.compound.utils;

import com.xiao.compound.R;

/**
 * Desc:
 * <p> @Author: ZhouTao
 * <p>Date: 2020/1/18
 * <p>Copyright: Copyright (c) 2016-2020
 * <p>Company: @小牛科技
 * <p>Email:zhoutao@xiaoniuhy.com
 * <p>Update Comments:
 */
public class ResUtils {
    public static int getCatImage(int level) {
        int res = 0;
        if (level == 1) {
            res = R.mipmap.icon_cat;
        }else if (level == 2){
            res = R.mipmap.icon_cat_level2;
        }else if (level == 3){
            res = R.mipmap.icon_cat_level3;
        }else if (level == 4){
            res = R.mipmap.icon_cat_level4;
        }else if (level == 5){
            res = R.mipmap.icon_cat_level5;
        }else if (level == 6){
            res = R.mipmap.icon_cat_level6;
        }
        return res;
    }
}
