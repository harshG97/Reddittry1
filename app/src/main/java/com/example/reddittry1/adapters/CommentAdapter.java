package com.example.reddittry1.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.example.reddittry1.R;
import com.example.reddittry1.models.RedditComment;

import java.util.HashMap;
import java.util.List;

public class CommentAdapter extends BaseAdapter {
    Context context;
    List<RedditComment> comments;
    //int flags[];
    LayoutInflater inflter;
    click mclick;


    HashMap<String, String> postDetails;
    public CommentAdapter(Context applicationContext, List<RedditComment> comments, HashMap<String, String> postDetails) {
        this.context = applicationContext;
        this.comments = comments;
        this.postDetails = postDetails;

       // this.flags = flags;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return comments.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    public interface click{
        void reply(View v, int position);
    }

    public void setOnItemClickListener(final click mItemClickListener) {
        this.mclick = mItemClickListener;
    }
    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {


        if(i>0) {
            view = inflter.inflate(R.layout.comment_item, null);
            TextView author = (TextView) view.findViewById(R.id.comment_author);
            RelativeLayout card =  view.findViewById(R.id.rl_comment);
            int depth = comments.get(i-1).getDepth();
            card.setPadding(100*depth,0,0,0);
            TextView body = (TextView) view.findViewById(R.id.comment_body);
            author.setText(comments.get(i-1).getUser());
            body.setText(comments.get(i-1).getText());
            final TextView reply = view.findViewById(R.id.reply);
           // reply.setOnClickListener(this);
            reply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // do something when the corky is clicked
                    if(mclick!=null){
                        mclick.reply(reply, i);
                    }
                }
            });


        }
        else{
            view = inflter.inflate(R.layout.post_detail_item, null);
            TextView postSubreddit = view.findViewById(R.id.post_subreddit);
            VideoView video = view.findViewById(R.id.videoView2);
            TextView postAuthor = view.findViewById(R.id.post_author);
            TextView postSubtext = view.findViewById(R.id.post_subtext);
            TextView postLink = view.findViewById(R.id.post_link);
            ImageView postImage = view.findViewById(R.id.post_image);
            TextView postTitle = view.findViewById(R.id.post_title);
            postTitle.setText(postDetails.get("title"));
            postAuthor.setText("Posted by u/" + postDetails.get("author"));
            postSubreddit.setText("r/" + postDetails.get("subreddit"));
            String type = postDetails.get("type");
            if(postDetails.get("type").equals("video")){
                postImage.setVisibility(View.GONE);
                postLink.setVisibility(View.GONE);
                Uri uri = Uri.parse(postDetails.get("url"));
                video.setVideoURI(uri);
                video.start();
            }
            else{
                video.setVisibility(View.GONE);
                if(type.equals("image")){
                    postLink.setVisibility(View.GONE);
                    Glide.with(context).load(postDetails.get("url")).into(postImage);
                }else{
                    postImage.setVisibility(View.GONE);
                    if(type.equals("link")||type.equals("link2")||type.equals("web")){
                        postLink.setText(postDetails.get("url"));
                        postLink.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // do something when the corky is clicked
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(postDetails.get("url")));
                                context.startActivity(i);
                            }
                        });
                    }
                }

            }
            if(postDetails.get("subtext").equals("")){
                postSubtext.setVisibility(View.GONE);
            }
            else
            postSubtext.setText(postDetails.get("subtext"));



        }

//        CardView.LayoutParams layoutParams = (CardView.LayoutParams) card.getLayoutParams();
//        layoutParams.setMargins(depth*3, 0, 0, 0);
//        card.setLayoutParams(layoutParams);

//        ViewGroup.MarginLayoutParams layoutParams =
//                (ViewGroup.MarginLayoutParams) card.getLayoutParams();
//        layoutParams.setMargins(10*depth, 0, 0, 0);
//        card.requestLayout();

//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT);
//        layoutParams.setMargins(10*depth, 0, 0, 0);
//        card.setLayoutParams(layoutParams);

//        LinearLayout.LayoutParams relativeParams = new LinearLayout.LayoutParams(
//                new LinearLayout.LayoutParams(
//                        LinearLayout.LayoutParams.WRAP_CONTENT,
//                        LinearLayout.LayoutParams.WRAP_CONTENT));
//        relativeParams.setMargins(20*depth, 0, 0, 0);
//        card.setLayoutParams(relativeParams);
//        card.requestLayout();

//        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)card.getLayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
//           RelativeLayout.LayoutParams.WRAP_CONTENT);
//        params.setMargins(20*depth, 0, 0, 0);
//        card.setLayoutParams(params);
//        TextView body = (TextView) view.findViewById(R.id.comment_body);
//author.setText(comments.get(i).getUser());
//body.setText(comments.get(i).getText());
//        country.setText(countryList[i]);
//        icon.setImageResource(flags[i]);
        return view;
    }


}