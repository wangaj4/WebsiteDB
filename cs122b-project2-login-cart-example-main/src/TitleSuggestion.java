import com.google.gson.JsonArray;
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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/title-suggestion")
public class TitleSuggestion extends HttpServlet {


	public DataSource dataSource;

	public void init(ServletConfig config) {
		try {
			dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/moviedb");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			// setup the response json arrray
			JsonArray jsonArray = new JsonArray();

			// get the query string from parameter
			String str = request.getParameter("query");

			// return the empty json array if query is null or empty
			if (str == null || str.trim().isEmpty()) {
				response.getWriter().write(jsonArray.toString());
				return;
			}

			Class.forName("com.mysql.jdbc.Driver").newInstance();
			// create database connection
			Connection connection = dataSource.getConnection();

			String query = "SELECT * FROM movies WHERE movies.title LIKE ? LIMIT 10";
			PreparedStatement statement = connection.prepareStatement(query);

			statement.setString(1, "%" + str + "%");

			ResultSet resultSet = statement.executeQuery();
			while(resultSet.next()){
				String title = resultSet.getString("title");
				String id = resultSet.getString("id");
				jsonArray.add(generateJsonObject(id, title));
			}

			response.getWriter().write(jsonArray.toString());
		} catch (Exception e) {
			System.out.println(e);
			response.sendError(500, e.getMessage());
		}
	}


	private static JsonObject generateJsonObject(String id, String title) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("value", title);

		JsonObject additionalDataJsonObject = new JsonObject();
		additionalDataJsonObject.addProperty("id", id);

		jsonObject.add("data", additionalDataJsonObject);
		return jsonObject;
	}


}
