
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import java.sql.*;


public class LoginServlet extends HttpServlet {
	
	private static final int LOGIN_OK = 0;    
	private static final int LOGIN_ERROR_NAME = 1;    
	private static final int LOGIN_ERROR_PASSWORD = 2;    


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
        //out.write("password:"+password);  
        //out.write("<br>");
        
        int logined = Login(username,password);
        String logined_str = "login success!";
        
        if(logined == LOGIN_ERROR_NAME) {
        	logined_str = "login failed: error name!" ;
        }
        else if(logined == LOGIN_ERROR_PASSWORD) {
        	logined_str = "login failed: error password!" ;
        }
        out.write(logined_str);
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

    public int Login (String username, String password)
    {
    	final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";  
        final String DB_URL = "jdbc:mysql://localhost:3306/pet?useSSL=false&serverTimezone=GMT%2B8";
     
        final String USER = "root";
        final String PASS = "xie123";
        int logined = LOGIN_OK;
        	
        Connection conn = null;
        Statement stmt = null;
        
        if(username==null || username.length() == 0) {
        	System.out.println("username error!");
        	return LOGIN_ERROR_NAME;
        }
        if(password==null || password.length() == 0) {
        	System.out.println("password error!");
        	return LOGIN_ERROR_PASSWORD;
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
            if(rs.getRow() == 0) {
            	logined = LOGIN_ERROR_NAME;
            	System.out.println("username error!");
            }
            else {
    			rs.beforeFirst();
                while(rs.next()) {
                    String pw  = rs.getString("password");
                    if( !pw.equals(password)) {
                    	logined = LOGIN_ERROR_PASSWORD;
                    	System.out.println("password error!");
                    }
                }
            }
			
			if(logined == LOGIN_OK) {
				System.out.println("login success!");
			}
			else {
				System.out.println("login failed!");
			}
			
            rs.close();
            stmt.close();
            conn.close();
            
        }catch(SQLException se) {
            se.printStackTrace();
        }catch(Exception e) {
            e.printStackTrace();
        }finally{
            try{
                if(stmt!=null) {
                	stmt.close();
                }
            }catch(SQLException se2) {
            	se2.printStackTrace();
            }
            try{
                if(conn!=null) {
                	conn.close();
                }
            }catch(SQLException se) {
                se.printStackTrace();
            }
        }
        System.out.println("Goodbye!");
        
        return logined;
    }

}

