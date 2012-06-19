package nl.clockwork.virm.android.scanner;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

import nl.clockwork.virm.android.C;
import nl.clockwork.virm.net.DataPacket;
import nl.clockwork.virm.net.Packet;
import nl.clockwork.virm.net.PacketHeaders;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class RemoteOpenCVScanner extends BasicOpenCVScanner {
	private Socket socket;
	private InputStream in;
	private OutputStream out;
	private boolean connected;
	private long start;

	public RemoteOpenCVScanner(Context context) {
		super(context);

		connected = false;

		connect();
	}

	@Override
	public void scan(byte[] data, int width, int height) {
		if (connected) {
			super.scan(data, width, height);

			start = System.currentTimeMillis();
			
			detector.detect(yuvResized, keypoints);
			if (keypoints.toList().size() > 0) {
				extractor.compute(yuvResized, keypoints, descriptor);

				int rows = descriptor.rows();
				int cols = descriptor.cols();
				ByteArrayOutputStream bytes = new ByteArrayOutputStream(rows * cols);
				for (int i = 0; i < rows; i++) {
					for (int j = 0; j < cols; j++) {
						byte[] b = new byte[1];
						bytes.write((byte) descriptor.get(i, j, b));
					}
				}
				
				sendMat(bytes.toByteArray());

				new ListenTask().execute();
			} else {
				fireNoMatchEvent();
			}
		}
	}

	private void sendMat(byte[] data) {
		Packet p = new Packet();
		p.addByte(PacketHeaders.DETECT);
		p.addInt(descriptor.rows());
		p.addInt(descriptor.cols());
		p.addBytes(data);
		p.send(out);
	}

	private void connect() {
		try {
			socket = new Socket();
			socket.connect(new InetSocketAddress("10.203.81.62", 1337), 5000);
			in = socket.getInputStream();
			out = socket.getOutputStream();
			connected = true;
			Toast.makeText(context, "Connected", Toast.LENGTH_LONG).show();
		} catch (SocketTimeoutException e) {
			Toast.makeText(context, "Could not connect to server", Toast.LENGTH_LONG).show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void destroy() {
		if (connected) {
			Packet p = new Packet();
			p.addByte(PacketHeaders.CLOSE);
			p.send(out);
		}

		super.destroy();
	}

	private class ListenTask extends AsyncTask<Byte, Void, String> {		
		@Override
		protected String doInBackground(Byte... params) {
			try {
				while (true) {
					byte command = (byte) in.read();
					if (command > -1) {
						switch (command) {
							case PacketHeaders.MATCH: 	 return new DataPacket(in).readString();
							case PacketHeaders.NO_MATCH: return "";
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result == null) {
				Log.w(C.TAG, "Something went wrong, result == null");
			} else if (result.isEmpty()) {
				fireNoMatchEvent();
			} else {
				fireMatchEvent(new Result(result, System.currentTimeMillis() - start));
			}
		}
	}
}
