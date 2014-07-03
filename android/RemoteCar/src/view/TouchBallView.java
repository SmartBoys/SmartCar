package view;

import java.util.Timer;
import java.util.TimerTask;
import bluetooth.Cmd;
import com.zjlh.remotecar.R;
import activity.BaseActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class TouchBallView extends SurfaceView implements SurfaceHolder.Callback {
	private animBallThread animThread;
	private Bitmap ball;
	private Bitmap ballHot;
	private int ballRadius;
	private float ballX;
	private float ballY;
	private Bitmap bg;
	private int centerX;
	private int centerY;
	private boolean isHot;
	private Timer timer;
	private BaseActivity mBaseActivity;
	private String mCurrCmd;

	private void init(Context context) {
		getHolder().addCallback(this);
		mBaseActivity = (BaseActivity) context;
	}
	public TouchBallView(Context context) {
		super(context);
		init(context);
	}

	public TouchBallView(Context context, AttributeSet paramAttributeSet) {
		super(context, paramAttributeSet);
		init(context);
	}

	public TouchBallView(Context context, AttributeSet paramAttributeSet, int paramInt) {
		super(context, paramAttributeSet, paramInt);
		init(context);
	}

	private void actionCmd(String cmd) {
		if (!cmd.equals(mCurrCmd)) {
			mBaseActivity.writeCharacteristic(cmd);
			mCurrCmd = cmd;
		}
	}
	
	private void calcDirection(double angle)
	{
		if (angle >= 22.5 && angle < 67.5) {
			actionCmd(Cmd.FORWARD_RIGHT);
		} else if (angle >= 67.5 && angle < 112.5) {
			actionCmd(Cmd.FORWARD);
		} else if (angle >= 112.5 && angle < 157.5) {
			actionCmd(Cmd.FORWARD_LEFT);
		} else if (angle >= 157.5 && angle < 202.5) {
			actionCmd(Cmd.ORIGIN_LEFT);
		} else if (angle >= 202.5 && angle < 247.5) {
			actionCmd(Cmd.BACK_LEFT);
		} else if (angle >= 247.5 && angle < 292.5) {
			actionCmd(Cmd.BACK);
		} else if (angle >= 292.5 && angle < 337.5) {
			actionCmd(Cmd.BACK_RIGHT);
		} else {
			actionCmd(Cmd.ORIGIN_RIGHT);
		}
	}
	
	private void draw(float paramFloat1, float paramFloat2, boolean paramBoolean) {
		Canvas localCanvas = getHolder().lockCanvas();
		if (localCanvas == null)
			return;
		localCanvas.drawBitmap(this.bg, null, new Rect(0, 0, localCanvas.getWidth(), localCanvas.getHeight()), null);
		this.ballX = (paramFloat1 + this.ballRadius);
		this.ballY = (paramFloat2 + this.ballRadius);
		if (paramBoolean) {
			localCanvas.drawBitmap(ballHot, paramFloat1, paramFloat2, null);
		}
		localCanvas.drawBitmap(ball, paramFloat1, paramFloat2, null);
		getHolder().unlockCanvasAndPost(localCanvas);
	}

	public boolean onTouchEvent(MotionEvent event) {
		float f1 = event.getX();
		float f2 = event.getY();
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if ((f1 <= this.ballX - this.ballRadius) || (f1 >= this.ballX + this.ballRadius)
						|| (f2 <= this.ballY - this.ballRadius) || (f2 >= this.ballY + this.ballRadius)) {
					return false;
				}
				if (this.timer != null) {
					this.timer.cancel();
					this.timer = null;
				}
				if ((this.animThread != null) && (this.animThread.isAlive())) {
					this.animThread.finish();
				}
				break;
				
			case MotionEvent.ACTION_MOVE:
				double d1 = Math.sqrt((this.centerX - f1) * (this.centerX - f1) + (this.centerY - f2) * (this.centerY - f2));
				double d2 = Math.atan2(f2 - this.centerY, f1 - this.centerX);
				if (d1 < this.centerX - this.ballRadius) {
					draw(f1 - this.ballRadius, f2 - this.ballRadius, false);
				} else {
					if (d1 <= 40.0D) {
						return true;
					}
					double angle = Math.acos((f1-centerX)/d1) * 180 / Math.PI;
					if (f2 > centerY) {
						angle = 360 - angle;
					}
					calcDirection(angle);
					draw((float) (Math.cos(d2) * (this.centerX - this.ballRadius) + this.centerX - this.ballRadius),
							(float) (Math.sin(d2) * (this.centerX - this.ballRadius) + this.centerY - this.ballRadius),
							false);
				}
				break;
				
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				mBaseActivity.mHandler.postDelayed(new Runnable() {
					@Override
					public void run() {
						actionCmd(Cmd.STOP);
					}
				}, 100);
				this.animThread = new animBallThread(this.ballX, this.ballY);
				this.animThread.start();
				break;
		}
		return true;
	}

	public void surfaceChanged(SurfaceHolder paramSurfaceHolder, int paramInt1, int paramInt2, int paramInt3) {
	}

	public void surfaceCreated(SurfaceHolder paramSurfaceHolder) {
		this.bg = BitmapFactory.decodeResource(getResources(), R.drawable.background_move);
		this.ball = BitmapFactory.decodeResource(getResources(), R.drawable.ball1);
		this.ballHot = BitmapFactory.decodeResource(getResources(), R.drawable.ball2);
		this.centerX = (getWidth() / 2);
		this.centerY = (-12 + getHeight() / 2);
		this.ballRadius = (this.ball.getWidth() / 2);
		draw(this.centerX - this.ballRadius, this.centerY - this.ballRadius, false);
		this.isHot = true;
		this.timer = new Timer();
		this.timer.schedule(new buttonFlashTask(), 1000, 1000);
	}

	public void surfaceDestroyed(SurfaceHolder paramSurfaceHolder) {
		if (this.timer != null)
			this.timer.cancel();
	}

	private class animBallThread extends Thread {
		private float fx;
		private float fy;
		private boolean stop = false;

		public animBallThread(float ballX, float ballY) {
			this.fx = ballX;
			this.fy = ballY;
		}

		public void finish() {
			this.stop = true;
		}

		public void run() {
			int GRADE = 20;
			int i = 0;
			while (!stop) {
				if (++i < GRADE) {
					TouchBallView.this.draw(this.fx - (this.fx - TouchBallView.this.centerX) * i / GRADE
							- TouchBallView.this.ballRadius, this.fy - (this.fy - TouchBallView.this.centerY) * i
							/ GRADE - TouchBallView.this.ballRadius, false);
				} else {
					stop = true;
					draw(centerX - ballRadius, centerY - ballRadius, false);
					TouchBallView.this.timer = new Timer();
					TouchBallView.this.timer.schedule(new TouchBallView.buttonFlashTask(), 1000L, 1000L);
					return;
				}
			}
		}
	}

	private class buttonFlashTask extends TimerTask {
		public void run() {
			float f1 = TouchBallView.this.centerX - TouchBallView.this.ballRadius;
			float f2 = TouchBallView.this.centerY - TouchBallView.this.ballRadius;
			if (isHot) {
				draw(f1, f2, true);
			} else {
				draw(f1, f2, false);
			}
			isHot = !isHot;
		}
	}
}
