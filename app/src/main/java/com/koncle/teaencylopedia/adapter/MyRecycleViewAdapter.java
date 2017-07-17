package com.koncle.teaencylopedia.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.koncle.teaencylopedia.R;
import com.koncle.teaencylopedia.activity.DetailActivity;
import com.koncle.teaencylopedia.bean.ImgContent;
import com.koncle.teaencylopedia.bean.ImgJson;
import com.koncle.teaencylopedia.bean.Tea;
import com.koncle.teaencylopedia.bean.TeaJson;
import com.koncle.teaencylopedia.data.DataApi;
import com.koncle.teaencylopedia.data.HandlerType;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by 10976 on 2017/7/16.
 */

public class MyRecycleViewAdapter extends RecyclerView.Adapter<MyRecycleViewAdapter.ViewHolder> implements View.OnClickListener {

    private final int CAROUSEL_TYPE = 0xf;
    private final int LIST_TYPE = 0xff;

    private final LayoutInflater inflater;
    private final int listStartPos;
    private final String url;
    private List<Tea> teaList;
    private int page;
    private Context context;
    private List<RadioButton> indicators;
    private ViewPager viewPager;
    private int curPos;
    private Handler handler;
    private MCarouselAdapter carousel;

    private boolean animationsLocked = false;
    private int lastAnimatedPosition = -1;
    private boolean delayEnterAnimation = true;

    public MyRecycleViewAdapter(Context context, int listStartPos, String url){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.listStartPos = listStartPos;
        this.page = 1;
        this.teaList = new ArrayList<>();
        this.url = url;
        this.curPos = 0;
        this.handler = new TeaHandler();
    }

    public void setData(List<Tea> data){
        if (data == null){
            Toast.makeText(this.context, "没有数据啦~~", Toast.LENGTH_SHORT).show();
            return;
        }
        this.teaList.addAll(data);
        this.notifyDataSetChanged();
    }

    public void addMoreData(){
        DataApi.getData(this.handler, this.url + (page++), TeaJson.class,HandlerType.TEA_MSG);
    }

    public void initCarouselData(String carouselDataUrl) {
        DataApi.getData(this.handler, carouselDataUrl, ImgJson.class, HandlerType.CAROUSEL_MSG);
    }

    // 消息的数据处理
    public Handler getTeaHandler() {
        return handler;
    }

    // 每隔 3 秒，发送轮播消息
    public void startTimer(){
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Message msg = new Message() ;
                msg.what = HandlerType.TIME_MSG;
                handler.sendMessage(msg);
            }
        }, 3000, 3000);
    }

    class TeaHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            // 处理 列表数据
            if (msg.what == HandlerType.TEA_MSG){
                List<Tea> teas = ((TeaJson)msg.getData().getParcelableArrayList("data").get(0)).getData();
                setData(teas);
                // 处理轮播图数据
            }else if (msg.what == HandlerType.CAROUSEL_MSG){
                List<ImgContent> imgContents = ((ImgJson)msg.getData().getParcelableArrayList("data").get(0)).getData();
                carousel.setData(imgContents);
                // 处理轮播图定时器
            }else if (msg.what == HandlerType.TIME_MSG){
                int nextPos = (++curPos) % indicators.size();
                indicators.get(nextPos).setChecked(true);
                MyRecycleViewAdapter.this.viewPager.setCurrentItem(nextPos);
            }
        }
    }
    // ==================
    // 轮播图初始化
    private void initIndicator(View view) {

        this.indicators = new ArrayList<>();
        indicators.add((RadioButton) view.findViewById(R.id.indicator0));
        indicators.add((RadioButton) view.findViewById(R.id.indicator1));
        indicators.add((RadioButton) view.findViewById(R.id.indicator2));

        for (int i = 0; i < indicators.size(); i++) {
            indicators.get(i).setOnClickListener(this);
        }
    }

    private void initViewPager(View view) {
        this.viewPager = (ViewPager)view.findViewById(R.id.carousel);
        this.carousel = new MCarouselAdapter(this.context);
        this.viewPager.setAdapter(this.carousel);
        this.viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            // 设置页面选择事件监听来同时控制指示器的变化
            @Override
            public void onPageSelected(int position) {indicators.get(position).setChecked(true);}

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    // 轮播事件
    @Override
    public void onClick(View view) {
        int pos = -1;
        switch (view.getId()){
            case R.id.indicator0:
                pos = 0;
                break;
            case R.id.indicator1:
                pos = 1;
                break;
            case R.id.indicator2:
                pos = 2;
                break;
        }
        curPos = pos;
        indicators.get(pos).setChecked(true);
        this.viewPager.setCurrentItem(pos);
    }

    // =========================================
    // 重写方法
    @Override
    public int getItemViewType(int position) {
        if (position == listStartPos - 1){
            return CAROUSEL_TYPE;
        }else {
            return LIST_TYPE;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        // 初始化轮播图
        if (viewType == CAROUSEL_TYPE){
            view = inflater.inflate(R.layout.carousel_layout, parent, false);
            initViewPager(view);
            initIndicator(view);
            initCarouselData(DataApi.HEADERIMAGE_URL);
            startTimer();

            indicators.get(0).setChecked(true);

            // 初始化 列表
        }else if (viewType == LIST_TYPE){
            view = inflater.inflate(R.layout.content_main, parent, false);
        }else {
            //throw new Exception("wrong type exception");
        }
        return new ViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder.viewType == LIST_TYPE){

            runEnterAnimation(holder.itemView, position);

            final Tea tea = teaList.get(position - this.listStartPos);
            String imgUrl = tea.getWap_thumb();
            if (!TextUtils.isEmpty(imgUrl)) {
                holder.img.setVisibility(VISIBLE);
                Glide.with(this.context)
                        .load(imgUrl)
                        .into(holder.img);
            }else {
                holder.img.setVisibility(GONE);
            }

            holder.time.setText(tea.getCreate_time());
            holder.from.setText(tea.getSource());
            holder.author.setText(tea.getNickname());
            holder.title.setText(tea.getTitle());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MyRecycleViewAdapter.this.context, DetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("id", tea.getId());
                    intent.putExtras(bundle);
                    MyRecycleViewAdapter.this.context.startActivity(intent);
                }
            });
        }

        if (position == teaList.size() - 1){
            addMoreData();
        }
    }

    private void runEnterAnimation(View view, int position) {

        if (animationsLocked) return;//animationsLocked是布尔类型变量，一开始为false，确保仅屏幕一开始能够显示的item项才开启动画

        if (position > lastAnimatedPosition) {//lastAnimatedPosition是int类型变量，一开始为-1，这两行代码确保了recycleview滚动式回收利用视图时不会出现不连续的效果

            lastAnimatedPosition = position;

            // 设置初始状态
            if (position % 2 == 1) {
                // 右边进入
                view.setTranslationX(500f);
            }else {
                // 左边进入
                view.setTranslationX(-500f);
            }
            view.setAlpha(0.f);//完全透明

            // 设置动画最终状态并开始
            view.animate()
                    .translationX(0).alpha(1.f)//设置最终效果为完全不透明，并且在原来的位置
                    .setStartDelay(delayEnterAnimation ? 100 * (position) : 0)//根据item的位置设置延迟时间，达到依次动画一个接一个进行的效果
                    .setInterpolator(new DecelerateInterpolator(0.5f))//设置动画效果为在动画开始的地方快然后慢
                    .setDuration(200)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            animationsLocked = true;//确保仅屏幕一开始能够显示的item项才开启动画，也就是说屏幕下方还没有显示的item项滑动时是没有动画效果
                        }
                    })
                    .start();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView from;
        public TextView author;
        public TextView time;
        public ImageView img;
        public int viewType;
        public TextView title;

        public ViewHolder(View view, int viewType) {
            super(view);
            if (viewType == LIST_TYPE) {
                from = (TextView) view.findViewById(R.id.from);
                author = (TextView) view.findViewById(R.id.author);
                time = (TextView) view.findViewById(R.id.time);
                img = (ImageView) view.findViewById(R.id.pic);
                title = (TextView) view.findViewById(R.id.title);
            }
            this.viewType = viewType;
        }
    }

    @Override
    public int getItemCount() {
        return this.teaList.size() + listStartPos;
    }
}
