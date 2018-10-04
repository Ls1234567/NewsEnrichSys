package name.sxli.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jgrapht.graph.DirectedMultigraph;

import name.zanbry.agent.MemoryGraphAgent;
import name.zanbry.agent.MemoryOracleAgent;
import name.zanbry.alg.Configuration;
import name.zanbry.beans.GraphBuilder2;
import name.zanbry.beans.IntegerEdge;
import name.zanbry.oracle.DefaultOracleBuilder;
import name.zanbry.oracle.Oracle;

/**
 * Servlet implementation class InitialServlet
 */
public class InitialServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static Configuration conf;
	public static DirectedMultigraph<Integer, IntegerEdge> graph;
	public static MemoryGraphAgent graphAgent;//基于内存的图
	public static Oracle oracle;
	public static MemoryOracleAgent oracleAgent;//基于内存的Oracle
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InitialServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public void init() throws ServletException {
    	super.init();
    	conf = Configuration.loadConf();
    	graph = GraphBuilder2.build(conf.getDirPath());
    	graphAgent = new MemoryGraphAgent(graph);//基于内存的图
    	oracle = DefaultOracleBuilder.build(conf.getDirPath());
    	oracleAgent = new MemoryOracleAgent(oracle);//基于内存的Oracle
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
