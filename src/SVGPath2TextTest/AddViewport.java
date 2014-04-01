/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SVGPath2TextTest;

import java.awt.geom.Rectangle2D;
import org.apache.batik.dom.svg.SVGOMSVGElement;
import org.apache.batik.gvt.GraphicsNode;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGDocument;

public class AddViewport {

    public static void setViewBoxPageSize(SVGOMSVGElement myRootSVGElement, GraphicsNode rootGN, SVGDocument doc) 
            throws DOMException {
        // Calculate the viewport.
        GraphicsNode gn;
        Rectangle2D rd = null;
        Node ndbckgrd = myRootSVGElement.getElementById("canvas_background");
        if ( ndbckgrd != null) ndbckgrd.getParentNode().removeChild(ndbckgrd);
        rd = rootGN.getTransformedSensitiveBounds(rootGN.getTransform());

        int minX = (int)rd.getX() - 1;
        int minY = (int)rd.getY() - 1;
        int width = (int)(2 + rd.getWidth());
        int height = (int)(2 + rd.getHeight());
        String strViewPort = "" + minX  
                + " " +  minY
                + " "+ width
                + " "+ height;
        System.out.println("viewBox: \t" + strViewPort);
        myRootSVGElement.setAttributeNS(null, "viewBox"
                , strViewPort );
        myRootSVGElement.setAttributeNS(null, "width"
                , String.valueOf(rd.getWidth()).substring(0, 8) );
        myRootSVGElement.setAttributeNS(null, "height"
                , String.valueOf(rd.getHeight()).substring(0, 8) );
    }
}
        
        
