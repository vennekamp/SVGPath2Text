package JavaFXPathElements;

import javafx.geometry.Point2D;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.PathElement;

//S (absolute)
//s (relative)	shorthand/smooth curveto	(x2 y2 x y)+	
// Draws a cubic Bézier curve from the current point to (x,y). The first control 
// point is assumed to be the reflection of the second control point on the 
// previous command relative to the current point. (If there is no previous 
// command or if the previous command was not an C, c, S or s, assume the first 
// control point is coincident with the current point.) (x2,y2) is the second 
// control point (i.e., the control point at the end of the curve). S (uppercase) 
// indicates that absolute coordinates will follow; s (lowercase) indicates that 
// relative coordinates will follow. Multiple sets of coordinates may be specified 
// to draw a polybézier. At the end of the command, the new current point becomes 
// the final (x,y) coordinate pair used in the polybézier.

public class MySVGShortHandCubicCurveTo extends CubicCurveTo {
    Point2D lastPoint;    
    Point2D pStartControl;
    Point2D pEndControl;
    Point2D pTo;

    public Point2D getpTo() {
        return pTo;
    }
    MySVGShortHandCubicCurveTo(PathElement lastElement, 
            float lastX, float lastY, float x2, float y2, float x, float y,
            Point2D lastPoint) {
        super( 0f, 0f, x2, y2, x, y);
        float x1 = lastX;
        float y1 = lastY;
        if (lastElement instanceof CubicCurveTo){
            x1 =  2 * lastX - (float)((CubicCurveTo)lastElement).getControlX2();
            y1 =  2 * lastY - (float)((CubicCurveTo)lastElement).getControlY2();
        }
        this.setControlX1(x1);
        this.setControlY1(y1);
        this.lastPoint = new Point2D(lastPoint.getX(), lastPoint.getY());
        pStartControl = new Point2D(x1, y1);
        pEndControl = new Point2D(x2, y2);
        pTo = new Point2D(x, y);
    }

    public char toChar(){
        if (this.isAbsolute() )
            return 'S';
        return 's';
    }
    
}
