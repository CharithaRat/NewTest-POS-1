import javax.annotation.Resource;
import javax.json.*;
import javax.json.stream.JsonParsingException;
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

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            JsonReader reader = Json.createReader(req.getReader());
            JsonObject item = reader.readObject();

            String code = item.getString("Code");
            String description = item.getString("Description");
//            double unitPrice = item.getInt("UnitPrice");
//            double unit = BigDecimal.valueOf(item.getDouble("UnitPrice")).doubleValue();
//            double qty = item.getInt("QtyOnHand");
            double unitPrice = Double.parseDouble(item.getString("UnitPrice"));
            double qty = Double.parseDouble(item.getString("QtyOnHand"));

            Connection connection = ds.getConnection();
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO ITEM VALUES(?,?,?,?)");
            stmt.setObject(1, code);
            stmt.setObject(2, description);
            stmt.setObject(3, unitPrice);
            stmt.setObject(4, qty);
            int affectedrows = stmt.executeUpdate();
            System.out.println("Save Item");
            if (affectedrows > 0) {
                resp.setStatus(HttpServletResponse.SC_CREATED);
            } else {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
            connection.close();
        } catch (JsonParsingException | NullPointerException ex) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String code = req.getParameter("Code");

        if (code != null) {
            try {
                Connection connection = ds.getConnection();
                PreparedStatement statement = connection.prepareStatement("DELETE FROM ITEM WHERE Code=?");
                statement.setObject(1, code);
                int affectedrows = statement.executeUpdate();
                if (affectedrows > 0) {
                    resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                } else {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
                connection.close();
            } catch (Exception e) {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("Code") != null) {
            try {

                JsonReader reader = Json.createReader(req.getReader());
                JsonObject jsonObject = reader.readObject();

                String code = jsonObject.getString("Code");
                String description = jsonObject.getString("Description");
//                int price = jsonObject.getInt("UnitPrice");
//                int qty = jsonObject.getInt("QtyOnHand");
                double price = Double.parseDouble(jsonObject.getString("UnitPrice"));
                double qty = Double.parseDouble(jsonObject.getString("QtyOnHand"));

                if (!code.equals(req.getParameter("Code"))) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }

                Connection connection = ds.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("UPDATE ITEM SET Description=?, UnitPrice=?, QtyOnHand=? WHERE Code=?");

                preparedStatement.setObject(4, code);
                preparedStatement.setObject(1, description);
                preparedStatement.setObject(2, price);
                preparedStatement.setObject(3, qty);

                int affectrows = preparedStatement.executeUpdate();

                if (affectrows > 0) {
                    resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
//                    System.out.println("xxxx");
                } else {
                    resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//                    System.out.println("yyyy");
                }
                connection.close();
            } catch (JsonParsingException | NullPointerException ex) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            } catch (Exception e) {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//                System.out.println("zzzz");
                e.printStackTrace();
            }
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
