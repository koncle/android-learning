package com.koncle.teaencylopedia.data;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 10976 on 2017/7/16.
 */

public class DataApi {
    //茶百科中头条
    public static final String HEADLINE_URL = "http://sns.maimaicha.com/api?apikey=b4f4ee31a8b9acc866ef2afb754c33e6&format=json&method=news.getHeadlines";

    //茶百科中 百科，资讯，经营，数据
    //基础的地址
    public static final String BASE_URL = "http://sns.maimaicha.com/api?apikey=b4f4ee31a8b9acc866ef2afb754c33e6&format=json&method=news.getListByType";

    //基础的地址和
    public static final String CYCLOPEDIA_TYPE = "&type=16&rows=15&page=";// 百科
    public static final String CONSULT_TYPE = "&type=52&rows=15&page=";// 资讯
    public static final String OPERATE_TYPE = "&type=53&rows=15&page=";// 经营
    public static final String DATA_TYPE = "&type=54&rows=15&page=";// 数据
    public static final String HEADLINE_TYPE = "&rows=15&page=";// 头条

    //茶百科头条中的图片的地址
    public static final String HEADERIMAGE_URL = "http://sns.maimaicha.com/api?apikey=b4f4ee31a8b9acc866ef2afb754c33e6&format=json&method=news.getSlideshow";

    //新闻详情的地址
    public static final String CONTENT_URL = "http://sns.maimaicha.com/api?apikey=b4f4ee31a8b9acc866ef2afb754c33e6&format=json&method=news.getNewsContent&id=";
    //显示详情，不是url，而是具体的数据，webView可以展示url，还可以展示具体数据（html）

    public static final String DETAIL_URL = "http://sns.maimaicha.com/news/detail/";
    public static final String ADS_URL = "https://b-ssl.duitang.com/uploads/item/201703/05/20170305215814_PQt2a.thumb.700_0.jpeg";
    String s = "http://img4.imgtn.bdimg.com/it/u=1750277932,3113102643&fm=214&gp=0.jpg";

    static OkHttpClient client = new OkHttpClient();

    public static void getData(final Handler handler, String dataUrl, final Class jsonClass, final int msgType) {
        final Request request = new Request.Builder()
                .url(dataUrl)
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String data = response.body().string();
                Gson gson = new Gson();

                Bundle bundle = new Bundle();
                ArrayList list = new ArrayList<>();
                list.add(gson.fromJson(data, jsonClass));
                bundle.putParcelableArrayList("data", list);

                Message msg = new Message();
                msg.what = msgType;
                msg.setData(bundle);
                handler.sendMessage(msg);
            }
        });
    }
}
