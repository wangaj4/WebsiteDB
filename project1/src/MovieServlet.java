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

import java.util.regex.Pattern;
import java.util.regex.Matcher;

// This annotation maps this Java Servlet Class to a URL
@WebServlet("/Movie")
public class MovieServlet extends HttpServlet {
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
        out.println("<body>");


        try {
            out.println("<td><a href=\"http://localhost:8080/cs122b_project1_star_example_war/MovieList\">" + "Back to Movie List" + "</a></td>");
            out.println("<p></p>");

            Class.forName("com.mysql.jdbc.Driver").newInstance();
            String id = request.getParameter("id");
            // create database connection
            Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
            // declare statement
            Statement statement = connection.createStatement();
            String query = "SELECT * from movies left join ratings on id = movieId WHERE id = '"+id+"'";


            //Print all info of a movie
            if (id != null) {
                ResultSet resultSet = statement.executeQuery(query);
                resultSet.next();

                out.println("<table border>");
                out.println("<tr>");
                out.println("<td>Title</a></td>");
                out.println("<td>Year</td>");
                out.println("<td>Director</td>");
                out.println("<td>Rating</td>");
                out.println("</tr>");

                String title = resultSet.getString("title");
                String year = resultSet.getString("year");
                String director = resultSet.getString("director");
                String rating = resultSet.getString("rating");


                out.println("<tr>");
                out.println("<td>" + title + "</td>");
                out.println("<td>" + year + "</td>");
                out.println("<td>" + director + "</td>");
                out.println("<td>" + rating + "</td>");
                out.println("</tr>");

                out.println("</table>");


                //Show all stars in the movie
                String star = "SELECT * FROM stars left join stars_in_movies on id = starId WHERE movieId = '"+id+"'";
                ResultSet starSet = statement.executeQuery(star);

                out.println("<h2>Stars:</h2>");
                starSet.next();

                String starName = starSet.getString("name");
                String starId = starSet.getString("id");
                out.println("<p><a href=\"http://localhost:8080/cs122b_project1_star_example_war/stars?id=" + starId + "\">" + starName + "</a></p>");
                while (starSet.next()) {
                    starName = starSet.getString("name");
                    starId = starSet.getString("id");
                    out.println("<p><a href=\"http://localhost:8080/cs122b_project1_star_example_war/stars?id=" + starId + "\">" + starName + "</a></p>");

                }



                //Show all genres in the movie
                String genre = "SELECT name FROM genres left join genres_in_movies on id = genreId WHERE movieId = '"+id+"'";
                ResultSet genreSet = statement.executeQuery(genre);

                String genreString = "";
                genreSet.next();
                genreString += genreSet.getString("name");
                while (genreSet.next()) {
                    genreString += ", ";
                    genreString += genreSet.getString("name");
                }
                genreSet.close();
                out.println("<h2>Genres:</h2>");
                out.println("<p>" + genreString + "</p>");


            }else {
                out.println("<head><title>MyServlet</title></head>");
                out.println("<h1>No ID found</h1>");
            }


            statement.close();
            connection.close();

        } catch (Exception e) {

            request.getServletContext().log("Error: ", e);

            out.println("<p>");
            out.println("Exception in doGet: " + e.getMessage());
            out.println("</p>");
        }

        out.println("</body>");
        out.println("</html>");
        out.close();

    }


}
