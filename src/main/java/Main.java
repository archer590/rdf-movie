import org.apache.jena.atlas.json.JsonObject;
import org.apache.jena.vocabulary.RDF;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 *
 */
public class Main {

    public static void main(String args[]) {
        System.out.println("Start");
        Parser parser = new Parser();
        ArrayList<JSONObject> moviesParsed = new ArrayList<JSONObject>();
        moviesParsed = parser.parsingURL();
        RDFGraphCreator graphCreator = new RDFGraphCreator();
        graphCreator.createRDFGraph(moviesParsed);
        System.out.println("End");
    }

}
