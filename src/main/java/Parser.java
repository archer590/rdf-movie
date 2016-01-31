import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * 
 */
public class Parser {
    /*
        private static final int MAX_MOVIE_RANGE = 134854;
        private static final int MIN_MOVIE_RANGE = 107290;
        */
          private static final int MAX_MOVIE_RANGE = 107300;
          private static final int MIN_MOVIE_RANGE = 107290;

    public ArrayList<JSONObject> parsingURL(){
        ArrayList<JSONObject> movies = new ArrayList<JSONObject>();
        System.out.println("Movie to parse: "+(MAX_MOVIE_RANGE-MIN_MOVIE_RANGE));
        try {
            for(int i=MIN_MOVIE_RANGE; i<MAX_MOVIE_RANGE; i++) {
                String imdbID = getImdbID(i);
                System.out.println(imdbID);
                URL url = new URL("http://www.omdbapi.com/?i=" + imdbID + "&plot=full&r=json");
                InputStream is = url.openStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
                JSONObject json = new JSONObject(rd.readLine());
                if (checkResponse(json) && checkEmptyProperties(json)) {
                    movies.add(json);
                    System.out.println(i+" --> response OK properties OK >>> added");
                } else {
                    System.out.println(i+" --> response OR properties NOT OK >>> skipped");
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Movie parsed (satisfying prerequisites): "+movies.size());
        return movies;
    }


    /** tt0107290 - tt0134854 */
    private String getImdbID(int i){
        return "tt0"+i;
    }

    private boolean checkEmptyProperties(JSONObject currentJson){
        if(currentJson.getString("Title").equalsIgnoreCase("N/A") ||
                currentJson.getString("Actors").equalsIgnoreCase("N/A") ||
                currentJson.getString("Director").equalsIgnoreCase("N/A") ||
                currentJson.getString("Country").equalsIgnoreCase("N/A") ||
                currentJson.getString("Language").equalsIgnoreCase("N/A") ||
                currentJson.getString("Year").equalsIgnoreCase("N/A") ||
                currentJson.getString("Runtime").equalsIgnoreCase("N/A") ||
                currentJson.getString("Genre").equalsIgnoreCase("N/A")){
            return false;
        }
        return true;
    }


    private boolean checkResponse(JSONObject currentJson){
        if (currentJson.getString("Response").equalsIgnoreCase("False")) {
            return false;
        }
        return true;
    }

}
