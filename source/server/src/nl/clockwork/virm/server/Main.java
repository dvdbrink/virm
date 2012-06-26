package nl.clockwork.virm.server;

import nl.clockwork.virm.log.Log;
import nl.clockwork.virm.server.controller.BasicController;
import nl.clockwork.virm.server.controller.Controller;
import nl.clockwork.virm.server.model.Model;
import nl.clockwork.virm.server.model.ServerModel;
import nl.clockwork.virm.server.view.BasicGUI;
import nl.clockwork.virm.server.view.View;

public class Main {
	private static final String NO_GUI_PARAM = "nogui";

	public static void main(String[] args) {
		if (args.length > 0 && args[0].equalsIgnoreCase(NO_GUI_PARAM)) {
			Log.init(new String[] { "nl.clockwork.virm.log.ConsoleLogger" });
			new ServerModel();
		} else {
			Log.init(new String[] { "nl.clockwork.virm.log.ConsoleLogger",
					"nl.clockwork.virm.server.view.BasicGUILogger" });

			View view = new BasicGUI();
			Model model = new ServerModel();
			Controller controller = new BasicController(model, view);

			model.addObserver(view);
			view.addController(controller);
		}
	}
}
