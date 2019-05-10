package com.example.reddittry1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.reddittry1.adapters.FeedAdapter;
import com.example.reddittry1.models.ChildrenData;
import com.example.reddittry1.models.ChildrenItem;
import com.example.reddittry1.models.FeedResponse;
import com.example.reddittry1.reddit_api.ApiClient;
import com.example.reddittry1.reddit_api.ApiInterface;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

//import okhttp3.Response;


///**
// * A simple {@link Fragment} subclass.
// * Activities that contain this fragment must implement the
// * {@link HomeFragment.OnFragmentInteractionListener} interface
// * to handle interaction events.
// * Use the {@link HomeFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String after;
    private int f = 0;

    ProgressDialog pd;
    SharedPreferences pref;
    FeedAdapter mAdapter;

    private ArrayList<ChildrenData> feedList = new ArrayList();
    String token;


   // private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        pref = getActivity().getSharedPreferences("AppPref", MODE_PRIVATE);
        Log.d("create home", "oncreate");

        pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading...");
        pd.show();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d("in create view","oncreateview");
        token = pref.getString("token", "");
        View rootView = inflater.inflate(R.layout.fragment_home,
                container, false);
        final RecyclerView mrecyclerView = (RecyclerView) rootView.findViewById(R.id.feed_list);
        mrecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if(f==0) {
            try {
                ApiInterface apiService =
                        ApiClient.getClient().create(ApiInterface.class);
                Log.d("token", pref.getString("token", ""));

                Call<FeedResponse> call = apiService.getHomeFeed("bearer " + token, "android:com.example.reddittry1:v1.0 (by /u/harshg97)");
                call.enqueue(new Callback<FeedResponse>() {
                    @Override
                    public void onResponse(Call<FeedResponse> call, Response<FeedResponse> response) {
                        after = response.body().getData().getAfter();
                        pd.dismiss();
                        f=1;
                        for (ChildrenItem i : response.body().getData().getChildren()) {
                            feedList.add(i.getChildrenData());
                        }
                        mAdapter = new FeedAdapter(feedList, R.layout.feed_item, getContext());
                        mrecyclerView.setAdapter(mAdapter);
                        mAdapter.setOnItemClickListener(onItemClickListener);
                        mAdapter.setOnBottomReachedListener(onBottomReachedListener);
                    }

                    @Override
                    public void onFailure(Call<FeedResponse> call, Throwable t) {
                        // Log error here since request failed
                        Log.e("response faik", t.getMessage());
                    }
                });
            }
            catch (Exception e) {
            }
        }
        else{
            mAdapter = new FeedAdapter(feedList, R.layout.feed_item, getContext());
            mrecyclerView.setAdapter(mAdapter);
            mAdapter.setOnItemClickListener(onItemClickListener);
            mAdapter.setOnBottomReachedListener(onBottomReachedListener);
        }

        return rootView;
    }

    FeedAdapter.OnBottomReachedListener onBottomReachedListener = new FeedAdapter.OnBottomReachedListener() {
        @Override
        public void onBottomReached(int position) {
            //your code goes here

            try {
                ApiInterface apiService =
                        ApiClient.getClient().create(ApiInterface.class);
                Log.d("token", pref.getString("token", ""));
                Call<FeedResponse> call = apiService.getHomeFeed("bearer " + token,  "android:com.example.reddittry1:v1.0 (by /u/harshg97)", after);
                call.enqueue(new Callback<FeedResponse>() {
                    @Override
                    public void onResponse(Call<FeedResponse>call, Response<FeedResponse> response) {
                        Log.d("feedresponse", Integer.toString(response.code()));
                        after = response.body().getData().getAfter();

                        for(ChildrenItem i : response.body().getData().getChildren()){
                            feedList.add(i.getChildrenData());

                        }

                        Log.d("feedlist", Integer.toString(feedList.size()));
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<FeedResponse>call, Throwable t) {
                        // Log error here since request failed
                        Log.e("response faik", t.getMessage());
                    }
                });



            } catch (Exception e) {
                // TODO Auto-generated catch block

            }

        }
    };

    FeedAdapter.OnItemClickListener onItemClickListener = new FeedAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View v, View holder, int position) {

            System.out.println(position);
            System.out.println(v.getId());
            System.out.println(R.id.feedimage);
            String url = new String();
            String type = new String();
            if(v.getId() == R.id.feedimage||v.getId()==R.id.comment||v.getId()== R.id.title){

                try {
                    Log.d("hint", feedList.get(position).getPosthint());

                    if (feedList.get(position).getPosthint().equals("link")) {
                        type = "link";
                        Intent i = new Intent(getActivity(), PostActivity.class);
                        i.putExtra("title", feedList.get(position).getTitle());
                        i.putExtra("url", feedList.get(position).getUrl());
                        i.putExtra("author", feedList.get(position).getAuthor());
                        i.putExtra("subtext", feedList.get(position).getSubtext());
                        i.putExtra("subreddit", feedList.get(position).getSubreddit());
                        i.putExtra("id", feedList.get(position).getId());
                        i.putExtra("type", type);

                        startActivity(i);
                    }

                    if (feedList.get(position).getPosthint().equals("hosted:video")) {
                        url = feedList.get(position).getSecureMedia().getReddit_video().getSecureMediaVideoUrl();
                        type = "video";
                        Log.d("url", url);
                        if (v.getId() == R.id.feedimage) {
                            Intent i = new Intent(getActivity(), FullscreenActivity.class);
                            i.putExtra("url", url);
                            i.putExtra("type", "video");
                            startActivity(i);
                        }
                    } else if (feedList.get(position).getPosthint().equals("rich:video")) {
                        try {
                            url = feedList.get(position).getPreview().getRedditVideoPreview().getVideo_url();
                            type = "video";
                            Log.d("url", url);
                            if (v.getId() == R.id.feedimage) {
                                Intent i = new Intent(getActivity(), FullscreenActivity.class);
                                i.putExtra("url", url);
                                i.putExtra("type", "video");
                                startActivity(i);
                            }
                        } catch (Exception e) {
                            url = feedList.get(position).getUrl();
                            type = "web";
                            Log.d("url", url);
                            if (v.getId() == R.id.feedimage) {
                                Intent i = new Intent(getActivity(), FullscreenActivity.class);
                                i.putExtra("url", url);
                                i.putExtra("type", "web");
                                startActivity(i);
                            }
                        }
                    } else if (feedList.get(position).getPosthint().equals("image")) {
                        url = feedList.get(position).getUrl();
                        type = "image";
                        Log.d("url", url);
                        if (v.getId() == R.id.feedimage) {
                            Intent i = new Intent(getActivity(), FullscreenActivity.class);
                            i.putExtra("url", url);
                            i.putExtra("type", "image");
                            startActivity(i);
                        }


                    }

                    if (v.getId() == R.id.comment || v.getId() == R.id.title&&!type.equals("link")) {
                        Intent i = new Intent(getActivity(), PostActivity.class);
                        i.putExtra("title", feedList.get(position).getTitle());
                        i.putExtra("url", url);
                        i.putExtra("author", feedList.get(position).getAuthor());
                        i.putExtra("subtext", feedList.get(position).getSubtext());
                        i.putExtra("subreddit", feedList.get(position).getSubreddit());
                        i.putExtra("id", feedList.get(position).getId());
                        i.putExtra("type", type);

                        startActivity(i);
                    }
                }
                catch (Exception e){
                    Intent i = new Intent(getActivity(), PostActivity.class);
                    i.putExtra("title", feedList.get(position).getTitle());
                    i.putExtra("url", feedList.get(position).getUrl());
                    i.putExtra("author", feedList.get(position).getAuthor());
                    i.putExtra("subtext", feedList.get(position).getSubtext());
                    i.putExtra("subreddit", feedList.get(position).getSubreddit());
                    i.putExtra("id", feedList.get(position).getId());
                    i.putExtra("type", "link2");

                    startActivity(i);

                }



            }

            if(v.getId()==R.id.share){
                System.out.println(R.id.share);
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, feedList.get(position).getTitle());
                sendIntent.putExtra(Intent.EXTRA_TEXT, feedList.get(position).getPermalink());
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }

            if(v.getId()==R.id.upvote){

                int vote = feedList.get(position).getVote();

                int dir = 1;
                int i=0;
                if(pref.contains(feedList.get(position).getName())){
                    i = pref.getInt(feedList.get(position).getName(), 0);
                }

                if(vote == 0||i==0){
                    dir = 1;
                    feedList.get(position).setVote(1);
                    v.setBackgroundColor(Color.parseColor("#00BCD4"));
                }
                else if(vote ==-1||i==-1){
                    dir = 1;
                    feedList.get(position).setVote(1);
                    holder.findViewById(R.id.downvote).setBackgroundColor(Color.parseColor("#00FFFFFF"));
                    v.setBackgroundColor(Color.parseColor("#00BCD4"));
                }
                else if(vote ==1||i==1){
                    dir = 0;
                    feedList.get(position).setVote(0);
                    v.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                }


                System.out.println(feedList.get(position).getName());

                try {
                    ApiInterface apiService =
                            ApiClient.getClient().create(ApiInterface.class);
                    Log.d("token", pref.getString("token", ""));
                    if(pref.contains(feedList.get(position).getName())){
                        pref.edit().remove(feedList.get(position).getName()).commit();
                    }
                    pref.edit().putInt(feedList.get(position).getName(), dir).apply();
                    Call<JSONObject> call = apiService.postVote("bearer " + token, dir,feedList.get(position).getName());
                    call.enqueue(new Callback<JSONObject>() {
                        @Override
                        public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {

                            Log.d("feedresponse", Integer.toString(response.code()));
                        //    Log.d("feedresponse", response.body().toString());
                            Log.d("feedresponse", Boolean.toString(response.isSuccessful()));


                        }

                        @Override
                        public void onFailure(Call<JSONObject> call, Throwable t) {
                            // Log error here since request failed
                            Log.e("response faik", t.getMessage());
                        }
                    });




                }
                catch (Exception e){
                    System.out.println(e);

                }


            }

            if(v.getId()==R.id.downvote){

                int vote = feedList.get(position).getVote();

                int dir = 1;

                int i=0;
                if(pref.contains(feedList.get(position).getName())){
                    i = pref.getInt(feedList.get(position).getName(), 0);
                }
                if(vote == 0||i==0){
                    dir = -1;
                    feedList.get(position).setVote(-1);
                    if(pref.contains(feedList.get(position).getName())){
                        pref.edit().remove(feedList.get(position).getName()).commit();
                    }
                    pref.edit().putInt(feedList.get(position).getName(), dir).commit();
                    v.setBackgroundColor(Color.parseColor("#FF5722"));
                }
                else if(vote ==-1||i==-1){
                    dir = 0;
                    feedList.get(position).setVote(0);
                    if(pref.contains(feedList.get(position).getName())){
                        pref.edit().remove(feedList.get(position).getName()).commit();
                    }
                    pref.edit().putInt(feedList.get(position).getName(), dir).commit();
                    //holder.findViewById(R.id.downvote).setBackgroundColor(Color.parseColor("#00FFFFFF"));
                    v.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                }
                else if(vote ==1||i==1){
                    dir = -1;
                    feedList.get(position).setVote(-1);
                    if(pref.contains(feedList.get(position).getName())){
                        pref.edit().remove(feedList.get(position).getName()).commit();
                    }
                    pref.edit().putInt(feedList.get(position).getName(), dir).commit();
                    v.setBackgroundColor(Color.parseColor("#FF5722"));
                    holder.findViewById(R.id.upvote).setBackgroundColor(Color.parseColor("#00FFFFFF"));
                }


                System.out.println(feedList.get(position).getName());

                try {
                    ApiInterface apiService =
                            ApiClient.getClient().create(ApiInterface.class);
                    Log.d("token", pref.getString("token", ""));

//                    if(pref.contains(feedList.get(position).getName())){
//                        pref.edit().remove(feedList.get(position).getName()).commit();
//                    }
//                    pref.edit().putInt(feedList.get(position).getName(), dir).apply();
                    Call<JSONObject> call = apiService.postVote("bearer " + token, dir,feedList.get(position).getName());
                    call.enqueue(new Callback<JSONObject>() {
                        @Override
                        public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {

                            Log.d("feedresponse", Integer.toString(response.code()));
                            //    Log.d("feedresponse", response.body().toString());
                            Log.d("feedresponse", Boolean.toString(response.isSuccessful()));


                        }

                        @Override
                        public void onFailure(Call<JSONObject> call, Throwable t) {
                            // Log error here since request failed
                            Log.e("response faik", t.getMessage());
                        }
                    });




                }
                catch (Exception e){
                    System.out.println(e);

                }


            }

            if(v.getId()==R.id.comment||v.getId()== R.id.title){



            }

        }
    };






}
