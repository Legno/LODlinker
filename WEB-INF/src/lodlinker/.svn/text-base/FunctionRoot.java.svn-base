package lodlinker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;

public class FunctionRoot {
	static RDFConnector conn = new RDFConnector();

	public static String setRDF(HttpServletRequest request){
		String output = new String();

		String url_str = request.getParameter("url");
		String label = request.getParameter("label");
		String type = request.getParameter("type");
		String date = request.getParameter("date");

		if("".equals(label) || "undefined".equals(label) || "null".equals(label)){
			label = url_str;
		}

		if(url_str.indexOf("http://") == -1 && url_str.indexOf("https://") == -1){
			url_str = "http://" + url_str;
		}

		try {
			//output = SetTriple.setRDF(url_str, label, type);
			output = conn.read(url_str, type, label, date);
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			LLErrorException rdfe = new LLErrorException("undefined","エラーが発生しました","詳細情報: "+e.toString());
			output = rdfe.getErrorJson();
		}


		return output;
	}

	public static String setEP(HttpServletRequest request){
		String output = new String();

		String url_str = request.getParameter("url");
		String label = request.getParameter("label");
		String date = request.getParameter("date");

		if("".equals(label) || "undefined".equals(label) || "null".equals(label)){
			label = url_str;
		}

		if(url_str.indexOf("http://") == -1 && url_str.indexOf("https://") == -1){
			url_str = "http://" + url_str;
		}

		try {
			conn.EPread(url_str, label, date);
			//System.out.println("test5555");
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			LLErrorException rdfe = new LLErrorException("undefined","エラーが発生しました","詳細情報: "+e.toString());
			output = rdfe.getErrorJson();
		}

		return output;
	}

	public static String executeQuery(HttpServletRequest request){
		String output = new String();

		String query = request.getParameter("query");
		String style = request.getParameter("output");
		String service = request.getParameter("service");

		System.out.println(service);

		if("".equals(style) || "undefined".equals(style) || "null".equals(style) || style == null){
			style = "JSON";
		}
		if("".equals(service) || "undefined".equals(service) || "null".equals(service) || service == null){
			service = null;
		}

		try {
			if(service == null){
				output = conn.executeQuery(query, style);
			}else{
				output = conn.executeQuery(query, style, service);
			}
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			LLErrorException rdfe = new LLErrorException("undefined","エラーが発生しました","詳細情報: "+e.toString());
			output = rdfe.getErrorJson();
		}


		return output;
	}
}
