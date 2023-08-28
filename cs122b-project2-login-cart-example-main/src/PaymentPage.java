import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;


// This annotation maps this Java Servlet Class to a URL
@WebServlet("/Payment")
public class PaymentPage extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {



        response.setContentType("text/html");

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

        out.println("        </div>");

        out.println("        <div class=\"navbar-spacer\" id=\"spacer\"></div>");



        try {




            HttpSession session = request.getSession();
            CartList cart = (CartList) session.getAttribute("cart");


                out.println("<h1>Payment Page</h1>");
                int totalPrice = 0;
                for (MovieInCart item : cart.getCart()) {
                    totalPrice += item.getPrice() * item.getCount();
                }
                out.println("<h2>Total Price: " + totalPrice + "</h2>");



            out.println("<form method='post' action=Order>");

            out.println("<label><b>Card Holder First Name</b></label>");
            out.println("<label><input name='first' placeholder='Enter' type='text'></label><br>");

            out.println("<label><b>Card Holder Last Name</b></label>");
            out.println("<label><input name='last' placeholder='Enter' type='text'></label><br>");

            out.println("<label><b>Card Number</b></label>");
            out.println("<label><input name='num' placeholder='Enter' type='text'></label><br>");

            out.println("<label><b>Expiration Date</b></label>");
            out.println("<label><input name='date' placeholder='YYYY/MM/DD' type='text'></label><br>");

            out.println("<input type='submit' value='Place Order'>");
            out.println("</form>");


            out.println("</body>");





        } catch (Exception e) {

            request.getServletContext().log("Error: ", e);

            out.println("<body>");
            out.println("<p>");
            out.println("Exception in doGet: " + e.getMessage());
            out.println("</p>");
            out.print("</body>");
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
