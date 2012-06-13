package nl.clockwork.virm.server;

import java.util.Observable;

import nl.clockwork.virm.log.Log;
import nl.clockwork.virm.server.controller.BasicController;
import nl.clockwork.virm.server.controller.Controller;
import nl.clockwork.virm.server.model.Model;
import nl.clockwork.virm.server.model.ServerModel;
import nl.clockwork.virm.server.view.BasicGUI;
import nl.clockwork.virm.server.view.View;

public class Main {
	public static void main(String[] args) {
		Log.init(new String[] { "nl.clockwork.virm.log.ConsoleLogger",
				"nl.clockwork.virm.server.view.ViewLogger" });

		Model model = new ServerModel();
		View view = new BasicGUI();
		Controller controller = new BasicController(model, view);

		model.addObserver(view);
		view.addController(controller);

		((Observable) Log.get("nl.clockwork.virm.server.view.ViewLogger")).addObserver(view);
	}
}
