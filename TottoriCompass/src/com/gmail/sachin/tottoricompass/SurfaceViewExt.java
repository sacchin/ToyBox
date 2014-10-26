package com.gmail.sachin.tottoricompass;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

class SurfaceViewExt extends SurfaceView implements SurfaceHolder.Callback, Runnable {

	private final static int ONE_FRAME_TICK = 1000 / 25;    // 1�t���[���̎���
	private final static int MAX_FRAME_SKIPS = 5;           // ���Ԃ��]�����Ƃ��ő剽��t���[�����X�L�b�v���邩

	private SurfaceHolder mSurfaceHolder;   // �T�[�t�F�C�X�z���_�[
	private Thread mMainLoop;   // ���C���̃Q�[�����[�v�̗l�ȃ��m

	// �摜��\�����邽�߂̃��m
	private final Resources mRes = this.getContext().getResources();
	private Bitmap mBitmap;
	private float mArrowDir;    // ���̕���

	public SurfaceViewExt(Context context) {
		super(context);
		this.loadBitmap();
		this.initSurfaceView(context);
	}
	
	public SurfaceViewExt(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.loadBitmap();
		this.initSurfaceView(context);
	}

	public SurfaceViewExt(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.loadBitmap();
		this.initSurfaceView(context);
	}

	private void loadBitmap() {
		this.mBitmap = BitmapFactory.decodeResource(this.mRes, R.drawable.arrow_third);
	}

	// ////////////////////////////////////////////////////////////
	// �p�x���Z�b�g����
	public void setArrowDir(float dir) {
		this.mArrowDir = dir;
	}
	
	// ////////////////////////////////////////////////////////////
	// �p�x���Z�b�g����
	public void addArrowDir(float dir) {
		if(lastdeg == -1000){
			this.mArrowDir = -dir;
		}else if(1 < Math.abs(lastdeg - dir)){
			this.mArrowDir += (lastdeg - dir);
			lastdeg = dir;
		}
	}
	float lastdeg = -1000;

	// ////////////////////////////////////////////////////////////
	// SurfaceView�̏�����
	private void initSurfaceView(Context context) {
		// �T�[�t�F�C�X�z���_�[�����o��
		this.mSurfaceHolder = this.getHolder();
		// �R�[���o�b�N�֐���o�^����
		this.mSurfaceHolder.addCallback(this);
	}

	public void onDraw() {
		Canvas canvas = this.mSurfaceHolder.lockCanvas();
		this.draw(canvas);
		this.mSurfaceHolder.unlockCanvasAndPost(canvas);
	}
	// ////////////////////////////////////////////////////////////
	// �`�揈��
	public void draw(final Canvas canvas) {

		// Arrow��\��
		Paint paint = new Paint();
		canvas.save();
		canvas.translate(this.mScreenCenter[0] - 512, this.mScreenCenter[1] - 512);
		canvas.scale(2, 2);
		canvas.rotate(this.mArrowDir, 256, 256);
		canvas.drawBitmap(this.mBitmap, 0, 0, paint);
		canvas.restore();

		// ������\��
		paint.setColor(Color.WHITE);
		paint.setAntiAlias(true);
		paint.setTextSize(20);
		canvas.save();
		canvas.translate(this.mScreenCenter[0] - 30, this.mScreenCenter[1]);
		canvas.rotate(this.mArrowDir, 0, 0);
		canvas.drawText("N:" + String.format("%02.1f", this.mArrowDir), 1, 100, paint);
		canvas.restore();

	}

	private int[] mScreenCenter = { 0, 0 };
	// ////////////////////////////////////////////////////////////
	// �T�[�t�F�C�X�T�C�Y�̕ύX���������Ƃ��Ƃ��ɌĂ΂��
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

		// �Z���^�[�ʒu
		this.mScreenCenter[0] = width / 2;
		this.mScreenCenter[1] = height / 2;

	}

	// ////////////////////////////////////////////////////////////
	// �T�[�t�F�C�X�����ꂽ�Ƃ��ɌĂ΂��
	public void surfaceCreated(SurfaceHolder holder) {
		// ���[�J�[�X���b�h�����
		this.mMainLoop = new Thread(this);
		this.mMainLoop.start();
	}

	// ////////////////////////////////////////////////////////////
	// �T�[�t�F�C�X���j�����ꂽ���ɌĂ΂��
	public void surfaceDestroyed(SurfaceHolder holder) {
		this.mMainLoop = null;
	}

	public void move() {}

	public void run() {
		Canvas canvas;
		long beginTime = 0; // �����J�n����
		long pastTick = 0;  // �o�ߎ���
		int sleep = 0;
		int frameSkipped = 0;   // ���t���[�����X�L�b�v������

		// �t���[�����[�g�֘A
		int frameCount = 0;
		long beforeTick = 0;
		long currTime = 0;
		String tmp = "";

		// ������������
		Paint paint = new Paint();
		paint.setColor(Color.RED);
		paint.setAntiAlias(true);
		paint.setTextSize(60);

		// �X���b�h�����ł��Ă��Ȃ��Ԃ͂����Ə�����������
		while (this.mMainLoop != null) {
			canvas = null;

			// �t���[�����[�g�̕\��
			frameCount++;
			currTime = System.currentTimeMillis();
			if (beforeTick + 1000 < currTime) {
				beforeTick = currTime;
				tmp = "" + frameCount;
				frameCount = 0;
			}

			try {
				synchronized (this.mSurfaceHolder) {
					canvas = this.mSurfaceHolder.lockCanvas();
					// �L�����o�X�Ƃ�Ȃ�����
					if (canvas == null){
						continue;
					}

					canvas.drawColor(Color.rgb(93, 233, 120));

					// ���ݎ���
					beginTime = System.currentTimeMillis();
					frameSkipped = 0;

					// ////////////////////////////////////////////////////////////
					// ���A�b�v�f�[�g���`����
					this.move();
					canvas.save();
					this.draw(canvas);
					canvas.restore();
					// ////////////////////////////////////////////////////////////

					// �o�ߎ���
					pastTick = System.currentTimeMillis() - beginTime;

					// �]�������������
					sleep = (int)(ONE_FRAME_TICK - pastTick);

					// �]�������Ԃ�����Ƃ��͑҂�����
					if (0 < sleep) {
						try {
							Thread.sleep(sleep);
						} catch (Exception e) {}
					}

					// �`��Ɏ��ԌW�߂���������ꍇ�͍X�V������
					while (sleep < 0 && frameSkipped < MAX_FRAME_SKIPS) {
						// ////////////////////////////////////////////////////////////
						// �x�ꂽ�������X�V��������
						this.move();
						// ////////////////////////////////////////////////////////////
						sleep += ONE_FRAME_TICK;
						frameSkipped++;
					}
					canvas.drawText("FPS:" + tmp, 1, 100, paint);
				}
			} finally {
				// �L�����o�X�̉�����Y��ɒ���
				if (canvas != null) {
					this.mSurfaceHolder.unlockCanvasAndPost(canvas);
				}
			}
		}
	} 
}