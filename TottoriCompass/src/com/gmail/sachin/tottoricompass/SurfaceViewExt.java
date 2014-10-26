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

	private final static int ONE_FRAME_TICK = 1000 / 25;    // 1フレームの時間
	private final static int MAX_FRAME_SKIPS = 5;           // 時間が余ったとき最大何回フレームをスキップするか

	private SurfaceHolder mSurfaceHolder;   // サーフェイスホルダー
	private Thread mMainLoop;   // メインのゲームループの様なモノ

	// 画像を表示するためのモノ
	private final Resources mRes = this.getContext().getResources();
	private Bitmap mBitmap;
	private float mArrowDir;    // 矢印の方向

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
	// 角度をセットする
	public void setArrowDir(float dir) {
		this.mArrowDir = dir;
	}
	
	// ////////////////////////////////////////////////////////////
	// 角度をセットする
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
	// SurfaceViewの初期化
	private void initSurfaceView(Context context) {
		// サーフェイスホルダーを取り出す
		this.mSurfaceHolder = this.getHolder();
		// コールバック関数を登録する
		this.mSurfaceHolder.addCallback(this);
	}

	public void onDraw() {
		Canvas canvas = this.mSurfaceHolder.lockCanvas();
		this.draw(canvas);
		this.mSurfaceHolder.unlockCanvasAndPost(canvas);
	}
	// ////////////////////////////////////////////////////////////
	// 描画処理
	public void draw(final Canvas canvas) {

		// Arrowを表示
		Paint paint = new Paint();
		canvas.save();
		canvas.translate(this.mScreenCenter[0] - 512, this.mScreenCenter[1] - 512);
		canvas.scale(2, 2);
		canvas.rotate(this.mArrowDir, 256, 256);
		canvas.drawBitmap(this.mBitmap, 0, 0, paint);
		canvas.restore();

		// 文字を表示
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
	// サーフェイスサイズの変更があったときとかに呼ばれる
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

		// センター位置
		this.mScreenCenter[0] = width / 2;
		this.mScreenCenter[1] = height / 2;

	}

	// ////////////////////////////////////////////////////////////
	// サーフェイスが作られたときに呼ばれる
	public void surfaceCreated(SurfaceHolder holder) {
		// ワーカースレッドを作る
		this.mMainLoop = new Thread(this);
		this.mMainLoop.start();
	}

	// ////////////////////////////////////////////////////////////
	// サーフェイスが破棄された時に呼ばれる
	public void surfaceDestroyed(SurfaceHolder holder) {
		this.mMainLoop = null;
	}

	public void move() {}

	public void run() {
		Canvas canvas;
		long beginTime = 0; // 処理開始時間
		long pastTick = 0;  // 経過時間
		int sleep = 0;
		int frameSkipped = 0;   // 何フレーム分スキップしたか

		// フレームレート関連
		int frameCount = 0;
		long beforeTick = 0;
		long currTime = 0;
		String tmp = "";

		// 文字書いたり
		Paint paint = new Paint();
		paint.setColor(Color.RED);
		paint.setAntiAlias(true);
		paint.setTextSize(60);

		// スレッドが消滅していない間はずっと処理し続ける
		while (this.mMainLoop != null) {
			canvas = null;

			// フレームレートの表示
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
					// キャンバスとれなかった
					if (canvas == null){
						continue;
					}

					canvas.drawColor(Color.rgb(93, 233, 120));

					// 現在時刻
					beginTime = System.currentTimeMillis();
					frameSkipped = 0;

					// ////////////////////////////////////////////////////////////
					// ↓アップデートやら描画やら
					this.move();
					canvas.save();
					this.draw(canvas);
					canvas.restore();
					// ////////////////////////////////////////////////////////////

					// 経過時間
					pastTick = System.currentTimeMillis() - beginTime;

					// 余っちゃった時間
					sleep = (int)(ONE_FRAME_TICK - pastTick);

					// 余った時間があるときは待たせる
					if (0 < sleep) {
						try {
							Thread.sleep(sleep);
						} catch (Exception e) {}
					}

					// 描画に時間係過ぎちゃった場合は更新だけ回す
					while (sleep < 0 && frameSkipped < MAX_FRAME_SKIPS) {
						// ////////////////////////////////////////////////////////////
						// 遅れた分だけ更新をかける
						this.move();
						// ////////////////////////////////////////////////////////////
						sleep += ONE_FRAME_TICK;
						frameSkipped++;
					}
					canvas.drawText("FPS:" + tmp, 1, 100, paint);
				}
			} finally {
				// キャンバスの解放し忘れに注意
				if (canvas != null) {
					this.mSurfaceHolder.unlockCanvasAndPost(canvas);
				}
			}
		}
	} 
}