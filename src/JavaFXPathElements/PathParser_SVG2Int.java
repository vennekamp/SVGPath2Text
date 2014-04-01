package JavaFXPathElements;

import JavaFXPathElements.JavaFXPathElementHandler;
import org.apache.batik.dom.svg.SVGOMPathElement;
import org.apache.batik.parser.PathParser;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author twak
 */
public final class PathParser_SVG2Int {
    
    public void visit(Node node, int depth, JavaFXPathElementHandler pa) {
        NodeList nl = node.getChildNodes();

        for (int i = 0; i < nl.getLength(); ++i) {
            Node elt = (Node) nl.item(i);
            
            if (elt instanceof SVGOMPathElement) {
                NamedNodeMap nnm = elt.getAttributes();
                PathParser pp = new PathParser();
                pp.setPathHandler(pa);
                pa.setId( nnm.getNamedItem("id").getNodeValue() );
                pp.parse(nnm.getNamedItem("d").getTextContent());
            }
            visit(elt, depth + 1, pa);
        }
    }

}















