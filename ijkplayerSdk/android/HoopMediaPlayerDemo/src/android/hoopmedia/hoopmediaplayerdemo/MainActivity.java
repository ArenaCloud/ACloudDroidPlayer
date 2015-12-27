package android.hoopmedia.hoopmediaplayerdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {
	private String rtmpAddress;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
        Button btn = (Button) this.findViewById(R.id.StartPlay1);
        final EditText et = (EditText) this.findViewById(R.id.RtmpAddress1);

        et.setText("http://test-kw-vod.arenacdn.com/prod/n2hxVa0M8lBPEAZe/index_vod.m3u8");
        
        btn.setOnClickListener(new OnClickListener(){

        	public void onClick(View v) {
				
        		 Log.i("debug", "onClick point1");
        		rtmpAddress = et.getText().toString();
        		
        		 Log.i("debug", "onClick point2");
        		
        		Intent intent = new Intent(MainActivity.this, VideoPlayerActivity.class);
        		intent.putExtra("RtmpAddress", rtmpAddress);
        		startActivity(intent);
        		 Log.i("debug", "onClick point3");
			}
        });
	}
}
