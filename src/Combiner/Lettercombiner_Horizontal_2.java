/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Combiner;

import javax.swing.JFrame;
import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.dom.svg.SVGOMSVGElement;
import org.apache.batik.dom.svg.SVGOMTextElement;
import org.apache.batik.gvt.GraphicsNode;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.svg.SVGDocument;

/**
 *
 * @author martin-win8
 */
public class Lettercombiner_Horizontal_2 {
        private static GraphicsNode gnElement1;
    private static GraphicsNode gnElement2;
    private static NodeList nlText;
    public static void CombineLetters2Words(
            JFrame jFrame,
            SVGDocument doc,
            SVGOMSVGElement myRootSVGElement,
            BridgeContext ctx,            
            GraphicsNode gnElement,
            GraphicsNode gntxtElem,
            NodeList nlText,
            String svgNS
    ) throws DOMException {
        long memUsage = Runtime.getRuntime().totalMemory();
        System.err.println("Runtime.getRuntime().totalMemory(): " 
                + memUsage  );
        if ( memUsage > 1253818880) return;
        // Buchstaben zu Wörtern zusammenfassen
        for (int i = nlText.getLength() - 1 ;  i >=0; i--) {
            if ( nlText.item(i) != null 
                    && !((SVGOMTextElement)nlText.item(i)).hasAttributeNS(null, "transform")) {
                Node nextNeighbour = null;
                Double distNextNeighbour = 5.0;
                gnElement1 
                        = ctx.getGraphicsNode(nlText.item(i));
                double posXofI
                        = gnElement1.getBounds().getX()
                        +  gnElement1.getBounds().getWidth();
                double posYofI 
                        = gnElement1.getBounds().getY();
                for ( int j = nlText.getLength() - 1; j >= 0; j--) {
                    if ( i != j && nlText.item(j) != null) {
                        gnElement2
                                = ctx.getGraphicsNode(nlText.item(j));
                        double posXofJ
                                = gnElement2.getBounds().getX();
                        double posYofJ
                                = gnElement2.getBounds().getY();
                        if ( Math.abs(posYofI - posYofJ) < 2
                                && Math.abs(posXofI - posXofJ)  < distNextNeighbour 
                                && posXofJ - posXofI > 0 ) {
                            distNextNeighbour = Math.abs(posXofI - posXofJ);
                            nextNeighbour = nlText.item(j);
                            System.err.print("DistanceX: \t" + (posXofI - posXofJ) );
                            System.err.println("\tDistanceY: \t" + (posYofI - posYofJ) );
                        }
                    } //  if ( i != j && nlText.item(j) != null)
                }// For j
                gnElement1 = null;
                if ( nextNeighbour != null) {
                    ((SVGOMTextElement)nlText.item(i)).setTextContent(
                            ((SVGOMTextElement)nlText.item(i)).getTextContent()
                                    + ((SVGOMTextElement)nextNeighbour).getTextContent());
                    System.out.println("..->" 
                            + ((SVGOMTextElement)nlText.item(i)).getTextContent() );
                    nextNeighbour.getParentNode().removeChild(nextNeighbour);                    
                    nextNeighbour = null;
                    distNextNeighbour = null;
                    nlText = myRootSVGElement.getElementsByTagName("text");
                    System.gc();
                    
                    CombineLetters2Words(
                        jFrame,
                        doc,
                        myRootSVGElement,
                        ctx,            
                        gnElement,
                        gntxtElem,
                        nlText,
                        svgNS
                    );
                    return;
                } // if ( nextNeighbour != null)
            } // if (nlText ..)
        } // for i
    }

}
