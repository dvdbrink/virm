package nl.clockwork.virm.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class Detect {

	public static int[][] matFromBytes(byte[] raw) {
		int rows = Convert.byteArrayToInt(raw, 0);
		int cols = Convert.byteArrayToInt(raw, 4);
		if (rows > 0 && cols > 0) {
			int[][] data = new int[rows][cols];
			int offset = 8;
			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < cols; j++) {
					data[i][j] = raw[offset] & 0xFF;
					offset++;
				}
			}
			return data;
		}
		return null;
	}

	public static byte[] bytesFromFile(File file) throws IOException {
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
			throw new IOException("Could not completely read file "
					+ file.getName());
		}

		is.close();
		return bytes;
	}

}
