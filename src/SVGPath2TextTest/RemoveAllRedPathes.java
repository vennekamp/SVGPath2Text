/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SVGPath2TextTest;

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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.svg.SVGDocument;

public class RemoveAllRedPathes {

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
    private static NodeList nlPath;

    private static String uri;
    /**
     * @param args
     */
    public static void main(String[] args) throws IOException {

        // Create a new JSVGCanvas.
        JSVGCanvas canvas;
        canvas = new JSVGCanvas();

        // A frame for the canvas to live in
        jFrame = new JFrame("uri");
        jFrame.setSize(800, 400);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setLayout(new GridLayout());
        jFrame.getContentPane().add(canvas);

        jFrame.setVisible(true);
        uri = new File("C:Temp").toURI().toString();
//                (ExtraktLettersFromPath.getUri()).toURL().toString();
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

        ///////////////////////////////////////////////////////////////////////////
        svgNS = myRootSVGElement.SVG_NAMESPACE_URI;
        // Make the text look nice.
//        svgRoot.setAttributeNS(null, "text-rendering", "geometricPrecision");

        // Remove the xml-stylesheet PI.
        for (Node n = myRootSVGElement.getPreviousSibling();
                n != null;
                n = n.getPreviousSibling()) {
            if (n.getNodeType() == Node.PROCESSING_INSTRUCTION_NODE) {
                doc.removeChild(n);
                break;
            }
        }

//        // Remove the Batik sample mark 'use' element.
//        for (Node n = svgRoot.getLastChild();
//                n != null;
//                n = n.getPreviousSibling()) {
//            if (n.getNodeType() == Node.ELEMENT_NODE
//                    && n.getLocalName().equals("use")) {
//                svgRoot.removeChild(n);
//                break;
//            }
//        }
        // Display the document.
        canvas.setDocumentState(JSVGCanvas.ALWAYS_DYNAMIC);
        canvas.setDocument(doc);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(RemoveAllRedPathes.class.getName()).log(Level.SEVERE, null, ex);
        }


        nlPath = myRootSVGElement.getElementsByTagName("path");
        
        for (int i = nlPath.getLength() - 1; i >= 0; i--) {
            SVGOMPathElement nlElem = (SVGOMPathElement) nlPath.item(i);
            System.out.print("Untersuche: "+ nlElem.getAttributeNS(null, "id"));
            String attrRotate = nlElem.getAttributeNS(null, "style");
            if ( attrRotate.equals( ExtraktLettersFromPath.getStrColor4Done()[1] )) {
                nlElem.getParentNode().removeChild(nlElem);
                System.out.println("\t...gelöscht");
            }
            else {
                System.out.println();
            }
        }
        saveSVG();
        System.exit(0);
    }
    
    
    private static void saveSVG() {
        setViewBoxPageSize(myRootSVGElement, rootGN, doc);
        OutputStream os = null;
        String strOutPutFile = uri.substring(0, uri.indexOf("neu.svg")) + "text.svg";
        try {
            os = new FileOutputStream(strOutPutFile);
//                ("C:\\Users\\Martin\\Programming\\git\\SVG2Text_Netbeans\\Daten\\test.svg");
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
}
        
        
