package nl.clockwork.virm.android.scanner;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import nl.clockwork.virm.android.Settings;
import nl.clockwork.virm.android.Virm;
import nl.clockwork.virm.net.DataPacket;
import nl.clockwork.virm.net.Packet;
import nl.clockwork.virm.net.PacketHeaders;
import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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
						bytes.write((byte) ((int) descriptor.get(i, j)[0]));
					}
				}
				
				sendMat(bytes.toByteArray());
				Log.d(Virm.TAG, "Send DETECT");

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
			socket.connect(new InetSocketAddress(Settings.SERVER_IP, Settings.SERVER_PORT), 5000);
			in = socket.getInputStream();
			out = socket.getOutputStream();
			connected = true;
		} catch (IOException e) {
			Log.e(Virm.TAG, "IOException", e);
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setMessage("Could not connect to server");
			builder.setCancelable(true);
			AlertDialog alert = builder.create();
			alert.show();
		}
	}

	@Override
	public void destroy() {
		if (connected) {
			// send CLOSE packet
			Packet p = new Packet();
			p.addByte(PacketHeaders.CLOSE);
			p.send(out);
			Log.d(Virm.TAG, "Send CLOSE");
		}

		super.destroy();
	}

	private class ListenTask extends AsyncTask<Byte, Void, String> {		
		@Override
		protected String doInBackground(Byte... params) {
			try {
				// wait for either a MATCH or NO_MATCH packet
				while (true) {
					byte command = (byte) in.read();
					if (command > -1) {
						switch (command) {
							case PacketHeaders.MATCH: 	 
								Log.d(Virm.TAG, "Received MATCH");
								return new DataPacket(in).readString();
							case PacketHeaders.NO_MATCH:
								Log.d(Virm.TAG, "Received NO_MATCH");
								return "";
						}
					}
				}
			} catch (IOException e) {
				Log.e(Virm.TAG, "IOException", e);
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result == null) {
				Log.w(Virm.TAG, "Something went wrong, result == null");
			} else if (result.isEmpty()) {
				fireNoMatchEvent();
			} else {
				fireMatchEvent(new Result(result, System.currentTimeMillis() - start));
			}
		}
	}
}
