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
public class Lettercombiner_Rotate_0 {

	private static GraphicsNode gnElement;
	private static GraphicsNode gntxtElem;
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
        
        double interSectionRectSize = 1.0;
        // combine letters to words
        for (int i = 0; i < nlText.getLength(); i++) {
            SVGOMTextElement nlElem = (SVGOMTextElement) nlText.item(i);
            // Handle only single letters 
            if (nlElem.getTextContent().length() < 3) {
                double angle = 0.0;
                angle = getAngle(svgNS, nlElem);
                if (angle != 0.0) {
                    blabla(ctx, nlText, interSectionRectSize, nlElem, angle);
                } // if (nlText ..)
            }
        } // for i
    }

	private static void blabla(BridgeContext ctx, NodeList nlText,
			double interSectionRectSize, SVGOMTextElement nlElem,
			double angle) {
		MySVGWordCombined wordCombined = MySVGWordCombined.MySVGWordCombinedFactory(nlElem, angle);
		
		Node nextNeighbourBehind = null;
		Node nextNeighbourBefore = null;
		
		// Collection for the nextNeighbours 
		ArrayList<Node> nextNeighboursBehind = new ArrayList<Node>();
		ArrayList<Node> nextNeighboursBefore = new ArrayList<Node>();
		gnElement
		        = ctx.getGraphicsNode(nlElem);
		
		System.err.println("new Element: \t"
		        + nlElem.getTextContent() + "\tAngle:\t" + angle != null ? angle : "0" );
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
		rectXAfter = rectXAfter + pd1.getX() - interSectionRectSize;
		rectYAfter = rectYAfter + pd1.getY() - interSectionRectSize;
		Rectangle2D rdBehind = new Rectangle2D.Double(rectXAfter, rectYAfter
				, 2 * interSectionRectSize, 2 * interSectionRectSize);
		
		rectXBefore = rectXBefore - pd1.getX() - interSectionRectSize;
		rectYBefore = rectYBefore - pd1.getY() - interSectionRectSize;
		Rectangle2D rdBefore = new Rectangle2D.Double(rectXBefore, rectYBefore
				, 2 * interSectionRectSize, 2 * interSectionRectSize);
		
		// FOR DEBUGGING
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
		// END FOR DEBUGGING

//                System.out.println("rectx: \t" + (rectX + dist) + "\t" + (rectY + dist / 2) );
//                System.err.println("angle\t" + angle);
		for (int j = 0; j < nlText.getLength(); j++) {
			// try to combine other single letters (only single letters)
		    if ( (SVGOMTextElement) nlText.item(j) != nlElem && ((SVGOMTextElement) nlText.item(j)).getTextContent().length() < 3) {
		        gntxtElem
		                = ctx.getGraphicsNode(nlText.item(j));
//                        System.out.println("Vergeich:\t" + gntxtElem.getSensitiveBounds().getX() 
//                                + "\t" + gntxtElem.getSensitiveBounds().getY() 
//                                + "\t" + gntxtElem.getSensitiveBounds().getWidth()
//                                + "\t" + gntxtElem.getSensitiveBounds().getHeight());
//                        if ( Math.abs(posYofI - posYofJ) < 2
//                                && Math.abs(posXofI - posXofJ)  < distNextNeighbour 
//                                && posXofJ - posXofI > 0 ) {
		        if (gntxtElem.getSensitiveBounds()
		                .intersects(rdBehind)) {
		            nextNeighboursBehind.add(nlText.item(j));
		            System.out.println("nextNeighboursBehind zugefügt: \t" + ((SVGOMTextElement) nlText.item(j)).getTextContent());
		        }
		        if (gntxtElem.getSensitiveBounds()
		                .intersects(rdBefore)) {
		            nextNeighboursBefore.add(nlText.item(j));
		            System.out.println("nextNeighboursBefore zugefügt: \t" + ((SVGOMTextElement) nlText.item(j)).getTextContent());
		        }
		    } //  if ( i != j && nlText.item(j) != null)
		}// For j
		// All next neighbours are found: try to find the nearest
		System.out.println("...nextNeighboursBehind.size()...\t" + nextNeighboursBehind.size() );
		
		nextNeighbourBehind = getSingleNextNeighbour(ctx,
				gnElement, angle,
				nextNeighboursBehind );
		if ( nextNeighbourBehind != null ) {
			if ( wordCombined.add2MyText((SVGOMTextElement)nextNeighbourBehind, true) ) {
				blabla(ctx, nlText, interSectionRectSize, (SVGOMTextElement) nextNeighbourBehind, angle);
			}
		}

		System.out.println("...nextNeighboursBefore.size()...\t" + nextNeighboursBefore.size() );

		nextNeighbourBefore = getSingleNextNeighbour(ctx,
				gnElement, angle,
				nextNeighboursBefore );
		if ( nextNeighbourBefore != null ) {
			if ( wordCombined.add2MyText((SVGOMTextElement)nextNeighbourBefore, false) ) {
				blabla(ctx, nlText, interSectionRectSize, (SVGOMTextElement) nextNeighbourBefore, angle);
			}
		}
	}

	private static Double getAngle(String svgNS, SVGOMTextElement nlElem) {
		Double angle = 0.0;
		String attrRotate = nlElem.getAttributeNS(svgNS, "transform");
		if (!attrRotate.isEmpty()) {
		    attrRotate = attrRotate.substring(7, attrRotate.indexOf(","));
		    angle = Double.parseDouble(attrRotate);
		}
		return angle;
	}

	private static Node getSingleNextNeighbour(BridgeContext ctx
			, GraphicsNode gnElement, Double angle
			, ArrayList<Node> nextNeighboursList) {
        double distNextNeighbour = 5;
		GraphicsNode gntxtElem;
		Node nextNeighbour = null;
		for (Node aNode : nextNeighboursList) {
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

		    if (distNextNeighbour > dist2 ) {
		        distNextNeighbour = dist2;
		        nextNeighbour = aNode;
		    } else  if (distNextNeighbour < dist2) {
		    	distNextNeighbour = dist2;
		        nextNeighbour = aNode;
		    }
		}
		return nextNeighbour;
	}
    
}
