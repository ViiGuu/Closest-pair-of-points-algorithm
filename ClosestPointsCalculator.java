import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Finds the closest pair of Point objects in a collection, using a divide and conquer search.
 */
public class ClosestPointsCalculator{
	private static final int BRUTE_FORCE_BOUNDARY = 25;
	private static final int STRIP_CHECK_LIMIT = 7;

	/**
	 * The initial method call to the algorithm that sorts the array before recursively calling itself.
	 *
	 * @param  points  an array of points
	 * @return         an array containing the closest pair of points
	 * @author		   Viggo Gustafsson
	 */
	public static Point[] findClosestPairOfPoints(Point[] points) {
		if(points != null && points.length >= 2) {
			Arrays.sort(points, Comparator.comparingInt(Point::x));
			Range range = new Range(0, points.length);
			return findClosestPairOfPoints(points, range);
		}
		return points;
	}

	/**
	 * A recursive function to find the closest pair of points in a given array of points within a specified range.
	 * Recursively calls itself until the brute force boundary has been reach. It then calculates the closest pair
	 * of points between the left side, right side, and the strip inbetween them (in case the closest pair is crossing
	 * the two sides.
	 *
	 * @param  points  the array of points objects
	 * @param  range   the range that the current recursion should search through
	 * @return         the closest pair of points found within the given range
	 * @author		   Viggo Gustafsson
	 */
	private static Point[] findClosestPairOfPoints(Point[] points, Range range){
		if((range.end - range.start) <= BRUTE_FORCE_BOUNDARY){
			return bruteForceSearch(points, range);
		}

		Point[] leftClosest = findClosestPairOfPoints(points, new Range(range.start, range.mid));
		Point[] rightClosest = findClosestPairOfPoints(points, new Range(range.mid, range.end));

		double leftDistance = leftClosest[0].distanceTo(leftClosest[1]);
		double rightDistance = rightClosest[0].distanceTo(rightClosest[1]);

		Point[] stripClosest = strip(points, leftDistance, rightDistance, range);

		if(stripClosest[0] == null || stripClosest[1] == null)
			return leftDistance < rightDistance ? leftClosest : rightClosest;

		double stripDistance = stripClosest[0].distanceTo(stripClosest[1]);

        if(leftDistance < rightDistance && leftDistance < stripDistance)
			return leftClosest;

		else if(rightDistance < stripDistance)
			return rightClosest;

		return stripClosest;
	}

	/**
	 * A function that performs an O(N^2) search to find the closest pair of points within a given range.
	 * Called by the main algorithm once the specified boundary has been reached to ensure efficiency.
	 *
	 * @param  points    an array of point objects
	 * @param  range     the range within which to search for the closest pair of points
	 * @return           an array containing the two closest points found in the given range
	 * @author			 Viggo Gustafsson
	 */
	private static Point[] bruteForceSearch(Point[] points, Range range){
		Point[] closestPair = new Point[2];
		closestPair[0] = points[range.start];
		closestPair[1] = points[range.start + 1];
		for(int i = range.start; i < range.end; i++){
			for(int j = i + 1; j < range.end; j++){
				if(points[i].distanceTo(points[j])
						< closestPair[0].distanceTo(closestPair[1])){
					closestPair[0] = points[i];
					closestPair[1] = points[j];
				}
			}
		}
		return closestPair;
	}

	/**
	 * Calculates whether the closest set of points is crossing the left and right sides.
	 *
	 * @param  points        array of points
	 * @param  leftDistance  distance between left side points
	 * @param  rightDistance distance between right side points
	 * @param  range         range to search through
	 * @return               the two closest points within the strip
	 * @author				 Viggo Gustafsson
	 */
	private static Point[] strip(Point[] points, double leftDistance,
			double rightDistance, Range range){
		double stripWidth = Math.min(leftDistance, rightDistance);
		List<Point> strip = new ArrayList<>();

		for(int i = range.start; i < range.end; i++){
			if (Math.abs(points[i].x() - points[range.mid].x()) < stripWidth) {
				strip.add(points[i]);
			}
		}

		strip.sort(Comparator.comparingInt(Point::y));
		Point[] stripClosest = new Point[2];
		double stripDistance = stripWidth;

		for(int i = 0; i < strip.size(); i++){
			for(int j = i + 1; j < Math.min(i + STRIP_CHECK_LIMIT, strip.size()); j++){
				if(strip.get(i).distanceTo(strip.get(j)) < stripDistance){
					stripDistance = strip.get(i).distanceTo(strip.get(j));
					stripClosest[0] = strip.get(i);
					stripClosest[1] = strip.get(j);
				}
			}
		}

		return stripClosest;
	}

	/**
	 * Simple wrapper class to keep track of the range for the current recursion of the algorithm
	 */
	private static class Range {
		private final int start;
		private final int mid;
		private final int end;

		Range(int start, int end) {
			this.start = start;
			this.mid = start + (end - start) / 2;
			this.end = end;
		}
	}
}
