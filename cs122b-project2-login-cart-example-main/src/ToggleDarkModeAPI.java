import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.PrintWriter;


@WebServlet(name = "ToggleDarkModeAPI", urlPatterns = "/api/darkmode")
public class ToggleDarkModeAPI extends HttpServlet {


    public void doGet(HttpServletRequest request, HttpServletResponse response){

        String dark = request.getParameter("dark");
        String get = request.getParameter("get");

        try {
            HttpSession session = request.getSession();
            if(dark!=null) {
                session.setAttribute("dark", dark);
            }


            if(get!=null){
                String current = (String) session.getAttribute("dark");
                PrintWriter out = response.getWriter();
                out.print(current);
            }



        }catch (Exception e){
            request.getServletContext().log("Error: ", e);
        }



    }
}
