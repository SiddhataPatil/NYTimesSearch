package com.codepath.siddhatapatil.nytimessearch.Activity;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import com.codepath.siddhatapatil.nytimessearch.Model.Article;
import com.codepath.siddhatapatil.nytimessearch.Adapter.ArticleArrayAdapter;
import com.codepath.siddhatapatil.nytimessearch.Model.Filter;
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
    Button btnSearch;
    ArrayList<Article> articles;
    ArticleArrayAdapter adapter;
    private Filter filter;
    private List<String> newsList;
    private String beginDate;
    private String sort;
    private final int REQUEST_CODE = 20;
    private String filterQuery;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setupViews();
    }
    private void customLoadMoreDataFromApi(int page) {
        //onArticleSearch (filterQuery, page);
    }
    private void setupViews() {

        etQuery = (EditText)findViewById(R.id.etQuery);
        gvResults = (GridView)findViewById(R.id.gvResults);
        btnSearch = (Button)findViewById(R.id.btnSearch);
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


    public void onArticleSearch(View view) {
        String query = etQuery.getText().toString();
        //Toast.makeText(this, "Searching for " + query, Toast.LENGTH_LONG).show();
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json";
        RequestParams params = new RequestParams();
        params.put("api-key","eccffa7896a34b52beb277f76a763cea");
        params.put("page", 0);
        params.put("q",query);
        client.get(url,params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("DEBUG",response.toString());
                JSONArray articleJSonResults = null;
                try {
                    articleJSonResults = response.getJSONObject("response").getJSONArray("docs");
                    //Log.d("DEBUG", articleJSonResults.toString());
                    articles.addAll(Article.fromJSONArray(articleJSonResults));
                    adapter.notifyDataSetChanged();
                    Log.d("DEBUG", articles.toString());
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                adapter.clear();

                searchView.clearFocus();
                filterQuery = query;
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
        int id = item.getItemId();

        switch(item.getItemId()) {
            case R.id.miFilter:
                Intent intent = new Intent(this, SettingsActivity.class);
                intent.putExtra("filter", filter);
                this.startActivityForResult(intent, REQUEST_CODE);
                break;

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

