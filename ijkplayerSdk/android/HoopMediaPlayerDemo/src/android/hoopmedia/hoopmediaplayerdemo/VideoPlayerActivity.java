/*
 * Copyright (C) 2013 Zhang Rui <bbcallen@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package android.hoopmedia.hoopmediaplayerdemo;

/*

import tv.danmaku.ijk.media.widget.MediaController;
import tv.danmaku.ijk.media.widget.VideoView;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IMediaPlayer.OnCompletionListener;
import tv.danmaku.ijk.media.player.IMediaPlayer.OnErrorListener;
import tv.danmaku.ijk.media.player.IMediaPlayer.OnPreparedListener;
import android.app.Activity;
import android.os.Bundle;


public class VideoPlayerActivity extends Activity {
	private VideoView mVideoView;
	private String mVideoPath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mVideoPath = getIntent().getExtras().getString("RtmpAddress");

		mVideoView = new VideoView(this);
		mVideoView.setOnErrorListener(mErrorListener);
		mVideoView.setOnCompletionListener(mCompletionListener);
		mVideoView.setOnPreparedListener(mPreparedListener);
		
//		MediaController mediaController = new MediaController(this);
//		mVideoView.setMediaController(mediaController);
		
//		mVideoView.setDataSourceType(VideoView.LOWDELAY_LIVE_STREAMING_TYPE);
//		mVideoView.setDataCache(10000);//10s
		mVideoView.setVideoPath(mVideoPath);
		
		setContentView(mVideoView);
	}
	private OnPreparedListener mPreparedListener = new OnPreparedListener() {

		@Override
		public void onPrepared(IMediaPlayer arg0) {
		}
	};
	
	private OnErrorListener mErrorListener = new OnErrorListener() {

		@Override
		public boolean onError(IMediaPlayer arg0, int arg1, int arg2) {
			mVideoView.stopPlayback();
			finish();
			return true;
		}
	};

	private OnCompletionListener mCompletionListener = new OnCompletionListener() {

		@Override
		public void onCompletion(IMediaPlayer arg0) {
			mVideoView.stopPlayback();
			
			finish();
		}
	};
}*/


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IMediaPlayer.OnCompletionListener;
import tv.danmaku.ijk.media.player.IMediaPlayer.OnErrorListener;
import tv.danmaku.ijk.media.player.IMediaPlayer.OnPreparedListener;
import tv.danmaku.ijk.media.widget.MediaController;
import tv.danmaku.ijk.media.widget.VideoView;

public class VideoPlayerActivity extends Activity {
    private VideoView mVideoView;
    private View mBufferingIndicator;
    //private MediaController mMediaController;

    private String mVideoPath;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        Log.i("debug", "onCreate");
        
       // mVideoPath = getIntent().getStringExtra("videoPath");
        mVideoPath = getIntent().getExtras().getString("RtmpAddress");

        mBufferingIndicator = findViewById(R.id.buffering_indicator);
       // mMediaController = new MediaController(this);

        mVideoView = (VideoView) findViewById(R.id.video_view);
        
        mVideoView.setOnPreparedListener(mPreparedListener);
        mVideoView.setOnErrorListener(mErrorListener);
        mVideoView.setOnCompletionListener(mCompletionListener);
        
       // mVideoView.setDataSourceType(VideoView.LOWDELAY_LIVE_STREAMING_TYPE);
        mVideoView.setDataCache(6000);
        
       // mVideoView.setMediaController(mMediaController);
        mVideoView.setMediaBufferingIndicator(mBufferingIndicator);
        
        mVideoView.setVideoLayout(VideoView.VIDEO_LAYOUT_ZOOM);
        
        mVideoView.setDataSourceType(VideoView.VOD_STREAMING_TYPE);
        Log.i("debug", "onCreate point1");
        mVideoView.setVideoPath(mVideoPath);
        Log.i("debug", "onCreate point2");
//        startPrint();
    }
    
	private HandlerThread handlerThread=null;
	private Handler handler=null;
	private Runnable runnable=null;
	public void startPrint()
	{
		if(handlerThread==null)
		{
			handlerThread = new HandlerThread("PrintAbsoluteTimestamp");
			handlerThread.start();
			Looper looper = handlerThread.getLooper();
			handler = new Handler(looper);
			
			runnable = new Runnable() {
				
				@Override
				public void run() {
					Log.v("PrintAbsoluteTimestamp", String.valueOf(mVideoView.getAbsoluteTimestamp()));
					handler.postDelayed(runnable, 3*1000);
				}
			};
			
			handler.postDelayed(runnable, 3*1000);
		}
	}
	
	public void endPrint()
	{
		if (handlerThread!=null) {
			
			if (handler!=null) {
				handler.removeCallbacks(runnable);
				handler = null;
			}
			
			handlerThread.quit();
			handlerThread = null;
		}
	}
    
	private OnPreparedListener mPreparedListener = new OnPreparedListener() {

		@Override
		public void onPrepared(IMediaPlayer mp) {
			Log.v("VideoPlayerActivity", "onPrepared");
		}
	};
	
	private OnErrorListener mErrorListener = new OnErrorListener() {
		@Override
		public boolean onError(IMediaPlayer mp, int what, int extra) {
			mVideoView.stopPlayback();
 			finish();
			return true;
		}
	};

	private OnCompletionListener mCompletionListener = new OnCompletionListener() {
		@Override
		public void onCompletion(IMediaPlayer mp) {
			mVideoView.stopPlayback();
			finish();
		}
	};
	
    @Override  
    protected void onResume() {
        super.onResume();
    }
    
    @Override  
    protected void onPause() {
        super.onPause();
    }
    
    @Override
    protected void onDestroy()
    {
    	super.onDestroy();
//    	endPrint();
    }
}






