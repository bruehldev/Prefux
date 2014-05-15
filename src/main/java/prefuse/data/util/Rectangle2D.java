package prefuse.data.util;

public class Rectangle2D extends javafx.geometry.Rectangle2D {

	public Rectangle2D(double minX, double minY, double width, double height) {
		super(minX, minY, width, height);
	}

	public double getX() {
		return getMinX();
	}

	public double getY() {
		return getMinY();
	}
	
	public static Rectangle2D get(java.awt.geom.Rectangle2D rec) {
		return new Rectangle2D(rec.getMinX(),rec.getMinY(),rec.getWidth(),rec.getHeight());
	}

	public double getCenterX() {
		return getMinX()+getWidth()/2;
	}

	public double getCenterY() {
		return getMinY()+getHeight()/2;
	}

}
