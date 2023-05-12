import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;

import java.sql.*;


@WebServlet("/Dashboard")
public class Dashboard extends HttpServlet {
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */

    Boolean success = null;
    String movieid = "";
    String genreid = "";
    String starid = "";

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String loginUser = "mytestuser";
        String loginPasswd = "My6$Password";
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb";

        // Set response mime type
        response.setContentType("text/html");

        // Get the PrintWriter for writing response
        PrintWriter out = response.getWriter();


        out.println("<html>");
        out.println("<body>");
        try{
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            // create database connection
            Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet tables = metaData.getTables(null, null, null, new String[] {"TABLE"});

            out.println("<h1>Table MetaData:</h1>");

            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                out.println("<h2>" + tableName + "</h2>");

                // Retrieve the column information
                ResultSet columns = metaData.getColumns(null, null, tableName, null);

                out.println("<ul>");
                while (columns.next()) {
                    String columnName = columns.getString("COLUMN_NAME");
                    String columnType = columns.getString("TYPE_NAME");
                    out.println("<li>" + columnName + " (" + columnType + ")</li>");
                }
                out.println("</ul>");

                columns.close();
            }

            out.println("</body>");
            out.println("</html>");

            // 5. Close the resources
            tables.close();
            connection.close();




        }catch (Exception e){

            request.getServletContext().log("Error: ", e);

            out.println("<p>");
            out.println("Exception in doGet: " + e.getMessage());
            out.println("</p>");
        }


        //Print add star form
        {
            out.println("<form method=\"post\">");
            out.println("<label><b>Star Name</b></label>");
            out.println("<label>");
            out.println("<input name=\"name\" placeholder=\"Enter Star Name\" type=\"text\">");
            out.println("</label>");
            out.println("<label><b>Birth Year</b></label>");
            out.println("<label>");
            out.println("<input name=\"year\" placeholder=\"Enter Birth Year\" type=\"text\">");
            out.println("</label>");
            out.println("<input type=\"submit\" value=\"Submit\">");
            out.println("</form>");

        }
        //add movie form
        {
            out.println("<form method=\"post\">");
            out.println("<label><b>Movie Name</b></label>");
            out.println("<label>");
            out.println("<input name=\"movie\" placeholder=\"Enter Movie Title\" type=\"text\">");
            out.println("</label>");

            out.println("<label><b>Release Year</b></label>");
            out.println("<label>");
            out.println("<input name=\"year\" placeholder=\"Enter Year\" type=\"text\">");
            out.println("</label>");

            out.println("<label><b>Director</b></label>");
            out.println("<label>");
            out.println("<input name=\"director\" placeholder=\"Enter Director\" type=\"text\">");
            out.println("</label>");

            out.println("<label><b>Star Name</b></label>");
            out.println("<label>");
            out.println("<input name=\"name\" placeholder=\"Enter Star Name\" type=\"text\">");
            out.println("</label>");

            out.println("<label><b>Genre Name</b></label>");
            out.println("<label>");
            out.println("<input name=\"genre\" placeholder=\"Enter Genre\" type=\"text\">");
            out.println("</label>");


            out.println("<input type=\"submit\" value=\"Submit\">");
            out.println("</form>");
        }


        if(success != null && success == true){
            out.println("<p>success</p>");
            if(!movieid.equals("")){
                out.println("<p>New Movie ID: " + movieid + "</p>");
            }
            if(!starid.equals("")){
                out.println("<p>New Star ID: " + starid + "</p>");
            }
            if(!genreid.equals("")){
                out.println("<p>New Genre ID: " + genreid + "</p>");
            }

        }else if (success != null && !success){
            out.println("<p>Error, existing movie</p>");
        }

        out.println("</body>");
        out.println("</html>");

    }
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try{

            String loginUser = "mytestuser";
            String loginPasswd = "My6$Password";
            String loginUrl = "jdbc:mysql://localhost:3306/moviedb";
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            // create database connection
            Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);

            String starName = request.getParameter("name");

            String y = request.getParameter("year");
            int releaseYear = 0;
            Boolean yearExists = true;
            if (y != null && !y.equals("")) releaseYear = Integer.parseInt(y);
            else yearExists = false;

            String movie = request.getParameter("movie");
            String director = request.getParameter("director");
            String genre = request.getParameter("genre");

            success = false;

            //Add new movie
            if (movie != null){

                //Check if movie already exists
                String query = "SELECT count(*) as c from movies WHERE title = ? AND director = ? AND year = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, movie);
                statement.setString(2, director);
                statement.setInt(3, releaseYear);
                ResultSet existingMovie = statement.executeQuery();
                if(existingMovie.next()){
                    int count = existingMovie.getInt("c");
                    if (count > 0) {
                        success = false;
                        starid = "";
                        movieid = "";
                        response.sendRedirect("Dashboard");
                        return;
                    }
                }

                //Execute stored procedure to add movie
                //This implementation only works with new stars and genres

                //First get new movieid
                query = "SELECT id from movies order by id desc limit 1";
                statement = connection.prepareStatement(query);
                ResultSet recentID = statement.executeQuery();
                String movieID = "";
                while (recentID.next()){
                    movieID = recentID.getString("id");
                    int digits = Integer.parseInt(movieID.substring(movieID.length() - 6));
                    digits += 1;
                    movieID = movieID.substring(0,3) + Integer.toString(digits);
                }

                //Then get new starid
                String starID = "";
                query = "SELECT id from stars order by id desc limit 1";
                statement = connection.prepareStatement(query);
                recentID = statement.executeQuery();
                while (recentID.next()){
                    starID = recentID.getString("id");
                    int digits = Integer.parseInt(starID.substring(starID.length() - 7));
                    digits += 1;
                    starID = starID.substring(0,2) + Integer.toString(digits);
                }

                //Then get new genreid
                int genreID = 0;
                query = "SELECT id from genres order by id desc limit 1";
                statement = connection.prepareStatement(query);
                recentID = statement.executeQuery();
                while (recentID.next()){
                    genreID = recentID.getInt("id")+1;
                }

                //Execute stored procedure with variables
                String storedProcedure = "{call add_movie(?, ?, ?, ?, ?, ?, ?, ?)}";
                CallableStatement callableStatement = connection.prepareCall(storedProcedure);
                callableStatement.setString(1,movieID);
                callableStatement.setString(2,movie);
                callableStatement.setInt(3,releaseYear);
                callableStatement.setString(4,director);
                callableStatement.setString(5,starName);
                callableStatement.setString(6,starID);
                callableStatement.setString(7,genre);
                callableStatement.setInt(8,genreID);

                callableStatement.execute();

                movieid = movieID;
                starid = starID;
                genreid = Integer.toString(genreID);
                success = true;


            }

            //Add new star
            else if (starName != null && !starName.equals("")){

                String query = "SELECT id from stars order by id desc limit 1";
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet recentID = statement.executeQuery();
                String lastID = "";
                while (recentID.next()){
                    lastID = recentID.getString("id");
                    int digits = Integer.parseInt(lastID.substring(lastID.length() - 7));
                    digits += 1;
                    lastID = lastID.substring(0,2) + Integer.toString(digits);
                }

                query = "INSERT INTO stars(id, name, birthYear) VALUES (?, ?, ?)";
                statement = connection.prepareStatement(query);

                statement.setString(1, lastID);
                statement.setString(2,starName);
                if (yearExists) statement.setInt(3,releaseYear);
                else statement.setNull(3, Types.INTEGER);
                statement.executeUpdate();

                starid = lastID;
                movieid = "";
                success = true;

            }

            response.sendRedirect("Dashboard");





        }catch (Exception e){
            request.getServletContext().log("Error: ", e);
        }


    }

}

