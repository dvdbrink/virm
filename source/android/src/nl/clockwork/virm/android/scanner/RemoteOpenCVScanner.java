package nl.clockwork.virm.android.scanner;

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
	private boolean pause;
	
	public RemoteOpenCVScanner(Context context) {
		super(context);
		
		connected = false;
		pause = false;
		
		connect();
	}
	
	@Override
	public void scan(byte[] data, int width, int height) {
		super.scan(data, width, height);
		
		if (connected) {
			if (!pause) {
				detector.detect(yuvResized, keypoints);
				extractor.compute(yuvResized, keypoints, descriptor);
				
				byte[] raw = new byte[descriptor.rows() * descriptor.cols()];
				descriptor.get(0, 0, raw);
				sendMat(raw);
				pause = true;
				
				new ListenTask().execute();
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
			socket.connect(new InetSocketAddress("172.19.2.30", 1337), 5000);
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
		Packet p = new Packet();
		p.addByte(PacketHeaders.CLOSE);
		p.send(out);
		
		super.destroy();
	}
	
	private class ListenTask extends AsyncTask<Byte, Void, String> {
		@Override
		protected String doInBackground(Byte... params) {
			try {
				while (pause) {
					byte command = (byte) in.read();
					if (command > -1) {
						switch (command) {
							case PacketHeaders.MATCH:
								pause = false;
								return new DataPacket(in).readString();
							case PacketHeaders.NO_MATCH:
								pause = false;
								return "";
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
			if (result.isEmpty()) {
				Log.d(C.TAG, "No match");
				fireNoMatchEvent();
			} else {
				Log.d(C.TAG, "Match (" + result + ")");
				fireMatchEvent(new Result(result, 0));
			}
		}
	}
}
