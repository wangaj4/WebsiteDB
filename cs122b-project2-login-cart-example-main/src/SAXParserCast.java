import org.apache.commons.dbcp2.BasicDataSource;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;


public class SAXParserCast extends DefaultHandler {

    Connection connection;
    private BasicDataSource dataSource;
    private PreparedStatement preparedStatement;

    private String tempVal;
    private String tempName = "";
    private String tempMovieID = "";
    private int count = 0;

    //to maintain context
    private Map<String, String> nameIdDictionary;

    private void populateDictionary(){
        //Runs a mysql query to get all star's name id pairs and stores them in dictionary for lookup
        nameIdDictionary = new HashMap<>();

        try{
            String query = "SELECT name, id FROM stars";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                String name = resultSet.getString("name");
                String id = resultSet.getString("id");
                nameIdDictionary.put(name, id);
            }



        }catch (Exception e){
            System.out.println("Error occurred while populating dictionary: " + e.getMessage());
        }




    }

    public void runExample() {
        try{


            dataSource = new BasicDataSource();
            dataSource.setUrl("jdbc:mysql://localhost:3306/moviedb");
            dataSource.setUsername("mytestuser");
            dataSource.setPassword("My6$Password");
            // create database connection
            connection = dataSource.getConnection();

            populateDictionary();

            connection.setAutoCommit(false);

            String query = "INSERT INTO stars_in_movies2(starId, movieId) VALUES(?,?)";
            preparedStatement = connection.prepareStatement(query);

            parseDocument();

            preparedStatement.executeBatch();


            connection.commit();
            preparedStatement.close();

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
            sp.parse("casts124.xml", this);

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
            System.out.println(count);

    }

    //Event Handlers
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        //reset
        tempVal = "";

        if (qName.equalsIgnoreCase("m")) {
            tempName = "";
            tempMovieID = "";

        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        tempVal = new String(ch, start, length);
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {

        if (qName.equalsIgnoreCase("m")) {
            count+=1;
            //add it to the list
            try{
                //Convert tempname to star id

                String starID = nameIdDictionary.get(tempName);
                preparedStatement.setString(1, starID);
                preparedStatement.setString(2, tempMovieID);

                preparedStatement.addBatch();

            }catch (Exception e){
                System.out.println("error in endElement: " + e);
            }


        } else if (qName.equalsIgnoreCase("a")) {
            tempName = tempVal;
        } else if (qName.equalsIgnoreCase("f")) {
            tempMovieID = tempVal;
        }

    }

    public static void main(String[] args) {
        SAXParserCast spe = new SAXParserCast();
        spe.runExample();
    }

}
