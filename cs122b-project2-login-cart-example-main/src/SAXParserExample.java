import com.mysql.cj.protocol.Resultset;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import java.util.HashMap;
import java.util.Map;
import java.sql.*;




public class SAXParserExample extends DefaultHandler {

    Connection connection;
    private PreparedStatement preparedStatement;
    private PreparedStatement genreStatement;
    private PreparedStatement genreMovieStatement;
    private boolean useReleased;
    List<Film> Films;

    Map<String, Integer> existingGenres = new HashMap<>();
    private int numGenres = 0;


    private String tempVal;

    //to maintain context
    private Film tempFilm;

    public SAXParserExample() {
        Films = new ArrayList<Film>();
    }

    public void getExistingGenres(){
        try{
            //Get all existing genres and put them in existingGenres
            String getGenres = "SELECT * FROM genres2";
            PreparedStatement getGenreStatement = connection.prepareStatement(getGenres);
            ResultSet genres = getGenreStatement.executeQuery();
            while (genres.next()){
                String name = genres.getString("name");
                int id = genres.getInt("id");
                existingGenres.put(name, id);
                numGenres+=1;
            }
            System.out.println("Num Genres at Start '" + existingGenres.size() + "'.");
        }catch(Exception e){

        }



    }
    public void runExample() {
        try{
            String loginUser = "mytestuser";
            String loginPasswd = "My6$Password";
            String loginUrl = "jdbc:mysql://localhost:3306/moviedb";
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            // create database connection
            connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);

            connection.setAutoCommit(false);

            getExistingGenres();

            String insertQuery = "INSERT INTO movies2 (id, title, year, director) VALUES (?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(insertQuery);

            String insertGenre = "INSERT INTO genres2(name) VALUES(?)";
            genreStatement = connection.prepareStatement(insertGenre);

            String insertGenreMovie = "INSERT INTO genres_in_movies2(genreId, movieId) VALUES(?, ?)";
            genreMovieStatement = connection.prepareStatement(insertGenreMovie);

            parseDocument();

            preparedStatement.executeBatch();
            genreStatement.executeBatch();
            genreMovieStatement.executeBatch();

            connection.commit();
            preparedStatement.close();
            genreStatement.close();
            genreMovieStatement.close();
        }catch  (Exception e){
            System.out.println("error in run " + e);
        }
        printData();
    }

    private void parseDocument() {

        //get a factory
        SAXParserFactory spf = SAXParserFactory.newInstance();
        try {

            //get a new instance of parser
            SAXParser sp = spf.newSAXParser();

            //parse the file and also register this class for call backs
            sp.parse("mains243.xml", this);

        } catch (SAXException se) {
            se.printStackTrace();
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    /**
     * Iterate through the list and print
     * the contents
     */
    private void printData() {

        System.out.println("No of Films '" + Films.size() + "'.");
        System.out.println("No of Genres '" + existingGenres.size() + "'.");

        Iterator<Film> it = Films.iterator();
        while (it.hasNext()) {
            System.out.println(it.next().toString());

        }
    }

    //Event Handlers
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        //reset
        tempVal = "";
        if (qName.equalsIgnoreCase("film")) {
            //create a new instance of a film
            tempFilm = new Film();
            useReleased = false;
        }else if (qName.equalsIgnoreCase("released")) {
            useReleased = true;
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        tempVal = new String(ch, start, length);
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {

        if (qName.equalsIgnoreCase("film")) {
            //add it to the list
            Films.add(tempFilm);
            try{
                preparedStatement.setString(1, tempFilm.getID());
                preparedStatement.setString(2, tempFilm.getTitle());
                preparedStatement.setInt(3,tempFilm.getYear());
                preparedStatement.setString(4, tempFilm.getDirector());
                preparedStatement.addBatch();

                //Second preparedStatement to add genres of the movie to genres_in_movies and genre
                //Iterate through genres of the film
                ArrayList<String> genres = tempFilm.getGenres();
                for(int i = 0; i < genres.size(); i++){
                    //Check if genre is in existingGenres
                    int genreId;
                    String genreName = genres.get(i);
                    if(!existingGenres.containsKey(genreName)){
                        //If not, add to existingGenres
                        existingGenres.put(genres.get(i), numGenres+1);
                        numGenres += 1;
                        genreId = numGenres;
                        //Add to genres database
                        genreStatement.setString(1,genreName);
                        genreStatement.addBatch();

                    }else{
                        //If genre already exists, get genreId from the hashmap
                        genreId = existingGenres.get(genreName);
                    }
                    //insert into genres_in_movies table
                    genreMovieStatement.setInt(1,genreId);
                    genreMovieStatement.setString(2,tempFilm.getID());
                    genreMovieStatement.addBatch();


                }



            }catch (Exception e){
                System.out.println("error in endElement");
            }


        } else if (qName.equalsIgnoreCase("t")) {
            tempFilm.setTitle(tempVal);
        } else if (qName.equalsIgnoreCase("fid")) {
            tempFilm.setID(tempVal);
        } else if (qName.equalsIgnoreCase("year") && !useReleased) {
            tempFilm.setYear(Integer.parseInt(tempVal));
        } else if (qName.equalsIgnoreCase("released") && useReleased) {
            tempFilm.setYear(Integer.parseInt(tempVal));
        }else if (qName.equalsIgnoreCase("dirn")) {
            tempFilm.setDirector(tempVal);
        }else if (qName.equalsIgnoreCase("cat")) {
            tempFilm.addGenre(tempVal);
        }

    }

    public static void main(String[] args) {
        SAXParserExample spe = new SAXParserExample();
        spe.runExample();
    }

}
