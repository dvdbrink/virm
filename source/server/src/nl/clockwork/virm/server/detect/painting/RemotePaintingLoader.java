package nl.clockwork.virm.server.detect.painting;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import nl.clockwork.virm.log.Log;
import nl.clockwork.virm.server.detect.Detectable;
import nl.clockwork.virm.server.detect.Loader;
import nl.clockwork.virm.util.Detect;

public class RemotePaintingLoader implements Loader {
	private Connection conn;

	public RemotePaintingLoader() {
		try {
			Properties prop = new Properties();
			prop.load(new FileInputStream("conf/default.properties"));
			String host = prop.getProperty("db_host");
			String user = prop.getProperty("db_user");
			String pass = prop.getProperty("db_pass");
			String name = prop.getProperty("db_name");
			conn = DriverManager.getConnection(host + name, user, pass);
		} catch (SQLException e) {
			Log.e(RemotePaintingLoader.class.getSimpleName(), "SQLException", e);
		} catch (FileNotFoundException e) {
			Log.e(RemotePaintingLoader.class.getSimpleName(), "FileNotFoundException", e);
		} catch (IOException e) {
			Log.e(RemotePaintingLoader.class.getSimpleName(), "IOException", e);
		}
	}

	@Override
	public List<Detectable> load() {
		List<Detectable> paintings = new ArrayList<Detectable>();
		ResultSet rs = selectPaintings();
		try {
			while (rs.next()) {
				Painting painting = loadPainting(rs);
				if (painting != null) {
					paintings.add(painting);
				}
			}
		} catch (SQLException e) {
			Log.e(RemotePaintingLoader.class.getSimpleName(), "SQLException", e);
		}
		Log.i("Loader", paintings.size() + " paintings loaded");
		return paintings;
	}

	private Painter loadPainter(long id) {
		Painter painter = null;
		ResultSet rs = selectPainter(id);
		try {
			if (rs.next()) {
				painter = new Painter(rs.getLong(1), rs.getString(2), rs.getString(3));
			}
		} catch (SQLException e) {
			Log.e(RemotePaintingLoader.class.getSimpleName(), "SQLException", e);
		}
		return painter;
	}

	private int[][] loadData(Blob b) {
		int[][] data = null;
		try {
			if (b.length() > 0) {
				byte[] raw = b.getBytes(1, (int) b.length());
				data = Detect.matFromBytes(raw);
			}
		} catch (SQLException e) {
			Log.e(RemotePaintingLoader.class.getSimpleName(), "SQLException", e);
		}
		return data;
	}

	private Painting loadPainting(ResultSet rs) {
		Painting painting = null;
		try {
			int[][] data = loadData(rs.getBlob(4));
			if (data != null) {
				painting = new Painting(rs.getLong(1), rs.getString(2),
						rs.getString(3), data, loadPainter(rs.getLong(5)));
			}
		} catch (SQLException e) {
			Log.e(RemotePaintingLoader.class.getSimpleName(), "SQLException", e);
		}
		return painting;
	}

	private ResultSet selectPaintings() {
		String query = "SELECT * FROM painting";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(query);
		} catch (SQLException e) {
			Log.e(RemotePaintingLoader.class.getSimpleName(), "SQLException", e);
		} finally {
			if (pstmt != null) {
				try {
					rs = pstmt.executeQuery();
				} catch (SQLException e) {
					Log.e(RemotePaintingLoader.class.getSimpleName(), "SQLException", e);
				}
			}
		}
		return rs;
	}

	private ResultSet selectPainter(long id) {
		String query = "SELECT * FROM painter WHERE id=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setLong(1, id);
		} catch (SQLException e) {
			Log.e(RemotePaintingLoader.class.getSimpleName(), "SQLException", e);
		} finally {
			if (pstmt != null) {
				try {
					rs = pstmt.executeQuery();
				} catch (SQLException e) {
					Log.e(RemotePaintingLoader.class.getSimpleName(), "SQLException", e);
				}
			}
		}
		return rs;
	}
}
