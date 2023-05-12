import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

// This annotation maps this Java Servlet Class to a URL
@WebServlet("/Movie")
public class MovieServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private String titleName;
    private String movieID;

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
            out.println("<td><a href=\"MovieList\">" + "Back to Movie List" + "</a>");
            out.println("<P align = \"right\"><a href=\"ShoppingCart\">" + "Proceed to Cart" + "</a></P align = \"right\"></td>");
            out.println("<p></p>");

            Class.forName("com.mysql.jdbc.Driver").newInstance();
            String id = request.getParameter("id");
            // create database connection
            Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
            // declare statement
            String query = "SELECT * from movies left join ratings on id = movieId WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,id);


            //Print all info of a movie
            if (id != null) {
                ResultSet resultSet = statement.executeQuery();
                resultSet.next();

                out.println("<table border>");
                out.println("<tr>");
                out.println("<td>Title</a></td>");
                out.println("<td>Year</td>");
                out.println("<td>Director</td>");
                out.println("<td>Rating</td>");
                out.println("</tr>");

                String title = resultSet.getString("title");
                titleName = title;
                movieID = resultSet.getString("id");
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
                String star = "SELECT * FROM stars left join stars_in_movies on id = starId WHERE movieId = ? ORDER BY name";
                statement = connection.prepareStatement(star);
                statement.setString(1,id);
                ResultSet starSet = statement.executeQuery();

                out.println("<h2>Stars:</h2>");
                starSet.next();

                String starName = starSet.getString("name");
                String starId = starSet.getString("id");
                out.println("<p><a href=\"stars?id=" + starId + "\">" + starName + "</a></p>");
                while (starSet.next()) {
                    starName = starSet.getString("name");
                    starId = starSet.getString("id");
                    out.println("<p><a href=\"stars?id=" + starId + "\">" + starName + "</a></p>");

                }



                //Show all genres in the movie
                String genre = "SELECT name FROM genres left join genres_in_movies on id = genreId WHERE movieId = ? ORDER BY name";
                statement = connection.prepareStatement(genre);
                statement.setString(1,id);
                ResultSet genreSet = statement.executeQuery();
                out.println("<h2>Genres:</h2>");

                genreSet.next();
                String genreString = genreSet.getString("name");
                out.println("<p><a href=\"MovieList?Genre=" + genreString + "\">" + genreString + "</a></p>");
                while (genreSet.next()) {
                    genreString = genreSet.getString("name");
                    out.println("<p><a href=\"MovieList?Genre=" + genreString + "\">" + genreString + "</a></p>");
                }
                genreSet.close();

                out.println("<html><body>");
                out.println("<form method='post' action='Movie?id=" + id + "'>");
                out.println("<input type='submit' value='Add To Cart'>");
                out.println("</form>");


                if (request.getParameter("success") != null && request.getParameter("success").equals("1")){
                    out.println("<p>Success!</p>");
                }
                out.println("</body></html>");

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


    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        MovieInCart addedMovie = new MovieInCart(titleName, 10, movieID);
        HttpSession session = request.getSession();
        CartList cart = (CartList) session.getAttribute("cart");
        if(cart == null){
            cart = new CartList();
        }
        cart.addToCart(addedMovie);
        session.setAttribute("cart",cart);
        String redirectURL = "Movie?id=" + request.getParameter("id") + "&success=1";
        response.sendRedirect(redirectURL);

    }


}
