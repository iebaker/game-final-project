package iebaker.argon.world;

public class Place extends Vertex {
	private int x_position;
	private int y_position;

	public enum Heading {
		EAST	(0, "EAST"),
		SOUTH	(270, "SOUTH"),
		WEST	(180, "WEST"),
		NORTH	(90, "NORTH");

		private int number;
		private String name;

		Heading(int n, String s) {
			this.number = n;
		}

		public int asInt() {
			return number;
		}

		public String asString() {
			return name;
		}

		public Heading plus(Turn t) {
			Heading res = headingOf(number + t.asInt());
			if(res != null) {
				return res;
			}
			return this;
		}

		public static Heading random() {
			float test = new java.util.Random().nextFloat();

			if(test > 0.75) {
				return Heading.NORTH;
			} else if(test > 0.5) {
				return Heading.SOUTH;
			} else if(test > 0.25) {
				return Heading.EAST;
			} else {
				return Heading.WEST;
			}
		}

		public Heading headingOf(int i) {
			if(i < 0) i += 360;
			i = i % 360;
			switch(i) {
				case 0:
					return Heading.EAST;
				case 270:
					return Heading.SOUTH;
				case 180:
					return Heading.WEST;
				case 90:
					return Heading.NORTH;
			}
			return null;
		}
	}

	public enum Turn { //I don't think I ever use this which is a damn shame...
		LEFT(90, "LEFT"),
		RIGHT(-90, "RIGHT"),
		FRONT(0, "FRONT"),
		BACK(180, "BACK");

		private int number;
		private String name;

		Turn(int n, String s) {
			this.number = n;
		}

		public int asInt() {
			return number;
		}

		public String asString() {
			return name;
		}
	}

	public Place(int x, int y, String id) {
		super(id);
		x_position = x;
		y_position = y;
	}

	public Place(int x, int y) {
		super(GridGraph.coordString(x, y));
		x_position = x;
		y_position = y;
	}

	public int getX() {
		return x_position;
	}

	public int getY() {
		return y_position;
	}

	public Place getNextPlace(Heading h) {
		switch(h) {
			case EAST:
				return new Place(x_position + 1, y_position, GridGraph.coordString(x_position + 1, y_position));
			case NORTH:
				return new Place(x_position, y_position - 1, GridGraph.coordString(x_position, y_position - 1));
			case SOUTH:
				return new Place(x_position, y_position + 1, GridGraph.coordString(x_position, y_position + 1));
			case WEST:
				return new Place(x_position - 1, y_position, GridGraph.coordString(x_position - 1, y_position));
		}
		return this;
	}

	public Heading approxHeadingTo(Place p) {
		int x_difference = p.getX() - this.x_position;
		int y_difference = p.getY() - this.y_position;

		return Math.abs(x_difference) >= Math.abs(y_difference) ? 
			(x_difference > 0 ? Heading.EAST : Heading.WEST) : 
			(y_difference > 0 ? Heading.SOUTH : Heading.NORTH);
	}

	public float euclideanDistanceTo(Place p) {
		float x_difference = (float) p.getX() - (float) this.x_position;
		float y_difference = (float) p.getY() - (float) this.y_position;

		return (float) Math.sqrt(x_difference * x_difference + y_difference * y_difference);
	}

	public float manhattanDistanceTo(Place p) {
		float x_difference = (float) p.getX() - (float) this.x_position;
		float y_difference = (float) p.getY() - (float) this.y_position;

		return (float) (Math.abs(x_difference) + Math.abs(y_difference));
	}

	@Override
	public boolean equals(Object o) {
		if(o instanceof Place) {
			Place p = (Place) o;
			return p.getX() == x_position && p.getY() == y_position;
		}
		return false;
	}

	@Override
	public String toString() {
		return "[Place object at (" + x_position + ", " + y_position + ") with " + this.getOutEdges().size() + " out edges]";
	}
}