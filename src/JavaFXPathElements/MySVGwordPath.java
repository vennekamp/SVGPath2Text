package JavaFXPathElements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import javafx.scene.shape.Path;

public class MySVGwordPath extends Path {

    private String myTextContent = "";
    private static final HashMap<String, MySVGwordPath> allWordsByID
            = new HashMap<String, MySVGwordPath>();
    private ArrayList<Double> myAngles;
    private ArrayList<Double> myDistances;

    public MySVGwordPath(String strID) {
        super();
        this.setId(strID);
    }

    public static HashMap<String, MySVGwordPath> getAllWordsByIDs() {
        return allWordsByID;
    }
    
    public String getMyTextContent() {
        return myTextContent;
    }

    public void setMyTextContent(String myTextContent) {
        this.myTextContent = myTextContent;
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

    public static void add2MyText(String strId, MySVGletterPath aMySVGletterPath) {
        char charOfElement = aMySVGletterPath.getMyCharacter();
        MySVGwordPath aMySVGwordPath;
        if (allWordsByID.get(strId) == null) {
            aMySVGwordPath = new MySVGwordPath(strId);
            aMySVGwordPath.myTextContent = Character.toString(charOfElement);
            aMySVGwordPath.myAngles = new ArrayList<Double>();
            aMySVGwordPath.myDistances = new ArrayList<Double>();
            if ( aMySVGletterPath.getMyDistance() > 1.0 ) {
                aMySVGwordPath.myAngles.add( aMySVGletterPath.getMyAngle() ); 
                aMySVGwordPath.myDistances.add( aMySVGletterPath.getMyDistance() ); 
            }
        } else {
            aMySVGwordPath = allWordsByID.get(strId);
            aMySVGwordPath.setMyTextContent(aMySVGwordPath.getMyTextContent() + charOfElement);
            if ( aMySVGletterPath.getMyDistance() > 1.0 ) {
                aMySVGwordPath.myAngles.add( aMySVGletterPath.getMyAngle() ); 
                aMySVGwordPath.myDistances.add( aMySVGletterPath.getMyDistance() ); 
            }
        }
//            System.err.println("myTextContent: \t" + aMySVGwordPath.getMyTextContent() );
        if (aMySVGwordPath.getMyTextContent().endsWith(".i")) {
            aMySVGwordPath.setMyTextContent(aMySVGwordPath.getMyTextContent()
                    .substring(0, aMySVGwordPath.getMyTextContent().length() - 2) + "i");
        }
        if (aMySVGwordPath.getMyTextContent().endsWith(".j")) {
            aMySVGwordPath.setMyTextContent(aMySVGwordPath.getMyTextContent()
                    .substring(0, aMySVGwordPath.getMyTextContent().length() - 2) + "j");
        }
        if (aMySVGwordPath.getMyTextContent().endsWith("äü")) {
            while (!aMySVGwordPath.getMyTextContent().endsWith("A")
                    && !aMySVGwordPath.getMyTextContent().endsWith("O")
                    && !aMySVGwordPath.getMyTextContent().endsWith("U")
                    && !aMySVGwordPath.getMyTextContent().endsWith("a")
                    && !aMySVGwordPath.getMyTextContent().endsWith("o")
                    && !aMySVGwordPath.getMyTextContent().endsWith("u")
                    && aMySVGwordPath.getMyTextContent().length() > 1
                    ) {
                aMySVGwordPath.setMyTextContent(aMySVGwordPath.getMyTextContent()
                        .substring(0, aMySVGwordPath.getMyTextContent().length() - 1));
            }
            if (aMySVGwordPath.getMyTextContent().endsWith("A")) {
                aMySVGwordPath.setMyTextContent( aMySVGwordPath.getMyTextContent()
                                .substring(0, aMySVGwordPath.getMyTextContent().length() - 1) + "Ä");
            } else if (aMySVGwordPath.getMyTextContent().endsWith("O")) {
                aMySVGwordPath.setMyTextContent(aMySVGwordPath.getMyTextContent()
                                .substring(0, aMySVGwordPath.getMyTextContent().length() - 1) + "Ö");
            } else if (aMySVGwordPath.getMyTextContent().endsWith("U")) {
                aMySVGwordPath.setMyTextContent(aMySVGwordPath.getMyTextContent()
                                .substring(0, aMySVGwordPath.getMyTextContent().length() - 1) + "Ü");
            } else if (aMySVGwordPath.getMyTextContent().endsWith("a")) {
                aMySVGwordPath.setMyTextContent(aMySVGwordPath.getMyTextContent()
                                .substring(0, aMySVGwordPath.getMyTextContent().length() - 1) + "ä");
            } else if (aMySVGwordPath.getMyTextContent().endsWith("o")) {
                aMySVGwordPath.setMyTextContent(aMySVGwordPath.getMyTextContent()
                                .substring(0, aMySVGwordPath.getMyTextContent().length() - 1) + "ö");
            } else if (aMySVGwordPath.getMyTextContent().endsWith("u")) {
                aMySVGwordPath.setMyTextContent(aMySVGwordPath.getMyTextContent()
                                .substring(0, aMySVGwordPath.getMyTextContent().length() - 1) + "ü");
            }
        }
        allWordsByID.put(strId, aMySVGwordPath);
//        System.out.println(" -->" + aMySVGwordPath.getMyTextContent());
    }

    @Override
    public String toString() {
        return "(" + this.allWordsByID.size() + ") " + myTextContent;
    }

    public double getMyAngle() {
        return getMedian( this.myAngles );
    }

    public double getMyDistance() {
        return getMedian( this.myDistances );
    }
    
    public static double getMedian(ArrayList<Double> numArray) {
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

    public void addMyAdditionalSpace() {
        StringBuilder stringBuilder;
        stringBuilder = new StringBuilder(myTextContent);
        char[] charArray = myTextContent.toCharArray();
        for ( int i = 1; i < charArray.length; i++) {
            if ( Character.isUpperCase(charArray[i])
                    && !Character.isUpperCase(charArray[i-1])
                    && charArray[i-1] != '-'
                    && charArray[i] != ',' ) {
                stringBuilder.insert(i, ' ');
            }
        }
        myTextContent = stringBuilder.toString();
        myTextContent = myTextContent.replaceAll("des ", " des ");
        myTextContent = myTextContent.replaceAll("ohne ", " ohne ");
        myTextContent = myTextContent.replaceAll(" \\.", "\\. ");
        myTextContent = myTextContent.replaceAll("\\(", " \\(");
        
        myTextContent = myTextContent.replaceAll("\\&", "\\& ");
        myTextContent = myTextContent.replaceAll(" ,", ", ");
        myTextContent = myTextContent.replaceAll("Var\\.", "Var\\. ");
    }
}
