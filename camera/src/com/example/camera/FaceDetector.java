package com.example.camera;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.objdetect.CascadeClassifier;

import android.content.Context;
import android.util.Log;

public class FaceDetector {
	
	private CascadeClassifier	mCascadeClassifier;
	private Size	mMinSize;
	private Size	mMaxSize;
	private Mat		mInputFrameRgba;
	private Mat		mInputFrameGray;

	public FaceDetector(String dir, String filename, Context context) {
		// �猟�o�p�̃p�����[�^�����\�[�X����ǂݍ���
		File workingDir = context.getDir(dir, Context.MODE_PRIVATE);
		File workingFile = loadModelFile(workingDir, filename, context);
		
		mCascadeClassifier = new CascadeClassifier(workingFile.getAbsolutePath());
		workingDir.delete();
	}
	
	// �t�@�C���ǂݍ���
	private File loadModelFile(File dir, String filename, Context context) {
		try {
			int id = context.getResources().getIdentifier(filename, "raw", context.getPackageName());
			InputStream is = context.getResources().openRawResource(id);
			
			File modelFile = new File(dir, filename);
			FileOutputStream os = new FileOutputStream(modelFile);
			
			byte[] buffer = new byte[4096];
			int byteRead = 0;
			while((byteRead = is.read(buffer)) != -1) {
				os.write(buffer, 0, byteRead);
			}
			is.close();
			os.close();
			return modelFile;
		}
		catch(IOException e) {
			e.printStackTrace();
			Log.e("ERROR", "file load failure : " + e);
			return null;
		}
	}
	
	// �猟�o�p
	// ���҂���摜���̊�̑傫����ݒ肵�Ă���
	public void setExpectedSize(int width, int height, float minSize) {
		mInputFrameRgba = new Mat();
		mInputFrameGray = new Mat();
		
		int minWidth = Math.round(width * minSize);
		int minHeight = Math.round(height * minSize);
		mMinSize = new Size(minWidth, minHeight);
		mMaxSize = new Size();
	}
	
	// ���o����
	public MatOfRect detect(CvCameraViewFrame inputFrame) {
		MatOfRect result = new MatOfRect();
		mInputFrameGray = inputFrame.gray();
		
		mCascadeClassifier.detectMultiScale(
				  mInputFrameGray
				, result	//objects
				, 1.1		//rejectLevels
				, 2			//levelWeights
				, 2			//scaleFactor
				, mMinSize	//minNeighbors
				, mMaxSize	//max
		);
		return result;
	}
	
	// �l�p�Ŋ���͂�ł��̉摜��Ԃ�
	public Mat drawRect(CvCameraViewFrame inputFrame, MatOfRect faces, Scalar color) {
		//�J���[�̓��͏���
		mInputFrameRgba		= inputFrame.rgba();
		
		Rect[] rectFaces	= faces.toArray();
		int len				= rectFaces.length;
		
		for( int i = 0; i < len; ++i) {
			// ����͂�
			Rect rect = rectFaces[i];
			Core.rectangle(mInputFrameRgba, rect.tl(), rect.br(), color, 3);
		}

		return mInputFrameRgba;
	}
	
	public void releaseImages() {
		mInputFrameGray.release();
		mInputFrameRgba.release();
	}
}