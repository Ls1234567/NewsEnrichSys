package name.sxli.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;

import name.sxli.beans.Title;
import name.sxli.utils.DBHelper;

/**
 * Servlet implementation class TitleServlet
 */
public class TitleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TitleServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("text/html;charset=utf-8");    	
    	PrintWriter out = response.getWriter();
    	
    	String option = request.getParameter("option");
    	
    	if(option.equals("show")){
//    		System.out.println("test");
    		List<Title> titles = new ArrayList<>();
    		String sql = "select title,id from chosen_news where has_null=0 and id>6000 and id<=6100";
    		DBHelper dbHelper = new DBHelper();
    		try {
				Statement smt = dbHelper.conn.createStatement();
				ResultSet rs = smt.executeQuery(sql);
				while(rs.next()){
					titles.add(new Title(rs.getInt(2), rs.getString(1)));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
//        	for(int i=5; i<=5; i++){
//        		for(int j=1; j<=20; j++){
//        			File infile = new File("E:\\semantic_web\\web browsing\\NewsIR_1M\\titleContent\\NewsIR_num"+i+"_doc"+j+".txt");
//        			BufferedReader reader = new BufferedReader(new FileReader(infile));
//        			String title = reader.readLine();
//        			titles.add(new Title(j, title));
//        		}
//        	}
        	String jsonString = JSON.toJSONString(titles,true).toString();
//    	    System.out.println("查询数据：jsonText=="+jsonString);
    	    out.write(jsonString);
    	}
    	else if(option.equals("goDetail")){
    		String id = request.getParameter("id");
    		request.setAttribute("id", id);
    		RequestDispatcher dispatcher = request.getRequestDispatcher("html/detail.html");
    		dispatcher.forward(request, response);
    	}
	}
}


