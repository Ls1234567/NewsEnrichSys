package name.sxli.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;

import name.sxli.beans.News;
import name.sxli.utils.DBHelper;

/**
 * Servlet implementation class ContentServlet
 */
public class ContentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ContentServlet() {
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
		response.setContentType("text/html;charset=utf-8");    	
    	PrintWriter out = response.getWriter();
    	
    	String option = request.getParameter("option");
    	
    	if(option.equals("getContent")){
    		Integer id = Integer.parseInt(request.getParameter("id"));
    		String sql = "select title,content,title_names,content_names,title_uris,content_uris from chosen_news where id="+id;
    		DBHelper dbHelper = new DBHelper();
    		Statement smt;
    		String title="", content="";
			try {
				smt = dbHelper.conn.createStatement();
	    		ResultSet rs = smt.executeQuery(sql);
	    		rs.next();
	    		title = rs.getString(1);
	    		content = rs.getString(2);
	    		String[] titleNames = rs.getString(3).split(";");
	    		for(String name:titleNames){
	    			title = title.replaceAll(name, "<a id='"+name+"' href='javascript:void(0);' onclick='addEntity(this)'>"+name+"</a>");
	    		}
	    		String[] contentNames = rs.getString(4).split(";");
	    		for(String name:contentNames){
	    			content = content.replaceAll(name, "<a id='"+name+"' href='javascript:void(0);' onclick='addEntity(this)'>"+name+"</a>");
	    		}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			File infile = new File("E:\\semantic_web\\web browsing\\NewsIR_1M\\titleContent\\NewsIR_num5_doc"+id+".txt");
//			BufferedReader reader = new BufferedReader(new FileReader(infile));
//			String title = reader.readLine();
//			String content = reader.readLine();
//			title = title.replaceAll("Infratil", "<a>Infratil</a>");
//			try {
//				content = EntityExtraction.getAnnotateContent(content);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			System.out.println(content);
			News news = new News(title, content);
			String jsonString = JSON.toJSONString(news,true).toString();
    	    out.write(jsonString);
    	}
	}

}
