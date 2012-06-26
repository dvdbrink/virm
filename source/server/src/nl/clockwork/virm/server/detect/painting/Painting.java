package nl.clockwork.virm.server.detect.painting;

import nl.clockwork.virm.server.detect.Detectable;

public class Painting implements Detectable {
	public long id;
	public String name;
	public String description;
	public int[][] data;
	public Painter painter;

	public Painting(long id, String name, String description, int[][] data, Painter painter) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.data = data;
		this.painter = painter;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public int[][] getData() {
		return data;
	}

	public Painter getPainter() {
		return painter;
	}
}
