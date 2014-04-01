/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Combiner;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
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
public class Lettercombiner_Rotate {

    public static void CombineLetters2Words(
            JFrame jFrame,
            SVGDocument doc,
            SVGOMSVGElement myRootSVGElement,
            BridgeContext ctx,            
            GraphicsNode gnElement,
            GraphicsNode gntxtElem,
            NodeList nlText,
            String svgNS,
            int orgMinLength,
            int addMinLength
    ) throws DOMException {
        long memUsage = Runtime.getRuntime().totalMemory();
        double dist = 0.75;
        // Buchstaben zu Wörtern zusammenfassen
        for (int i = nlText.getLength() - 1; i >= 0; i--) {
            SVGOMTextElement nlElem = (SVGOMTextElement) nlText.item(i);
            if (nlElem.getTextContent().length() < orgMinLength) {
                String attrRotate = nlElem.getAttributeNS(svgNS, "transform");
                Double angle = 0.0;
                if (!attrRotate.isEmpty()) {
                    attrRotate = attrRotate.substring(7, attrRotate.indexOf(","));
                    angle = Double.parseDouble(attrRotate);
                }
                if (angle != 0.0) {
                    Node nextNeighbourBehind = null;
                    Node nextNeighbourBefore = null;
                    ArrayList<Node> nextNeighbours = new ArrayList<>();
//                 Double distNextNeighbour = Double.MAX_VALUE;
                    gnElement
                            = ctx.getGraphicsNode(nlText.item(i));
                    
                    System.err.println("new Element: \t"
                            + nlElem.getTextContent() + "\tAngle:\t" + angle);
                    double rectXAfter = gnElement.getBounds().getCenterX();
                    double rectYAfter = gnElement.getBounds().getCenterY();
                    double rectXBefore = gnElement.getBounds().getCenterX();
                    double rectYBefore = gnElement.getBounds().getCenterY();
                    AffineTransform aft = new AffineTransform();
                    aft.translate(gnElement.getBounds().getWidth() / 2, gnElement.getBounds().getHeight() / 2);
                    Point2D pd1;
                    pd1 = new Point2D.Double();
                    Point2D pd2;
                    pd2 = new Point2D.Double();
                    aft.transform(pd1, pd2);
                    aft.setToIdentity();
                    aft.rotate(Math.toRadians(angle - 45));
                    aft.transform(pd2, pd1);
                    rectXAfter = rectXAfter + pd1.getX() - dist;
                    rectYAfter = rectYAfter + pd1.getY() - dist;
                    Rectangle2D rdAfter = new Rectangle2D.Double(rectXAfter, rectYAfter, 2 * dist, 2 * dist);
                    
                    rectXBefore = rectXBefore - pd1.getX() - dist;
                    rectYBefore = rectYBefore - pd1.getY() - dist;
                    Rectangle2D rdBefore = new Rectangle2D.Double(rectXAfter, rectYAfter, 2 * dist, 2 * dist);
                    
//                    // FOR DEBUGGING
//                    nlElem.setAttributeNS(null, "fill", "blue");
//                    SVGOMRectElement rectElemAfter = (SVGOMRectElement) doc.createElementNS(svgNS, "rect");
//                    rectElemAfter.setAttributeNS(null, "fill", "red");
//                    rectElemAfter.setAttributeNS(null, "x", String.valueOf(rectXAfter)); // 
//                    rectElemAfter.setAttributeNS(null, "y", String.valueOf(rectYAfter)); // 
//                    rectElemAfter.setAttributeNS(null, "width", String.valueOf(2 * dist));
//                    rectElemAfter.setAttributeNS(null, "height", String.valueOf(2 * dist));
//                    rectElemAfter.setAttributeNS(null, "opacity", "0.8");
//                    myRootSVGElement.appendChild(rectElemAfter);
//                    
//                    SVGOMRectElement rectElemBefore = (SVGOMRectElement) doc.createElementNS(svgNS, "rect");
//                    rectElemBefore.setAttributeNS(null, "fill", "purple");
//                    rectElemBefore.setAttributeNS(null, "x", String.valueOf(rectXBefore)); // 
//                    rectElemBefore.setAttributeNS(null, "y", String.valueOf(rectYBefore)); // 
//                    rectElemBefore.setAttributeNS(null, "width", String.valueOf(2 * dist));
//                    rectElemBefore.setAttributeNS(null, "height", String.valueOf(2 * dist));
//                    rectElemBefore.setAttributeNS(null, "opacity", "0.8");
//                    myRootSVGElement.appendChild(rectElemBefore);
//                    ExtraktLettersFromPath.saveSVG(false);
//                    myRootSVGElement.removeChild(rectElemAfter);
//                    myRootSVGElement.removeChild(rectElemBefore);
//                    // END FOR DEBUGGING

//                System.out.println("rectx: \t" + (rectX + dist) + "\t" + (rectY + dist / 2) );
//                System.err.println("angle\t" + angle);
                    for (int j = nlText.getLength() - 1; j >= 0; j--) {
                        if (i != j && ((SVGOMTextElement) nlText.item(j)).getTextContent().length() < addMinLength) {
                            gntxtElem
                                    = ctx.getGraphicsNode(nlText.item(j));
                            boolean intersect
                                    = gntxtElem.getSensitiveBounds()
                                    .intersects(rdBefore)
                                    || gntxtElem.getSensitiveBounds()
                                    .intersects(rdAfter);
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
                    System.out.println("...nextNeighbours.size()...\t" + nextNeighbours.size() );
                    double distNextNeighbourAfter = 5;
                    double distNextNeighbourBefore = -5;
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
                        if (distNextNeighbourAfter > dist2 ) {
                            distNextNeighbourAfter = dist2;
                            nextNeighbourBehind = aNode;
                        } else  if (distNextNeighbourBefore < dist2) {
                            distNextNeighbourBefore = dist2;
                            nextNeighbourBefore = aNode;
                        }
                    }
//                    if (nextNeighbour != null) {
//                        AffineTransform aft = gnElement.getInverseTransform();
//                        if ( gntxtElem.getTransformedBounds(aft).getX() 
//                                - gnElement.getTransformedBounds(aft).getX() 
//                                > 0) {
//                            ((SVGOMTextElement) nlText.item(i)).setTextContent(
//                                    ((SVGOMTextElement) nlText.item(i)).getTextContent()
//                                    + ((SVGOMTextElement) nextNeighbour).getTextContent());
//                            System.out.println("..->"
//                                    + ((SVGOMTextElement) nlText.item(i)).getTextContent());
//                            nextNeighbour.getParentNode().removeChild(nextNeighbour);
//                            nextNeighbour = null;
//                            nlText = myRootSVGElement.getElementsByTagName("text");
//                            System.gc();
//                            CombineLetters2Words(
//                                jFrame,
//                                doc,
//                                myRootSVGElement,
//                                ctx,            
//                                gnElement,
//                                gntxtElem,
//                                nlText,
//                                svgNS,
//                                orgMinLength,
//                                addMinLength
//                            );
//                            return;
//                        } else {
//                            ((SVGOMTextElement) nextNeighbour).setTextContent(
//                                    ((SVGOMTextElement) nextNeighbour).getTextContent()
//                                    + ((SVGOMTextElement) nlText.item(i)).getTextContent());
//                            System.out.println("..->"
//                                    + ((SVGOMTextElement) nextNeighbour).getTextContent());
//                            nlText.item(i).getParentNode().removeChild(nlText.item(i));
//                            if (angle != 0) {
//                                ((SVGOMTextElement) nextNeighbour).setAttributeNS(svgNS, "transform", "rotate("
//                                        + angle
//                                        + ", " + gnElement.getBounds().getCenterX()
//                                        + ", " + gnElement.getBounds().getCenterY() + ")");
//                            }
//                            nextNeighbour = null;
//                            nlText = myRootSVGElement.getElementsByTagName("text");
//                            System.gc();
//                            CombineLetters2Words(
//                                jFrame,
//                                doc,
//                                myRootSVGElement,
//                                ctx,            
//                                gnElement,
//                                gntxtElem,
//                                nlText,
//                                svgNS,
//                                orgMinLength,
//                                addMinLength
//                            );
//                            return;
//                        }
//                    } // if ( nextNeighbour != null)
                } // if (nlText ..)
            }
        } // for i
    }
    
}
