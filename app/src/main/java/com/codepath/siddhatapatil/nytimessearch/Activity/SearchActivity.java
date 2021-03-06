package com.codepath.siddhatapatil.nytimessearch.Activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.codepath.siddhatapatil.nytimessearch.Listener.EndlessScrollListener;
import com.codepath.siddhatapatil.nytimessearch.Model.Article;
import com.codepath.siddhatapatil.nytimessearch.Adapter.ArticleArrayAdapter;
import com.codepath.siddhatapatil.nytimessearch.Model.Filter;
import com.codepath.siddhatapatil.nytimessearch.Model.Status;
import com.codepath.siddhatapatil.nytimessearch.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity{

    EditText etQuery;
    GridView gvResults;
    ImageButton btnSearch;
    ArrayList<Article> articles;
    ArticleArrayAdapter adapter;
    private Filter filter;
    private List<String> newsList;
    private String beginDate;
    private String sort;
    private final int REQUEST_CODE = 20;
    private final int DELAY = 1000;
    String userSubmittedQuery = null;
    private String filterQuery;
    private EndlessScrollListener scrollListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setupViews();
        GridView lvItems = (GridView) findViewById(R.id.gvResults);
        // Attach the listener to the AdapterView onCreate
        lvItems.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                loadNextDataFromApi(page);
                return true;
            }
        });
    }
    private void loadNextDataFromApi(int page) {
            fetchArticles(userSubmittedQuery, page);
        }

    public void fetchArticles(final String query, final int page) {

        Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                AsyncHttpClient client = new AsyncHttpClient();
                String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json";
                // String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json?q=health&begin_date=20160112&sort=oldest&fq=news_desk:(%22Education%22%20%22Health%22)&api-key=227c750bb7714fc39ef1559ef1bd8329";

                RequestParams params = new RequestParams();
                params.put("api-key", "eccffa7896a34b52beb277f76a763cea");
                params.put("page", page);
                params.put("q", query);

                client.get(url, params, new JsonHttpResponseHandler() {
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.d("DEBUG", response.toString());
                        JSONArray articleJsonResults = null;
                        try {
                            articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                            Log.d("DEBUG", articleJsonResults.toString());
                            articles.clear();
                            articles.addAll(Article.fromJSONArray(articleJsonResults));
                            adapter.notifyDataSetChanged();
                            Log.d("DEBUG", articles.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        Log.d("DEBUG", responseString.toString());
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        Log.d("DEBUG", errorResponse.toString());
                    }
                });
            }
        };

        handler.postDelayed(runnable, DELAY);
    }


    private void setupViews() {

        //etQuery = (EditText)findViewById(R.id.etQuery);
        gvResults = (GridView)findViewById(R.id.gvResults);
        btnSearch = (ImageButton)findViewById(R.id.btnSearch);
        articles = new ArrayList<>();
        adapter = new ArticleArrayAdapter(this,articles);
        gvResults.setAdapter(adapter);
        filter = new Filter();
        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(getApplicationContext(),ArticleActivity.class);
                Article article = articles.get(position);
                i.putExtra("article", article/*article.getWebUrl()*/);
                startActivity(i);
            }
        });
    }

    /*
        public void onArticleSearch(View view) {
            if (Status.getInstance(this).isOnline()) {
                String query = etQuery.getText().toString();
                //Toast.makeText(this, "Searching for " + query, Toast.LENGTH_LONG).show();
                AsyncHttpClient client = new AsyncHttpClient();
                String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json";
                RequestParams params = new RequestParams();
                params.put("api-key", "eccffa7896a34b52beb277f76a763cea");
                params.put("page", 0);
                params.put("q", query);
                client.get(url, params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        Log.d("DEBUG", response.toString());
                        JSONArray articleJSonResults = null;
                        try {
                            articleJSonResults = response.getJSONObject("response").getJSONArray("docs");
                            //Log.d("DEBUG", articleJSonResults.toString());
                            articles.addAll(Article.fromJSONArray(articleJSonResults));
                            adapter.notifyDataSetChanged();
                            Log.d("DEBUG", articles.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
            else {
                Toast.makeText(this,"No Internet",Toast.LENGTH_SHORT).show();
            }
        }
    */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint("Search news here...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // allows for search results following first search to have endless scrolling

                AsyncHttpClient client = new AsyncHttpClient();
                String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json";
                RequestParams params = new RequestParams();

                params.put("api-key", "eccffa7896a34b52beb277f76a763cea");
                params.put("page", 0);
                params.put("q", query);

                Log.d("searchactivity", url);
                // make network request
                client.get(url, params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.d("DEBUG", response.toString());
                        JSONArray articleJsonResults = null;

                        try {
                            articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                            Log.d("DEBUG", articleJsonResults.toString());
                            articles.clear();
                            articles.addAll(Article.fromJSONArray(articleJsonResults));
                            adapter.notifyDataSetChanged();
                            Log.d("DEBUG", articles.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.miFilter:
                Intent intent = new Intent(this, SettingsActivity.class);
                intent.putExtra("filter", filter);
                this.startActivityForResult(intent, REQUEST_CODE);
                break;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

        return super.onOptionsItemSelected(item);
    }



    private void addFilterParamsToQuery(RequestParams params) {
        if (!TextUtils.isEmpty(filter.getBeginDate())) {
            params.put("begin_date", filter.getBeginDate());
        }
        if (!TextUtils.isEmpty(filter.getSort())) {
            params.put("sort", filter.getSort());
        }
        String fl = TextUtils.join(",", filter.getFl());
        if (!TextUtils.isEmpty(fl)) {
            params.put("fq", fl);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            // Extract name value from result extras
            filter = data.getExtras().getParcelable("filter");
        }
    }



}