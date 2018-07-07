
import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
import javax.servlet.*;
import javax.servlet.http.*;

import java.sql.*;


public class InquireServlet extends HttpServlet {  
	
	private static final int INQUIRE_OK = 0;    
	private static final int INQUIRE_ERROR_SN = 1;    
	
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
		response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
		
		request.setCharacterEncoding("utf-8");
		Writer out = response.getWriter();
        String sn =request.getParameter("sn");
        System.out.println("sn : " + sn);

        out.write("SN:"+sn); 
        out.write("<br>");
        
        UserData inquired = Inquire(sn);
        String inquired_str = "inquire success!";
        
        if(inquired.statu == INQUIRE_ERROR_SN) {
        	inquired_str = "inquire failed: error sn!" ;
        }
        else if(inquired.statu == INQUIRE_OK) {
        	
            out.write("name:    " + inquired.name);  
            out.write("<br>");
            out.write("mobile:  " + inquired.mobile);  
            out.write("<br>");
            out.write("address: " + inquired.address);  
            out.write("<br>");
        }
        out.write(inquired_str);
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
    
    public class UserData 
    {
    	public int statu;
        public String  name = "";
        public String  mobile = "";
        public String  address = "";
    }

    public UserData Inquire (String sn)
    {
    	final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";  
        final String DB_URL = "jdbc:mysql://localhost:3306/pet?useSSL=false&serverTimezone=GMT%2B8";
     
        final String USER = "root";
        final String PASS = "xie123";
        UserData userData = new UserData();
        userData.statu = INQUIRE_OK;
        	
        Connection conn = null;
        Statement stmt = null;
        
        if(sn==null || sn.length() != 10) {
        	System.out.println("sn error!");
        	userData.statu = INQUIRE_ERROR_SN;
        	return userData;
        }
        else {
        	Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");  
            if( !pattern.matcher(sn).matches()) {
            	System.out.println("sn error error!");
            	userData.statu = INQUIRE_ERROR_SN;
            	return userData;
            }
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
            sql = "SELECT * FROM pet_table where pet_sn = '" + sn + "'";
            ResultSet rs = stmt.executeQuery(sql);
            rs.last();
			System.out.println("execute Query: record = " + rs.getRow() );
			rs.beforeFirst();
            while(rs.next()) {
            	userData.name = rs.getString("pet_name");
            	userData.mobile = rs.getString("pet_mobile");
            	userData.address = rs.getString("pet_address");
            	
            	System.out.println("name:    " + userData.name);
            	System.out.println("mobile:  " + userData.mobile);
            	System.out.println("address: " + userData.address);
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
		return userData;
    }

}

