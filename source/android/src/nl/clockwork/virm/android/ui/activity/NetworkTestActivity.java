package nl.clockwork.virm.android.ui.activity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import nl.clockwork.virm.android.R;
import nl.clockwork.virm.net.Packet;
import nl.clockwork.virm.net.Packets;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

public class NetworkTestActivity extends Activity {
	private TextView log;

	private Socket socket;
	private InputStream in;
	private OutputStream out;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.networktest);
		log = (TextView) findViewById(R.id.log);

		connect();
		
		// Send ping..
		Packet p = new Packet();
		p.addByte(Packets.PING);
		p.send(out);
		// ..and listen for reply
		new ListenTask().execute(Packets.PING);
	}

	private void connect() {
		log.append("Initializing...\n");
		try {
			socket = new Socket();
			socket.connect(new InetSocketAddress("172.19.2.62", 1337));

			in = socket.getInputStream();
			out = socket.getOutputStream();
			
			log.append(String.format("Connected to server on %s:%s\n", 
					socket.getInetAddress().getHostAddress(),
					socket.getLocalPort()));
		} catch (IOException e) {
			log.append("An error occured connecting to server\n");
			e.printStackTrace();
		}
	}

	private class ListenTask extends AsyncTask<Byte, Void, Void> {
		@Override
		protected void onPreExecute() {
			log.append("waiting for packet...");
		}

		@Override
		protected Void doInBackground(Byte... params) {
			try {
				while ((byte) in.read() != params[0]) {}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			log.append("done\n");
		}
	}
}
