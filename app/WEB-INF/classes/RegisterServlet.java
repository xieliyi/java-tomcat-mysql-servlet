
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import java.sql.*;


public class RegisterServlet extends HttpServlet {

	private static final int REGISTER_OK = 0;    
	private static final int REGISTER_ERROR_NAME = 1;    
	private static final int REGISTER_ERROR_PASSWORD = 2;   
	
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
		response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
		
		request.setCharacterEncoding("utf-8"); 
		Writer out = response.getWriter();
        String username=request.getParameter("username");  
        String password=request.getParameter("password");
        System.out.println("username : " + username);
        
        out.write("username:"+username);
        out.write("<br>");
        out.write("password:"+password);  
        out.write("<br>");
        
        int registered = Register(username,password);
        String registered_str = "register success!";
        
        if(registered == REGISTER_ERROR_NAME) {
        	registered_str = "register failed: error name!" ;
        }
        else if(registered == REGISTER_ERROR_PASSWORD) {
        	registered_str = "register failed: error password!" ;
        }
        out.write(registered_str);
        out.write("<br>");

        out.flush();  
        out.close(); 
    }

    public void doPost(HttpServletRequest request,
                      HttpServletResponse response)
        throws IOException, ServletException
    {
        doGet(request, response);
    }

    public int Register (String username, String password)
    {
    	final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";  
        final String DB_URL = "jdbc:mysql://localhost:3306/pet?useSSL=false&serverTimezone=GMT%2B8";
     
        final String USER = "root";
        final String PASS = "xie123";
        int registered = REGISTER_OK;
        	
        Connection conn = null;
        Statement stmt = null;
        
        if(username==null || username.length() == 0) {
        	System.out.println("username error!");
        	return REGISTER_ERROR_NAME;
        }
        if(password==null || password.length() == 0) {
        	System.out.println("password error!");
        	return REGISTER_ERROR_PASSWORD;
        }
        
        try {
        	
            Class.forName(JDBC_DRIVER);
        
            System.out.println("connecting...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
			if(!conn.isClosed()) {
				
				System.out.println("Succeeded connecting to the Database!");
			}
			
            System.out.println("create Statement...");
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT * FROM user_table where username = '" + username + "'";
            ResultSet rs = stmt.executeQuery(sql);
            rs.last();
            if(rs.getRow() > 0)
            {
            	registered = REGISTER_ERROR_NAME;
            	System.out.println("This name registered!");
            }
            else
            {
            	PreparedStatement pStmt = null;
                try
                {
                	sql = "insert into user_table (username,password) values (?,?)";
                    pStmt = conn.prepareStatement(sql);
                    pStmt.setString(1, username);
                    pStmt.setString(2, password);
                    pStmt.executeUpdate();
                    pStmt.close();
                }catch(SQLException e){
                    e.printStackTrace();
                }catch (Exception e) {
                    e.printStackTrace();
                }finally{
                    try{
                        if(pStmt!=null) 
                        {
                        	pStmt.close();
                        }
                    }catch(SQLException se2){
                    	se2.printStackTrace();
                    }
                }
            }
			
			if(registered == REGISTER_OK)
			{
				System.out.println("register success!");
			}
			else
			{
				System.out.println("register failed!");
			}
			
            rs.close();
            stmt.close();
            conn.close();
        }catch(SQLException se){
            se.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(stmt!=null) 
                {
                	stmt.close();
                }
            }catch(SQLException se2){
            	se2.printStackTrace();
            }
            try{
                if(conn!=null) conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
        System.out.println("Goodbye!");
		return registered;
    }

}

