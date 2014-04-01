/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Combiner;

import java.awt.geom.AffineTransform;
import java.util.ArrayList;
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
public class Lettercombiner_Horizontal {

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
        double dist = 0.5;
        // Buchstaben zu Wörtern zusammenfassen
        for (int i = nlText.getLength() - 1; i >= 0; i--) {
            SVGOMTextElement nlElem = (SVGOMTextElement) nlText.item(i);
                String attrRotate = nlElem.getAttributeNS(svgNS, "transform");
                Double angle = 0.0;
                if (!attrRotate.isEmpty()) {
                    attrRotate = attrRotate.substring(7, attrRotate.indexOf(","));
                    angle = Double.parseDouble(attrRotate);
                }
                if (angle == 0.0) {
                    Node nextNeighbour = null;
                    ArrayList<Node> nextNeighbours = new ArrayList<>();
//                 Double distNextNeighbour = Double.MAX_VALUE;
                    gnElement
                            = ctx.getGraphicsNode(nlText.item(i));
//                    nlElem.setAttributeNS(null, "fill", "blue");

                    System.err.println("neues Element: \t"
                            + nlElem.getTextContent() + "\tAngle:\t" + angle);
                    double rectX = gnElement.getBounds().getCenterX();
//                        + gnElement.getBounds().getWidth() / 2 
//                        * Math.cos(angle) - Math.signum(angle) * dist;
                    double rectY = gnElement.getBounds().getCenterY();
//                        + gnElement.getBounds().getHeight() / 2 
//                        * Math.sin(angle) + Math.signum(angle) * dist;
                  
                    rectX = rectX + gnElement.getBounds().getWidth() / 2 + dist;
                    rectY = rectY - dist;
                    
//                System.out.println("rectx: \t" + (rectX + dist) + "\t" + (rectY + dist / 2) );
//                System.err.println("angle\t" + angle);
                    for (int j = nlText.getLength() - 1; j >= 0; j--) {
                        if ( i != j ) {
                            gntxtElem
                                    = ctx.getGraphicsNode(nlText.item(j));
                            boolean intersect
                                    = gntxtElem.getSensitiveBounds()
                                    .intersects(rectX, rectY, 2 * dist, 2 * dist);
//                        System.out.println("Vergeich:\t" + gntxtElem.getSensitiveBounds().getX() 
//                                + "\t" + gntxtElem.getSensitiveBounds().getY() 
//                                + "\t" + gntxtElem.getSensitiveBounds().getWidth()
//                                + "\t" + gntxtElem.getSensitiveBounds().getHeight());
//                        if ( Math.abs(posYofI - posYofJ) < 2
//                                && Math.abs(posXofI - posXofJ)  < distNextNeighbour 
//                                && posXofJ - posXofI > 0 ) {
                            if (intersect) {
                                nextNeighbours.add(nlText.item(j));
                                System.out.println("zugefügt: \t" + ((SVGOMTextElement) nlText.item(j)).getTextContent());
                            }
                        } //  if ( i != j && nlText.item(j) != null)
                    }// For j
                    System.out.println("...nextNeighbours.size()...\t" + nextNeighbours.size()
                            + "\t..." + nextNeighbour);
                    double distNextNeighbour = 5;
                    for (Node aNode : nextNeighbours) {
                        gntxtElem
                                = ctx.getGraphicsNode(aNode);
                        double dist2 = Math.sqrt((gnElement.getBounds().getCenterX()
                                - gntxtElem.getBounds().getCenterX()) * Math.cos(angle)
                                * (gnElement.getBounds().getCenterX()
                                - gntxtElem.getBounds().getCenterX()) * Math.cos(angle)
                                + (gnElement.getBounds().getCenterY()
                                - gntxtElem.getBounds().getCenterY()) * Math.sin(angle)
                                * (gnElement.getBounds().getCenterY()
                                - gntxtElem.getBounds().getCenterY()) * Math.sin(angle));

//                    System.err.println("" + nlText.item(i).getTextContent() 
//                            + "\t...bla: --> \t" + aNode.getTextContent()
//                            + "\tdist:\t" + dist );
                        if (distNextNeighbour > dist2) {
                            distNextNeighbour = dist2;
                            nextNeighbour = aNode;
                        }
                    }
                    if (nextNeighbour != null) {
                        AffineTransform aft = gnElement.getInverseTransform();
                        if ( gntxtElem.getTransformedBounds(aft).getX() 
                                - gnElement.getTransformedBounds(aft).getX() 
                                > 0) {
                            ((SVGOMTextElement) nlText.item(i)).setTextContent(
                                    ((SVGOMTextElement) nlText.item(i)).getTextContent()
                                    + ((SVGOMTextElement) nextNeighbour).getTextContent());
                            System.out.println("..->"
                                    + ((SVGOMTextElement) nlText.item(i)).getTextContent());
                            nextNeighbour.getParentNode().removeChild(nextNeighbour);
                            nextNeighbour = null;
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
                        } else {
                            ((SVGOMTextElement) nextNeighbour).setTextContent(
                                    ((SVGOMTextElement) nextNeighbour).getTextContent()
                                    + ((SVGOMTextElement) nlText.item(i)).getTextContent());
                            System.out.println("..->"
                                    + ((SVGOMTextElement) nextNeighbour).getTextContent());
                            nlText.item(i).getParentNode().removeChild(nlText.item(i));
                            if (angle != 0) {
                                ((SVGOMTextElement) nextNeighbour).setAttributeNS(svgNS, "transform", "rotate("
                                        + angle
                                        + ", " + gnElement.getBounds().getCenterX()
                                        + ", " + gnElement.getBounds().getCenterY() + ")");
                            }
                            nextNeighbour = null;
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
                        }
                    } // if ( nextNeighbour != null)
                } // if (nlText ..)
        } // for i
    }
    
}
