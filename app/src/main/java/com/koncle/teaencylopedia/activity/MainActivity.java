package com.koncle.teaencylopedia.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.koncle.teaencylopedia.R;
import com.koncle.teaencylopedia.anim.MAnimation;
import com.koncle.teaencylopedia.data.DataApi;
import com.koncle.teaencylopedia.fragment.HasName;
import com.koncle.teaencylopedia.fragment.MainFragment;
import com.koncle.teaencylopedia.fragment.OtherFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 10976 on 2017/7/16.
 */

public class MainActivity extends AppCompatActivity {
    private TabLayout tab;
    private ViewPager viewPager;
    private List<Fragment> fragmentList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        initFragments();

        initViewPager();

        initTab();
    }

    private void initFragments() {
        fragmentList = new ArrayList<>();
        fragmentList.add(MainFragment.newInstance("头条", DataApi.HEADLINE_URL));
        fragmentList.add(OtherFragment.newInstance("百科", DataApi.BASE_URL + DataApi.CYCLOPEDIA_TYPE));
        fragmentList.add(OtherFragment.newInstance("资讯", DataApi.BASE_URL + DataApi.CONSULT_TYPE));
        fragmentList.add(OtherFragment.newInstance("经营", DataApi.BASE_URL + DataApi.OPERATE_TYPE));
        fragmentList.add(OtherFragment.newInstance("数据", DataApi.BASE_URL + DataApi.DATA_TYPE));
    }

    private void initViewPager() {
        viewPager.setAdapter(new FragmentPagerAdapter(this.getSupportFragmentManager()){
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList == null? 0 : fragmentList.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return ((HasName)fragmentList.get(position)).getName();
            }
        });
        viewPager.setPageTransformer(false, new MAnimation());
        viewPager.setOffscreenPageLimit(2); // 最大缓存页数 为 limit * 2 + 1
    }

    private void initTab() {
        tab.setupWithViewPager(viewPager);
        tab.setTabMode(TabLayout.MODE_FIXED);
    }

    private void initView() {
        tab = (TabLayout)findViewById(R.id.tab);
        viewPager = (ViewPager)findViewById(R.id.viewpager);
    }
}
