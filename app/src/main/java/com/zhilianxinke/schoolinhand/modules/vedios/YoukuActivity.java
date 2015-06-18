package com.zhilianxinke.schoolinhand.modules.vedios;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import com.zhilianxinke.schoolinhand.R;
import com.zhilianxinke.schoolinhand.base.BaseActivity;
import com.zhilianxinke.schoolinhand.domain.AppAsset;

public class YoukuActivity extends BaseActivity {

    private static AppAsset appAsset;
    private WebView webView;

    @Override
    protected int setContentViewResId() {
        return R.layout.activity_youku;
    }

    @Override
    protected void initView() {

        getSupportActionBar().setTitle("在线视频");
        if (appAsset != null && appAsset.getUrl() != null) {
            webView = (WebView) findViewById(R.id.webView);

            WebSettings settings = webView.getSettings();
            //WebView启用Javascript脚本执行
            settings.setJavaScriptEnabled(true);//是否允许javascript脚本
            settings.setJavaScriptCanOpenWindowsAutomatically(true);//是否允许页面弹窗

            //        webView.loadUrl("http://v.youku.com/v_show/id_XNjgxOTU4NTE2.html");
            webView.loadData(setWebView("ef4e4a8d841c39b7", appAsset.getUrl()),
                    "text/html", "UTF-8");
        }
    }

    public String setWebView(String clientid, String vid) {
        return "<div id=\"youkuplayer\" style=\"width:100%;height:100%\"></div>\n" +
                "<script type=\"text/javascript\" src=\"http://player.youku.com/jsapi\">\n" +
                "player = new YKU.Player('youkuplayer',{\n" +
                "styleid: '0',\n" +
                "client_id: '"+clientid+"',\n" +
                "vid: '" + vid + "'\n" +
                "});\n" +
                "</script>";
    }

    @Override
    protected void initData() {

    }

    public static void actionStart(Context context, AppAsset appAsset) {
        YoukuActivity.appAsset = appAsset;
        Intent intent = new Intent(context, YoukuActivity.class);
        context.startActivity(intent);
    }
}
