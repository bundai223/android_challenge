package com.example.camera;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    // メニューアイテム選択イベント
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.activity_camera:
//        	startActivity(new Intent(MainActivity.this, CameraActivity.class));
        	startActivity(new Intent(MainActivity.this, FacedetectActivity.class));
            break;
        case R.id.activity_gl:
        	startActivity(new Intent(MainActivity.this, GLActivity.class));
        	break;
        }
        return super.onOptionsItemSelected(item);
    }
}
