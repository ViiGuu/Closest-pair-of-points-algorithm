public record Point(int x, int y) {

	/**
	 * Calculates the distance between two points. Results are undefined if the argument is null.
	 *
	 * @param  point	The point to calculate distance to
	 * @return          The distance between the current point and the parameter.
	 * @author			Henrik Bergström
	 * @author			Viggo Gustafsson
	 */
	public double distanceTo(Point point) {
		int dx = x - point.x;
		int dy = y - point.y;
        return Math.sqrt(dx * dx + dy * dy);
	}

	public String toString() {
		return "(x=%d, y=%d)".formatted(x,y);
	}
	
}
