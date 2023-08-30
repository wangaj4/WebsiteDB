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
        out.println("    <title>FlickBase Checkout</title>");
        out.println("    <link rel=\"stylesheet\" href=\"style.css\">");
        out.println("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
        out.println("    <!-- jQuery is required -->");
        out.println("    <script src=\"https://code.jquery.com/jquery-3.6.4.min.js\"></script>");
        out.println("    <!-- include jquery autocomplete JS  -->");
        out.println("    <script src=\"https://cdnjs.cloudflare.com/ajax/libs/jquery.devbridge-autocomplete/1.4.11/jquery.autocomplete.min.js\"></script>");
        out.println("</head>");
        out.println("<body>");

//        out.println("    <script>");
//        out.println("        alert('Do not enter any information! This is NOT a real checkout page');");
//        out.println("    </script>");

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


            out.println("<div class = 'split'>");
            out.println("<div class = 'split-page'>");
            out.println("<div class = 'display-contents darken'>");
                out.println("<h1>Checkout</h1>");
                int totalPrice = 0;
                for (MovieInCart item : cart.getCart()) {
                    out.println("<div class = 'confirm-item'>");
                    out.println("<p style = 'font-weight:bold'>" + item.getName() + " * " + item.getCount() + "</p>");
                    out.println("<p>$" + item.getCount() * item.getPrice() + ".00</p>");
                    out.println("</div>");


                    totalPrice += item.getPrice() * item.getCount();


                }
                out.println("<h2>Subtotal: $" + totalPrice + ".00</h2>");
            out.println("<div class=\"shipping-form darken\">");
            out.println("<h3>Shipping Information</h3>");
            out.println("<div class=\"form-group\">");
            out.println("<input name='first' placeholder='First Name' type='text' class = 'shipping-info'><br>");
            out.println("</div>");

            out.println("<div class=\"form-group\">");
            out.println("<input name='last' placeholder='Last Name' type='text' class = 'shipping-info'><br>");
            out.println("</div>");

            out.println("<div class=\"form-group\">");
            out.println("<input name='address' placeholder='Address' type='text' class = 'shipping-info'><br>");
            out.println("</div>");

            out.println("<div class=\"form-group-half\">");
            out.println("<input name='zip' placeholder='ZIP Code' type='text' class = 'shipping-info-half'><br>");
            out.println("</div>");

            out.println("<div class=\"form-group-half\">");
            out.println("<input name='state' placeholder='State' type='text' class = 'shipping-info-half'><br>");
            out.println("</div>");


            out.println("<div class='coverup'></div>");

            out.println("</div>");

            out.println("</div>");
            out.println("</div>");

            out.println("<div class = 'split-page'>");
            out.println("<div class=\"payment-form darken\">");

            out.println("<div class=\"form-group\">");
            out.println("<label><b>Card Holder First Name</b></label>");
            out.println("<input name='first' placeholder='Enter' type='text' class = 'payment-info'><br>");
            out.println("</div>");

            out.println("<div class=\"form-group\">");
            out.println("<label><b>Card Holder Last Name</b></label>");
            out.println("<input name='last' placeholder='Enter' type='text' class = 'payment-info'><br>");
            out.println("</div>");

            out.println("<div class=\"form-group\">");
            out.println("<label><b>Card Number</b></label>");
            out.println("<input name='num' placeholder='Enter' type='text' class = 'payment-info'><br>");
            out.println("</div>");

            out.println("<div class=\"form-group\">");
            out.println("<label><b>Expiration Date</b></label>");
            out.println("<input name='date' placeholder='YYYY/MM/DD' type='text' class = 'payment-info'><br>");
            out.println("</div>");

            out.println("<div style='text-align:center;'>");
            out.println("<input type='submit' value='Place Order' class = 'payment-button'>");
            out.println("</div>");

            out.println("<div class='coverup'></div>");

            out.println("</div>");

            out.println("</div>");

            out.println("</div>");


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
