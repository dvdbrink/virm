package nl.clockwork.virm.server.controller;

import java.awt.event.ActionListener;

import nl.clockwork.virm.server.model.Model;
import nl.clockwork.virm.server.view.View;

public abstract class Controller implements ActionListener {
	protected Model model;
	protected View view;
	
	public Controller(Model model, View view) {
		this.model = model;
		this.view = view;
	}
}
