package com.example.camera;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.JavaCameraView;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class CameraActivity extends Activity implements CvCameraViewListener2{

	public static final int BACK_CAMERA = 0;
	public static final int FRONT_CAMERA = 1;
	
	private CameraBridgeViewBase mCameraView;
	private Mat	mOutputFrame;

	// ����
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sub, menu);
        return true;
    }

    // ���j���[�A�C�e���I���C�x���g
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.back_to_main:
        	finish();
            break;
        }
        return super.onOptionsItemSelected(item);
    }

	@Override
	public void onCameraViewStarted(int width, int height) {
		
		// �o�͉摜�p�o�b�t�@
		mOutputFrame = new Mat();
	}

	@Override
	public void onCameraViewStopped() {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		
	}

	// �摜�ւ̃��C������
	@Override
	public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
		
		// �G�b�W���o
		Imgproc.Canny(inputFrame.gray(), mOutputFrame, 80, 100);
		
		// �������]
		Core.bitwise_not(mOutputFrame, mOutputFrame);
		return mOutputFrame;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		// ���C�A�E�g�ƃJ�����`��p�̃r���[�̐���
		Context context = getApplicationContext();
		RelativeLayout relativeLayout = new RelativeLayout(context);
		setContentView(relativeLayout);
		
		JavaCameraView mJavaCameraView = new JavaCameraView(context, BACK_CAMERA);
		mJavaCameraView.setLayoutParams(new LinearLayout.LayoutParams(
				  LinearLayout.LayoutParams.MATCH_PARENT
				, LinearLayout.LayoutParams.MATCH_PARENT));
		mJavaCameraView.enableFpsMeter();
		relativeLayout.addView(mJavaCameraView);
		
		mCameraView = (CameraBridgeViewBase)mJavaCameraView;
		mCameraView.setCvCameraViewListener(this);
		
		// OpenCV�̓ǂݍ���
		OpenCVLoader.initAsync(
			OpenCVLoader.OPENCV_VERSION_2_4_2
			, this
			, mLoaderCallback
		);
	}
	
	@Override
	protected void onPause(){
		super.onPause();
		
		if(mCameraView != null) {
			mCameraView.disableView();
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(mCameraView != null) {
			mCameraView.disableView();
		}
	}
	
	private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
		@Override
		public void onManagerConnected(int status) {
			switch(status) {
			case LoaderCallbackInterface.SUCCESS:
				if(mCameraView != null) {
					mCameraView.enableView();
				}
				break;
			default:
				super.onManagerConnected(status);
				break;
			}
		}
	};
}