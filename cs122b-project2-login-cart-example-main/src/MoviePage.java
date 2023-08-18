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
            session.setAttribute("page", null);
            session.setAttribute("Genre", null);
            session.setAttribute("start",null);
            session.setAttribute("Title", null);
            session.setAttribute("Director", null);
            session.setAttribute("Year", null);
            session.setAttribute("Full", null);
            response.sendRedirect("index.html");
            return;
        }


        // Set response mime type
        response.setContentType("text/html");

        // Get the PrintWriter for writing response
        PrintWriter out = response.getWriter();

        out.println("<html>");
        out.println("<head><title>Flickbase</title></head>");
        out.println("<P align = \"right\"><a href=\"ShoppingCart\">" + "Proceed to Cart" + "</a></P align = \"right\"></td>");


        try {

            Connection connection = dataSource.getConnection();
            // Prepare query

            //String query = "SELECT * from movies left join ratings on id = movieId ORDER BY rating DESC limit 20";
            String query = "SELECT * from movies left join ratings on id = movieId WHERE TRUE";


            String Year = request.getParameter("Year");
            if (Year == null || Year == "") {
                Year = (String) session.getAttribute("Year");
            }
            if(Year != null){
                query += " AND movies.year = " + Year;
                session.setAttribute("Year", Year);
            }

            String Director = request.getParameter("Director");
            if(Director == null || Director == ""){
                Director = (String) session.getAttribute("Director");
            }
            if(Director != null){
                query += " AND movies.director LIKE '%" + Director + "%'";
                session.setAttribute("Director", Director);
            }

            String Title = request.getParameter("Title");
            if(Title == null || Title == ""){
                Title = (String) session.getAttribute("Title");
            }
            if(Title != null){
                query += " AND movies.title LIKE '%" + Title + "%'";
                session.setAttribute("Title", Title);
            }


            //If genre parameter exists, change query to find matching genre movies instead
            String Genre = request.getParameter("Genre");
            if(Genre == null || Genre == ""){
                Genre = (String) session.getAttribute("Genre");
            }
            if(Genre != null){
                query = "SELECT m.id, m.title, m.year, m.director, r.rating FROM movies m left join ratings r on m.id = r.movieId, " +
                        "genres, genres_in_movies WHERE genres.id = genres_in_movies.genreId " +
                        "AND genres_in_movies.movieId = m.id " +
                        "AND genres.name = '" + Genre + "'";
                session.setAttribute("Genre", Genre);
                session.setAttribute("start",null);
                session.setAttribute("Title", null);
                session.setAttribute("Director", null);
                session.setAttribute("Year", null);
                session.setAttribute("Full", null);
            }


            //Full text search overrides
            String Full = request.getParameter("Full");
            boolean fts = false;
            String fullTextSearch = "";

            if(Full == null || Full == ""){
                Full = (String) session.getAttribute("Full");
            }
            if(Full != null){
                //loop over each word in full text search
                String[] searchTerms = Full.split("\\s+");

                for (String term : searchTerms) {
                    fullTextSearch += "+" + term + "* ";
                }
                query = "SELECT * FROM movies m left join ratings r ON m.id = r.movieId WHERE MATCH(m.title) AGAINST (? IN BOOLEAN MODE)";
                fts = true;
                session.setAttribute("Full", Full);

            }

            out.println("<body>");



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

            out.println("<td><a href=\"MovieList?reset=true\">" + "Back to Movie Search" + "</a></td>");

            out.println("<h1>Found Movies</h1>");

            //Display active filters
            if(Title != null){
                out.println("<h4>Showing Movies with: "+ Title + "</h4>");
            }
            if(Director != null){
                out.println("<h4>Showing Movies with Director: "+ Director + "</h4>");

            }
            if(Genre != null){
                out.println("<h4>Showing Movies with Genre: "+ Genre + "</h4>");
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



            out.println("<table border>");

            // Add table header row
            {
                out.println("<tr>");
                out.println("<td>Title</a></td>");
                out.println("<td>Year</td>");
                out.println("<td>Director</td>");
                out.println("<td>Genres</td>");
                out.println("<td>Stars</td>");
                out.println("<td>Rating</td>");
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
                out.println("<td>" + director + "</td>");


                //Find first three genres

                out.println("<td>");
                int index = 0;
                for (List<String> x : movieGenresMap.get(movieid)){
                    if (index != 0) {
                        out.println(",");
                    }
                    out.println("<a href=\"MovieList?Genre=" + x.get(0) + "\">" + x.get(1) + "</a>");
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