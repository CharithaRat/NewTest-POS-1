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

@WebServlet(urlPatterns = "/customers")
public class CustomerServlet extends HttpServlet {

    @Resource(name="java:comp/env/jdbc/pool")
    private DataSource ds;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();

        try {
            Connection connection = ds.getConnection();
            Statement stm = connection.createStatement();
            ResultSet rst = stm.executeQuery("SELECT * FROM Customer");

            resp.setContentType("application/json");
            JsonArrayBuilder ab = Json.createArrayBuilder();

            while (rst.next()) {
                JsonObjectBuilder ob = Json.createObjectBuilder();
                ob.add("id", rst.getString("id"));
                ob.add("name", rst.getString("name"));
                ob.add("address", rst.getString("address"));
                ab.add(ob.build());
            }
            out.println(ab.build().toString());
//            System.out.println("doGet Executed");
            connection.close();
        } catch (Exception ex) {
            resp.sendError(500, ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            JsonReader reader = Json.createReader(req.getReader());
            JsonObject customer = reader.readObject();

            String id = customer.getString("id");
            String name = customer.getString("name");
            String address = customer.getString("address");

            Connection con = ds.getConnection();
            PreparedStatement pstm = con.prepareStatement("INSERT INTO Customer1 VALUES (?,?,?)");
            pstm.setObject(1,id);
            pstm.setObject(2,name);
            pstm.setObject(3,address);
            int affectedRows = pstm.executeUpdate();

            if (affectedRows > 0){
                resp.setStatus(HttpServletResponse.SC_CREATED);
            }else{
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }

        }catch (JsonParsingException | NullPointerException  ex){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }catch (Exception ex){
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

    }
}
