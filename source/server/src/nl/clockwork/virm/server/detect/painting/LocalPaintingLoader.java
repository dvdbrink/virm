package nl.clockwork.virm.server.detect.painting;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import nl.clockwork.virm.log.Log;
import nl.clockwork.virm.server.Settings;
import nl.clockwork.virm.server.detect.Detectable;
import nl.clockwork.virm.server.detect.Loader;
import nl.clockwork.virm.util.Detect;

public class LocalPaintingLoader implements Loader {
	private String path;

	public LocalPaintingLoader() {
		try {
			Properties prop = new Properties();
			prop.load(new FileInputStream(Settings.CONFIG_FILE));
			String path = prop.getProperty("local_db_path");
			this.path = path;
		} catch (FileNotFoundException e) {
			Log.e(LocalPaintingLoader.class.getSimpleName(), "FileNotFoundException", e);
		} catch (IOException e) {
			Log.e(LocalPaintingLoader.class.getSimpleName(), "IOException", e);
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
			byte[] raw = Detect.bytesFromFile(file);
			data = Detect.matFromBytes(raw);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}
}
