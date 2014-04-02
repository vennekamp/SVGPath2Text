/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JavaFXPathElements;

import LetterTamplates.Templateletter;
import java.util.LinkedHashMap;
import javafx.geometry.Point2D;
import javafx.scene.shape.PathElement;

import org.apache.batik.parser.ParseException;
import org.apache.batik.parser.PathHandler;

// http://www.hameister.org/JavaFX_SVGPath.html
// https://code.google.com/p/siteplan/source/browse/CampSkeleton/src/camp/bigoldataset/SVG2Int.java?r=e97f1e14e2d45bc11f2d0cfc781c38ed88251cd8
/**
 *
 * @see PathHandler Interface
 *
 *
 * http://www.w3.org/TR/SVG/paths.html
 * http://www.hameister.org/JavaFX_SVGPath.html
 *
 * @author j.hameister
 *
 */
public class JavaFXPathElementHandler implements PathHandler {

    private MySVGletterPath path = null;
    private static final LinkedHashMap<String, MySVGletterPath> paths
            = new LinkedHashMap<String, MySVGletterPath>();
    private static String strID = "";
    private PathElement lastElement;
    private float lastX = 0;
    private float lastY = 0;
    private Point2D lastPoint = new Point2D(lastX, lastX);
    private final static double MINDISTANCE = 0.001;
    private final static double MINDISTANCELINEFACTOR = 100;

    public JavaFXPathElementHandler(String pathStyling) {
        path.getStyleClass().add(pathStyling);
    }

    public JavaFXPathElementHandler() {
        super();
    }

    @Override
    public void startPath() throws ParseException {
        path = new MySVGletterPath(strID);
    }

    @Override
    public void endPath() throws ParseException {

    }

    @Override
    public void closePath() throws ParseException {
        lastElement = new MySVGClosePath();
        path.getElements().add(lastElement);
        path.addMyStrElement(((MySVGClosePath) lastElement).toChar());
        paths.put(JavaFXPathElementHandler.strID, path);
//        System.err.println("Path beendet -- suche Buchstaben für: " + path.toString());
        Templateletter.getChar(path);
    }

    @Override
    public void curvetoCubicAbs(float x1, float y1, float x2, float y2, float x, float y) throws ParseException {
        // System.out.println("Dist:\t" + Math.abs(lastPoint.distance(x, y)));
//        if (Math.abs(lastPoint.distance(x, y)) > MINDISTANCE)
        {
            lastElement = new MySVGCubicCurveTo(x1, y1, x2, y2, x, y, lastPoint);
            lastElement.setAbsolute(true);
            path.getElements().add(lastElement);
            path.addMyStrElement(((MySVGCubicCurveTo) lastElement).toChar());
        }
        lastX = x;
        lastY = y;
        lastPoint = new Point2D(lastX, lastY);
    }

    @Override
    public void curvetoCubicRel(float x1, float y1, float x2, float y2, float x, float y) throws ParseException {
        // System.out.println("Dist:\t" + Math.abs(lastPoint.distance(x, y)));
//        if (Math.abs(lastPoint.distance(x, y)) > MINDISTANCE) 
        {
            lastElement = new MySVGCubicCurveTo(x1, y1, x2, y2, x, y, lastPoint);
            lastElement.setAbsolute(false);
            path.getElements().add(lastElement);
            path.addMyStrElement(((MySVGCubicCurveTo) lastElement).toChar());
        }
        lastX = x;
        lastY = y;
        lastPoint = new Point2D(lastX, lastY);
    }

    @Override
    public void curvetoCubicSmoothAbs(float x2, float y2, float x, float y) throws ParseException {
        // System.out.println("Dist:\t" + Math.abs(lastPoint.distance(x, y)));
//        if (Math.abs(lastPoint.distance(x, y)) > MINDISTANCE) 
        {
            lastElement = new MySVGShortHandCubicCurveTo(lastElement, lastX, lastY, x2, y2, x, y, lastPoint);
            lastElement.setAbsolute(true);
            path.getElements().add(lastElement);
            path.addMyStrElement(((MySVGShortHandCubicCurveTo) lastElement).toChar());
        }
        lastX = x;
        lastY = y;
        lastPoint = new Point2D(lastX, lastY);
    }

    @Override
    public void curvetoCubicSmoothRel(float x2, float y2, float x, float y) throws ParseException {
        // System.out.println("Dist:\t" + Math.abs(lastPoint.distance(x, y)));
//        if (Math.abs(lastPoint.distance(x, y)) > MINDISTANCE) 
        {
            lastElement = new MySVGShortHandCubicCurveTo(lastElement, lastX, lastY, x2, y2, x, y, lastPoint);
            lastElement.setAbsolute(false);
            path.getElements().add(lastElement);
            path.addMyStrElement(((MySVGShortHandCubicCurveTo) lastElement).toChar());
        }
        lastX = x;
        lastY = y;
        lastPoint = new Point2D(lastX, lastY);
    }

    @Override
    public void linetoAbs(float x, float y) throws ParseException {
        // skip treatment of very small line-Elements (might be just an artefact)
        // System.out.println("Dist:\t" + Math.abs(lastPoint.distance(x, y)));
        if (Math.abs(x) > MINDISTANCELINEFACTOR  * MINDISTANCE 
                ||
           Math.abs(y) > MINDISTANCELINEFACTOR  * MINDISTANCE  ) {
            lastElement = new MySVGLineTo(lastPoint, x, y);
            lastX = x;
            lastY = y;
            lastPoint = new Point2D(lastX, lastY);
            lastElement.setAbsolute(true);
            path.getElements().add(lastElement);
            path.addMyStrElement(((MySVGLineTo) lastElement).toChar());
        }
    }

    @Override
    public void linetoRel(float x, float y) throws ParseException {
        // skip treatment of very small line-Elements (might be just an artefact)
        if (Math.abs(x) > MINDISTANCELINEFACTOR  * MINDISTANCE 
                ||
           Math.abs(y) > MINDISTANCELINEFACTOR  * MINDISTANCE  ) {
            lastElement = new MySVGLineTo(lastPoint, x, y);
            lastX = x;
            lastY = y;
            lastPoint = new Point2D(lastX, lastY);
            lastElement.setAbsolute(false);
            path.getElements().add(lastElement);
            path.addMyStrElement(((MySVGLineTo) lastElement).toChar());
        }
    }

    @Override
    public void linetoHorizontalAbs(float x) throws ParseException {
        // skip treatment of very small line-Elements (might be just an artefact)
         if (Math.abs(x) > MINDISTANCELINEFACTOR  * MINDISTANCE) {
            lastElement = new MySVGLineTo(lastPoint, x, lastY);
            lastX = x;
            lastPoint = new Point2D(lastX, lastY);
            lastElement.setAbsolute(true);
            path.getElements().add(lastElement);
            path.addMyStrElement(((MySVGLineTo) lastElement).toChar());
        }
    }

    @Override
    public void linetoHorizontalRel(float x) throws ParseException {
        // skip treatment of very small line-Elements (might be just an artefact)
        if (Math.abs(x) > MINDISTANCELINEFACTOR  * MINDISTANCE) {
            lastElement = new MySVGLineTo(lastPoint, x, lastY);
            lastX = x;
            lastPoint = new Point2D(lastX, lastY);
            lastElement.setAbsolute(false);
            path.getElements().add(lastElement);
            path.addMyStrElement(((MySVGLineTo) lastElement).toChar());
        }
    }

    @Override
    public void linetoVerticalAbs(float y) throws ParseException {
        // skip treatment of very small line-Elements (might be just an artefact)
        if (Math.abs(y) > MINDISTANCELINEFACTOR  * MINDISTANCE  ) {
            lastElement = new MySVGLineTo(lastPoint, lastX, y);
            lastY = y;
            lastPoint = new Point2D(lastX, lastY);
            lastElement.setAbsolute(true);
            path.getElements().add(lastElement);
            path.addMyStrElement(((MySVGLineTo) lastElement).toChar());
        }
    }

    @Override
    public void linetoVerticalRel(float y) throws ParseException {
        // skip treatment of very small line-Elements (might be just an artefact)
        if ( Math.abs(y) > MINDISTANCELINEFACTOR  * MINDISTANCE  ) {
            lastElement = new MySVGLineTo(lastPoint, lastX, y);
            lastY = y;
            lastPoint = new Point2D(lastX, lastY);
            lastElement.setAbsolute(true);
            path.getElements().add(lastElement);
            path.addMyStrElement(((MySVGLineTo) lastElement).toChar());
        }
    }

    @Override
    public void movetoAbs(float x, float y) throws ParseException {
        path = new MySVGletterPath(strID);
        lastElement = new MySVGMoveto(lastX, lastY, x, y);
        lastX = x;
        lastY = y;
        lastPoint = new Point2D(lastX, lastY);
        lastElement.setAbsolute(true);
        path.getElements().add(lastElement);
        path.addMyStrElement(((MySVGMoveto) lastElement).toChar());
    }

    @Override
    public void movetoRel(float x, float y) throws ParseException {
        path = new MySVGletterPath(strID);
        lastElement = new MySVGMoveto(lastX, lastY, x, y);
        lastX = x;
        lastY = y;
        lastPoint = new Point2D(lastX, lastY);
        lastElement.setAbsolute(path.getElements().size() == 0);
        path.getElements().add(lastElement);
        path.addMyStrElement(((MySVGMoveto) lastElement).toChar());
    }

    @Override
    public void curvetoQuadraticAbs(float x1, float y1, float x, float y) throws ParseException {
        throw new ParseException("curvetoQuadraticAbs is currently not supported.", null);
    }

    @Override
    public void curvetoQuadraticRel(float x1, float y1, float x, float y) throws ParseException {
        throw new ParseException("curvetoQuadraticRel is currently not supported.", null);
    }

    @Override
    public void curvetoQuadraticSmoothAbs(float x, float y) throws ParseException {
        throw new ParseException("curvetoQuadraticSmoothAbs is currently not supported.", null);
    }

    @Override
    public void curvetoQuadraticSmoothRel(float x, float y) throws ParseException {
        throw new ParseException("curvetoQuadraticSmoothRel is currently not supported.", null);
    }

    @Override
    public void arcAbs(float rx, float ry, float xAxisRotation, boolean largeArcFlag, boolean sweepFlag, float x, float y) throws ParseException {
        throw new ParseException("arcAbs is currently not supported.", null);
//        lastX = x;
//        lastY = y;
//        lastElement = new ArcTo(rx, ry, xAxisRotation, x, y, largeArcFlag, sweepFlag);
//        path.getElements().add(lastElement);
    }

    @Override
    public void arcRel(float rx, float ry, float xAxisRotation, boolean largeArcFlag, boolean sweepFlag, float x, float y) throws ParseException {
        throw new ParseException("arcAbs is currently not supported.", null);
//        lastX = x;
//        lastY = y;
//        lastElement = new ArcTo(rx, ry, xAxisRotation, x, y, largeArcFlag, sweepFlag);
//        path.getElements().add(lastElement);
    }

    public MySVGletterPath getPath() {
        return path;
    }

    public static LinkedHashMap<String, MySVGletterPath> getPaths() {
        return paths;
    }

    void setId(String nodeValue) {
        JavaFXPathElementHandler.strID = nodeValue;
    }
}
