package com.koncle.teaencylopedia.anim;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by 10976 on 2017/7/16.
 */

public class MAnimation implements ViewPager.PageTransformer{

    @Override
    public void transformPage(View page, float position) {
        if (position > 0){
            page.setTranslationX(0);
            page.setTranslationY(0);
            page.setScaleY(1 - position);
            page.setScaleX(1 - position);
            page.setAlpha(1f - position);
        }else if (position < 0){
            page.setScaleY(1 + position);
            page.setScaleX(1 + position);
            page.setAlpha(1f + position);
        }else {
            page.setScaleX(1);
            page.setScaleX(1);
        }
    }
}
