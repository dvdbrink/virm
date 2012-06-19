package nl.clockwork.virm.server.detect.painting;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import nl.clockwork.virm.log.Log;
import nl.clockwork.virm.server.detect.Detectable;
import nl.clockwork.virm.server.detect.Loader;
import nl.clockwork.virm.util.Convert;

public class LocalPaintingLoader implements Loader {
	private String path;

	public LocalPaintingLoader() {
		try {
			Properties prop = new Properties();
			prop.load(new FileInputStream("conf/default.properties"));
			String path = prop.getProperty("local_db_path");
			this.path = path;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
			int rows = Convert.byteArrayToInt(raw, 0);
			int cols = Convert.byteArrayToInt(raw, 4);
			if (rows > 0 && cols > 0) {
				data = new int[rows][cols];
				int offset = 8;
				for (int i = 0; i < rows; i++) {
					for (int j = 0; j < cols; j++) {
						data[i][j] = raw[offset] & 0xFF;
						offset++;
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
