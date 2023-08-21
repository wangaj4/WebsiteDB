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

        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");

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
        out.println("            <a href=\"MovieList?reset=true\" class = \"logo\"><img src=\"img/logoblack.png\" id=\"logo\"></a>");
        out.println("            <div class=\"navbar-content\">");

        out.println("                <form ACTION=\"MovieList\" class=\"genres\">");
        out.println("                    <select id=\"genre\" name=\"Genre\" class=\"select-form\">");
        out.println("                        <option value=\"\">Browse Genre</option>");
        out.println("                           <option value=\"Action\">Action</option>");
        out.println("                           <option value=\"Adult\">Adult</option>");
        out.println("                           <option value=\"Adventure\">Adventure</option>");
        out.println("                           <option value=\"Animation\">Animation</option>");
        out.println("                           <option value=\"Biography\">Biography</option>");
        out.println("                           <option value=\"Comedy\">Comedy</option>");
        out.println("                           <option value=\"Crime\">Crime</option>");
        out.println("                           <option value=\"Documentary\">Documentary</option>");
        out.println("                           <option value=\"Drama\">Drama</option>");
        out.println("                           <option value=\"Family\">Family</option>");
        out.println("                           <option value=\"Fantasy\">Fantasy</option>");
        out.println("                           <option value=\"History\">History</option>");
        out.println("                           <option value=\"Horror\">Horror</option>");
        out.println("                           <option value=\"Music\">Music</option>");
        out.println("                           <option value=\"Musical\">Musical</option>");
        out.println("                           <option value=\"Mystery\">Mystery</option>");
        out.println("                           <option value=\"Reality-TV\">Reality-TV</option>");
        out.println("                           <option value=\"Romance\">Romance</option>");
        out.println("                           <option value=\"Sci-Fi\">Sci-Fi</option>");
        out.println("                           <option value=\"Sport\">Sport</option>");
        out.println("                           <option value=\"Thriller\">Thriller</option>");
        out.println("                           <option value=\"War\">War</option>");
        out.println("                           <option value=\"Western\">Western</option>");
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

            String skip = request.getParameter("skip");
            if(skip == null){
                skip = "false";
            }


            String id = request.getParameter("id");

            if (request.getParameter("success") != null && request.getParameter("success").equals("1")){
                out.println("<div class = \"results-addcart\" style = \"justify-content:center\"><p>Successfully added to cart</p></div>");

            }

            out.println("<div class = \"results-addcart\">");

            if(skip.equals("true")){
                out.println("<a href=\"./\" class = \"results darken\"> " + "< Back to Search" + "</a>");
            }else{
                out.println("<a href=\"MovieList\" class = \"results darken\"> " + "< Back to Results" + "</a>");
            }
            out.println("<form method='post' class = \"cart-form\" action='Movie?id=" + id + "'>");
            out.println("<button type='submit' >Add To Cart</button>");
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


                out.println("<div class = \"main-container\">");
                String title = resultSet.getString("title");
                titleName = title;
                movieID = resultSet.getString("id");
                String year = resultSet.getString("year");
                String director = resultSet.getString("director");
                String rating = resultSet.getString("rating");
                if(rating==null){
                    rating = "None";
                }


                out.println("<div class = \"left-container\">");
                out.println("   <h1>" + title + "</h1>");

                out.println("<label class = 'only-left-margin smalltext' ><b>Release Year</b></label>");
                out.println("   <h2 class = 'only-left-margin' >" + year + "</h1><p></p>");

                out.println("<label class = 'only-left-margin smalltext' ><b>Director</b></label>");
                out.println("   <h2 class = 'only-left-margin' >" + director + "</h1><p></p>");

                out.println("<label class = 'only-left-margin smalltext' ><b>Rating</b></label>");
                out.println("   <h2 class = 'only-left-margin' >" + rating + "</h1><p></p>");

                out.println("</div>");
                out.println("<div class = \"right-container\">");
                out.println("<h2>Genres:</h2>");
                String genre = "SELECT name FROM genres left join genres_in_movies on id = genreId WHERE movieId = ? ORDER BY name";
                statement = connection.prepareStatement(genre);
                statement.setString(1,id);
                ResultSet genreSet = statement.executeQuery();


                genreSet.next();
                String genreString = genreSet.getString("name");
                out.println("<a href=\"MovieList?Genre=" + genreString + "\" target = \"_blank\">" + genreString + "<br></a>");
                while (genreSet.next()) {
                    genreString = genreSet.getString("name");
                    out.println("<a href=\"MovieList?Genre=" + genreString + "\" target = \"_blank\">" + genreString + "<br></a>");
                }
                genreSet.close();


                out.println("</div>");
                out.println("</div>");
                //Show all stars in the movie
                String star = "SELECT * FROM stars left join stars_in_movies on id = starId WHERE movieId = ? ORDER BY name";
                statement = connection.prepareStatement(star);
                statement.setString(1,id);
                ResultSet starSet = statement.executeQuery();

                out.println("   <h2 ALIGN = \"center\">Actors:</h2>");
                out.println("<div class = \"actor_grid\">");



                starSet.next();

                String starName = starSet.getString("name");
                String starId = starSet.getString("id");
                out.println("<a href=\"stars?id=" + starId + "&skip=true\" target = \"_blank\">" + starName + "</a>");
                while (starSet.next()) {
                    starName = starSet.getString("name");
                    starId = starSet.getString("id");
                    out.println("<a href=\"stars?id=" + starId + "&skip=true\" target = \"_blank\">" + starName + "</a>");

                }
                out.println("</div>");


                out.println("</div>");







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
