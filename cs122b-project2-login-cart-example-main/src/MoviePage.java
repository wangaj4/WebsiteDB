import jakarta.servlet.ServletConfig;
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
import java.sql.PreparedStatement;

import java.util.List;
import java.util.ArrayList;

import javax.naming.InitialContext;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.sql.DataSource;

import jakarta.servlet.http.*;
import jakarta.servlet.RequestDispatcher;
import org.apache.commons.dbcp2.BasicDataSource;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;




// This annotation maps this Java Servlet Class to a URL
@WebServlet("/MovieList")
public class MoviePage extends HttpServlet{
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

        HttpSession session = request.getSession();
        if (request.getParameter("reset") != null && request.getParameter("reset").equals("true")){
            session.setAttribute("Genre", null);
            session.setAttribute("Title", null);
            session.setAttribute("Director", null);
            session.setAttribute("Full", null);
            response.sendRedirect("./");
            return;
        }


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

            Connection connection = dataSource.getConnection();
            // Prepare query

            //String query = "SELECT * from movies left join ratings on id = movieId ORDER BY rating DESC limit 20";
            String query = "SELECT * from movies left join ratings on id = movieId WHERE TRUE";




            String Director = request.getParameter("Director");
            String Title = request.getParameter("Title");
            String Genre = request.getParameter("Genre");
            String Full = request.getParameter("Full");
            boolean fts = false;
            String fullTextSearch = "";

            if(Director == null && Title == null && Genre == null && Full == null){
                //if url is naked, check session to get last searched thing
                Director = (String) session.getAttribute("Director");
                Title = (String) session.getAttribute("Title");
                Genre = (String) session.getAttribute("Genre");
                Full = (String) session.getAttribute("Full");
            }else{
                //if one of those strings isn't null, that means the url has a parameter, which means
                //it's a new search. clear session.
                session.setAttribute("Title", null);
                session.setAttribute("Director", null);
                session.setAttribute("Full", null);
                session.setAttribute("Genre", null);
                session.setAttribute("page", null);

            }





            if(Director != null && !Director.equals("")){
                query += " AND movies.director LIKE '%" + Director + "%'";
                session.setAttribute("Director", Director);
            }else if(Title != null && !Title.equals("")){
                query += " AND movies.title LIKE '%" + Title + "%'";
                session.setAttribute("Title", Title);
            }else if(Genre != null && !Genre.equals("")){
                query = "SELECT m.id, m.title, m.year, m.director, r.rating FROM movies m left join ratings r on m.id = r.movieId, " +
                        "genres, genres_in_movies WHERE genres.id = genres_in_movies.genreId " +
                        "AND genres_in_movies.movieId = m.id " +
                        "AND genres.name = '" + Genre + "'";
                session.setAttribute("Genre", Genre);
            }else if(Full != null && !Full.equals("")){

                //loop over each word in full text search
                String[] searchTerms = Full.split("\\s+");

                for (String term : searchTerms) {
                    fullTextSearch += "+" + term + "* ";
                }
                query = "SELECT * FROM movies m left join ratings r ON m.id = r.movieId WHERE MATCH(m.title) AGAINST (? IN BOOLEAN MODE)";
                fts = true;
                session.setAttribute("Full", Full);
            }

            {
//            String Director = request.getParameter("Director");
//            if(Director == null || Director == ""){
//                Director = (String) session.getAttribute("Director");
//            }
//            if(Director != null){
//                query += " AND movies.director LIKE '%" + Director + "%'";
//                session.setAttribute("Director", Director);
//            }
//
//            String Title = request.getParameter("Title");
//            if(Title == null || Title == ""){
//                Title = (String) session.getAttribute("Title");
//            }
//            if(Title != null){
//                query += " AND movies.title LIKE '%" + Title + "%'";
//                session.setAttribute("Title", Title);
//            }
//
//
//            //If genre parameter exists, change query to find matching genre movies instead
//            String Genre = request.getParameter("Genre");
//            if(Genre == null || Genre == ""){
//                Genre = (String) session.getAttribute("Genre");
//            }
//            if(Genre != null){
//                query = "SELECT m.id, m.title, m.year, m.director, r.rating FROM movies m left join ratings r on m.id = r.movieId, " +
//                        "genres, genres_in_movies WHERE genres.id = genres_in_movies.genreId " +
//                        "AND genres_in_movies.movieId = m.id " +
//                        "AND genres.name = '" + Genre + "'";
//                session.setAttribute("Genre", Genre);
//                session.setAttribute("Title", null);
//                session.setAttribute("Director", null);
//                session.setAttribute("Full", null);
//                Title = null;
//                Genre = null;
//                Director = null;
//            }
//
//
//            //Full text search overrides
//            String Full = request.getParameter("Full");
//            boolean fts = false;
//            String fullTextSearch = "";
//
//            if(Full == null || Full == ""){
//                Full = (String) session.getAttribute("Full");
//            }
//            if(Full != null){
//                session.setAttribute("Genre", null);
//                session.setAttribute("Title", null);
//                session.setAttribute("Director", null);
//                Title = null;
//                Genre = null;
//                Director = null;
//                //loop over each word in full text search
//                String[] searchTerms = Full.split("\\s+");
//
//                for (String term : searchTerms) {
//                    fullTextSearch += "+" + term + "* ";
//                }
//                query = "SELECT * FROM movies m left join ratings r ON m.id = r.movieId WHERE MATCH(m.title) AGAINST (? IN BOOLEAN MODE)";
//                fts = true;
//                session.setAttribute("Full", Full);
//
//            }
            }




            //Pagination and sorting

            int perPage = 25;
            String Per = request.getParameter("Per");
            if(Per == null){
                Per = (String) session.getAttribute("Per");
            }
            if(Per != null){
                perPage = Integer.parseInt(Per);
                session.setAttribute("Per", Per);
            }


            int offset = 0;
            String Page = request.getParameter("page");
            if(Page == null){
                Page = (String) session.getAttribute("page");
            }
            if(Page != null){
                offset = Integer.parseInt(Page) * perPage;
                session.setAttribute("page", Page);
            }else{
                Page = "0";
            }

            query += " limit " + perPage + " OFFSET " + offset;

            // execute query
            PreparedStatement statement = connection.prepareStatement(query);
            if(fts){
                statement.setString(1,fullTextSearch.trim());
            }

            //out.println("<h1>" + query + "</h1>");

            ResultSet resultSet = statement.executeQuery();


            out.println("<body>");
            out.println("<h1>Found Movies</h1>");

            //Display active filters
            if(Genre != null){
                out.println("<h4>Showing Movies with Genre: "+ Genre + "</h4>");
            }
            else if(Title != null){
                out.println("<h4>Showing Movies with: "+ Title + "</h4>");
            }
            else if(Director != null){
                out.println("<h4>Showing Movies with Director: "+ Director + "</h4>");

            }
            else if(Full != null){
                out.println("<h4>Showing Movies with Keywords: "+ Full + "</h4>");

            }

            //Change number of results per page
            out.println("<h4>Current results per page: "+ Integer.toString(perPage) + "</h4>");
            out.println("<form ACTION = \"MovieList\">");
            out.println("<label for=\"Per\">Results per page:</label>");
            out.println("<select name=\"Per\" id=\"Per\">");
            out.println("<option value='10'>10</option>");
            out.println("<option value='25'>25</option>");
            out.println("<option value='50'>50</option>");
            out.println("<option value='100'>100</option>");
            out.println("</select>");
            out.println("<input type='submit' value='Refresh'>");
            out.println("</form>");



            out.println("<div class = \"table-container darken\">");
            out.println("<table border class = \"styled-table\" >");

            // Add table header row
            {
                out.println("<tr>");
                out.println("<th>Title</a></th>");
                out.println("<th>Year</th>");
                out.println("<th>Director</th>");
                out.println("<th>Genres</th>");
                out.println("<th>Stars</th>");
                out.println("<th>Rating</th>");
                out.println("</tr>");
            }

            int onpage = 0;


            //Get genres from database
            Map<String, List<List<String>>> movieGenresMap = new HashMap<>();
            String genre = "SELECT * FROM genres_in_movies LEFT JOIN genres on genres.id = genres_in_movies.genreId WHERE TRUE";

            statement = connection.prepareStatement(genre);
            ResultSet genreSet = statement.executeQuery();

            while (genreSet.next()){
                String movieId = genreSet.getString("movieId");
                int genreId = genreSet.getInt("genreId");
                String genreName = genreSet.getString("name");

                if (!movieGenresMap.containsKey(movieId)) {
                    movieGenresMap.put(movieId, new ArrayList<>());
                }
                List<String> g = new ArrayList<>();
                g.add(Integer.toString(genreId));
                g.add(genreName);
                movieGenresMap.get(movieId).add(g);
            }

            //Get stars from database
            Map<String, List<List<String>>> movieStarsMap = new HashMap<>();
            String star = "SELECT * FROM stars_in_movies LEFT JOIN stars on stars.id = stars_in_movies.starId WHERE TRUE";

            statement = connection.prepareStatement(star);
            ResultSet starSet = statement.executeQuery();

            while (starSet.next()){
                String movieId = starSet.getString("movieId");
                String starId = starSet.getString("starId");
                String starName = starSet.getString("name");

                if (!movieStarsMap.containsKey(movieId)) {
                    movieStarsMap.put(movieId, new ArrayList<>());
                }
                List<String> s = new ArrayList<>();
                s.add((starId));
                s.add(starName);
                movieStarsMap.get(movieId).add(s);
            }

            // Add a row for every movie result
            while (resultSet.next()) {
                onpage += 1;
                // get a movie from result set
                String movieid = resultSet.getString("id");
                String title = resultSet.getString("title");
                String year = resultSet.getString("year");
                String director = resultSet.getString("director");
                String rating = resultSet.getString("rating");

                out.println("<tr>");
                out.println("<td><a href=\"Movie?id=" + movieid + "\">" + title + "</a></td>");
                out.println("<td>" + year + "</td>");
                out.println("<td><a href=\"MovieList?Director=" + director + "\">" + director + "</a></td>");


                //Find first three genres

                out.println("<td>");
                int index = 0;
                for (List<String> x : movieGenresMap.get(movieid)){
                    if (index != 0) {
                        out.println(",");
                    }
                    out.println("<a href=\"MovieList?Genre=" + x.get(1) + "\">" + x.get(1) + "</a>");
                    index+=1;
                }
                out.println("</td>");

                //Find first three stars
                out.println("<td>");
                index = 0;
                for (List<String> x : movieStarsMap.get(movieid)){
                    if (index != 0) {
                        out.println(",");
                    }
                    if(index==3){
                        break;
                    }
                    out.println("<a href=\"stars?id=" + x.get(0) + "\">" + x.get(1) + "</a>");
                    index+=1;
                }
                out.println("</td>");


                out.println("<td>" + rating + "</td>");
                out.println("</tr>");
            }


            out.println("</table>");

            out.println("</div>");
            out.println("</body>");


            int curr = Integer.parseInt(Page);
            int displayedPage = curr+1;
            out.println("<h4>Current Page: "+ String.valueOf(displayedPage) + "</h4>");

            if (!Page.equals("0")){
                int prev = curr-1;
                out.println("<a href=\"MovieList?page=" + prev + "\">" + "Previous Page" + "</a>");
            }
            if(onpage == perPage){
                int next = curr + 1;
                out.println("<a href=\"MovieList?page=" + next + "\">" + "Next Page" + "</a>");
            }



            resultSet.close();
            genreSet.close();
            starSet.close();
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



}