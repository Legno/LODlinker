package lodlinker;

public class test {
	public static void main(String[] args){
		String output = new String();
		RDFConnector conn = new RDFConnector();

		String query = "select * where{graph ?g{?s ?p ?o}}";
		//String query = "select * where{?s ?p ?o}";

		//RDFConnector.loadRDFFile("./sample_fuji.n3", "test");

		String q = new String("INSERT DATA" +
								"{" +
								"<http://example/book1>  ns:price  10." +
									"GRAPH <http://SparqlEPCU/> { <http://example/book2>  ns:price  42 }" +
									"GRAPH <http://SparqlEPCU/> { <http://example/book3>  ns:price  55 }" +
								"}");

		//RDFConnector.executeUpdate(q);

		//output = conn.executeQuery(query);

		//System.out.println(output);


		conn.command();
	}
}
