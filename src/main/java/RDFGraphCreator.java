import org.apache.jena.graph.FrontsNode;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.VCARD;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 *
 */
public class RDFGraphCreator {

    private Model movieModel = null;
    private static final String SOURCE = "http://www.rdf-movie.org/";

    public RDFGraphCreator() {
        Path input = Paths.get("movies.rdf");
        movieModel = ModelFactory.createDefaultModel();
        movieModel.read(input.toUri().toString(), "RDF/XML");
    }

    public void createRDFGraph(ArrayList<JSONObject> movies) {
        try {
            System.out.println("Start to create the RDF file...");

            for (int i = 0; i < movies.size(); i++) {
                JSONObject json = movies.get(i);
                String movieURI = SOURCE +"movie/"+ json.getString("Title").replaceAll("[\\W\\s]", "");
                //System.out.println(movieURI);
                Resource currentMovie = movieModel.createResource(movieURI);
                //System.out.println(currentMovie.asResource());
                currentMovie.addProperty(movieModel.getProperty("Title"), json.getString("Title"));
                currentMovie.addProperty(movieModel.getProperty("genre"), json.getString("Genre"));
                currentMovie.addProperty(movieModel.getProperty("language"), json.getString("Language"));
                currentMovie.addProperty(movieModel.getProperty("country"), json.getString("Genre"));
                currentMovie.addProperty(movieModel.getProperty("duration"), json.getString("Runtime"));
                currentMovie.addProperty(movieModel.getProperty("releaseYear"), json.getString("Year"));

                this.createResourceActor(json.getString("Actors"),currentMovie.asResource(),currentMovie);
                this.createResourceDirector(json.getString("Director"),currentMovie.asResource(),currentMovie);

                System.out.println(json.getString("Title")+" --> added");
            }

            File file = new File("movies.ttl");
            FileOutputStream fos = new FileOutputStream(file);

            movieModel.write(fos, "TURTLE");

            System.out.println("RDF file completed.");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    private void createResourceActor(String actors, Resource n, Resource m) {
        String currentActors[] = actors.split(", ");
        //System.out.println(n);
        for (int i=0; i<currentActors.length; i++) {
            String actorURI = SOURCE+"actor/"+currentActors[i].replaceAll("[\\W\\s]", "");
            Resource currentActor = movieModel.createResource(actorURI);
            if (!checkResourceActor(movieModel.getResource(actorURI))) {
                currentActor.addProperty(VCARD.FN, currentActors[i]);
                //System.out.println(currentActors[i]+" --> added");
                currentActor.addProperty(movieModel.getProperty("playRoleIn"),n);
                m.addProperty(movieModel.getProperty("Actor"), actorURI);
            } else {
                //System.out.println(currentActors[i]+" already present --> skipped");
            }
        }

    }

    private boolean checkResourceActor(Resource r) {
        if (movieModel.contains(r, VCARD.FN)) {
            return true;
        }
        return false;
    }


    private void createResourceDirector(String directors, Resource n, Resource m) {
        String currentDirectors[] = directors.split(", <");
        //System.out.println(n);
        for (int i=0; i<currentDirectors.length; i++) {
            String directorURI = SOURCE+"director/"+currentDirectors[i].replaceAll("[\\W\\s]", "");
            Resource currentDirector = movieModel.createResource(directorURI);
            if (!checkResourceDirector(movieModel.getResource(directorURI))) {
                currentDirector.addProperty(VCARD.FN, currentDirectors[i]);
                //System.out.println(currentDirectors[i]+" --> added");
                currentDirector.addProperty(movieModel.getProperty("directedBy"),n);
            } else {
                //System.out.println(currentDirectors[i]+" already present --> skipped");
            }
        }
    }

    private boolean checkResourceDirector(Resource r) {
        if (movieModel.contains(r, VCARD.FN)) {
            return true;
        }
        return false;
    }



}
