package Combiner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;

import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.dom.svg.SVGOMTextElement;
import org.apache.batik.gvt.GraphicsNode;

import javafx.scene.shape.Path;

public class MySVGWordCombined extends Path {

	// Contains all instances of this class, i.e. all words already assembled by letters 
    private static final ArrayList<MySVGWordCombined> allWordsCombined
            = new ArrayList<MySVGWordCombined>();
	// Contains all instances of the letter class which have been combined to a word
    private static final ArrayList<SVGOMTextElement> allSVGletterPaths 
            = new ArrayList<SVGOMTextElement>();
    // holds the string of all combined letters.
    private String myTextContent;
    // 	holds the letter object of all letters  combined to THIS word.
    private final LinkedList<SVGOMTextElement> mySVGletterPaths;
    // all angles of the letters combined to this word
    private LinkedList<Double> myAngles;

    public MySVGWordCombined(SVGOMTextElement mySVGletterPath, double angle) {
        super();
        myTextContent = String.valueOf( mySVGletterPath.getTextContent() );
        mySVGletterPaths = new LinkedList<SVGOMTextElement>();
        allSVGletterPaths.add(mySVGletterPath);
        mySVGletterPaths.add(mySVGletterPath);
        myAngles = new LinkedList<Double>();
        if ( angle != 0) {
            myAngles.add( angle );
        }
        allWordsCombined.add(this);
    }
    
    public void createCombinedWord(String svgNS, BridgeContext ctx) {
    	
    	SVGOMTextElement firstLetter = this.mySVGletterPaths.getFirst();
    	firstLetter.setTextContent(myTextContent);
    	GraphicsNode gnElement = ctx.getGraphicsNode(firstLetter);
    	firstLetter.setAttributeNS(svgNS, "transform", "rotate("
                + this.getMyAngle()
                + ", " + gnElement.getBounds().getCenterX()
                + ", " + gnElement.getBounds().getCenterY() + ")");
    	for ( int i = this.mySVGletterPaths.size() - 1; i > 0; i -- ) {
    		SVGOMTextElement rm = this.mySVGletterPaths.get(i);
    		System.out.println("lösche:... " + rm.getTextContent());
    		rm.getParentNode().removeChild(rm);
    		this.mySVGletterPaths.remove(i);
     	}
    	
    	
    }
    
    
    
    public static MySVGWordCombined MySVGWordCombinedFactory(SVGOMTextElement mySVGletterPath, double angle){
    	for ( MySVGWordCombined aWordCombined : allWordsCombined) {
    		if (aWordCombined.mySVGletterPaths.contains(mySVGletterPath) ) {
    			return aWordCombined;
    		}
    	}
    	return new MySVGWordCombined(mySVGletterPath, angle); 
    }
    
    public String getMyTextContent() {
        return myTextContent;
    }

    public double getAngle() {
        for (int i = myAngles.size() - 1; i >= 0; i--) {
            if ( Math.abs( myAngles.get(i) ) < 1 )
                myAngles.remove(i);
        }
        double rtnDbl = 0;
        for (int i = 0; i < myAngles.size(); i++) {
            if ( myAngles.get(i) < 1 )
            rtnDbl += myAngles.get(i);
        }
        if (myAngles.size() > 0) {
            return rtnDbl / myAngles.size();
        }
        return 0;
    }

     
    
    public boolean add2MyText( SVGOMTextElement mySVGletterPath, boolean add2End ) {
        if (allSVGletterPaths.contains( mySVGletterPath ) ) {
            return false;
        }
        if ( add2End ) {
            myTextContent += mySVGletterPath.getTextContent();
            mySVGletterPaths.addLast(mySVGletterPath);
            if ( getAngle( mySVGletterPath ) != 0) {
                myAngles.add( getAngle( mySVGletterPath));
            }
        }
        else {
            myTextContent = String.valueOf( mySVGletterPath.getTextContent()  ) +  myTextContent;
            mySVGletterPaths.addFirst(mySVGletterPath);
            if ( getAngle( mySVGletterPath ) != 0) {
                myAngles.add( getAngle( mySVGletterPath ));
            }
        }
        allSVGletterPaths.add(mySVGletterPath);
        System.err.println("  ---> " + myTextContent);
        return true;
    }

    
    public double getMyAngle() {
        return getMedian( this.myAngles );
    }


	private static Double getAngle(SVGOMTextElement nlElem) {
		Double angle = 0.0;
		String attrRotate = nlElem.getAttributeNS(null, "transform");
		if (!attrRotate.isEmpty()) {
		    attrRotate = attrRotate.substring(7, attrRotate.indexOf(","));
		    angle = Double.parseDouble(attrRotate);
		}
		return angle;
	}
	
    public static double getMedian(LinkedList<Double> numArray) {
//        System.err.print("WinkelArray: " );
//        
//        for ( Double d : numArray)
//            System.err.println(" --> \t" + d);
        if (numArray.size() == 0) return 0;
        Double[] sortedArray;
        sortedArray = numArray.toArray(new Double[ numArray.size() ] );
        Arrays.sort(sortedArray);
        double median;
        if (sortedArray.length % 2 == 0) {
            median = ((double) sortedArray[sortedArray.length / 2] 
                    + (double) sortedArray[sortedArray.length / 2 - 1]) / 2;
        } else {
            median = (double) sortedArray[sortedArray.length / 2];
        }
        return median;
    }

	public LinkedList<Double> getMyAngles() {
		return myAngles;
	}

	public void setMyAngles(LinkedList<Double> myAngles) {
		this.myAngles = myAngles;
	}

	public static ArrayList<MySVGWordCombined> getAllWordsCombined() {
		return allWordsCombined;
	}

	public static ArrayList<SVGOMTextElement> getAllSVGletterPaths() {
		return allSVGletterPaths;
	}

	public LinkedList<SVGOMTextElement> getMySVGletterPaths() {
		return mySVGletterPaths;
	}

	public void setMyTextContent(String myTextContent) {
		this.myTextContent = myTextContent;
	}

}
