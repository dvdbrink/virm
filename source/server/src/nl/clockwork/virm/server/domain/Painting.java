package nl.clockwork.virm.server.domain;

import org.opencv.core.Mat;

public class Painting {
	public long id;
	public String name;
	public String description;
	public Mat data;
	public Painter painter;
}
