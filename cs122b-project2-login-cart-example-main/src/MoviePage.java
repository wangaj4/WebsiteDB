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
import javax.servlet.ServletException;
import javax.sql.DataSource;

import jakarta.servlet.http.*;
import jakarta.servlet.RequestDispatcher;
import org.apache.commons.dbcp2.BasicDataSource;

import java.io.BufferedWriter;
import java.io.FileWriter;


// This annotation maps this Java Servlet Class to a URL
@WebServlet("/MovieList")
public class MoviePage extends HttpServlet{
    private static final long serialVersionUID = 1L;

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
        long startTimeTS = System.nanoTime();

        // Set response mime type
        response.setContentType("text/html");

        // Get the PrintWriter for writing response
        PrintWriter out = response.getWriter();

        out.println("<html>");
        out.println("<head><title>Fabflix</title></head>");
        out.println("<P align = \"right\"><a href=\"ShoppingCart\">" + "Proceed to Cart" + "</a></P align = \"right\"></td>");


        try {

            BasicDataSource dataSource = new BasicDataSource();
            dataSource.setUrl("jdbc:mysql://localhost:3306/moviedb");
            dataSource.setUsername("mytestuser");
            dataSource.setPassword("My6$Password");
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


            //Browse by title matching starting letter
            String start = request.getParameter("start");
            if(start == null || start == ""){
                start = (String) session.getAttribute("start");
            }
            if(start != null){
                if (start.equals("*")){
                    query += " AND movies.title REGEXP '^[^a-zA-Z0-9]'";
                }else {
                    query += " AND movies.title LIKE '" + start + "%'";
                }
                session.setAttribute("start",start);
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
            if(fts == true){
                statement.setString(1,fullTextSearch.trim());
            }

            //out.println("<h1>" + query + "</h1>");

            long startTimeTJ = System.nanoTime();
            ResultSet resultSet = statement.executeQuery();
            long endTimeTJ = System.nanoTime();
            long elapsedTimeTJ = endTimeTJ - startTimeTJ; // elapsed time in nano seconds. Note: print the values in nanoseconds

            out.println("<td><a href=\"MovieList?reset=true\">" + "Back to Movie Search" + "</a></td>");

            out.println("<h1>Found Movies</h1>");

            //Display active filters
            out.println("<h4>Title Filter: "+ Title + "</h4>");
            out.println("<h4>Year Filter: "+ Year + "</h4>");
            out.println("<h4>Director Filter: "+ Director + "</h4>");
            out.println("<h4>First Letter Filter: "+ start + "</h4>");
            out.println("<h4>Genre Filter: "+ Genre + "</h4>");

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
                String genre = "SELECT name FROM genres left join genres_in_movies on id = genreId WHERE movieId = '"+movieid+"' order by name limit 3";

                statement = connection.prepareStatement(genre);
                ResultSet genreSet = statement.executeQuery(genre);

                out.println("<td>");

                genreSet.next();
                String genreString = genreSet.getString("name");
                out.println("<a href=\"MovieList?Genre=" + genreString + "\">" + genreString + "</a>");
                while (genreSet.next()) {
                    genreString = genreSet.getString("name");
                    out.println(", <a href=\"MovieList?Genre=" + genreString + "\">" + genreString + "</a>");
                }
                genreSet.close();
                out.println("</td>");

                //Find first three stars
                String star = "SELECT * FROM stars left join stars_in_movies on id = starId WHERE movieId = '"+movieid+"' order by name limit 3";

                statement = connection.prepareStatement(star);
                ResultSet starSet = statement.executeQuery(star);


                starSet.next();
                String starString = starSet.getString("name");
                String starid = starSet.getString("starId");
                out.println("<td>");
                out.println("<a href=\"stars?id=" + starid + "\">" + starString + "</a>");
                while (starSet.next()){
                    starString = starSet.getString("name");
                    starid = starSet.getString("starId");
                    out.println(",");
                    out.println("<a href=\"stars?id=" + starid + "\">" + starString + "</a>");


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
            statement.close();
            connection.close();

            long endTimeTS = System.nanoTime();
            long elapsedTimeTS = endTimeTS - startTimeTS;

            String filePath = "/home/ubuntu/output.txt";
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            writer.write("\nTS: " + elapsedTimeTS);
            writer.write("\nTJ: " + elapsedTimeTJ);
            writer.flush();
            writer.close();

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
