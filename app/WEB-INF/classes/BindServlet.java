
import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
import javax.servlet.*;
import javax.servlet.http.*;

import java.sql.*;


public class BindServlet extends HttpServlet {

	private static final int BIND_OK = 0;    
	private static final int BIND_ERROR_SN = 1;    
	private static final int BIND_ERROR_MOBILE = 2;   
	
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
		response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
		
		request.setCharacterEncoding("utf-8"); 
		Writer out = response.getWriter();
		BindData bindData = new BindData();
		bindData.sn = request.getParameter("sn");  
		bindData.name = request.getParameter("name");
		bindData.mobile = request.getParameter("mobile");
		bindData.address = request.getParameter("address");

        System.out.println("sn:      " + bindData.sn);
        System.out.println("name:    " + bindData.name);
        System.out.println("mobile:  " + bindData.mobile);
		System.out.println("address: " + bindData.address);
        
        int binded = Bind(bindData);
        String bind_str = "bind success!";
        
        if(binded == BIND_ERROR_SN) {
			out.write("SN: " + bindData.sn);
			out.write("<br>");
        	bind_str = "bind failed: error sn!" ;
        }
        else if(binded == BIND_ERROR_MOBILE) {
			out.write("mobile: " + bindData.mobile);
			out.write("<br>");
        	bind_str = "bind failed: error mobile!" ;
        }
        out.write(bind_str);
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
    
    public class BindData 
    {
    	public String sn = "";
        public String name = "";
        public String mobile = "";
        public String address = "";
    }

    public int Bind (BindData bindData )
    {
    	final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";  
        final String DB_URL = "jdbc:mysql://localhost:3306/pet?useSSL=false&serverTimezone=GMT%2B8";
     
        final String USER = "root";
        final String PASS = "xie123";
        int binded = BIND_OK;
        	
        Connection conn = null;
        Statement stmt = null;
        
        if(bindData.sn==null || bindData.sn.length() != 10) {
        	System.out.println("sn error!");
        	return BIND_ERROR_SN;
        }
		else {
        	Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");  
            if( !pattern.matcher(bindData.sn).matches()) {
            	System.out.println("sn error error!");
            	return BIND_ERROR_SN;
            }
        }
        if(bindData.mobile==null || bindData.mobile.length() == 0) {
        	System.out.println("mobile error!");
        	return BIND_ERROR_MOBILE;
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
            sql = "SELECT * FROM pet_table where pet_sn = '" + bindData.sn + "'";
            ResultSet rs = stmt.executeQuery(sql);
            rs.last();
            if(rs.getRow() > 0)
            {
            	binded = BIND_ERROR_SN;
            	System.out.println("This sn binded!");
            }
            else
            {
            	PreparedStatement pStmt = null;
                try
                {
                	sql = "insert into pet_table (pet_sn,pet_name,pet_mobile,pet_address) values (?,?,?,?)";
                    pStmt = conn.prepareStatement(sql);
                    pStmt.setString(1, bindData.sn);
                    pStmt.setString(2, bindData.name);
                    pStmt.setString(3, bindData.mobile);
                    pStmt.setString(4, bindData.address);
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
			
			if(binded == BIND_OK)
			{
				System.out.println("bind success!");
			}
			else
			{
				System.out.println("bind failed!");
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
		return binded;
    }

}

