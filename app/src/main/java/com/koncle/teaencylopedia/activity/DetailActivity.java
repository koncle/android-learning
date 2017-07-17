package com.koncle.teaencylopedia.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.koncle.teaencylopedia.R;
import com.koncle.teaencylopedia.bean.Detail;
import com.koncle.teaencylopedia.bean.DetailJson;
import com.koncle.teaencylopedia.data.DataApi;
import com.koncle.teaencylopedia.data.HandlerType;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private CoordinatorLayout coordinatorLayout;
    private Toolbar toolbar;
    private String curUrl;
    private String dataUrl;
    private WebView webview;
    private UrlHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Bundle bundle = this.getIntent().getExtras();

        if (bundle != null){
            String id = bundle.getString("id");
            this.curUrl = DataApi.DETAIL_URL + id;
            this.dataUrl = DataApi.CONTENT_URL + id;
        }

        initData();

        initView();

        initToolbar();

        initWebview();

    }

    private void initData() {
        this.handler = new UrlHandler();
        DataApi.getData(this.handler, this.dataUrl, DetailJson.class, HandlerType.DETAIL_MSG);
    }

    private void initWebview() {
        WebSettings webSettings = this.webview.getSettings();

        // 启用 javascript
        webSettings.setJavaScriptEnabled(true);

        //设置自适应屏幕，两者合用
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        //缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        // 本页面处理请求
        this.webview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DetailActivity.this.finish();
                DetailActivity.this.overridePendingTransition(R.anim.in, R.anim.out);
            }
        });
        toolbar.setTitle("详情");
    }

    private void initView() {
        this.toolbar = (Toolbar)findViewById(R.id.toolbar);
        this.coordinatorLayout = (CoordinatorLayout)findViewById(R.id.coordinator_layout);
        this.webview = (WebView)findViewById(R.id.webview);
    }

    //Share Operation
    private void share(){
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);            // 设置action为 Intent.ACTION_SEND, 应用程序间发送数据，一般用这个
        shareIntent.putExtra(Intent.EXTRA_TEXT, curUrl + " share from TeaEncylopedia"); // 放入要分享的内容，用系统的key，以便于其他应用程序的接受
        shareIntent.setType("text/plain");                    // 设置MIME类型为纯文本
        if (hasApplication(shareIntent)) {
            startActivity(shareIntent);
        }
    }

    private boolean hasApplication(Intent intent){
        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
        return activities.size() > 0 ? true : false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.share) {
            share();
        }
        if (id == R.id.collect){
            Snackbar.make(coordinatorLayout, "收藏成功", Snackbar.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    class UrlHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == HandlerType.DETAIL_MSG){
                Detail detail = ((DetailJson)msg.getData().getParcelableArrayList("data").get(0)).getDetial();
                loadHtml(detail);
            }
        }
    }

    private void loadHtml(Detail detail) {
        String css ="<style>" +
                "img {\n" +
                "    height: auto;\n" +
                "    width: 100%;\n" +
                "    display: block;\n" +
                "    margin-top: 15px;\n" +
                "    margin-bottom: 15px;\n" +
                "}\n" +
                "\n" +
                "body {\n" +
                "    font-size: 15px;\n" +
                "    padding: 0 15px;\n" +
                "}\n" +
                "\n" +
                "p {\n" +
                "    text-indent: 15px;\n" +
                "    line-height: 25px;\n" +
                "}" +
                "h3 {text-align:center;}" +
                "</style>" +
                "<h3>" + detail.getTitle() + "</h3>";
        this.webview.loadData(css + detail.getWap_content(), "text/html; charset=UTF-8", null);
        toolbar.setTitle("详情");
    }
}
