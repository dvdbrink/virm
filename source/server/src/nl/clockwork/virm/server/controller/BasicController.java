package nl.clockwork.virm.server.controller;

import java.awt.event.ActionEvent;

import nl.clockwork.virm.server.model.Model;
import nl.clockwork.virm.server.model.ServerModel;
import nl.clockwork.virm.server.view.BasicGUI;
import nl.clockwork.virm.server.view.View;

public class BasicController extends Controller {
	public BasicController(Model model, View view) {
		super(model, view);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO hardcoded basicgui & servermodel
		if (e.getSource() == ((BasicGUI)view).startButton) {
			((ServerModel)model).startServer();
		}
	}
}
