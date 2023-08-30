import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

// This annotation maps this Java Servlet Class to a URL
@WebServlet("/stars")
public class StarServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;


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
        out.println("    <title>FlickBase Actor</title>");
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
        out.println("            <div class = \"cart-button\" id = \"cart\"></div>");
        out.println("            <div class = \"cart-list darken\"></div>");
        out.println("        </div>");

        out.println("        <div class=\"navbar-spacer\" id=\"spacer\"></div>");




        try {
            String skip = request.getParameter("skip");
            if(skip == null){
                skip = "false";
            }


            String id = request.getParameter("id");

            out.println("<div class = \"results-addcart\">");

            if(skip.equals("true")){
                out.println("<a href=\"./\" class = \"results darken\"> " + "< Back to Search" + "</a>");
            }else{
                out.println("<a href=\"MovieList\" class = \"results darken\"> " + "< Back to Results" + "</a>");
            }
            out.println("<div></div>");
            out.println("</div>");


            Class.forName("com.mysql.jdbc.Driver").newInstance();
            // create database connection
            Connection connection = dataSource.getConnection();
            // declare statement

            // prepare query
            String query = "SELECT * from stars WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,id);
            // execute query
            ResultSet resultSet = statement.executeQuery();

            resultSet.next();
            String starName = resultSet.getString("name");
            String birth = resultSet.getString("birthYear");
            if(birth==null){
                birth = "N/A";
            }

            out.println("<div class = \"star-container darken\">");
            out.println("<h1>"+ starName + "</h1>");
            out.println("<label class = 'only-left-margin smalltext' ><b>Birth Year</b></label>");
            out.println("   <h2 class = 'only-left-margin' >" + birth + "</h1><p></p>");

            out.println("<h2 ALIGN = \"CENTER\">Movies acted in: </h2>");


            //Get all movies that the star is in
            // prepare query
            String query2 = "SELECT * from stars_in_movies left join movies on movieId = id WHERE starId = ? ORDER BY year desc, title";
            // execute query
            statement = connection.prepareStatement(query2);
            statement.setString(1,id);
            ResultSet movieSet = statement.executeQuery();

            String movieid;
            String moviename;

            out.println("<div class = \"movie_grid\">");
            while (movieSet.next()){
                movieid = movieSet.getString("movieId");
                moviename = movieSet.getString("title");
                out.println("<a href=\"Movie?id=" + movieid + "&skip=true\">" + moviename + "</a>");
            }
            out.println("</div>");

            out.println("</div>");
            resultSet.close();
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
        out.println("<script src=\"./cart.js\"></script>");

        out.println("</body>");
        out.println("</html>");
        out.close();

    }


}
