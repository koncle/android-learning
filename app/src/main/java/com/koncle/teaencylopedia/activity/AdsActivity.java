package com.koncle.teaencylopedia.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.koncle.teaencylopedia.R;
import com.koncle.teaencylopedia.data.DataApi;
import com.koncle.teaencylopedia.data.HandlerType;

/**
 * Created by 10976 on 2017/7/16.
 */

public class AdsActivity extends AppCompatActivity {
    private TextView ticker;
    private ImageView ads;
    private String imgUrl;
    private boolean ticking = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads);

        this.imgUrl = DataApi.ADS_URL;

        initView();

        initImgView();

        initTicker();
    }

    private void initImgView() {
        Glide.with(this)
                .load(this.imgUrl)
                .into(ads);
    }

    private void initTicker() {
        ticker.setText("跳过");

        ticker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ticking = false;
                AdsActivity.this.closeActivity();
            }
        });

        // 绑定 handler 到当前线程
        final TickerHandler tickerHandler = new TickerHandler();

        // 开始计时器
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 5; i >= 0; i--) {
                    if (!ticking){
                        break;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    Message msg = new Message();
                    msg.what = HandlerType.TIME_MSG;
                    msg.arg1 = i;
                    if (i == 0){
                        ticking = false;
                    }
                    tickerHandler.sendMessage(msg);
                }
            }
        }).start();
    }

    private void initView() {
        ticker = (TextView)findViewById(R.id.ticker);
        ads = (ImageView)findViewById(R.id.ads);
    }

    class TickerHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == HandlerType.TIME_MSG){
                int nextTime = msg.arg1;
                if (nextTime <= 3) {
                    ticker.setText(nextTime + "秒 跳过");
                }
                if (nextTime == 0 && !ticking){
                    closeActivity();
                }
            }
        }
    }

    private void closeActivity(){
        AdsActivity.this.finish();
        AdsActivity.this.overridePendingTransition(R.anim.in, R.anim.out);
        Intent intent = new Intent(this.getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}
