package JavaFXPathElements;

import javafx.geometry.Point2D;
import javafx.scene.shape.LineTo;

//L (absolute)
//l (relative)	lineto	(x y)+	
// Draw a line from the current point to the given (x,y) coordinate which 
// becomes the new current point. L (uppercase) indicates that absolute 
// coordinates will follow; l (lowercase) indicates that relative coordinates 
// will follow. A number of coordinates pairs may be specified to draw a 
// polyline. At the end of the command, the new current point is set to the 
// final set of coordinates provided.

public class MySVGLineTo extends LineTo  {
    Point2D lastPoint = new Point2D(0.0, 0.0);    
    Point2D pTo;

    public Point2D getpTo() {
        return pTo;
    }
    MySVGLineTo(Point2D lastPoint, float x, float y) {
        super(x, y);
        this.lastPoint = new Point2D(lastPoint.getX(), lastPoint.getY());
        pTo = new Point2D(x, y);
    }
  
    public char toChar(){
        if (this.isAbsolute() )
            return 'L';
        return 'l';
    }  

    boolean isComparable(MySVGLineTo other) {
        boolean rtnBool = Math.abs(this.distanceBetween() - other.distanceBetween()) > 15 ;
        return rtnBool;     
    }

    public Double getMyElementAngle(){
        if ( Math.abs(this.distanceBetween()) < 0 ) return null;
        return Math.toDegrees( Math.atan2(
                pTo.getX() - lastPoint.getX(), pTo.getY() - lastPoint.getY()));
    }
    
    private int distanceBetween() {
        return (int) (100 * pTo.distance(lastPoint));
    }
}
