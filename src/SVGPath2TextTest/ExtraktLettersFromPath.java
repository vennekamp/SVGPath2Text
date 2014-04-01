/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SVGPath2TextTest;

import Combiner.Lettercombiner_Horizontal;
import Combiner.Lettercombiner_Horizontal_2;
import Combiner.Lettercombiner_Rotate;
import Combiner.Lettercombiner_Rotate_0;
import Combiner.MySVGWordCombined;
import JavaFXPathElements.JavaFXPathElementHandler;
import JavaFXPathElements.MySVGletterPath;
import JavaFXPathElements.MySVGwordPath;
import JavaFXPathElements.PathParser_SVG2Int;
import LetterTamplates.Templateletter;
import static SVGPath2TextTest.AddViewport.setViewBoxPageSize;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.DocumentLoader;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.bridge.UserAgent;
import org.apache.batik.bridge.UserAgentAdapter;
import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.dom.svg.SVGOMPathElement;
import org.apache.batik.dom.svg.SVGOMSVGElement;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.svggen.SVGGraphics2DIOException;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.svg.SVGDocument;

public class ExtraktLettersFromPath {

    // see also http://mt4j.googlecode.com/svn-history/r320/trunk/src/org/mt4j/util/xml/svg/SVGLoader.java
    private static String svgNS;
    private static JFrame jFrame;
    private static SVGDocument doc;
    private static BridgeContext ctx;
    private static GVTBuilder builder;
    private static SVGOMSVGElement myRootSVGElement;
    private static UserAgent userAgent;
    private static DocumentLoader loader;
    private static GraphicsNode rootGN;
    private static GraphicsNode gnElement;
    private static GraphicsNode gntxtElem;
    private static NodeList nlText;
    
    private static final String[] strColor4Done 
            = {"red", "fill:#ff0000"};

    public static String[] getStrColor4Done() {
        return strColor4Done;
    }
    
    private static String uri;

    /**
     * @param args
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        URL url
            = ExtraktLettersFromPath.class.getProtectionDomain().getCodeSource().getLocation();
        
  	String jarPath = null;
        try {
                jarPath = URLDecoder.decode(url.getFile(), "UTF-8"); //Should fix it to be read correctly by the system
        } catch (UnsupportedEncodingException e) {
        }
        String folder = new File(jarPath).getParentFile().getParentFile().getPath();
        uri = new File( folder, "/data/demo_test.svg" ).toURI().toString();
        Templateletter.initPaths();
        PathParser_SVG2Int svg2Int = new PathParser_SVG2Int();
        ///////////////////////////////////////////////////////////////////////////
        String parser = XMLResourceDescriptor.getXMLParserClassName();
        SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
//            URI uri = new File(name).toURI(); // the URI of your SVG document
        doc = (SVGDocument) f.createDocument(uri);
        userAgent = new UserAgentAdapter();
        loader = new DocumentLoader(userAgent);
        ctx = new BridgeContext(userAgent, loader);
        ctx.setDynamicState(BridgeContext.DYNAMIC);
        builder = new GVTBuilder();
        rootGN = builder.build(ctx, doc);
        myRootSVGElement = (SVGOMSVGElement) doc.getDocumentElement();
        
        
        // Create a new JSVGCanvas.
        JSVGCanvas canvas;
        canvas = new JSVGCanvas();

        // A frame for the canvas to live in
        jFrame = new JFrame("uri");
        jFrame.setSize(800, 800);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setLayout(new GridLayout());
        jFrame.getContentPane().add(canvas);

        jFrame.setVisible(true);
        
        svg2Int.visit(myRootSVGElement, 0, new JavaFXPathElementHandler());

        ///////////////////////////////////////////////////////////////////////////
        svgNS = SVGOMSVGElement.SVG_NAMESPACE_URI;
        // Make the text look nice.
        myRootSVGElement.setAttributeNS(null, "text-rendering", "geometricPrecision");

        // Remove the xml-stylesheet PI.
        for (Node n = myRootSVGElement.getPreviousSibling();
                n != null;
                n = n.getPreviousSibling()) {
            if (n.getNodeType() == Node.PROCESSING_INSTRUCTION_NODE) {
                doc.removeChild(n);
                break;
            }
        }

        // Remove the Batik sample mark 'use' element.
        for (Node n = myRootSVGElement.getLastChild();
                n != null;
                n = n.getPreviousSibling()) {
            if (n.getNodeType() == Node.ELEMENT_NODE
                    && n.getLocalName().equals("use")) {
                myRootSVGElement.removeChild(n);
                break;
            }
        }
        // Display the document.
        canvas.setDocumentState(JSVGCanvas.ALWAYS_DYNAMIC);
        canvas.setDocument(doc);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(ExtraktLettersFromPath.class.getName()).log(Level.SEVERE, null, ex);
        }

        MySVGletterPath.getAllSVGletterByIDs().stream().forEach((aMySVGletterPath) -> {
            MySVGwordPath.add2MyText(aMySVGletterPath.getId(), aMySVGletterPath);
        });
        // Pathes durch Buchstaben ersetzen / ergänzen
        ExtractedLetterById(ctx, builder);

        
        nlText = myRootSVGElement.getElementsByTagName("text");
        
        Lettercombiner_Rotate_0.CombineLetters2Words(
            jFrame,
            doc,
            myRootSVGElement,
            ctx,            
            gnElement,
            gntxtElem,
            nlText,
            svgNS
        );
        
        MySVGWordCombined.getAllWordsCombined().stream().map((mySVGWordCombined) -> {
            System.out.println("Text: \t\t" + mySVGWordCombined.getMyTextContent() );
            return mySVGWordCombined;
        }).forEach((mySVGWordCombined) -> {
            mySVGWordCombined.createCombinedWord(svgNS, ctx);
        });
        
        nlText = myRootSVGElement.getElementsByTagName("text");
        Lettercombiner_Rotate.CombineLetters2Words(
            jFrame,
            doc,
            myRootSVGElement,
            ctx,            
            gnElement,
            gntxtElem,
            nlText,
            svgNS,
            30,
            30
        );
        
        nlText = myRootSVGElement.getElementsByTagName("text");
        Lettercombiner_Horizontal.CombineLetters2Words(
            jFrame,
            doc,
            myRootSVGElement,
            ctx,            
            gnElement,
            gntxtElem,
            nlText,
            svgNS
        );
        
        nlText = myRootSVGElement.getElementsByTagName("text");
        Lettercombiner_Horizontal_2.CombineLetters2Words(
            jFrame,
            doc,
            myRootSVGElement,
            ctx,            
            gnElement,
            gntxtElem,
            nlText,
            svgNS
        );  

        saveSVG(true);
        System.out.println("Number of recordnized letters: "
                + MySVGwordPath.getAllWordsByIDs().size());
        jFrame.dispose();

    }

    public static void saveSVG( boolean setViewBox ) {
        if (setViewBox) setViewBoxPageSize(myRootSVGElement, rootGN, doc);
        OutputStream os = null;
        String out = uri.substring(6, uri.indexOf(".svg")) + "_path_text.svg";
        try {
//            os = new FileOutputStream
//         ("D:\\Users\\Martin\\Programming\\git\\SVGPath2Text\\Daten\\test.svg");
//                ("C:\\Users\\Martin\\Programming\\git\\SVG2Text_Netbeans\\Daten\\test.svg");
            
            os = new FileOutputStream(out);
            Writer w = new OutputStreamWriter(os, "UTF-8");
            SVGGraphics2D svgGenerator = new SVGGraphics2D(doc);
            svgGenerator.stream(myRootSVGElement, w);
            w.close();
            os.close();
            svgGenerator.dispose();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ExtraktLettersFromPath.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ExtraktLettersFromPath.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SVGGraphics2DIOException ex) {
            Logger.getLogger(ExtraktLettersFromPath.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ExtraktLettersFromPath.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                os.close();
            } catch (IOException ex) {
                Logger.getLogger(ExtraktLettersFromPath.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private static void ExtractedLetterById(BridgeContext ctx, GVTBuilder builder) {
        String oldID = "";
        for (Map.Entry<String, MySVGwordPath> aMySVGwordPath
                : MySVGwordPath.getAllWordsByIDs().entrySet()) {

            if (!oldID.equals(aMySVGwordPath.getKey())) {
                System.out.print(aMySVGwordPath.getKey() + "\t\t");
                oldID = aMySVGwordPath.getKey();

                System.out.print(aMySVGwordPath.getValue().getMyTextContent());
            }
            SVGOMPathElement svgElement
                    = (SVGOMPathElement) myRootSVGElement.getElementById(aMySVGwordPath.getKey());
            if (svgElement != null) {
                gnElement = ctx.getGraphicsNode(svgElement);
                // svgElement.setAttributeNS(null, "fill",  strColor4Done[0]);
                // System.err.println(svgElement.getNodeName());
                Element txtElem = doc.createElementNS(svgNS, "text");
                //  xml:space="preserve" font-family="Comic Sans MS" fill="#008000" 
                // font-weight="normal" font-style="normal" font-size="7px"
                txtElem.setAttributeNS(null, "xml:space", "preserve");
                txtElem.setAttributeNS(null, "font-family", "Comic Sans MS");
                txtElem.setAttributeNS(null, "font-size", "7px");

                txtElem.setTextContent(aMySVGwordPath.getValue().getMyTextContent());
                String attrFill = svgElement.getAttributeNS(null, "fill");
                if ( !attrFill.isEmpty() ) {
                    txtElem.setAttributeNS(null, "fill", attrFill);
                }
                txtElem.setAttributeNS(null, "id", aMySVGwordPath.getKey() + "TEXT");
                svgElement.setAttributeNS(null, "fill", strColor4Done[0]);
                txtElem.setAttributeNS(null, "x", String.valueOf(gnElement.getBounds().getX()));
                txtElem.setAttributeNS(null, "y", String.valueOf(gnElement.getBounds().getY()
                        + gnElement.getBounds().getHeight()));
                gntxtElem = builder.build(ctx, txtElem);
                // transform="rotate(30 20,40)"

                double deltaX = gnElement.getBounds().getX() - gntxtElem.getBounds().getX();
                double deltaY = gnElement.getBounds().getY() - gntxtElem.getBounds().getY();
//                System.out.print("\tOrgHeight: \t" + gnElement.getBounds().getHeight() );
//                System.out.print("\tOrgWidth: \t" + gnElement.getBounds().getWidth());
                txtElem.setAttributeNS(null, "x", String.valueOf(gnElement.getBounds().getX() + deltaX));
                txtElem.setAttributeNS(null, "y", String.valueOf(gnElement.getBounds().getY()
                        + gnElement.getBounds().getHeight() + deltaY));
//                System.out.print("\tNeuHeight: \t" + gntxtElem.getBounds().getHeight() );
//                System.out.println("\tNeuWidth: \t" +gntxtElem.getBounds().getWidth());
                // Leerzeichen einfügen
                if (gnElement.getBounds().getWidth() - gntxtElem.getBounds().getWidth() > 2) {
                    aMySVGwordPath.getValue().addMyAdditionalSpace();
                    txtElem.setTextContent(aMySVGwordPath.getValue().getMyTextContent());
                }
                // Element in den DOM einfügen
                myRootSVGElement.appendChild(txtElem);
//                svgElement.getParentNode().removeChild(svgElement);
//                svgRoot.appendChild(svgElement);
                // Rotation einfügen
                System.out.println("\tMyAngle(): \t"
                        + aMySVGwordPath.getValue().getMyAngle()
                        + "\tMyDistance(): \t"
                        + aMySVGwordPath.getValue().getMyDistance());
                
                if (0 != aMySVGwordPath.getValue().getMyAngle()) {
                    txtElem.setAttributeNS(svgNS, "transform", "rotate("
                            + aMySVGwordPath.getValue().getMyAngle()
                            + ", " + gnElement.getBounds().getCenterX()
                            + ", " + gnElement.getBounds().getCenterY() + ")");
//                    System.err.println("...gnElement");
//                    getMyBBoxManual(gnElement, gnElement.getTransform());
//                    System.err.println("...gntxtElem");
//                    getMyBBoxManual(gntxtElem, gntxtElem.getTransform());
                }
//                System.out.println();
//                svgElement.getParentNode().removeChild(svgElement);
            }
        }
    }

//    private static void getAngleFromBBox(BridgeContext ctx1, Element txtElem, String svgNS) throws DOMException {
////      ohne Drehung nochmals vielleicht drehen..
//        gntxtElem = ctx1.getGraphicsNode(txtElem);
//        // Manually calculate the bounds
//        Double[] gnElement1MyBBox = getMyBBox(gnElement, gnElement.getTransform());
//        Double[] gnElement2MyBBox = getMyBBox(gntxtElem, gntxtElem.getTransform());
//        double dblDistX;
//        double dblDistY;
//        double dblDistSum = Double.MAX_VALUE;
//        double optAngle = 0.0;
//        for (double dblWinkel = -90.0; dblWinkel <= 90.0; dblWinkel = dblWinkel + 0.01) {
//            dblDistX = Math.abs(gnElement1MyBBox[0] - gnElement2MyBBox[0]);
//            dblDistY = Math.abs(gnElement1MyBBox[1] - gnElement2MyBBox[1]);
//            if (dblDistSum > dblDistX + dblDistY) {
//                optAngle = dblWinkel;
//                dblDistSum = dblDistX + dblDistY;
//            }
//            AffineTransform aft = gnElement.getTransform();
//            aft.setToIdentity();
//            aft.rotate(Math.toRadians(dblWinkel), gntxtElem.getGeometryBounds().getCenterX(), gntxtElem.getGeometryBounds().getCenterY());
//            gntxtElem.setTransform(aft);
////                        System.err.println("gedreht..\t"
////                                + (int)Math.toDegrees( Math.atan2(aft.getShearY(), aft.getScaleY()) )
////                                + "\tDeltaX:\t" + dblDistX
////                                + "\tDeltaY:\t" + dblDistY
////                                + "\tSumme:\t"  + dblDistSum
////                                + "\toptAngle:\t"  + optAngle );
//            gnElement2MyBBox = getMyBBox(gntxtElem, aft);
//        }
//        if (optAngle > 3) {
//            System.err.println("gedreht..\t"
//                    + "\toptAngle:\t" + optAngle
//                    + "\tSumme:\t" + dblDistSum);
//            txtElem.setAttributeNS(svgNS, "transform", "rotate("
//                    + (-optAngle)
//                    + ", " + gnElement.getBounds().getCenterX()
//                    + ", " + gnElement.getBounds().getCenterY() + ")");
//        }
//    }
//
//    // can also be based on: http://stackoverflow.com/questions/10610355/batik-calculating-bounds-of-cubic-spline
//    private static Double[] getMyBBox(GraphicsNode sn, AffineTransform aft) {
//        // Manually calculate the bounds
//        Double[] rtnDoubles = new Double[2];
//        rtnDoubles[0] = sn.getTransformedGeometryBounds(aft).getHeight();
//        rtnDoubles[1] = sn.getTransformedGeometryBounds(aft).getWidth();
////        System.out.println("height:" + rtnDoubles[0]);
//        return rtnDoubles;// new Double[] {minX, minY, maxX, maxY};
//    }
//
//    private static Double[] getMyBBoxManual(GraphicsNode sn, AffineTransform aft) {
//        // Manually calculate the bounds
//        double[] vals = new double[7];
//        Point2D point2D = new Point2D.Double();
//        double minX = Double.MAX_VALUE;
//        double maxX = 0;
//        double minY = Double.MAX_VALUE;
//        double maxY = 0;
//        // Get a path iterator iterating to a certain level of accuracy
//        PathIterator pi = sn.getOutline().getPathIterator(null, 0.01);
//        while (!pi.isDone()) {
//            pi.currentSegment(vals);
//            aft.deltaTransform(new Point2D.Double(vals[0], vals[1]), point2D);
//            if (point2D.getX() < minX) { minX = point2D.getX(); }
//            if (point2D.getX() > maxX) { maxX = point2D.getX(); }
//            if (point2D.getY() < minY) { minY = point2D.getY(); }
//            if (point2D.getY() > maxY) { maxY = point2D.getY(); }
//            System.err.print("--> X\t" + point2D.getX());
//            System.err.println("\t--> Y\t" + point2D.getY());
//            pi.next();
//        }
//        return new Double[]{minX, minY, maxX - minX,  maxY - minY};
//    }
}
