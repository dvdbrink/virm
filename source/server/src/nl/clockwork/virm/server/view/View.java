package nl.clockwork.virm.server.view;

import java.util.Observer;

import nl.clockwork.virm.server.controller.Controller;

public interface View extends Observer {
	void addController(Controller c);
}
