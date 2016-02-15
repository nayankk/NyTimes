package com.xc0ffee.nytimes.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.xc0ffee.nytimes.R;
import com.xc0ffee.nytimes.adapters.ArticleArrayAdapter;
import com.xc0ffee.nytimes.fragments.SettingsDialogFragement;
import com.xc0ffee.nytimes.models.Article;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity {

    private static final String BASE_URL = "http://api.nytimes.com/svc/search/v2/articlesearch.json";
    private static final String API_KEY = "4a9eb816606d015507181f245b7c91b6:0:74411814";

    @Bind(R.id.et_text) EditText etQuery;
    @Bind(R.id.bt_searchBtn) Button btSearch;
    @Bind(R.id.gv_results) GridView gridView;

    List<Article> mArticleList = new ArrayList<>();
    private ArticleArrayAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        mAdapter = new ArticleArrayAdapter(this, mArticleList);
        gridView.setAdapter(mAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), ArticleDetailActivity.class);
                Article article = mArticleList.get(position);
                i.putExtra("url", article);
                startActivity(i);
            }
        });
    }

    public void onArticleSearch(View view) {
        String query = etQuery.getText().toString();
        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();
        params.put("api-key", API_KEY);
        params.put("q", query);
        params.put("page", 0);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String beginDate = pref.getString("date", "");
        if (!TextUtils.isEmpty(beginDate)) params.put("begin_date", beginDate);

        String sortSettiing = pref.getString("sortorder", "");
        if (!TextUtils.isEmpty(sortSettiing)) params.put("sort", sortSettiing);

        String newsDeskString = pref.getString("desk", "");
        if (!TextUtils.isEmpty(newsDeskString)) params.put("fq", "news_desk:(" + newsDeskString + ")");

        client.get(BASE_URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray articleJsonResult = null;
                try {
                    articleJsonResult = response.getJSONObject("response").getJSONArray("docs");
                    mAdapter.addAll(Article.fromJSONArray(articleJsonResult));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.filter:
                onFilterClicked();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void onFilterClicked() {
        FragmentManager fm = getSupportFragmentManager();
        SettingsDialogFragement settingsDialog = SettingsDialogFragement.newInstance("Filter your search");
        settingsDialog.show(fm, "settings_fragement");
    }
}
