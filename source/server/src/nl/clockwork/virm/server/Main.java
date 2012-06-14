package nl.clockwork.virm.server;

import nl.clockwork.virm.log.Log;
import nl.clockwork.virm.server.controller.BasicController;
import nl.clockwork.virm.server.controller.Controller;
import nl.clockwork.virm.server.model.Model;
import nl.clockwork.virm.server.model.ServerModel;
import nl.clockwork.virm.server.view.BasicGUI;
import nl.clockwork.virm.server.view.View;

public class Main {
	public static void main(String[] args) {
		// instantiate logging with console and gui logger
		Log.init(new String[] { "nl.clockwork.virm.log.ConsoleLogger",
				"nl.clockwork.virm.server.view.ViewLogger" });

		// instantiate view first so all logging will find its way to the gui
		View view = new BasicGUI();
		Model model = new ServerModel();
		Controller controller = new BasicController(model, view);

		model.addObserver(view);
		view.addController(controller);
	}
}
