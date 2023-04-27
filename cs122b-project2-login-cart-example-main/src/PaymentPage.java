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

        out.println("<html>");

        try {


            out.println("<body>");

            HttpSession session = request.getSession();
            CartList cart = (CartList) session.getAttribute("cart");

                out.println("<a href=\"ShoppingCart\">" + "Back to Cart" + "</a>");
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

        out.println("</html>");
        out.close();

    }




}
