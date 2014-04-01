package JavaFXPathElements;

public class MySVGClosePath extends javafx.scene.shape.ClosePath {
    
    public char toChar(){
        if (this.isAbsolute() )
            return 'Z';
        return 'z';
    }
}
