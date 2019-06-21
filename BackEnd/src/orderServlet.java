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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet(urlPatterns = "/orders")
public class orderServlet extends HttpServlet {

    @Resource(name = "java:comp/env/jdbc/pool")
    private DataSource ds;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (PrintWriter out = resp.getWriter()) {

            if (req.getParameter("OID") != null) {
                String oid = req.getParameter("OID");
                try {
                    Connection connection = ds.getConnection();
                    PreparedStatement pstm = connection.prepareStatement("SELECT * FROM order WHERE OID=?");
                    pstm.setObject(1, oid);
                    ResultSet rst = pstm.executeQuery();

                    if (rst.next()) {
                        JsonObjectBuilder ob = Json.createObjectBuilder();
                        ob.add("OID", rst.getString(1));
//                        ob.add("ordDate",rst.getDate(2));
                        ob.add("itemName", rst.getString(3));
                        ob.add("unitPrice", rst.getDouble(4));
                        ob.add("qty", rst.getDouble(5));
                        ob.add("amount", rst.getDouble(6));
                        resp.setContentType("application/json");

                        System.out.println("Success@Get-Single");
                        out.println(ob.build());
                        connection.close();
                    } else {
                        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                try {
                    Connection con = ds.getConnection();
                    Statement stm = con.createStatement();
                    ResultSet rst = stm.executeQuery("SELECT * FROM order");

                    resp.setContentType("application/json");
                    resp.setCharacterEncoding("UTF-8");

                    JsonArrayBuilder ab = Json.createArrayBuilder();

                    while (rst.next()) {
                        JsonObjectBuilder ob = Json.createObjectBuilder();
                        ob.add("OID", rst.getString("OID"));
//                        ob.add("ordDate", rst.getDate("name"));

                        ob.add("itemName", rst.getString("itemName"));
                        ob.add("unitPrice", rst.getDouble("unitPrice"));
                        ob.add("unitQty", rst.getDouble("qty"));
                        ob.add("amount", rst.getDouble("amount"));
                        ab.add(ob.build());
                    }
                    System.out.println("Success@Get-All");
                    out.println(ab.build());
                    con.close();
                } catch (Exception ex) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                    ex.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            JsonReader reader = Json.createReader(req.getReader());
            JsonObject order = reader.readObject();

            String oid = order.getString("OID");
            String odate = order.getString("ordDate");
            Date odate1=new SimpleDateFormat("dd/MM/yyyy").parse(odate);
            String item = order.getString("itemName");
            double unitp = Double.parseDouble(order.getString("unitPrice"));
            double qty = Double.parseDouble(order.getString("qty"));
            double unamount = Double.parseDouble(order.getString("amount"));

            Connection con = ds.getConnection();
            PreparedStatement pstm = con.prepareStatement("INSERT INTO order VALUES (?,?,?,?,?,?)");
            pstm.setObject(1, oid);
            pstm.setObject(2, odate);
            pstm.setObject(3, item);
            pstm.setObject(4, unitp);
            pstm.setObject(5, qty);
            pstm.setObject(6, unamount);
            int affectedRows = pstm.executeUpdate();

            if (affectedRows > 0) {
                resp.setStatus(HttpServletResponse.SC_CREATED);
            } else {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }

        } catch (JsonParsingException | NullPointerException ex) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception ex) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String oid = req.getParameter("OID");

        if (oid != null) {

            try {
                Connection connection = ds.getConnection();
                PreparedStatement pstm = connection.prepareStatement("DELETE FROM order WHERE OID=?");
                pstm.setObject(1, oid);
                int affectedRows = pstm.executeUpdate();

                if (affectedRows > 0) {
                    resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                } else {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
                connection.close();
            } catch (Exception ex) {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                ex.printStackTrace();
            }

        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("OID") != null) {

            try {
                JsonReader reader = Json.createReader(req.getReader());
                JsonObject order = reader.readObject();

                String oid = order.getString("OID");
                String odate = order.getString("ordDate");
                Date odate1=new SimpleDateFormat("dd/MM/yyyy").parse(odate);
                String item = order.getString("itemName");
                double unitp = Double.parseDouble(order.getString("unitPrice"));
                double qty = Double.parseDouble(order.getString("qty"));
                double unamount = Double.parseDouble(order.getString("amount"));

                if (!oid.equals(req.getParameter("OID"))) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }

                Connection connection = ds.getConnection();
                PreparedStatement pstm = connection.prepareStatement("UPDATE order SET ordDate=?, itemName=?, unitPrice=?, qty=?, amount=? WHERE id=?");

                pstm.setObject(1, odate1);
                pstm.setObject(2, item);
                pstm.setObject(3, unitp);
                pstm.setObject(4, qty);
                pstm.setObject(5, unamount);
                pstm.setObject(6, oid);
                int affectedRows = pstm.executeUpdate();
                if (affectedRows > 0) {
                    resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                } else {
                    resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }
                connection.close();
            } catch (JsonParsingException | NullPointerException ex) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            } catch (Exception ex) {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }

        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
