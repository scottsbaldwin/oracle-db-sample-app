package app;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Helloworld extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String dataSourceName = "MyDS";

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<h1>Hello World!</h1>");

        out.println("<form method=\"post\" action=\"" + request.getContextPath() + "\">");
        out.println("<div>Your name: <input name=\"author\"/></div>");
        out.println("<div>Your message: <input name=\"message\"/></div>");
        out.println("<div><input type=\"submit\" value=\"Submit\"/></div>");
        out.println("</form>");
        out.println(getMessageFromDatabase());
    }

    protected void doPost(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        String author = request.getParameter("author");
        String message = request.getParameter("message");
        setMessageToDatabase(author, message);
        response.sendRedirect(request.getContextPath());
    }

    private void setMessageToDatabase(String author, String message) {
        Context ctx = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        StringBuilder sb = new StringBuilder();
        try {
            ctx = new InitialContext();
            javax.sql.DataSource ds = (javax.sql.DataSource) ctx.lookup(dataSourceName);
            conn = ds.getConnection();
            stmt = conn.prepareStatement("insert into messages (author, message) values (?, ?)");
            stmt.setString(1, author);
            stmt.setString(2, message);
            stmt.execute();

            // Do something with the result set
            stmt.close();
            stmt=null;
            conn.close();
            conn=null;
        }
        catch (Exception e) {
            // a failure occurred
            System.out.println(e.getMessage());
        }
        finally {
            try {
                ctx.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            try {
                if (rs != null) rs.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            try {
                if (stmt != null) stmt.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            try {
                if (conn != null) conn.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private String getMessageFromDatabase() {
        Context ctx = null;
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        StringBuilder sb = new StringBuilder();
        try {
            //ctx = new InitialContext(ht);
            ctx = new InitialContext();
            javax.sql.DataSource ds = (javax.sql.DataSource) ctx.lookup(dataSourceName);
            conn = ds.getConnection();
            stmt = conn.createStatement();
            stmt.execute("select author, message from messages");
            rs = stmt.getResultSet();
            while (rs.next()) {
                sb.append("<div>")
                        .append(rs.getString("author"))
                        .append(" said: ")
                        .append(rs.getString("message"))
                        .append("</div>");
            }

            // Do something with the result set
            stmt.close();
            stmt=null;
            conn.close();
            conn=null;
        }
        catch (Exception e) {
            // a failure occurred
            System.out.println(e.getMessage());
        }
        finally {
            try {
                ctx.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            try {
                if (rs != null) rs.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            try {
                if (stmt != null) stmt.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            try {
                if (conn != null) conn.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return sb.toString();

    }

}