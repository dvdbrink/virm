package nl.clockwork.virm.android.ui.preview;

import android.view.SurfaceHolder;
import android.view.View;

public interface Preview extends SurfaceHolder.Callback {
	void destroy();
	
	void setCapturing(boolean capturing);

	boolean isCapturing();

	void setAutoPause(boolean autoPause);

	boolean isAutoPause();

	View getView();

	void addFrameListener(FrameListener listener);

	void removeFrameListener(FrameListener listener);
}
