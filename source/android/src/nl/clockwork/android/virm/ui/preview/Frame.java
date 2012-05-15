package nl.clockwork.android.virm.ui.preview;

public class Frame {
	private byte[] data;
	private int width, height;

	public Frame(byte[] data, int width, int height) {
		this.data = data;
		this.width = width;
		this.height = height;
	}

	public byte[] getData() {
		return data;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}
