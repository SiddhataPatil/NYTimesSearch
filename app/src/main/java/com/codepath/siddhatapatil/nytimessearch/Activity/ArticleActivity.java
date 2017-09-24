package com.codepath.siddhatapatil.nytimessearch.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.codepath.siddhatapatil.nytimessearch.Model.Article;
import com.codepath.siddhatapatil.nytimessearch.R;

/**
 * Created by siddhatapatil on 9/23/17.
 */

public class ArticleActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        //String url = getIntent().getStringExtra("url");
        Article article = (Article)getIntent().getSerializableExtra("article");
        WebView webView = (WebView) findViewById(R.id.wvArticle);

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                view.loadUrl(url);
                return true;
            }
        });

       // webView.loadUrl(url);
        webView.loadUrl(article.getWebUrl());
    }
}
