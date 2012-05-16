package nl.clockwork.virm.server;

import java.awt.EventQueue;
import java.io.IOException;

import nl.clockwork.virm.server.net.Server;

/*
 * CPU -> VM-arguments: -Djava.library.path="${project_loc}/lib" -Xcheck:jni
 * GPI -> optirun java -Djava.library.path=/home/daniel/dev/projects/virm-android/virm-server/lib -jar virm-server.jar
 */

public class Main {
	public static void main(String[] args) throws IOException {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerView view = new ServerView();
					new Thread(new Server(view)).start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
