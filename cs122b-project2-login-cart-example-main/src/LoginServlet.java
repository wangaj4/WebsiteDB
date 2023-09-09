import com.google.gson.JsonObject;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
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
import org.jasypt.util.password.StrongPasswordEncryptor;


@WebServlet(name = "LoginServlet", urlPatterns = "/api/login")
public class LoginServlet extends HttpServlet {
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    public DataSource dataSource;


    public void init(ServletConfig config) {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/moviedb");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {


        //Check recaptcha
        String gRecaptchaResponse = request.getParameter("g-recaptcha-response");

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        /* This example only allows username/password to be test/test
        /  in the real project, you should talk to the database to verify username/password
        */
        response.setContentType("text/html");    // Response mime type

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();
        JsonObject responseJsonObject = new JsonObject();

        try{


            // create database connection
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/moviedb");
            Connection dbCon = dataSource.getConnection();


            if (gRecaptchaResponse == null || gRecaptchaResponse.isEmpty()) {
                responseJsonObject.addProperty("status", "fail");
                responseJsonObject.addProperty("message", "error: Input Captcha");
                response.getWriter().write(responseJsonObject.toString());
                return;
            }

            if(username.length()==0 || password.length()==0){
                responseJsonObject.addProperty("status", "fail");
                responseJsonObject.addProperty("message", "Please fill in both fields");
                response.getWriter().write(responseJsonObject.toString());
                return;
            }

            // Declare a new statement
            // Generate a SQL query
            //String query = String.format("SELECT * from customers where email = '%s'", username);
            String query = "SELECT * from customers where email = ?";
            PreparedStatement statement = dbCon.prepareStatement(query);
            statement.setString(1,username);

            // Log to localhost log
            request.getServletContext().log("query：" + query);



            // Perform the query
            ResultSet usernames = statement.executeQuery();



            Boolean existinguser = false;


            String correspondingPassword = "";
            Boolean success = false;
            Boolean employee = false;
            while (usernames.next()){
                existinguser = true;
                correspondingPassword = usernames.getString("password");
                success = new StrongPasswordEncryptor().checkPassword(password, correspondingPassword);

            }
            if (!existinguser){
                query = "SELECT * from employees where email = ?";
                statement = dbCon.prepareStatement(query);
                statement.setString(1,username);
                ResultSet employeeNames = statement.executeQuery();
                while (employeeNames.next()){
                    existinguser = true;
                    correspondingPassword = employeeNames.getString("password");
                    success = password.equals(correspondingPassword);
                    employee = true;
                }

            }


            if (existinguser && success) {
                // Login success:
                // set this user into the session
                request.getSession().setAttribute("user", new User(username));

                if(employee == true){
                    responseJsonObject.addProperty("status", "employee");
                    responseJsonObject.addProperty("message", "employee");
                }else{
                    responseJsonObject.addProperty("status", "success");
                    responseJsonObject.addProperty("message", "success");
                }



            } else {
                // Login fail
                responseJsonObject.addProperty("status", "fail");
                // Log to localhost log
                request.getServletContext().log("Login failed");
                // sample error messages. in practice, it is not a good idea to tell user which one is incorrect/not exist.
                if (!existinguser) {
                    responseJsonObject.addProperty("message", "error: user " + username + " doesn't exist");
                } else {
                    responseJsonObject.addProperty("message", "error: incorrect password");
                }
            }
            response.getWriter().write(responseJsonObject.toString());
        }
        catch(Exception e){
            request.getServletContext().log("Error: ", e);
            responseJsonObject.addProperty("status", "fail");
            responseJsonObject.addProperty("message", "error:" + e);
            response.getWriter().write(responseJsonObject.toString());
        }



    }
}
