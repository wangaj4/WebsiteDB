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

        out.println("<html>");
        out.println("<head><title>Fabflix</title></head>");
        out.println("<P align = \"right\"><a href=\"ShoppingCart\">" + "Proceed to Cart" + "</a></P align = \"right\"></td>");


        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            // create database connection
            Connection connection = dataSource.getConnection();
            // declare statement

            // prepare query
            String id = request.getParameter("id");
            String query = "SELECT * from stars WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,id);
            // execute query
            ResultSet resultSet = statement.executeQuery();


            out.println("<body>");
            out.println("<td><a href=\"MovieList\">" + "Back to Movie List" + "</a></td>");
            //out.println("<a href=\"javascript:history.back()\">Back to Movie List</a>");
            out.println("<p></p>");

            resultSet.next();
            String starName = resultSet.getString("name");
            String birth = resultSet.getString("birthYear");
            if(birth==null){
                birth = "N/A";
            }
            out.println("<h1>"+ starName + "</h1>");
            out.println("<h2>Birth Year: "+ birth + "</h2>");
            out.println("<h2>Movies acted in: </h2>");

            //Get all movies that the star is in
            // prepare query
            String query2 = "SELECT * from stars_in_movies left join movies on movieId = id WHERE starId = ? ORDER BY year desc, title";
            // execute query
            statement = connection.prepareStatement(query2);
            statement.setString(1,id);
            ResultSet movieSet = statement.executeQuery();

            String movieid = "";
            String moviename = "";
            while (movieSet.next()){
                movieid = movieSet.getString("movieId");
                moviename = movieSet.getString("title");
                out.println("<p><a href=\"Movie?id=" + movieid + "\">" + moviename + "</a></p>");
            }

            out.println("</body>");

            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            /*
             * After you deploy the WAR file through tomcat manager webpage,
             *   there's no console to see the print messages.
             * Tomcat append all the print messages to the file: tomcat_directory/logs/catalina.out
             *
             * To view the last n lines (for example, 100 lines) of messages you can use:
             *   tail -100 catalina.out
             * This can help you debug your program after deploying it on AWS.
             */
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
