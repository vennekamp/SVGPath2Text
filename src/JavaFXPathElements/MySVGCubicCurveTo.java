package JavaFXPathElements;

import LetterTamplates.Templateletter;
import javafx.geometry.Point2D;
import javafx.scene.shape.CubicCurveTo;

//C (absolute)
//c (relative)	
//curveto	(x1 y1 x2 y2 x y)+	
//Draws a cubic Bézier curve from the current point to (x,y) using (x1,y1) as the control point at the beginning 
//of the curve and (x2,y2) as the control point at the end of the curve. C (uppercase) indicates that absolute 
//coordinates will follow; c (lowercase) indicates that relative coordinates will follow. Multiple sets of 
//coordinates may be specified to draw a polybézier. At the end of the command, the new current point 
//becomes the final (x,y) coordinate pair used in the polybézier.
public class MySVGCubicCurveTo extends CubicCurveTo {

    Point2D pStartControl;
    Point2D pEndControl;
    Point2D pTo;
    Point2D lastPoint;

    public Point2D getpTo() {
        return pTo;
    }

    public MySVGCubicCurveTo(float x1, float y1, float x2, float y2, float x, float y, Point2D lastPoint) {
        super(x1, y1, x2, y2, x, y);
        pStartControl = new Point2D(x1, y1);
        pEndControl = new Point2D(x2, y2);
        pTo = new Point2D(x, y);
        this.lastPoint = new Point2D(lastPoint.getX(), lastPoint.getY());
    }

    public Double getMyElementAngle(){
        if ( distanceBetween() < 10 ) return null;
        return Math.toDegrees( Math.atan2(
                pTo.getX() - lastPoint.getX(), pTo.getY() - lastPoint.getY()));
    }
    
    public char toChar() {
        if (this.isAbsolute()) {
            return 'C';
        }
        return 'c';
    }

    public boolean isComparable(MySVGCubicCurveTo other){
        boolean rtnBool = Math.abs( this.distanceBetween()  
                - other.distanceBetween() ) * MySVGletterPath.getFontSize() / Templateletter.getFontSize() < 15 ;
        return rtnBool;     
    }
    
    public int distanceBetween() {
        int rtnInt =(int) (100 * pTo.distance(pEndControl) + pEndControl.distance(pStartControl));
        return rtnInt;
    }
}
