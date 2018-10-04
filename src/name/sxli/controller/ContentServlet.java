package name.sxli.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;

import name.sxli.beans.News;
import name.sxli.test.GetQueryRelaxResult;
import name.sxli.utils.DBHelper;

/**
 * Servlet implementation class ContentServlet
 */
public class ContentServlet extends HttpServlet {
	public static void main(String[] args){
		
	}
	
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
		response.addHeader("Access-Control-Allow-Origin", "*");
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
	    		String[] titleUris = rs.getString(5).split(";");
	    		String[] contentNames = rs.getString(4).split(";");
	    		String[] contentUris = rs.getString(6).split(";");
	    		for(int i=0; i<titleNames.length; i++){
	    			String name = titleNames[i];
	    			String uri = titleUris[i];
	    			sql = "select type from instance_type where instance='<http://dbpedia.org/resource/"+uri+">'";
	    			Statement smt2 = dbHelper.conn.createStatement();
	    			ResultSet rs2 = smt2.executeQuery(sql);
//	    			rs2.next();
//	    			String type = rs2.getString(1);
	    			String type = rs2.next() ? rs2.getString(1):"null";
	    			if(!type.equals("null") && !type.equals("<http://www.w3.org/2002/07/owl#Thing>"))
	    				type = type.substring(29, type.length()-1);
	    			else if(type.equals("<http://www.w3.org/2002/07/owl#Thing>"))
	    				type = "Thing";
	    			title = title.replaceAll(name, "<a id='"+uri+";"+type+"' href='javascript:void(0);' onclick='addEntity(this)'>"+name+"</a>");
	    		}
	    		for(int i=0; i<contentNames.length; i++){
	    			String name = contentNames[i];
	    			String uri = contentUris[i];
	    			sql = "select type from instance_type where instance='<http://dbpedia.org/resource/"+uri+">'";
	    			Statement smt2 = dbHelper.conn.createStatement();
	    			ResultSet rs2 = smt2.executeQuery(sql);
//	    			rs2.next();
//	    			String type = rs2.getString(1);
	    			String type = rs2.next() ? rs2.getString(1):"null";
	    			if(!type.equals("null") && !type.equals("<http://www.w3.org/2002/07/owl#Thing>"))
	    				type = type.substring(29, type.length()-1);
	    			else if(type.equals("<http://www.w3.org/2002/07/owl#Thing>"))
	    				type = "Thing";
	    			// id = uri+";"+type
	    			content = content.replaceAll(name, "<a id='"+uri+";"+type+"' href='javascript:void(0);' onclick='addEntity(this)'>"+name+"</a>");
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
    	else if(option.equals("getTypes")){
    		Integer id = Integer.parseInt(request.getParameter("id"));
    		Set<String> types = new HashSet<>();
    		String sql = "select title_uris,content_uris from chosen_news where id="+id;
    		DBHelper dbHelper = new DBHelper();
    		Statement smt;
			try {
				smt = dbHelper.conn.createStatement();
	    		ResultSet rs = smt.executeQuery(sql);
	    		rs.next();
	    		String[] titleUris = rs.getString(1).split(";");
	    		String[] contentUris = rs.getString(2).split(";");
	    		Set<String> uris = new HashSet<>();
	    		for(String uri:titleUris)
	    			uris.add(uri);
	    		for(String uri:contentUris)
	    			uris.add(uri);
	    		for(String uri:uris){
	    			sql = "select type from instance_type where instance='<http://dbpedia.org/resource/"+uri+">'";
	    			Statement smt2 = dbHelper.conn.createStatement();
	    			ResultSet rs2 = smt2.executeQuery(sql);
//	    			rs2.next();
//	    			String type = rs2.getString(1);
	    			String type = rs2.next() ? rs2.getString(1):"null";
	    			if(!type.equals("null") && !type.equals("<http://www.w3.org/2002/07/owl#Thing>"))
	    				types.add(type.substring(29, type.length()-1));
	    		}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			for(String type:types)
//				System.out.println(type);
			String jsonString = JSON.toJSONString(types,true).toString();
    	    out.write(jsonString);
    	}
    	else if(option.equals("getQrel")){
    		Integer id = Integer.parseInt(request.getParameter("id"));
    		String uriString = request.getParameter("uris");
    		String specString = request.getParameter("spec");
    		System.out.println(uriString);
    		if(uriString!=null && !uriString.equals("")){
    			String[] uris = uriString.split(";");
    			String[] spec = specString.split(";");
    			try {
					int[] result = GetQueryRelaxResult.getQrelResult(InitialServlet.graphAgent, InitialServlet.oracleAgent, id, uris, spec);
					if(result != null){
						String jsonString = JSON.toJSONString(result,true).toString();
			    	    out.write(jsonString);
					} else {
						out.write("noassociation");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
    		}
    	}
	}

}
