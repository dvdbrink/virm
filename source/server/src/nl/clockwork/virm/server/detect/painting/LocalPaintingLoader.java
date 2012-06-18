package nl.clockwork.virm.server.detect.painting;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import nl.clockwork.virm.log.Log;
import nl.clockwork.virm.server.detect.Detectable;
import nl.clockwork.virm.server.detect.Loader;
import nl.clockwork.virm.util.Convert;

public class LocalPaintingLoader implements Loader {
	private String path;

	public LocalPaintingLoader(String path) {
		this.path = path;
	}

	@Override
	public List<Detectable> load() {
		List<Detectable> paintings = new ArrayList<Detectable>();
		File folder = new File(path);
		File[] files = folder.listFiles();
		for (int i = 0; i < files.length; i++) {
			paintings.add(loadPainting(files[i].getPath()));
		}
		Log.i(LocalPaintingLoader.class.getSimpleName(), paintings.size() + " paintings loaded");
		return paintings;
	}

	private Painting loadPainting(String fileName) {
		Painting painting = null;

		File file = new File(fileName);
		if (file.canRead()) {
			painting = new Painting(0, file.getName(), "", loadData(file), null);
		}

		return painting;
	}

	private int[][] loadData(File file) {
		int[][] data = null;

		try {
			byte[] raw = getBytesFromFile(file);
			int offset = 0;
			int rows = Convert.byteArrayToInt(raw, offset);
			int cols = Convert.byteArrayToInt(raw, offset+=4);
			if (rows > 0 && cols > 0) {
				data = new int[rows][cols];
				for (int i = 0; i < rows; i++) {
					for (int j = 0; j < cols; j++) {
						data[i][j] = Convert.byteArrayToInt(raw, offset+=4);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return data;
	}

	// TODO util
	public byte[] getBytesFromFile(File file) throws IOException {
		InputStream is = new FileInputStream(file);

		long length = file.length();

		if (length > Integer.MAX_VALUE) {
			throw new IOException("File is too large " + file.getName());
		}

		byte[] bytes = new byte[(int) length];

		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length
				&& (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
			offset += numRead;
		}

		if (offset < bytes.length) {
			throw new IOException("Could not completely read file " + file.getName());
		}

		is.close();
		return bytes;
	}
}
