package com.example.camera;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.JavaCameraView;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Scalar;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class FacedetectActivity extends Activity implements CvCameraViewListener2{

	public static final int BACK_CAMERA = 0;
	public static final int FRONT_CAMERA = 1;
	
	private CameraBridgeViewBase	mCameraView;
	private MatOfRect				mFaces;			// ���o��������
	private FaceDetector			mFacesDetector;	// �猟�o�N���X
	
	private Scalar					mRectColor;		// ��`�`��F

	// ����
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        
        mRectColor = new Scalar(255, 0, 0);
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
		
		mFacesDetector.setExpectedSize(width, height, 0.2f);
	}

	@Override
	public void onCameraViewStopped() {
		
		mFacesDetector.releaseImages();
	}

	// �摜�ւ̃��C������
	// �摜�̊猟�o�Ƃ��̈ʒu�ɋ�`��ǉ����ĕ`��
	@Override
	public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
		
		mFaces = mFacesDetector.detect(inputFrame);

		return mFacesDetector.drawRect(inputFrame, mFaces, mRectColor);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		// ���C�A�E�g�ƃJ�����`��p�̃r���[�̐���
		Context context = getApplicationContext();
		RelativeLayout relativeLayout = new RelativeLayout(context);
		setContentView(relativeLayout);
		
		// �J�����̏�����
		// TODO: �t�����g�J�����g���������ǂ��܂��s���Ă��Ȃ��B
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
		
		// ���ɉ�����Ƃ��̓J�����`���~
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
				try {
					mFacesDetector = new FaceDetector(
							  "faceDir"
							, "lbpcascade_frontalface"
							, getApplicationContext()
					);
				} catch (Exception e){
					e.printStackTrace();
					Log.e("ERROR", "Failed to load cascade." + e);
				}

				mCameraView.enableView();
				break;
			default:
				super.onManagerConnected(status);
				break;
			}
		}
	};
}