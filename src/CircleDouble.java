import java.awt.geom.Point2D;

public class CircleDouble {
	static final double EPSILON = 1e-3;
	static final double ONE_THIRD = 1.0 / 3.0;
	private Point2D center;
	private double radius, radiusSquare;

	public CircleDouble(double centerX, double centerY, double radius) {
		this(new Point2D.Double(centerX, centerY), radius);
	}

	public CircleDouble(Point2D center, double radius) {
		this.center = center;
		this.radius = radius;
		radiusSquare = radius * radius;
	}

	/**
	 * 
	 * constructor with one point, center is the point itself and radius is zero
	 * 
	 * @param p
	 * 
	 */
	public CircleDouble(Point2D p) {
		this(p, 0);
	}

	/**
	 * 
	 * constructor with two points, center is halfway between them and radius is
	 * half the distance between them
	 * 
	 * @param p0
	 * @param p1
	 * 
	 */
	public CircleDouble(Point2D p0, Point2D p1) {
		center = new Point2D.Double((p0.getX() + p1.getX()) * 0.5, (p0.getY() + p1.getY()) * 0.5);
		radius = p0.distance(p1) * 0.5;
		radiusSquare = radius * radius;
	}

	/**
	 * 
	 * constructor with three points, center is a barycenter/center_of_mass Do
	 * the geometry if this does not look obvious :)
	 * 
	 * @param p0
	 * @param p1
	 * @param p2
	 * 
	 */
	public CircleDouble(Point2D p0, Point2D p1, Point2D p2) {
		// // calculate edges
		// Point2D e1 = new Point2D.Double(p1.getX() - p0.getX(), p1.getY() -
		// p0.getY());
		// Point2D e2 = new Point2D.Double(p2.getX() - p0.getX(), p2.getY() -
		// p0.getY());
		//
		// // prepare lengths
		// double edgeLen1 = (e1.getX() * e1.getX() + e1.getY() * e1.getY()) *
		// 0.5;
		// double edgeLen2 = (e2.getX() * e2.getX() + e2.getY() * e2.getY()) *
		// 0.5;
		//
		// // calculate determinant of the matrix that is made of the two edges
		// double det = e1.getX() * e2.getY() - e1.getY() * e2.getX();
		// if (Math.abs(det) > EPSILON) {
		// // create circle
		// double invDet = 1.0 / det;
		// double qx = (e2.getY() * edgeLen1 - e1.getY() * edgeLen2) * invDet;
		// double qy = (e1.getX() * edgeLen2 - e2.getX() * edgeLen1) * invDet;
		// System.out.println("center: " + center);
		// System.out.println("qx,p1x,qy,p1y " + qx + " " + p1.getX() + " " + qy
		// + " " + p1.getY());
		// center = new Point2D.Double(qx + p1.getX(), qy + p1.getY());
		// radius = Math.sqrt(qx * qx + qy * qy);
		// radiusSquare = radius * radius;
		// } else {
		// center.setLocation(0, 0);
		// radius = Double.MAX_VALUE;
		// radiusSquare = Double.MAX_VALUE;
		// }

		// ----------mine------------
		// double p0P1MidpointX = (p0.getX() + p1.getX()) * 0.5;
		// double p0P1MidpointY = (p0.getY() + p1.getY()) * 0.5;
		// double centerX = (p2.getX() - p0P1MidpointX) * ONE_THIRD +
		// p0P1MidpointX;
		// double centerY = (p2.getY() - p0P1MidpointY) * ONE_THIRD +
		// p0P1MidpointY;
		// center = new Point2D.Double(centerX, centerY);
		// radius = center.distance(p0);
		// System.out.println(center.distance(p0)+" "+center.distance(p1)+" "+center.distance(p2));
		// radiusSquare = radius * radius;

		// ----------mine------------

		double x0 = p0.getX();
		double y0 = p0.getY();
		double x1 = p1.getX();
		double y1 = p1.getY();
		double x2 = p2.getX();
		double y2 = p2.getY();
		double d = 2 * (x0 * (y1 - y2) + x1 * (y2 - y0) + x2 * (y0 - y1));
		double centerX = ((y1 - y2) * (x0 * x0 + (y0 - y1) * (y0 - y2)) + (x1 * x1) * (y2 - y0) + (x2 * x2) * (y0 - y1)) / d;
		double centerY = ((x0 * x0) * (x2 - x1) + x0 * ((x1 * x1) - (x2 * x2) + (y1 * y1) - (y2 * y2)) - (x1 * x1) * x2 + x1
				* ((x2 * x2) - (y0 * y0) + (y2 * y2)) + x2 * ((y0 * y0) - (y1 * y1)))
				/ d;
		center = new Point2D.Double(centerX, centerY);
		radius = center.distance(p0);
		System.out.println(center.distance(p0) + " " + center.distance(p1) + " " + center.distance(p2));
		radiusSquare = radius * radius;
	}

	public boolean contains(double px, double py) {
		return (Math.pow(px - center.getX(), 2) + Math.pow(py - center.getY(), 2) - radiusSquare < EPSILON);
	}

	public boolean contains(Point2D point) {
		return (center.distanceSq(point) - radiusSquare < EPSILON);
	}

	/**
	 * function that works the same as contains(Point2D point) but treat the
	 * circle's radius as radius+extraRadius
	 * 
	 * @param point
	 * @param extraRadius
	 * @return true if the circle contains, false otherwise
	 */
	public boolean contains(Point2D point, double extraRadius) {
		return (center.distance(point) - (radius + extraRadius) < EPSILON);
	}

	public void setCenter(Point2D center) {
		this.center = center;
	}

	public void setCenter(double newX, double newY) {
		setCenter(new Point2D.Double(newX, newY));
	}

	public Point2D getCenter() {
		return center;
	}

	public double getCenterX() {
		return center.getX();
	}

	public void setCenterX(double centerX) {
		center.setLocation(centerX, center.getY());
	}

	public double getCenterY() {
		return center.getY();
	}

	public void setCenterY(double centerY) {
		center.setLocation(center.getX(), centerY);
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
		radiusSquare = radius * radius;
	}

	public void set(CircleDouble circle) {
		setCenter(new Point2D.Double(circle.getCenterX(), circle.getCenterY()));
		setRadius(circle.getRadius());
	}

	public String toString() {
		return "[Center: " + getCenter() + ", Radius: " + getRadius() + "]";
	}

	/**
	 * get the intersection of (the line connecting the circle's center and the
	 * point) and (the circle's boundary)
	 * 
	 * @param point
	 * @return the new location on boundary
	 */
	public Point2D getOnBoundaryLocation(Point2D point) {
		double ratio = radius / center.distance(point);
		double newX = center.getX() + (point.getX() - center.getX()) * ratio;
		double newY = center.getY() + (point.getY() - center.getY()) * ratio;
		return new Point2D.Double(newX, newY);
	}

	/**
	 * function that works the same as getOnBoundaryLocation(Point2D point) but
	 * treat the circle's radius as radius +¡¡extraRadius
	 * 
	 * @param pointLocation
	 * @param i
	 * @return
	 */
	public Point2D getOnBoundaryLocation(Point2D point, double extraRadius) {
		double ratio = (radius + extraRadius) / center.distance(point);
		double newX = center.getX() + (point.getX() - center.getX()) * ratio;
		double newY = center.getY() + (point.getY() - center.getY()) * ratio;
		return new Point2D.Double(newX, newY);
	}

}
