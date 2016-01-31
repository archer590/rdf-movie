import org.apache.jena.atlas.json.JsonObject;
import org.apache.jena.vocabulary.RDF;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 *
 */
public class Main {

    static Logger log = Logger.getLogger(Main.class);

    public static void main(String args[]) {
        System.out.println("Start");
        BasicConfigurator.configure();
        log.info("This is Logger Info");

        Parser parser = new Parser();
        ArrayList<JSONObject> moviesParsed = new ArrayList<JSONObject>();
        moviesParsed = parser.parsingURL();
        RDFGraphCreator graphCreator = new RDFGraphCreator();
        graphCreator.createRDFGraph(moviesParsed);
        System.out.println("End");
    }

}
