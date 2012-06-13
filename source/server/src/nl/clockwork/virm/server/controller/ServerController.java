package nl.clockwork.virm.server.controller;

import java.awt.event.ActionEvent;

import nl.clockwork.virm.server.model.Model;
import nl.clockwork.virm.server.model.ServerModel;
import nl.clockwork.virm.server.view.ServerView;
import nl.clockwork.virm.server.view.View;

public class ServerController extends Controller {
	public ServerController(Model model, View view) {
		super(model, view);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == ((ServerView)view).startButton) {
			((ServerModel)model).startServer();
		}
	}
}
