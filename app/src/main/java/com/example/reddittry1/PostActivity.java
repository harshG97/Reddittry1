package com.example.reddittry1;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.example.reddittry1.adapters.CommentAdapter;
import com.example.reddittry1.models.RedditComment;
import com.example.reddittry1.reddit_api.ApiClient;
import com.example.reddittry1.reddit_api.ApiInterface;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostActivity extends AppCompatActivity {

    private String title;
    private String author;
    private String url;
    private String subreddit;
    private String selftext;
    private String type;
    private String id;
    private SharedPreferences pref;
    private String token;
    private String response;
    List<RedditComment> comments = new ArrayList<>();
    HashMap<String,String> postDetails = new HashMap<>();
    private int startingDepth = 0;
    ListView commentList;
    CommentAdapter commentAdapter;
    Context c;
    int pos;
    ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        Bundle extras = getIntent().getExtras();


        id = extras.getString("id");
        title = extras.getString("title");
        subreddit = extras.getString("subreddit");
        author = extras.getString("author");
        selftext = extras.getString("subtext");
        url = extras.getString("url");
        type = extras.getString("type");
        pref = this.getSharedPreferences("AppPref", MODE_PRIVATE);
        token = pref.getString("token", "");
        commentList = (ListView) findViewById(R.id.comment_list);
        postDetails.put("author", author);
        postDetails.put("subreddit", subreddit);
        postDetails.put("subtext", selftext);
        postDetails.put("url", url);
        postDetails.put("title", title);
        postDetails.put("type", type);

        pd = new ProgressDialog(this);
        pd.setMessage("Loading...");
        pd.show();
        c = getApplicationContext();
        new CommentsFetcher().execute("https://www.reddit.com/comments/" + id+"/.json?depth=4");



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if(requestCode==2)
        {
            try {
                ApiInterface apiService =
                        ApiClient.getClient().create(ApiInterface.class);
                Log.d("token", pref.getString("token", ""));

                Call<JSONObject> call = apiService.postComment("bearer " + token, comments.get(pos-1).getName(), data.getStringExtra("MESSAGE"));
                call.enqueue(new Callback<JSONObject>() {
                    @Override
                    public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {

                        Log.e("post",data.getStringExtra("MESSAGE") );
                        new CommentsFetcher().execute("https://www.reddit.com/comments/" + id+"/.json?depth=4");


                    }

                    @Override
                    public void onFailure(Call<JSONObject> call, Throwable t) {
                        // Log error here since request failed
                        Log.e("response faik", t.getMessage());
                    }
                });
            }
            catch (Exception e) {
            }
        }
    }

    CommentAdapter.click reply = new CommentAdapter.click() {
        @Override
        public void reply(View v, int position) {

            pos = position;
            System.out.println(position);
            Log.e("click", "reply");
            Intent intent=new Intent(c,ReplyActivity.class);
            startActivityForResult(intent, 2);



        }
    };

    public class CommentsFetcher extends AsyncTask<String , Void ,String> {
        String server_response;

        @Override
        protected String doInBackground(String... strings) {

            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();

                int responseCode = urlConnection.getResponseCode();

                if(responseCode == HttpURLConnection.HTTP_OK){
                    server_response = readStream(urlConnection.getInputStream());
                    Log.v("CatalogClient", server_response);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.e("Response", "" + server_response);
            response = server_response;

            comments = getItemsFromString(response);
            System.out.println(comments.size());

            for(int i =0;i<comments.size();++i){
                System.out.println(comments.get(i).getDepth());
            }

            commentAdapter = new CommentAdapter(getApplicationContext(), comments, postDetails);
            commentList.setAdapter(commentAdapter);
            commentAdapter.setOnItemClickListener(reply);
            pd.dismiss();


        }
    }

// Converting InputStream to String

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }

    public List<RedditComment> getItemsFromString(String rawData) {
        List<RedditComment> comments = new ArrayList<>();

        try {
            JSONArray children = new JSONArray(rawData).getJSONObject(1)
                    .getJSONObject("data")
                    .getJSONArray("children");
            getCommentsRecursive(comments, children, startingDepth);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return comments;
    }


    private void getCommentsRecursive(List<RedditComment> comments, JSONArray children, int depth) {
        try {
            for (int i = 0; i < children.length(); i++) {
                JSONObject commentData = children.getJSONObject(i)
                        .getJSONObject("data");
                RedditComment currentComment = new RedditComment();

                //Handle "more comments" objects
                if (children.getJSONObject(i).optString("kind").equals("more")) {
//                    currentComment.setUser("more");
//                    currentComment.setMoreChildren(JSONArrayConverter
//                            .convert(commentData.getJSONArray("children")));
//                    currentComment.setDepth(startingDepth + depth);
//                    comments.add(currentComment);
                    continue;
                }
                currentComment.setText(Html.fromHtml(commentData
                        .getString("body")).toString());
                currentComment.setUser(commentData.getString("author"));
                currentComment.setScore(commentData.getString("score"));
//                currentComment.setDate(TimeSpan
//                        .calculateTimeSpan(new BigDecimal(commentData.getString("created_utc"))
//                                .longValue(), System.currentTimeMillis() / 1000l));
                currentComment.setId(commentData.getString("id"));
                currentComment.setName(commentData.getString("name"));
                currentComment.setLikes(commentData.getString("likes"));

                currentComment.setDepth(startingDepth + depth);

                if (currentComment.getUser() == null) continue;
                comments.add(currentComment);

                if (commentData.get("replies").equals("")) continue;
                JSONArray replies = commentData.getJSONObject("replies")
                        .getJSONObject("data").getJSONArray("children");
                getCommentsRecursive(comments, replies, startingDepth + depth + 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
