package nl.clockwork.virm.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import nl.clockwork.virm.log.Log;
import nl.clockwork.virm.util.Convert;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

public class Main {
	/*
	 * public static void main(String[] args) throws IOException { Log.init(new
	 * String[] { "nl.clockwork.virm.log.ConsoleLogger",
	 * "nl.clockwork.virm.server.view.ViewLogger" });
	 * 
	 * Log.d(System.getProperty("java.library.path"));
	 * 
	 * Server server = new Server("172.19.2.30", 1337); Model model = new
	 * Model(server); View view = new View(); Controller controller = new
	 * Controller(model, view);
	 * 
	 * model.addObserver(view); view.addController(controller);
	 * 
	 * ((Observable)
	 * Log.get("nl.clockwork.virm.server.view.ViewLogger")).addObserver(view); }
	 */

	public static void main(String[] args) throws IOException {
		Log.init(new String[] { "nl.clockwork.virm.log.ConsoleLogger" });

		//byte[] data = getBytesFromFile(new File(
		//		"res/descriptor/134328_OrchidsAndGrasses.mat"));
		// Log.d(Convert.byteArrayToHexString(data));
		// Log.d("=============================================================");

		Connection con = null;
		Statement st = null;
		ResultSet rs = null;

		String url = "jdbc:mysql://localhost:3306/virm";
		String user = "root";
		String password = "root";

		try {
			con = (Connection) DriverManager.getConnection(url, user, password);
			st = (Statement) con.createStatement();

			/*
			 * String query = "UPDATE painting SET data=? WHERE id=1";
			 * PreparedStatement pstmt = (PreparedStatement)
			 * con.prepareStatement(query); pstmt.setBytes(1, data);
			 * pstmt.execute();
			 */

			
			
			String query = "INSERT INTO painting(name, description, data, painter_id) VALUES(?, ?, ?, ?)";
			
			File folder = new File("res/descriptor");
			File[] files = folder.listFiles();
			for (int i = 0; i < files.length; i++) {
				PreparedStatement pstmt = (PreparedStatement) con.prepareStatement(query);
				pstmt.setString(1, files[i].getName());
				pstmt.setString(2, "");
				pstmt.setBytes(3, getBytesFromFile(files[i]));
				pstmt.setLong(4, 1);
				pstmt.execute();
				Log.d(i + " ");
			}
			Log.d("done");
			
			/*String sql = "SELECT data FROM painting WHERE id=?";
			PreparedStatement prest = (PreparedStatement) con
					.prepareStatement(sql);
			prest.setInt(1, 1);
			rs = prest.executeQuery();
			rs.next();
			Blob d = rs.getBlob("data");
			byte[] dbdata = d.getBytes(1, (int) d.length());
			// Log.d(Convert.byteArrayToHexString(dbdata));*/

			/*if (data.length != dbdata.length) {
				Log.d("length !=");
			} else {
				Log.d("checking");
				for (int i = 0; i < data.length; i++) {
					if (data[i] != dbdata[i]) {
						Log.d("!=");
					}
				}
				Log.d("done");
			}*/
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
				if (con != null) {
					con.close();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static byte[] getBytesFromFile(File file) throws IOException {
		InputStream is = new FileInputStream(file);

		// Get the size of the file
		long length = file.length();

		// You cannot create an array using a long type.
		// It needs to be an int type.
		// Before converting to an int type, check
		// to ensure that file is not larger than Integer.MAX_VALUE.
		if (length > Integer.MAX_VALUE) {
			// File is too large
		}

		// Create the byte array to hold the data
		byte[] bytes = new byte[(int) length];

		// Read in the bytes
		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length
				&& (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
			offset += numRead;
		}

		// Ensure all the bytes have been read in
		if (offset < bytes.length) {
			throw new IOException("Could not completely read file "
					+ file.getName());
		}

		// Close the input stream and return bytes
		is.close();
		return bytes;
	}
}
