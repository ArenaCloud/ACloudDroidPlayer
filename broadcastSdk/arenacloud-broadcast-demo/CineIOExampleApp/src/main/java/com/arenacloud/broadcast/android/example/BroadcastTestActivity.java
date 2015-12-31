package com.arenacloud.broadcast.android.example;

import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.arenacloud.broadcast.android.api.Stream;
import com.arenacloud.broadcast.android.api.StreamResponseHandler;
import com.arenacloud.broadcast.android.streaming.AspectFrameLayout;
import com.arenacloud.broadcast.android.BroadcastView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class BroadcastTestActivity extends ActionBarActivity implements BroadcastView.BroadcastStatusCallback, BroadcastView.ScreenShotCallback{

    private AspectFrameLayout mFrameLayout;
    private BroadcastView mBroadcastView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Bundle extras = new Bundle();
        extras.putInt("WIDTH", 1280);
        extras.putInt("HEIGHT", 720);
        extras.putString("ORIENTATION", "portrait");
        extras.putString("CAMERA", "back");

        setContentView(R.layout.activity_arena_cloud_broadcast_test);
        mFrameLayout = (AspectFrameLayout) findViewById(R.id.cameraPreview_afl);
        mBroadcastView = (BroadcastView)findViewById(R.id.cameraPreview_surfaceView);
        mBroadcastView.initBroadcast(extras, this);

        if (mBroadcastView.getAspectRatio()!=0)
        {
            mFrameLayout.setAspectRatio(mBroadcastView.getAspectRatio());
        }
        mBroadcastView.setBroadcastStatusCallback(this);
        mBroadcastView.setScreenShotCallback(this);

        //设置码率
        mBroadcastView.setBitrate(680000);

        //设置推流地址
        mBroadcastView.setPublishUrl("rtmp://test-kw-pub.arenacdn.com/prod/yIynaa0M8swFEApb?pass=1c72136f29a7afa4579fd9dd16f7762f");

        Button toggleRecording = (Button) findViewById(R.id.toggleRecording_button);

        toggleRecording.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // show interest in events resulting from ACTION_DOWN
                if (event.getAction() == MotionEvent.ACTION_DOWN) return true;
                // don't handle event unless its ACTION_UP so "doSomething()" only runs once.
                if (event.getAction() != MotionEvent.ACTION_UP) return false;

                if (mBroadcastView.isRecordingEnabled()) {
                    mBroadcastView.toggleRecording();
                } else {

                   mBroadcastView.toggleRecording();
                }
                return true;
            }
        });

        Button switchRecording = (Button) findViewById(R.id.toggleSwitch_button);
        switchRecording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBroadcastView.switchCamera();
            }
        });

        Button sceenshotButton = (Button) findViewById(R.id.toggleScreenShot_button);
        sceenshotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBroadcastView.takeScreenShot();
            }
        });
      }

    @Override
    protected void onResume() {
        Log.i("debug", "BroadcastTestActivity onResume");
        super.onResume();
        mBroadcastView.onResumeHandler();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBroadcastView.onPauseHandler();
    }

    @Override
    protected void onDestroy() {
         mBroadcastView.onDestroyHandler();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_arena_cloud_broadcast, menu);
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


    @Override
    public void broadcastStatusUpdate(final BroadcastView.BROADCAST_STATE state) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView statusTextView = (TextView) findViewById(R.id.streamingStatus);

                String statusText;
                switch (state){
                    case PREPARING:
                        statusText = "Preparing";
                        break;
                    case CONNECTING:
                        statusText = "Connecting";
                        break;
                    case READY:
                        statusText = "Ready";
                        break;
                    case STREAMING:
                        statusText = "Streaming";
                        break;
                    case SHUTDOWN:
                        statusText = "Ready";
                        break;
                    default:
                        statusText = "Unknown";
                        break;
                }
                statusTextView.setText(statusText);
            }
        });
    }

    private Bitmap bitmap;

    @Override
    public void onScreenShotEvent(final Bitmap bitmap) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                File file = new File("/sdcard" + "/ArenaCloud/ScreenShot/");
                if (!file.exists())
                {
                    file.mkdir();
                }

                String savePath = "/sdcard" + "/ArenaCloud/ScreenShot/" + System.currentTimeMillis()+ ".jpeg";
                try {
                    FileOutputStream fout = new FileOutputStream(savePath);
                    BufferedOutputStream bos = new BufferedOutputStream(fout);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                    bos.flush();
                    bos.close();

                    bitmap.recycle();

                    Toast.makeText(BroadcastTestActivity.this, "YOU HAVE SAVED A SCREENSHOT",
                    Toast.LENGTH_LONG).show();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
