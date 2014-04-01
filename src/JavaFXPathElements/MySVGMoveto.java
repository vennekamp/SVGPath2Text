package JavaFXPathElements;

import javafx.geometry.Point2D;
import javafx.scene.shape.MoveTo;

public class MySVGMoveto extends MoveTo {
    // http://www.w3.org/TR/SVG/paths.html#PathDataMovetoCommands
    // M (absolute)
    // m (relative)	moveto	(x y)+	 
    // Start a new sub-path at the given (x,y) coordinate. M (uppercase) indicates 
    // that absolute coordinates will follow; m (lowercase) indicates that 
    // relative coordinates will follow. If a moveto is followed by multiple 
    // pairs of coordinates, the subsequent pairs are treated as implicit lineto 
    // commands. Hence, implicit lineto commands will be relative if the moveto 
    // is relative, and absolute if the moveto is absolute. If a relative moveto 
    // (m) appears as the first element of the path, then it is treated as a pair 
    // of absolute coordinates. In this case, subsequent pairs of coordinates are 
    // treated as relative even though the initial moveto is interpreted as an 
    // absolute moveto.
    
    Point2D pStart;
    Point2D pTo;
    MySVGMoveto(float lastX, float lastY, float x, float y) {
        super(x, y);
        pStart = new Point2D(lastX, lastY);
        pTo = new Point2D(x, y);
    }

    public char toChar(){
        if (this.isAbsolute() )
            return 'M';
        return 'm';
    }  
    
//    public boolean isSpace() {
//        double dist = pStart.distance(pTo);
//        return ( dist > 8.35 && dist < 15.0);
//    }
}
