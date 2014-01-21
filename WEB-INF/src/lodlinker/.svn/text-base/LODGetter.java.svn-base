package lodlinker;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import net.arnx.jsonic.JSON;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

public class LODGetter {
	static RDFConnector conn = new RDFConnector();

	public static void getCKAN() throws Exception{
		String url_str = "http://data.linkedopendata.jp/api/3/action/package_list";

		Map map = getJsonMap(url_str);

		ArrayList<String> list = (ArrayList)map.get("result");

		ListIterator itr = list.listIterator();
		while(itr.hasNext()){
			String dataset = (String)itr.next();

			Map temp = getJsonMap("http://data.linkedopendata.jp/api/3/action/package_show?id="+ dataset);

			//System.out.println(map);

			temp = (Map)temp.get("result");
			String label = (String)temp.get("title");
			ArrayList<Map> resouces = (ArrayList)temp.get("resources");
			Iterator itr_re = resouces.iterator();

			boolean flag = true;
			while(itr_re.hasNext()){
				Map temp_re = (Map)itr_re.next();
				String format = (String)temp_re.get("format");
				String temp_url = (String)temp_re.get("url");

				Date date = new Date();
				DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
				String date_str = df.format(date);

				try{

				if("api/sparql".equals(format)){
					conn.EPread(temp_url, label, date_str);
					System.out.println("add SPARQL EndPoint by"+ label);
					flag = false;
					//break;
				}else if("XML".equals(format)){
					conn.read(temp_url, "RDF/XML", label, date_str);
					System.out.println("add RDF FILE by"+ label);
					//break;
				}else if("turtle".equals(format)){
					conn.read(temp_url, "TTL", label, date_str);
					System.out.println("add RDF FILE by"+ label);
					//break;
				}else if("text/turtle".equals(format)){
					conn.read(temp_url, "TTL", label, date_str);
					System.out.println("add RDF FILE by"+ label);
					//break;
				}else if("ttl".equals(format)){
					conn.read(temp_url, "TTL", label, date_str);
					System.out.println("add RDF FILE by"+ label);
					//break;
				}

				flag = false;

				}catch(Exception e){
					e.printStackTrace();
				}
			}

			if(flag){
				itr.previous();
			}
		}
	}

	public static void getDatahub() throws Exception{
		String url_str = "http://datahub.io/api/3/action/package_list";

		Map map = getJsonMap(url_str);

		ArrayList<String> list = (ArrayList)map.get("result");

		int size = list.size();
		int c = 0;
		int offset = 1600;

		Iterator itr = list.iterator();
		while(itr.hasNext()){
			c++;

			String dataset = (String)itr.next();

			if(c < offset){
				continue;
			}

			Map temp = getJsonMap("http://datahub.io/api/3/action/package_show?id="+ dataset);

			//System.out.println(map);

			temp = (Map)temp.get("result");
			String label = (String)temp.get("title");
			ArrayList<Map> resouces = (ArrayList)temp.get("resources");
			Iterator itr_re = resouces.iterator();
			System.out.println("("+ c +"/"+ size +")label: "+ label);
			while(itr_re.hasNext()){
				Map temp_re = (Map)itr_re.next();
				String format = (String)temp_re.get("format");
				String temp_url = (String)temp_re.get("url");

				Date date = new Date();
				DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
				String date_str = df.format(date);

				System.out.println("format: "+ format);

				try{

				if("api/sparql".equals(format)){
					conn.EPread(temp_url, label, date_str);
					System.out.println("add SPARQL EndPoint by"+ label);
					//break;
				}else if("application/rdf+xml".equals(format)){
					conn.read(temp_url, "RDF/XML", label, date_str);
					System.out.println("add RDF FILE by"+ label);
					//break;
				}else if("turtle".equals(format)){
					conn.read(temp_url, "TTL", label, date_str);
					System.out.println("add RDF FILE by"+ label);
					//break;
				}else if("text/turtle".equals(format)){
					conn.read(temp_url, "TTL", label, date_str);
					System.out.println("add RDF FILE by"+ label);
					//break;
				}else if("ttl".equals(format)){
					conn.read(temp_url, "TTL", label, date_str);
					System.out.println("add RDF FILE by"+ label);
					//break;
				}

				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}

	static Map getJsonMap(String url){
		String json = new String();

		HttpClient client = new DefaultHttpClient();
		HttpParams params = client.getParams();

		URI uri = null;
		try {
			uri = new URI(url);
		} catch (URISyntaxException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

		HttpUriRequest httpRequest = new HttpGet(uri);

		HttpResponse httpResponse = null;

		try {
			httpResponse = client.execute(httpRequest);
		} catch (ClientProtocolException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

		if (httpResponse != null
				&& httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			HttpEntity httpEntity = httpResponse.getEntity();
			try {
				json = EntityUtils.toString(httpEntity);
			} catch (ParseException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		}

		return JSON.decode(json);
	}

	public static void main(String args[]){
		try {
			getCKAN();
			//getDatahub();
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}
}
