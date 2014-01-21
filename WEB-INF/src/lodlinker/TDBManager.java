package lodlinker;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFLanguages;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryException;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ReadWrite;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.tdb.TDBFactory;
import com.hp.hpl.jena.update.UpdateAction;
import com.hp.hpl.jena.update.UpdateFactory;
import com.hp.hpl.jena.update.UpdateRequest;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.sparql.core.Var;
import com.hp.hpl.jena.sparql.engine.binding.Binding;
import com.hp.hpl.jena.sparql.engine.http.QueryEngineHTTP ;

public class TDBManager {
	static String baseURI = "http://lodlinker.org/";
	static String database = "/var/lib/tomcat7/webapps/database_lodlinker";
	//static String database = "D:/workspace_Fuji/database/lodlinker";
	static ReadWrite read = ReadWrite.READ;
	static ReadWrite write = ReadWrite.WRITE;

	static String type_file = "http://lodlinker.org#rdfFILE";
	static String type_ep = "http://lodlinker.org#SPARQLEndPoint";

	static String prefix = "PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> PREFIX owl:  <http://www.w3.org/2002/07/owl#> PREFIX lodcu:<http://lodlinker.org#>" +
	"PREFIX prv: <http://purl.org/net/provenance/ns#> PREFIX dctype: <http://purl.org/dc/dcmitype/Dataset#>";

	public static String executeQuery(String query, String style) throws SparqlSyntaxException, Exception{
		System.out.println("Start \"executeQuery()\".");

		String output = new String();

		ByteArrayOutputStream resultOutputStream = new ByteArrayOutputStream();

		Dataset ds = null;

		style = style.toUpperCase();

		try{
			ds = TDBFactory.createDataset(database);
		}catch(Exception e){
			//System.out.println("aaa");

			File file = new File(database);
			file.mkdir();

			ds = TDBFactory.createDataset(database);
		}

		ds.begin(read);

		if("HTML".equals(style)){
			style = "JSON";
		}

		try{
		Query sparql = QueryFactory.create(prefix+query, baseURI);
		QueryExecution qexec = QueryExecutionFactory.create(sparql, ds);

		if(sparql.isSelectType()){
			ResultSet results = qexec.execSelect() ;

			if(style.equals("JSON")){
				ResultSetFormatter.outputAsJSON(resultOutputStream, results) ;
			}else if("XML".equals(style)){
				ResultSetFormatter.outputAsXML(resultOutputStream, results);
			}else if("TTL".equals(style)){
				ResultSetFormatter.outputAsRDF(resultOutputStream, "TTL", results);
			}
		}else if(sparql.isAskType()){
			boolean f = qexec.execAsk();
			System.out.println(f);
			return String.valueOf(f);
		}
		}catch(QueryException e){
			e.printStackTrace();
			throw new SparqlSyntaxException(e.getMessage());
		}

		output = resultOutputStream.toString("UTF-8");

		//System.out.println(output);

		ds.end();

		System.out.println("Successed \"executeQuery()\".");

		return output;
	}

	public static String executeQuery(String query, String style, String service) throws SparqlSyntaxException, Exception{
		System.out.println("Start \"executeQuery()\".");

		String output = new String();

		ByteArrayOutputStream resultOutputStream = new ByteArrayOutputStream();

		Dataset ds = null;

		style = style.toUpperCase();

		try{
			ds = TDBFactory.createDataset(database);
		}catch(Exception e){
			//System.out.println("aaa");

			File file = new File(database);
			file.mkdir();

			ds = TDBFactory.createDataset(database);
		}

		ds.begin(read);

		if("HTML".equals(style)){
			style = "JSON";
		}

		String q = "select ?type ?file_type where{<"+ service +"> a ?type.OPTIONAL{<"+ service +"> lodcu:fileType ?file_type}}";
		Query qu = QueryFactory.create(prefix+q, baseURI);
		QueryExecution qexecEX = QueryExecutionFactory.create(qu, ds);

		ResultSet re = qexecEX.execSelect() ;

		ResultSet results = null;
		Query sparql = QueryFactory.create(prefix+query);
		QueryExecution qexec = null;

		Model model = ModelFactory.createDefaultModel();

		//System.out.println(re.hasNext());
		try{
		if(re.hasNext()){
			QuerySolution qs = re.next();
			System.out.println(qs.toString());
			String type = qs.get("type").toString();
			if(type_ep.equals(type)){

				// Remote execution.
				qexec = QueryExecutionFactory.sparqlService(service, sparql);
				// Set the DBpedia specific timeout.
				((QueryEngineHTTP)qexec).addParam("timeout", "30000") ;

				// Execute.
			}else if(type_file.equals(type)){

				//System.out.println(lang);
				String lang = qs.get("file_type").toString();

				model.read(service, lang);

				qexec = QueryExecutionFactory.create(sparql, model);
			}
		}else{

			throw new LLErrorException("error","エラーが発生しました。詳細は第3パラメータのerrorInfoをご覧ください.","未登録のLODです.");
		}

			results = qexec.execSelect();
		}catch(QueryException e){
			ds.end();
			model.close();
			qexec.close();
			throw new SparqlSyntaxException(e.getStackTrace().toString());
		}

		if(style.equals("JSON")){
			ResultSetFormatter.outputAsJSON(resultOutputStream, results) ;
		}else if("XML".equals(style)){
			ResultSetFormatter.outputAsXML(resultOutputStream, results);
		}else if("TTL".equals(style)){
			ResultSetFormatter.outputAsRDF(resultOutputStream, "TTL", results);
		}

		output = resultOutputStream.toString("UTF-8");

		//System.out.println(output);

		ds.end();
		model.close();
		qexec.close();

		System.out.println("Successed \"executeQuery()\".");

		return output;
	}

	public static String executeQuery(String query, Model model) throws SparqlSyntaxException, Exception{
		System.out.println("Start \"executeQuery()\".");

		String output = new String();

		ByteArrayOutputStream resultOutputStream = new ByteArrayOutputStream();

		try{

		Query sparql = QueryFactory.create(prefix+query, baseURI);
		QueryExecution qexec = QueryExecutionFactory.create(sparql, model);

		if(sparql.isSelectType()){
			ResultSet results = qexec.execSelect() ;
			ResultSetFormatter.outputAsJSON(resultOutputStream, results) ;
		}else if(sparql.isAskType()){
			boolean f = qexec.execAsk();
			System.out.println(f);
		}

		}catch(QueryException e){
			throw new SparqlSyntaxException(e.getMessage());
		}

		output = resultOutputStream.toString("UTF-8");

		//System.out.println(output);

		System.out.println("Successed \"executeQuery()\".");

		return output;
	}

	public static void executeUpdate(String query){
		System.out.println("Start \"executeUpdate()\".");

		Dataset ds = null;

		try{
			ds = TDBFactory.createDataset(database);
		}catch(Exception e){
			File file = new File(database);
			file.mkdir();

			ds = TDBFactory.createDataset(database);
		}

		ds.begin(write);

		try{
			UpdateRequest update = UpdateFactory.create(prefix+query, baseURI);
			UpdateAction.execute(update, ds);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			ds.commit();
		}

		System.out.println("Successed \"executeUpdate()\".");

		return;
	}

	public static void loadRDFFile(String path, String graphID) throws Exception{
		File file = new File(path);

		Dataset ds = null;

		try{
			ds = TDBFactory.createDataset(database);
		}catch(Exception e){
			//System.out.println("aaa");
			File file2 = new File(database);
			file2.mkdir();

			ds = TDBFactory.createDataset(database);
		}

		ds.begin(write);

		Model model = ds.getDefaultModel();

		model.read(new FileInputStream(file), baseURI, "N3");
		//model = FileManager.get().loadModel("file:"+path, baseURI, "N3");

		ds.commit();

		return;
	}

	public static void read(String url, String type, String label, String date) throws Exception{
		System.out.println("Start loading from "+ url);

		Dataset ds = null;
		Model model = null;

		updateLOD(url, label, true);

		RDFConnector conn = new RDFConnector();
		try{

			ds = TDBFactory.createDataset(database);

			ds.begin(write);

			model = ds.getNamedModel(url);

			String lang = new String();

			if(RDFLanguages.nameToLang(type) != null){
				lang = RDFLanguages.nameToLang(type).getLabel();
			}else{
				lang = "RDF/XML";
			}
			System.out.println(lang);


			model.read(url, type);


			//model.write(System.out);

			ds.commit();

			String sparql = "insert data{"+ "<"+ url +"> rdf:type lodcu:rdfFILE; rdfs:label \""+ label +"\"; lodcu:fileType \""+ lang +"\"; lodcu:date \""+ date +"\"}";

			System.out.println(sparql);
			try{
				conn.executeUpdate(sparql);
			}catch(Exception e){
				e.printStackTrace();
			}

			sparql = "insert {?c lodcu:describedIn <"+ url +">.?c rdfs:label ?label.?c rdfs:subClassOf ?c_s; owl:equivalentClass ?c_e." +
					"}where{{graph <"+ url +">{?s rdf:type ?c. optional{?c rdfs:label ?label}}}" +
					"union {graph <"+ url +">{?c rdf:type rdfs:Class. optional{?c rdfs:label ?label}}}" +
					"union {graph <"+ url +">{?c rdf:type owl:Class. optional{?c rdfs:label ?label}}}" +
					"union {graph <"+ url +">{?c rdfs:subClassOf ?c_s. optional{?c rdfs:label ?label}}}" +
					"union {graph <"+ url +">{?s rdfs:subClassOf ?c. optional{?c rdfs:label ?label}}}" +
					"union {graph <"+ url +">{?c owl:equivalentClass ?c_e. optional{?c rdfs:label ?label}}}" +
					"union {graph <"+ url +">{?s owl:equivalentClass ?c. optional{?c rdfs:label ?label}}}}";

			System.out.println(sparql);


			conn.executeUpdate(sparql);


			conn.executeUpdate("drop graph <"+ url +">");

		}catch(Exception e){
			e.printStackTrace();

		}finally{
			model.close();
			if(ds.isInTransaction()){
				ds.end();
			}
			ds.close();
		}

		model.close();
		ds.close();

		return;
	}

	public static String EPread(String url, String label, String date) throws HTMLConnectionException, SparqlSyntaxException, Exception{

		RDFConnector conn = new RDFConnector();
		updateLOD(url, label, true);

		String sparql = "select distinct ?c ?label ?c_s ?c_e where{{?s rdf:type ?c. optional{?c rdfs:label ?label}}" +
						"union {?c rdf:type rdfs:Class. optional{?c rdfs:label ?label}}" +
						"union {?c rdf:type owl:Class. optional{?c rdfs:label ?label}}" +
						"union {?c rdfs:subClassOf ?c_s. optional{?c rdfs:label ?label}}" +
						"union {?c owl:equivalentClass ?c_e. optional{?c rdfs:label ?label}}" +
						"union {?s rdfs:subClassOf ?c. optional{?c rdfs:label ?label}}" +
						"union {?c_e owl:equivalentClass ?c. optional{?c rdfs:label ?label}}}";

		sparql = prefix + sparql;

		System.out.println(sparql);

		Query query = QueryFactory.create(sparql);

		// Remote execution.
		QueryExecution qexec = QueryExecutionFactory.sparqlService(url, query);
		// Set the DBpedia specific timeout.
		((QueryEngineHTTP)qexec).addParam("timeout", "100000") ;

		// Execute.
		ResultSet rs = null;
		try{
			rs = qexec.execSelect();
		}catch(Exception e){
			e.printStackTrace();
			qexec.close();
			throw new HTMLConnectionException(e.getMessage());
		}

		String update = "insert data{";
		update += "<"+ url +"> rdf:type lodcu:SPARQLEndPoint; rdfs:label \""+ label +"\"; lodcu:date \""+ date +"\".";
		//System.out.println(rs.getResultVars().toString());

		while(rs.hasNext()){
			Binding bin = rs.nextBinding();
			//System.out.println(bin.toString());

			String c = new String();
			String la = new String();
			String sub = new String();
			String eq = new String();

			if(bin.get(Var.alloc("c")) != null && !bin.get(Var.alloc("c")).isBlank()){
				c = bin.get(Var.alloc("c")).toString().replaceAll("\"", "");
				update += "<"+ c +"> lodcu:describedIn <"+ url +">.";
			}

			if(bin.get(Var.alloc("c")) != null && bin.get(Var.alloc("label")) != null){
				la = bin.get(Var.alloc("label")).toString();
				update += "<"+ c +"> rdfs:label "+ la +".";
			}

			if(bin.get(Var.alloc("c")) != null && bin.get(Var.alloc("c_s")) != null){
				sub = bin.get(Var.alloc("c_s")).toString();
				update += "<"+ c +"> rdfs:subClassOf <"+ sub +">.";
			}

			if(bin.get(Var.alloc("c")) != null && bin.get(Var.alloc("c_e")) != null){
				eq = bin.get(Var.alloc("c_e")).toString();
				update += "<"+ c +"> owl:equivalentClass <"+ eq +">.";
			}
		}

		update += "}";

		qexec.close();

		try{
			conn.executeUpdate(update);
		}catch(Exception e){
			e.printStackTrace();
			throw new SparqlSyntaxException(update);
		}

		//System.out.println(sparql);

		return update;
	}

	static void updateLOD(String url, String label, boolean flag){
		Dataset ds = null;
		Model model = null;

		RDFConnector conn = new RDFConnector();

		String query = "ASK{<"+ url +"> a ?type.}";

		boolean f = false;
		try {
			f = Boolean.valueOf(conn.executeQuery(query, "JSON"));
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

		if(f){
			String delete = new String();
			if(flag){
				delete = "delete {?uri lodcu:describedIn <"+ url +">.<"+ url +"> rdfs:label ?label}where{?uri lodcu:describedIn <"+ url +">.OPTIONAL{<"+ url +"> rdfs:label ?label}}";
			}else{
				delete = "delete {?uri lodcu:describedIn <"+ url +">}where{?uri lodcu:describedIn <"+ url +">}";
			}
			System.out.println(delete);
			try {
				conn.executeUpdate(delete);
			} catch (Exception e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		}

		return;
	}

	public static boolean judgeSearch(String query){

		Query sparql = null;

		try{
			sparql = QueryFactory.create(prefix+query, baseURI);
		}catch(Exception e){
			//e.printStackTrace();
			//System.out.println("error");
			return false;
		}

		if(sparql.isUnknownType()){
			System.out.println("unknown");
			return false;
		}else{
			return true;
		}

		/*
		if(query.indexOf("select") == -1 || query.indexOf("SELECT") == -1){
			return false;
		}else{
			return true;
		}
		*/
	}
}
