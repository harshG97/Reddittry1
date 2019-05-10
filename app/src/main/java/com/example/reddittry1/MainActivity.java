package com.example.reddittry1;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private static String CLIENT_ID = "GXfoPGznDLfpVQ";
    private static String CLIENT_SECRET ="";
    private static String REDIRECT_URI="http://www.example.com/my_redirect";
    private static String GRANT_TYPE="https://oauth.reddit.com/grants/installed_client";
    private static String GRANT_TYPE2="authorization_code";
    private static String TOKEN_URL ="access_token";
    private static String OAUTH_URL ="https://www.reddit.com/api/v1/authorize.compact";
    private static String OAUTH_SCOPE="read vote submit";
    private static String DURATION = "permanent";

    WebView web;
    Button auth;
    Button hot;
    SharedPreferences pref;
    TextView Access;
    Dialog auth_dialog;
    String DEVICE_ID = UUID.randomUUID().toString();
    String authCode;
    boolean authComplete = false;

    Intent resultIntent = new Intent();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        pref = getSharedPreferences("AppPref", MODE_PRIVATE);
        Access =(TextView)findViewById(R.id.Access);
        auth = (Button)findViewById(R.id.auth);
        //hot = (Button)findViewById(R.id.hot);
        auth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                auth_dialog = new Dialog(MainActivity.this);
                auth_dialog.setContentView(R.layout.auth_dialog);
                web = (WebView) auth_dialog.findViewById(R.id.webv);
                web.getSettings().setJavaScriptEnabled(true);
                String url = OAUTH_URL + "?client_id=" + CLIENT_ID + "&response_type=code&state=TEST&redirect_uri=" + REDIRECT_URI + "&scope=" + OAUTH_SCOPE;
                web.loadUrl(url);
                Toast.makeText(getApplicationContext(), "" + url, Toast.LENGTH_LONG).show();

                web.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);
                        return true;
                    }
                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
                        Log.i("start", "CODE : " );
                        super.onPageStarted(view, url, favicon);

                    }
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);

                        if (url.contains("?code=") || url.contains("&code=")) {

                            Uri uri = Uri.parse(url);
                            authCode = uri.getQueryParameter("code");
                            Log.i("", "CODE : " + authCode);
                            authComplete = true;
                            resultIntent.putExtra("code", authCode);
                            MainActivity.this.setResult(Activity.RESULT_OK, resultIntent);
                            setResult(Activity.RESULT_CANCELED, resultIntent);
                            SharedPreferences.Editor edit = pref.edit();
                            edit.putString("Code", authCode);
                            edit.commit();
                            auth_dialog.dismiss();

                            Toast.makeText(getApplicationContext(), "Authorization Code is: " + pref.getString("Code", ""), Toast.LENGTH_SHORT).show();

                            try {
                                new RedditRestClient(getApplicationContext()).getToken(TOKEN_URL, GRANT_TYPE2, DEVICE_ID);
                                Toast.makeText(getApplicationContext(), "Auccess Token: " + pref.getString("token", ""), Toast.LENGTH_SHORT).show();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else if (url.contains("error=access_denied")) {
                            Log.i("", "ACCESS_DENIED_HERE");
                            resultIntent.putExtra("code", authCode);
                            authComplete = true;
                            setResult(Activity.RESULT_CANCELED, resultIntent);
                            Toast.makeText(getApplicationContext(), "Error Occured", Toast.LENGTH_SHORT).show();

                            auth_dialog.dismiss();
                        }
                    }
                });
                auth_dialog.show();
                auth_dialog.setTitle("Authorize");
                auth_dialog.setCancelable(true);


            }
        });

//        hot.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("hot click", "click");
////                ApiInterface apiService =
////                        ApiClient.getClient().create(ApiInterface.class);
////                Log.d("token", pref.getString("token", ""));
////
////                Call<FeedResponse> call = apiService.getHomeFeed("bearer " + pref.getString("token", ""), "android:com.example.reddittry1:v1.0 (by /u/harshg97)");
////                call.enqueue(new Callback<FeedResponse>() {
////                    @Override
////                    public void onResponse(Call<FeedResponse>call, Response<FeedResponse> response) {
//////                        List<Movie> movies = response.body().getResults();
//////                        Log.d(TAG, "Number of movies received: " + movies.size());
////
////                        Log.d("feedresponse", response.body().getData().getChildren().get(0).getChildrenData().getTitle());
////
////
////                    }
////
////                    @Override
////                    public void onFailure(Call<FeedResponse>call, Throwable t) {
////                        // Log error here since request failed
////                        Log.e("response faik", t.getMessage());
////                    }
////                });
//
//                Intent i = new Intent(v.getContext(), FeedActivity.class);
//                startActivity(i);
//
//            }
//        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu_main; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
