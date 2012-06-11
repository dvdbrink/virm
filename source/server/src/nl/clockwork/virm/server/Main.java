package nl.clockwork.virm.server;

import java.io.IOException;

public class Main {
	public static void main(String[] args) throws IOException {
		Model model = new Model();
		View view = new View();
		
		Controller controller = new Controller(model, view);
		
		view.addController(controller);
		model.addObserver(view);
	}
}
