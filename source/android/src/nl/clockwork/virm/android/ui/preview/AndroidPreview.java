package nl.clockwork.virm.android.ui.preview;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nl.clockwork.virm.android.C;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PreviewCallback;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class AndroidPreview extends SurfaceView implements Preview {
	private static long AUTO_FOCUS_INTERVAL = 1500;
	private Handler handler;

	private List<FrameListener> listeners;
	private SurfaceHolder holder;
	private Camera camera;
	private byte[] lastFrame;
	private int frameWidth, frameHeight;
	private boolean capturing;
	private boolean autoPause;

	public AndroidPreview(Context context) {
		super(context);
		handler = new Handler();
		listeners = new ArrayList<FrameListener>();
		holder = getHolder();
		holder.addCallback(this);
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		lastFrame = null;
		frameWidth = 0;
		frameHeight = 0;
		capturing = false;
		autoPause = false;
		
		this.setWillNotDraw(false);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		if (camera != null) {
			int previewBufferSize = initializePreview(width, height);
			lastFrame = new byte[previewBufferSize];

			camera.addCallbackBuffer(lastFrame);
			camera.setPreviewCallbackWithBuffer(new PreviewCallback() {
				@Override
				public void onPreviewFrame(byte[] buffer, Camera camera) {
					if (isCapturing()) {
						lastFrame = buffer;
						if (isAutoPause()) {
							setCapturing(false);
						}
						fireOnFrameEvent();
					}
					camera.addCallbackBuffer(buffer);
				}
			});

			camera.startPreview();
			new AutoFocusRunnable().run();
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			camera = Camera.open();
			camera.setPreviewDisplay(holder);
		} catch (IOException e) {
			Log.e(C.TAG, "Failed to set preview display", e);
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		camera.setPreviewCallbackWithBuffer(null);
		camera.stopPreview();
		camera.release();
		camera = null;
	}
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);

		Paint paint = new Paint();
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(Color.RED);
		paint.setTextSize(15);
		
		canvas.drawText("Min. distance: " + C.MIN_DISTANCE_THRESHOLD, 10, 35, paint);
		canvas.drawText("Min. good matches: " + C.MIN_GOOD_MATCHES, 10, 50, paint);
		canvas.drawText("Frame size: " + C.DESIRED_FRAME_MAT_WIDTH, 10, 65, paint);
		
		Log.d(C.TAG, "ondraw");
	}

	@Override
	public void setCapturing(boolean capturing) {
		this.capturing = capturing;
	}

	@Override
	public boolean isCapturing() {
		return capturing;
	}

	@Override
	public void setAutoPause(boolean autoPause) {
		this.autoPause = autoPause;
	}

	@Override
	public boolean isAutoPause() {
		return autoPause;
	}

	@Override
	public View getView() {
		return this;
	}

	@Override
	public void addFrameListener(FrameListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeFrameListener(FrameListener listener) {
		listeners.remove(listener);
	}

	private void fireOnFrameEvent() {
		Frame event = new Frame(lastFrame, frameWidth, frameHeight);
		Iterator<FrameListener> it = listeners.iterator();
		while (it.hasNext()) {
			it.next().onFrame(event);
		}
	}

	private int initializePreview(int width, int height) {
		Camera.Parameters params = camera.getParameters();
		Camera.Size size = this.getOptimalPreviewSize(params.getSupportedPreviewSizes(), width, height);

		frameWidth = size.width;
		frameHeight = size.height;

		params.setPreviewSize(frameWidth, frameHeight);
		camera.setParameters(params);

		PixelFormat pixelFormat = new PixelFormat();
		PixelFormat.getPixelFormatInfo(params.getPreviewFormat(), pixelFormat);
		int previewBufferSize = size.height * size.width * pixelFormat.bitsPerPixel / 8;

		return previewBufferSize;
	}

	private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int width, int height) {
		Camera.Size optimalSize = camera.new Size(width, height);

		int minDiff = Integer.MAX_VALUE;
		for (Camera.Size size : sizes) {
			if (Math.abs(size.height - height) < minDiff) {
				optimalSize.width = size.width;
				optimalSize.height = size.height;
				minDiff = Math.abs(size.height - height);
			}
		}

		return optimalSize;
	}

	private class AutoFocusRunnable implements Runnable {
		@Override
		public void run() {
			if (camera != null) {
				try {
					camera.autoFocus(new AutoFocusCallback() {
						@Override
						public void onAutoFocus(boolean success, Camera camera) {
							handler.postDelayed(AutoFocusRunnable.this, AUTO_FOCUS_INTERVAL);
						}
					});
				} catch (Exception e) {
					Log.w(C.TAG, "Unable to auto-focus", e);
					handler.postDelayed(AutoFocusRunnable.this, AUTO_FOCUS_INTERVAL);
				}
			}
		}
	}
}
