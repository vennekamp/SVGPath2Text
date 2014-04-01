package JavaFXPathElements;

import JavaFXPathElements.MySVGCubicCurveTo;
import JavaFXPathElements.MySVGLineTo;
import JavaFXPathElements.MySVGMoveto;
import java.util.ArrayList;
import java.util.Objects;
import javafx.geometry.Point2D;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;

public class MySVGletterPath extends Path {

    private final static int fontSize = 16;
    public static int getFontSize() {
        return fontSize;
    }
    
    private String strOfPathElements = "";
    private Character chrMyElement = null;
    private Point2D myLongestDistance;
    private ArrayList<Double> myAngles;
    private double dblMyDistance;

    public Point2D getMyLongestDistance() {
        return myLongestDistance;
    }
    private static final ArrayList<MySVGletterPath> allLettersByID 
            = new ArrayList<MySVGletterPath>();

    public MySVGletterPath(String strID) {
        super();
        this.setId(strID);
        myAngles = new ArrayList<Double>();
    }

    public static ArrayList<MySVGletterPath> getAllSVGletterByIDs() {
        return allLettersByID;
    }

    public Character getMyCharacter() {
        return chrMyElement;
    }

    public void setMyCharacter(Character chrElement) {
        this.chrMyElement = chrElement;
    }

    public void setMyLongestDistance() {
        Point2D rtnPoint = new Point2D(0.0, 0.0);
        for (int i = 1; i < this.getElements().size(); i= i + 4) {
            final PathElement thisElement = this.getElements().get(i);
            if (thisElement instanceof MySVGCubicCurveTo && !thisElement.isAbsolute()) {
                Point2D aPoint = ((MySVGCubicCurveTo) thisElement).getpTo();
                aPoint = new Point2D( rtnPoint.getX() + aPoint.getX() 
                        ,  rtnPoint.getY() + aPoint.getY() );
                if ( aPoint.distance(0.0, 0.0) > rtnPoint.distance(0.0, 0.0) ) {
                    rtnPoint = aPoint;
                }
            } else if (thisElement instanceof MySVGLineTo && !thisElement.isAbsolute()) {
                Point2D aPoint = ((MySVGLineTo) thisElement).getpTo();
                aPoint = new Point2D(rtnPoint.getX() + aPoint.getX()
                        , rtnPoint.getY() + aPoint.getY() );
                if (aPoint.distance(0.0, 0.0) > rtnPoint.distance(0.0, 0.0)) {
                    rtnPoint = aPoint;
                }
            }
        }
        myLongestDistance = rtnPoint;
    }

    public double getMyAngle() {
        return MySVGwordPath.getMedian(myAngles);
    }

    public double getMyDistance() {
        return dblMyDistance;
    }
    
    public void addMyStrElement(char charOfElement) {
        strOfPathElements += charOfElement;
//       System.out.println(" -->" + strOfPathElements);
    }

    @Override
    public String toString() {
        return "(" + this.getElements().size() + ") " + strOfPathElements;
    }

    public boolean equals(MySVGletterPath other) {
        boolean rtnBool = true;
        if (!this.toString().toLowerCase().equals(other.toString().toLowerCase())) {
            return false;
        }
        for (int i = 0; i < this.getElements().size(); i++) {
            final PathElement thisElement = this.getElements().get(i);
            final PathElement otherElement = other.getElements().get(i);
            if (thisElement instanceof MySVGMoveto) {
//                this.hasTailoringSpace = true;
            } else if (thisElement instanceof MySVGCubicCurveTo) {
                boolean cIsComparable
                        = (((MySVGCubicCurveTo) thisElement)
                                .isComparable((MySVGCubicCurveTo) otherElement));
                if (cIsComparable
                        && ((MySVGCubicCurveTo) otherElement)
                            .getMyElementAngle() != null
                        && ((MySVGCubicCurveTo) thisElement)
                            .getMyElementAngle() != null ) {
                    myAngles.add(((MySVGCubicCurveTo) otherElement)
                            .getMyElementAngle()
                    - ((MySVGCubicCurveTo) thisElement)
                            .getMyElementAngle()
                    );
                }
                rtnBool = rtnBool & cIsComparable;
            } else if (thisElement instanceof MySVGLineTo) {
                boolean lIsComparable
                        = (((MySVGLineTo) thisElement)
                                .isComparable((MySVGLineTo) otherElement));
                if (lIsComparable
                        && ((MySVGLineTo) otherElement)
                            .getMyElementAngle() != null
                        && ((MySVGLineTo) thisElement)
                            .getMyElementAngle() != null) {
                    myAngles.add(((MySVGLineTo) otherElement)
                            .getMyElementAngle()
                    - ((MySVGLineTo) thisElement)
                            .getMyElementAngle());
                }
                rtnBool = rtnBool & true;
            }
        }
//        System.err.println("rtnBool:  \t" + rtnBool);
        if (rtnBool) {
            this.chrMyElement = other.getMyCharacter();
            MySVGletterPath.allLettersByID.add(this);
            this.setMyLongestDistance();
//            this.dblMyAngles =  Math.toDegrees(
//                    Math.atan2(other.getMyLongestDistance().getX(), other.getMyLongestDistance().getY())
//                    -
//                    Math.atan2(this.getMyLongestDistance().getX(), this.getMyLongestDistance().getY())
//            );
            
//            while ( this.dblMyAngles  < -90) this.dblMyAngles += 90;
//            while ( this.dblMyAngles  > 90) this.dblMyAngles -= 90;
            this.dblMyDistance
                    = this.myLongestDistance.distance(other.getMyLongestDistance());
//            System.err.println("'" + this.chrMyElement + "'\tLänge: \t" 
//                    + this.myLongestDistance.distance(new Point2D(0.0, 0.0))
//                    + "\tWinkel: \t" 
//                    + Math.toDegrees(Math.atan2(this.myLongestDistance.getX() 
//                            , this.myLongestDistance.getY() )));
            System.err.println("'" + this.chrMyElement + "'\tLength: \t" 
                    + this.dblMyDistance
                    + "\tAngle: \t" 
                    + MySVGwordPath.getMedian(this.myAngles));
        }
        else {
            myAngles = new ArrayList<Double>();
        }
        return rtnBool;
    }
       
    @Override
    public boolean equals(Object object){
        if (object != null && object instanceof MySVGletterPath){
            return this.getId().equals(((MySVGletterPath)object).getId()) && 
                    Objects.equals(this.getMyCharacter(), ((MySVGletterPath)object).getMyCharacter());
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.strOfPathElements);
        hash = 97 * hash + Objects.hashCode(this.chrMyElement);
        hash = 97 * hash + Objects.hashCode(this.myLongestDistance);
        return hash;
    }

    
}
