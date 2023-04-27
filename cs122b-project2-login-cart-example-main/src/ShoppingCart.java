import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


// This annotation maps this Java Servlet Class to a URL
@WebServlet("/ShoppingCart")
public class ShoppingCart extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {



        response.setContentType("text/html");

        PrintWriter out = response.getWriter();

        out.println("<html>");

        try {


            out.println("<body>");

            out.println("<P align = \"right\"><a href=\"Payment\">" + "Proceed to Payment" + "</a></P align = \"right\"></td>");

            HttpSession session = request.getSession();
            CartList cart = (CartList) session.getAttribute("cart");

            if (cart == null || cart.getCart().isEmpty()) {
                out.println("<h1>Your cart is empty</h1>");
                out.println("<td><a href=\"MovieList\">" + "Back to Movie List" + "</a>");
            } else {
                out.println("<td><a href=\"MovieList\">" + "Back to Movie List" + "</a>");
                out.println("<h2>Shopping Cart</h2>");
                out.println("<table>");
                out.println("<thead><tr><th>Item Name</th><th>Price per Movie</th><th>Quantity</th></tr></thead>");
                out.println("<tbody>");
                int totalPrice = 0;
                for (MovieInCart item : cart.getCart()) {
                    out.println("<tr><td>" + item.getName() + "</td><td>" + item.getPrice() + "</td><td>" + item.getCount() + "</td><td>");

                    out.println("<form method='post' action='ShoppingCart?add=" + item.getName() + "'>");
                    out.println("<input type='submit' value='Add'>");
                    out.println("</form></td><td>");

                    out.println("<form method='post' action='ShoppingCart?remove=" + item.getName() + "'>");
                    out.println("<input type='submit' value='Remove One'>");
                    out.println("</form></td><td>");

                    out.println("<form method='post' action='ShoppingCart?delete=" + item.getName() + "'>");
                    out.println("<input type='submit' value='Delete'>");
                    out.println("</form>");

                    out.println("</td></tr>");
                    totalPrice += item.getPrice() * item.getCount();
                }
                out.println("<tr><td>Total Price:</td><td>" + totalPrice + "</td></tr>");
                out.println("</tbody>");
                out.println("</table>");
            }


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


    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        CartList cart = (CartList) session.getAttribute("cart");

        String toDelete = request.getParameter("delete");
        if(toDelete != null){
            cart.deleteFromCart(toDelete);
        }

        String toAdd = request.getParameter("add");
        if(toAdd != null){
            cart.addToCartString(toAdd);
        }

        String toRemove = request.getParameter("remove");
        if(toRemove != null){
            cart.removeFromCart(toRemove);
        }

        session.setAttribute("cart",cart);
        response.sendRedirect("ShoppingCart");

    }



}
