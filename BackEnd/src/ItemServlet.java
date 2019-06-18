import javax.annotation.Resource;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet(urlPatterns = "/items")
public class ItemServlet extends HttpServlet {

    @Resource(name="java:comp/env/jdbc/pool")
    private DataSource ds;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();

        if (req.getParameter("Code") != null) {
            String code = req.getParameter("Code");
            try {
                Connection connection = ds.getConnection();
                PreparedStatement pst = connection.prepareStatement("SELECT * FROM ITEM WHERE Code = ?");
                pst.setObject(1, code);
                ResultSet set = pst.executeQuery();

                if (set.next()) {
                    JsonObjectBuilder ob = Json.createObjectBuilder();
                    ob.add("Code", set.getString(1));
                    ob.add("Description", set.getString(2));
                    ob.add("UnitPrice", set.getString(3));
                    ob.add("QtyOnHand", set.getString(4));
                    resp.setContentType("application/json");
                    resp.setCharacterEncoding("UTF-8");
                    writer.println(ob.build());
                    writer.flush();
                    System.out.println("Single Get Item");
                    connection.close();
                } else {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
//                    resp.sendError(HttpServletResponse.//Where the add code for error to return client from server like [404: PAGE NOT FOUND]);
                }
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            try {
                Connection connection = ds.getConnection();
                PreparedStatement stmt = connection.prepareStatement("SELECT * FROM ITEM");
                ResultSet rs = stmt.executeQuery();

                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");

                JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

                while (rs.next()) {
                    JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                    objectBuilder.add("Code", rs.getString(1));
                    objectBuilder.add("Description", rs.getString(2));
                    objectBuilder.add("UnitPrice", rs.getDouble(3));
                    objectBuilder.add("QtyOnHand", rs.getDouble(4));
                    arrayBuilder.add(objectBuilder.build());
                    System.out.println("All Get Item");
                }

                writer.println(arrayBuilder.build());
                writer.flush();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
