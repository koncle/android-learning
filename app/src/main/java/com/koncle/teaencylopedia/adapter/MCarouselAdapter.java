package com.koncle.teaencylopedia.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.koncle.teaencylopedia.activity.DetailActivity;
import com.koncle.teaencylopedia.bean.ImgContent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 10976 on 2017/7/16.
 */

class MCarouselAdapter extends PagerAdapter {
    private List<ImgContent> imgContentList;
    private Context context;

    public MCarouselAdapter(Context context){
        imgContentList = new ArrayList<>();
        this.context = context;
    }

    public void setData(List<ImgContent> imgContents){
        this.imgContentList.addAll(imgContents);
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return imgContentList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        ImageView imageView = null;
        if (imgContentList.size() >= position) {
            String imgUrl = imgContentList.get(position).getImage();
            imageView = new ImageView(this.context);
            Glide.with(context)
                    .load(imgUrl)
                    .into(imageView);

            imageView.setScaleType(ImageView.ScaleType.FIT_XY);  // 充满容器

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String id = imgContentList.get(position).getId();

                    if (TextUtils.isEmpty(id)){
                        return;
                    }

                    Intent intent = new Intent(MCarouselAdapter.this.context, DetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("id", id);
                    intent.putExtras(bundle);
                    MCarouselAdapter.this.context.startActivity(intent);
                }
            });
            container.addView(imageView);
        }
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ImageView)object);
    }
}
