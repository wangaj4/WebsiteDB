import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

// This annotation maps this Java Servlet Class to a URL
@WebServlet("/MovieList")
public class MoviePage extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Change this to your own mysql username and password
        String loginUser = "mytestuser";
        String loginPasswd = "My6$Password";
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb";

        // Set response mime type
        response.setContentType("text/html");

        // Get the PrintWriter for writing response
        PrintWriter out = response.getWriter();

        out.println("<html>");
        out.println("<head><title>Fabflix</title></head>");

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            // create database connection
            Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
            // declare statement
            Statement statement = connection.createStatement();
            // prepare query
            String query = "SELECT * from movies left join ratings on id = movieId ORDER BY rating DESC limit 20";
            // execute query
            ResultSet resultSet = statement.executeQuery(query);

            out.println("<body>");
            out.println("<h1>Top 20 Rated Movies</h1>");

            out.println("<table border>");

            // Add table header row
            out.println("<tr>");
            out.println("<td>Title</a></td>");
            out.println("<td>Year</td>");
            out.println("<td>Director</td>");
            out.println("<td>Genres</td>");
            out.println("<td>Stars</td>");
            out.println("<td>Rating</td>");
            out.println("</tr>");

            // Add a row for every star result
            while (resultSet.next()) {
                // get a star from result set
                String movieid = resultSet.getString("id");
                String title = resultSet.getString("title");
                String year = resultSet.getString("year");
                String director = resultSet.getString("director");
                String rating = resultSet.getString("rating");

                out.println("<tr>");
                out.println("<td><a href=\"http://localhost:8080/cs122b_project1_star_example_war/Movie?id=" + movieid + "\">" + title + "</a></td>");
                out.println("<td>" + year + "</td>");
                out.println("<td>" + director + "</td>");

                //Find first three genres
                Statement s2 = connection.createStatement();
                String genre = "SELECT name FROM genres left join genres_in_movies on id = genreId WHERE movieId = '"+movieid+"' limit 3";
                ResultSet genreSet = s2.executeQuery(genre);
                String genreString = "";

                genreSet.next();
                genreString += genreSet.getString("name");
                while (genreSet.next()){
                    genreString += ", ";
                    genreString += genreSet.getString("name");


                }
                out.println("<td>" + genreString + "</td>");

                //Find first three stars
                Statement s3 = connection.createStatement();
                String star = "SELECT * FROM stars left join stars_in_movies on id = starId WHERE movieId = '"+movieid+"' limit 3";
                ResultSet starSet = s3.executeQuery(star);


                starSet.next();
                String starString = starSet.getString("name");
                String starid = starSet.getString("starId");
                out.println("<td>");
                out.println("<a href=\"http://localhost:8080/cs122b_project1_star_example_war/stars?id=" + starid + "\">" + starString + "</a>");
                while (starSet.next()){
                    starString = starSet.getString("name");
                    starid = starSet.getString("starId");
                    out.println(",");
                    out.println("<a href=\"http://localhost:8080/cs122b_project1_star_example_war/stars?id=" + starid + "\">" + starString + "</a>");


                }
                s2.close();
                s3.close();
                out.println("</td>");



                out.println("<td>" + rating + "</td>");
                out.println("</tr>");
            }

            out.println("</table>");
            out.println("</body>");

            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception e) {

            request.getServletContext().log("Error: ", e);

            out.println("<body>");
            out.println("<p>");
            out.println("Exception in doGet: " + e.getMessage());
            out.println("</p>");
            out.print("</body>");
        }

        out.println("</html>");
        out.close();

    }


}
