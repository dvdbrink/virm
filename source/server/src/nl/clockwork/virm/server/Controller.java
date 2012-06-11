package nl.clockwork.virm.server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import nl.clockwork.virm.log.Log;

public class Controller implements ActionListener {
	private Model model;
	private View view;
	
	public Controller(Model model, View view) {
		this.model = model;
		this.view = view;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		/*if (e.getSource() == view.randomButton) {
			Log.d("Random button clicked");
		}*/
	}
}
