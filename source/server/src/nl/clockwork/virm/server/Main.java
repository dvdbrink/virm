package nl.clockwork.virm.server;

import java.io.IOException;

import nl.clockwork.virm.log.Log;
import nl.clockwork.virm.server.detect.Loader;
import nl.clockwork.virm.server.detect.painting.LocalPaintingLoader;
import nl.clockwork.virm.server.detect.painting.RemotePaintingLoader;

public class Main {

	public static void main(String[] args) throws IOException {
		Log.init(new String[] { "nl.clockwork.virm.log.ConsoleLogger",
				"nl.clockwork.virm.server.view.ViewLogger" });
		
		Loader loader = new RemotePaintingLoader("jdbc:mysql://localhost:3306/virm", "root", "root");
		//Loader loader = new LocalPaintingLoader("res/descriptor");
		loader.load();

		/*Server server = new Server("172.19.2.30", 1337);
		Model model = new ServerModel(server);
		View view = new ServerView();
		Controller controller = new ServerController(model, view);

		model.addObserver(view);
		view.addController(controller);

		((Observable) Log.get("nl.clockwork.virm.server.view.ViewLogger")).addObserver(view);*/
	}
}
