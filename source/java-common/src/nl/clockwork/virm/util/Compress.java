package nl.clockwork.virm.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public final class Compress {
	public static byte[] deflate(byte[] data, int level) throws IOException {
		Deflater compressor = new Deflater();
		compressor.setLevel(level);
		compressor.setInput(data);
		compressor.finish();

		ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length);

		byte[] buf = new byte[1024];
		while (!compressor.finished()) {
			int count = compressor.deflate(buf);
			bos.write(buf, 0, count);
		}
		try {
			bos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		byte[] compressedData = bos.toByteArray();
		return compressedData;
	}

	public static byte[] inflate(byte[] compressedData) throws IOException {
		Inflater decompressor = new Inflater();
		decompressor.setInput(compressedData);

		ByteArrayOutputStream bos = new ByteArrayOutputStream(
				compressedData.length);

		byte[] buf = new byte[1024];
		while (!decompressor.finished()) {
			try {
				int count = decompressor.inflate(buf);
				bos.write(buf, 0, count);
			} catch (DataFormatException e) {
				e.printStackTrace();
			}
		}
		try {
			bos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		byte[] decompressedData = bos.toByteArray();
		return decompressedData;
	}
}
