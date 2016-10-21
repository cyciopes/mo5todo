package com.technology.gisgz.mo5todo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.technology.gisgz.mo5todo.R;

public class TestWevViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_wev_view);
        init();
    }

    private void init(){
        WebView webView = (WebView) findViewById(R.id.webView);
        WebSettings webSettings =   webView.getSettings();
        webSettings.setUseWideViewPort(true);//设置此属性，可任意比例缩放
        //webSettings.setLoadWithOverviewMode(true);
        //WebView加载web资源
        //webView.loadUrl("http://baidu.com");
        String customHtml="<html>dd<br>dd<br><font color=\"#0090FF\">ddd<br><font color=\"#0000C0\">ddd<br><span><img src=\"http://144.210.190.84/MyOffice_mbwAP_siT//MyAsset/JobBag/Popup/AJB_File_Download_Popup.aspx?filePath=MTQ0LjIxMC4xOTAuODQ=/GIS/{ECE065AC-8096-44A6-AC88-FCBC18757BD8}.png&amp;amp;isFolder=false\"></span><br></font></font></html>";
        webView.loadData(customHtml, "text/html", "UTF-8");

        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });

//        WebView webView1 = (WebView) findViewById(R.id.webView1);
//        //WebView加载web资源
//        //webView.loadUrl("http://baidu.com");
//        String customHtml1="<html>dd<br>dd<br><font color=\"#0090FF\">ddd<br><font color=\"#0000C0\">ddd<br><span><img src=\"http://144.210.190.84/MyOffice_mbwAP_siT//MyAsset/JobBag/Popup/AJB_File_Download_Popup.aspx?filePath=MTQ0LjIxMC4xOTAuODQ=/GIS/{ECE065AC-8096-44A6-AC88-FCBC18757BD8}.png&amp;amp;isFolder=false\"></span><br></font></font></html>";
//        webView1.loadData(customHtml, "text/html", "UTF-8");
//        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
//        webView1.setWebViewClient(new WebViewClient(){
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                // TODO Auto-generated method stub
//                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
//                view.loadUrl(url);
//                return true;
//            }
//        });
    }
}
