package nl.clockwork.virm.android.ui.preview;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nl.clockwork.virm.android.Virm;
import android.content.Context;
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
		Log.d(Virm.TAG, "AndroidPreview");
		handler = new Handler();
		listeners = new ArrayList<FrameListener>();
		holder = getHolder();
		holder.addCallback(this);
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		camera = null;
		lastFrame = null;
		frameWidth = 0;
		frameHeight = 0;
		capturing = false;
		autoPause = false;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		Log.d(Virm.TAG, "surfaceChanged");
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
					// new buffer should be added to be able to capture a new frame
					camera.addCallbackBuffer(buffer);
				}
			});

			camera.startPreview();
			
			// force the camera to focus every 1500ms
			new AutoFocusRunnable().run();
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.d(Virm.TAG, "surfaceCreated");
		try {
			if (camera == null) {
				camera = Camera.open();
				camera.setPreviewDisplay(holder);
			}
		} catch (IOException e) {
			Log.e(Virm.TAG, "Failed to set preview display", e);
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.d(Virm.TAG, "surfaceDestroyed");
		if (camera != null) {
			camera.setPreviewCallbackWithBuffer(null);
			camera.stopPreview();
			camera.release();
			camera = null;
		}
	}
	
	@Override
	public void destroy() {
		Log.d(Virm.TAG, "destroy");
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
					Log.w(Virm.TAG, "Unable to auto-focus", e);
					handler.postDelayed(AutoFocusRunnable.this, AUTO_FOCUS_INTERVAL);
				}
			}
		}
	}
}
