package com.example.reddittry1;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {

    VideoView video;
    ImageView image;
    String image_url;
    String video_url;// = "http://file2.video9.in/english/movie/2014/x-men-_days_of_future_past/X-Men-%20Days%20of%20Future%20Past%20Trailer%20-%20[Webmusic.IN].3gp";
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);
        Bundle extras = getIntent().getExtras();

        if (extras.getString("type").equals("video")) {
            video_url = extras.getString("url");//"https://v.redd.it/qimxdlqr1tw21/DASH_720?source=fallback";


            video = (VideoView) findViewById(R.id.videoView);
            video.setVisibility(View.VISIBLE);

            pd = new ProgressDialog(this);
            pd.setMessage("Buffering video please wait...");
            pd.show();

            Uri uri = Uri.parse(video_url);
            video.setVideoURI(uri);
            video.start();

            video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    //close the progress dialog when buffering is done
                    pd.dismiss();
                }
            });
        } else if (extras.getString("type").equals("image")) {
            image = (ImageView) findViewById(R.id.imageView);
            image.setVisibility(View.VISIBLE);

            image_url = extras.getString("url");
//            pd = new ProgressDialog(this);
//            pd.setMessage("Loading image please wait...");
//            pd.show();

            Glide.with(this).load(image_url).into(image);

//            ImageDownloader task = new ImageDownloader();
//            Bitmap myImage = null;
//
//            try {
//                myImage = task.execute(image_url).get();
//
//
//                if (myImage == null) {
//                    // myBitmap is empty/blank
//                    image.setVisibility(View.GONE);
//                } else {
//                    image.setVisibility(View.VISIBLE);
//                }
//
//
//                image.setImageBitmap(myImage);
//
//                pd.dismiss();
//
//
//            } catch (Exception e) {
//
//                e.printStackTrace();
//
//
//            }

        }
        else if(extras.getString("type").equals("web")){
            WebView w = findViewById(R.id.webView);
            w.setVisibility(View.VISIBLE);
            w.getSettings().setJavaScriptEnabled(true);
            String url = extras.getString("url") ;
            w.loadUrl(url);

            w.setWebViewClient(new WebViewClient() {
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


                }
            });
        }
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);


    }
}
