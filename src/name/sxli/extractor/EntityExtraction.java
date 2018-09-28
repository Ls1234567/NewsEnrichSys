package name.sxli.extractor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Created by LiuXin on 2017/4/19.
 */
public class EntityExtraction {
    private String uri;
    private String querySentence;

    private int connectionTimeOut;
    private int readTimeout;
    private int endpointExecutionTimeOut=30000;

    private EntityExtraction(){}

    private String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            if(readTimeout>0)
                connection.setReadTimeout(readTimeout);
            if(connectionTimeOut>0)
                connection.setConnectTimeout(connectionTimeOut);
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
//            connection.setRequestProperty("Content-Type", "text/html");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
//            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
//            for (String key : map.keySet()) {
//                System.out.println(key + "--->" + map.get(key));
//            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    private String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性

            if(readTimeout>0)
                conn.setReadTimeout(readTimeout);
            if(connectionTimeOut>0)
                conn.setConnectTimeout(connectionTimeOut);

            conn.setRequestProperty("accept", "*/*");

            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//            conn.setRequestProperty("Content-Type", "text/html");
            

            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }
    
    public static String getAnnotateContent(String content) throws Exception{
		List<Pair<String, String>> params = new ArrayList<>();
		params.add(new Pair<String, String>("text", content));
		params.add(new Pair<String, String>("confidence", "0.8"));
		params.add(new Pair<String, String>("support", "0"));
		params.add(new Pair<String, String>("spotter", "Default"));
		params.add(new Pair<String, String>("disambiguator", "Default"));
		params.add(new Pair<String, String>("policy", "whitelist"));

		EntityExtraction sparqlExecutor = new EntityExtraction();
		String encodeParams = sparqlExecutor.EncodeParams(params);
		String resultString = sparqlExecutor.sendPost("http://model.dbpedia-spotlight.org/en/annotate", encodeParams);
		return resultString;
    }
    
    public static Collection<String> getDBlinks(String content) throws Exception{
		List<Pair<String, String>> params = new ArrayList<>();
		params.add(new Pair<String, String>("text", content));
		params.add(new Pair<String, String>("confidence", "0.8"));
		params.add(new Pair<String, String>("support", "0"));
		params.add(new Pair<String, String>("spotter", "Default"));
		params.add(new Pair<String, String>("disambiguator", "Default"));
		params.add(new Pair<String, String>("policy", "whitelist"));

		EntityExtraction sparqlExecutor = new EntityExtraction();
		String encodeParams = sparqlExecutor.EncodeParams(params);
		String resultString = sparqlExecutor.sendPost("http://model.dbpedia-spotlight.org/en/annotate", encodeParams);
//		System.out.println(resultString);

		Map<String,Integer> dblinkCounts = new HashMap<>();
		int hrefs = resultString.indexOf("href");
		int hreft = resultString.indexOf(" ", hrefs);
		while (hrefs != -1) {
			String link="<"+resultString.substring(hrefs + 6, hreft - 1)+">";
			if(!dblinkCounts.containsKey(link))
			    dblinkCounts.put(link,1);
			else
				dblinkCounts.put(link,dblinkCounts.get(link)+1);
			resultString = resultString.substring(hreft);
			hrefs = resultString.indexOf("href");
			hreft = resultString.indexOf(" ", hrefs);
		}
		dblinkCounts=sortMapByValue(dblinkCounts);
		return dblinkCounts.keySet();
    }

    public static void main(String[] args) throws Exception {
    	
    }
    
    private static Map sortMapByValue(Map m){
    	ArrayList<Map.Entry<String, Double>> list=new ArrayList<Map.Entry<String,Double>>(m.entrySet());
    	Collections.sort(list,new Comparator<Map.Entry<String, Double>>(){
			public int compare(Entry<String, Double> arg0, Entry<String, Double> arg1) {
				if(arg0.getValue() < arg1.getValue())
					return 1;
				else if(arg0.getValue() > arg1.getValue())
					return -1;
				else
					return 0;
			}
    	});
    	Map<String,Double> newMap=new LinkedHashMap<>();
    	for(int i=0;i<list.size();i++){
    		newMap.put(list.get(i).getKey(), list.get(i).getValue());
    	}
    	return newMap;
    }



    private String EncodeParams(List<Pair<String,String>> params){
        int i = 0;
        StringBuffer rs = new StringBuffer();

        for(Pair<String,String> param:params){
           if(i!=0)
               rs.append("&");
            rs.append(URLEncoder.encode(param.getKey()));
            rs.append("=");
            rs.append(URLEncoder.encode(param.getValue()));
            i++;
        }
        //System.out.println(rs.toString());
        return rs.toString();
    }

    public static class SparqlExecutorBuilder {
        public static EntityExtraction buildSPARQLExecutor(String service, String querySentence){
            EntityExtraction result = new EntityExtraction();
            result.uri = service;
            result.querySentence = querySentence;
            return result;
        }
    }

    public static class Pair<K,V> implements Serializable {
        /**
         * Key of this <code>Pair</code>.
         */
        private K key;
        /**
         * Gets the key for this pair.
         * @return key for this pair
         */
        public K getKey() { return key; }
        /**
         * Value of this this <code>Pair</code>.
         */
        private V value;

        /**
         * Gets the value for this pair.
         * @return value for this pair
         */
        public V getValue() { return value; }
        /**
         * Creates a new pair
         * @param key The key for this pair
         * @param value The value to use for this pair
         */
        public Pair( K key,  V value) {
            this.key = key;
            this.value = value;
        }

        /**
         * <p><code>String</code> representation of this
         * <code>Pair</code>.</p>
         *
         * <p>The default name/value delimiter '=' is always used.</p>
         *
         *  @return <code>String</code> representation of this <code>Pair</code>
         */
        @Override
        public String toString() {
            return key + "=" + value;
        }

        /**
         * <p>Generate a hash code for this <code>Pair</code>.</p>
         *
         * <p>The hash code is calculated using both the name and
         * the value of the <code>Pair</code>.</p>
         *
         * @return hash code for this <code>Pair</code>
         */
        @Override
        public int hashCode() {
            // name's hashCode is multiplied by an arbitrary prime number (13)
            // in order to make sure there is a difference in the hashCode between
            // these two parameters:
            //  name: a  value: aa
            //  name: aa value: a
            return key.hashCode() * 13 + (value == null ? 0 : value.hashCode());
        }
        /**
         * <p>Test this <code>Pair</code> for equality with another
         * <code>Object</code>.</p>
         *
         * <p>If the <code>Object</code> to be tested is not a
         * <code>Pair</code> or is <code>null</code>, then this method
         * returns <code>false</code>.</p>
         *
         * <p>Two <code>Pair</code>s are considered equal if and only if
         * both the names and values are equal.</p>
         *
         * @param o the <code>Object</code> to test for
         * equality with this <code>Pair</code>
         * @return <code>true</code> if the given <code>Object</code> is
         * equal to this <code>Pair</code> else <code>false</code>
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o instanceof Pair) {
                Pair pair = (Pair) o;
                if (key != null ? !key.equals(pair.key) : pair.key != null) return false;
                if (value != null ? !value.equals(pair.value) : pair.value != null) return false;
                return true;
            }
            return false;
        }
    }

    public void setConnectionTimeOut(int connectionTimeOut) {
        this.connectionTimeOut = connectionTimeOut;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public void setEndpointExecutionTimeOut(int endpointExecutionTimeOut) {
        this.endpointExecutionTimeOut = endpointExecutionTimeOut;
    }

}
