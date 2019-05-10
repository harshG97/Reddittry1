package com.example.reddittry1.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.reddittry1.R;
import com.example.reddittry1.models.ChildrenData;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder>{
    private ArrayList<ChildrenData> feedList = new ArrayList<>();
    private int rowLayout;
    private Context context;

    OnItemClickListener mItemClickListener;
    OnBottomReachedListener onBottomReachedListener;
    SharedPreferences pref;


    public class FeedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView feedCard;
        TextView subreddit;
        TextView title;
        ImageView feedImage;
        TextView vote;
        TextView comment;
        TextView share;
        ImageView upvote;
        ImageView downvote;
        TextView rating;
        RelativeLayout Holder;
        // private OnItemClickListener mItemClickListener;


        public FeedViewHolder(View v) {
            super(v);
            feedCard = (CardView) v.findViewById(R.id.feedcard);
            subreddit = (TextView) v.findViewById(R.id.subreddit);
            feedImage = (ImageView) v.findViewById(R.id.feedimage);
            // date = (TextView) v.findViewById(R.id.date);
            title =(TextView) v.findViewById(R.id.title);

            //vote = v.findViewById(R.id.vote);
            upvote = v.findViewById(R.id.upvote);
            downvote = v.findViewById(R.id.downvote);
            comment = v.findViewById(R.id.comment);
            share = v.findViewById(R.id.share);
            feedImage.setOnClickListener(this);
            comment.setOnClickListener(this);
            upvote.setOnClickListener(this);
            downvote.setOnClickListener(this);
            share.setOnClickListener(this);
            title.setOnClickListener(this);


        }


        @Override
        public void onClick(View v) {
            if (mItemClickListener != null ) {

                mItemClickListener.onItemClick(v, itemView, getPosition());
            }

        }
        // Holder.setOnClickListener(this);
    }

//    public interface OnItemClickListener {
//        void onItemClick(View view, int position);
//    }
    public interface OnItemClickListener {
        void onItemClick(View view, View v, int position);
    }

    public interface OnBottomReachedListener {

        void onBottomReached(int position);

    }


    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public void setOnBottomReachedListener(OnBottomReachedListener onBottomReachedListener){

        this.onBottomReachedListener = onBottomReachedListener;
    }

    public FeedAdapter(ArrayList<ChildrenData> feedList, int rowLayout, Context context) {
        this.feedList = feedList;
        this.rowLayout = rowLayout;
        this.context = context;
        pref = context.getSharedPreferences("AppPref", MODE_PRIVATE);
    }

    @Override
    public FeedAdapter.FeedViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new FeedViewHolder(view);
    }


    @Override
    public void onBindViewHolder(FeedViewHolder holder, final int position) {

        if (position == feedList.size() - 1){

            onBottomReachedListener.onBottomReached(position);

        }
        holder.feedImage.setVisibility(View.VISIBLE);
        int dir = 0;
        if(pref.contains(feedList.get(position).getName())){
            dir = pref.getInt(feedList.get(position).getName(), 0);
        }

        if(feedList.get(position).getVote()==0||dir==0){
            holder.upvote.setBackgroundColor(Color.parseColor("#00FFFFFF"));
            holder.downvote.setBackgroundColor(Color.parseColor("#00FFFFFF"));
        }

        if(feedList.get(position).getVote()==1||dir==1){
            holder.upvote.setBackgroundColor(Color.parseColor("#00BCD4"));
            holder.downvote.setBackgroundColor(Color.parseColor("#00FFFFFF"));
        }
        else if(feedList.get(position).getVote()==-1||dir==-1){
            holder.upvote.setBackgroundColor(Color.parseColor("#00FFFFFF"));
            holder.downvote.setBackgroundColor(Color.parseColor("#FF5722"));
        }
        holder.subreddit.setText("r/" + feedList.get(position).getSubreddit());
        holder.title.setText(feedList.get(position).getTitle());


        try {
            if (!Patterns.WEB_URL.matcher(feedList.get(position).getThumbnail()).matches() || feedList.get(position).getPosthint().equals("link")) {
                holder.feedImage.setVisibility(View.GONE);
            } else {
                Glide.with(context).load(feedList.get(position).getThumbnail()).into(holder.feedImage);
            }
        }
        catch (Exception e){
            holder.feedImage.setVisibility(View.GONE);
        }






    }



    @Override
    public int getItemCount() {
        return feedList.size();
    }




}

