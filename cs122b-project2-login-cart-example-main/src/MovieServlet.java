import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

// This annotation maps this Java Servlet Class to a URL
@WebServlet("/Movie")
public class MovieServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private String titleName;
    private String movieID;

    public DataSource dataSource;

    public void init(ServletConfig config) {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/moviedb");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // Set response mime type
        response.setContentType("text/html");

        // Get the PrintWriter for writing response
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("    <title>FlickBase Search</title>");
        out.println("    <link rel=\"stylesheet\" href=\"style.css\">");
        out.println("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
        out.println("    <!-- jQuery is required -->");
        out.println("    <script src=\"https://code.jquery.com/jquery-3.6.4.min.js\"></script>");
        out.println("    <!-- include jquery autocomplete JS  -->");
        out.println("    <script src=\"https://cdnjs.cloudflare.com/ajax/libs/jquery.devbridge-autocomplete/1.4.11/jquery.autocomplete.min.js\"></script>");
        out.println("</head>");
        out.println("<body>");

        out.println("    <div id=\"back\" class=\"cover font\">");

        out.println("        <div class=\"navbar darken\" id=\"navbar\">");
        out.println("            <div class=\"navbar-toggle\">");
        out.println("                <span class=\"bar\"></span>");
        out.println("                <span class=\"bar\"></span>");
        out.println("                <span class=\"bar\"></span>");
        out.println("            </div>");
        out.println("            <img src=\"img/logoblack.png\" class=\"logo\" id=\"logo\">");
        out.println("            <div class=\"navbar-content\">");

        out.println("                <form ACTION=\"MovieList\" class=\"genres\">");
        out.println("                    <select id=\"genre\" name=\"Genre\" class=\"select-form\">");
        out.println("                        <option value=\"\">Browse Genre</option>");
        out.println("                        <option value=\"Action\">Action</option>");
        out.println("                        <option value=\"Adult\">Adult</option>");
        out.println("                        <option value=\"Adventure\">Adventure</option>");
        out.println("                        <!-- Add more options here -->");
        out.println("                    </select>");
        out.println("                </form>");
        out.println("                <input type=\"text\" id=\"autocomplete\"");
        out.println("                       class=\"autocomplete-searchbox\"");
        out.println("                       placeholder=\"Search movies by keywords...\"/>");
        out.println("            </div>");
        out.println("            <button class=\"toggle\" id=\"toggle\">");
        out.println("                <span id=\"toggleButton\" class=\"toggleButton\"></span>");
        out.println("            </button>");
        out.println("            <a href=\"ShoppingCart\" class=\"cart-button\" id=\"cart\"></a>");
        out.println("        </div>");

        out.println("        <div class=\"navbar-spacer\" id=\"spacer\"></div>");





        try {

            String id = request.getParameter("id");


            out.println("<div class = \"results-addcart\">");
            out.println("<a href=\"MovieList\" class = \"results\"> " + "Back to Results" + "</a>");
            out.println("<form method='post' class = \"results cart-form\" action='Movie?id=" + id + "'>");
            out.println("<button type='submit'>Add To Cart</button>");
            out.println("</div>");

            Class.forName("com.mysql.jdbc.Driver").newInstance();

            // create database connection
            Connection connection = dataSource.getConnection();
            // declare statement
            String query = "SELECT * from movies left join ratings on id = movieId WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,id);


            //Print all info of a movie
            if (id != null) {
                ResultSet resultSet = statement.executeQuery();
                resultSet.next();

                out.println("<div class = \"star-container darken\">");
                out.println("   <table border>");
                out.println("   <tr>");
                out.println("   <td>Title</a></td>");
                out.println("   <td>Year</td>");
                out.println("   <td>Director</td>");
                out.println("   <td>Rating</td>");
                out.println("   </tr>");
                out.println("   </div>");

                String title = resultSet.getString("title");
                titleName = title;
                movieID = resultSet.getString("id");
                String year = resultSet.getString("year");
                String director = resultSet.getString("director");
                String rating = resultSet.getString("rating");




                out.println("   <tr>");
                out.println("   <td>" + title + "</td>");
                out.println("   <td>" + year + "</td>");
                out.println("   <td>" + director + "</td>");
                out.println("   <td>" + rating + "</td>");
                out.println("   </tr>");

                out.println("   </table>");


                //Show all stars in the movie
                String star = "SELECT * FROM stars left join stars_in_movies on id = starId WHERE movieId = ? ORDER BY name";
                statement = connection.prepareStatement(star);
                statement.setString(1,id);
                ResultSet starSet = statement.executeQuery();

                out.println("   <h2>Actors:</h2>");
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


                out.println("</form>");
                out.println("</div>");

                if (request.getParameter("success") != null && request.getParameter("success").equals("1")){
                    out.println("<p>Success!</p>");
                }

            }else {
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

        out.println("        <div class=\"navbar-spacer\"></div>");

        out.println("        <div class=\"footer\">");
        out.println("            <p>&copy; 2023 Andrew J Wang. All rights reserved.</p>");
        out.println("        </div>");
        out.println("    </div>");
        out.println("<script src=\"./index.js\"></script>");
        out.println("<script src=\"./toggle.js\"></script>");
        out.println("<script src=\"./select.js\"></script>");
        out.println("<script src=\"./navbarToggle.js\"></script>");
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
