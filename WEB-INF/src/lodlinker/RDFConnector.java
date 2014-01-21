package lodlinker;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class RDFConnector {
	public String executeQuery(String query, String style)throws Exception{

		String output = new String();

		output = TDBManager.executeQuery(query, style);

		return output;
	}

	public String executeQuery(String query, String style, String service)throws Exception{

		String output = new String();

			output = TDBManager.executeQuery(query, style, service);


		return output;
	}

	public String executeUpdate(String query)throws Exception{
		String output = new String();


		TDBManager.executeUpdate(query);


		return output;
	}

	public String loadRDFFile(String path, String graphID)throws Exception{
		String output = new String();


			TDBManager.loadRDFFile(path, graphID);


		return output;
	}

	public String read(String url, String type, String label, String date)throws Exception{
		String output = new String();

			TDBManager.read(url, type, label, date);

		return output;
	}

	public String EPread(String url, String label, String date)throws Exception{
		String output = new String();


			TDBManager.EPread(url, label, date);


		return output;
	}

	public void command(){
		System.out.println("Start Comand Line");

		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

		for(;;){

			System.out.print("SPARQL> ");

			try{

				String line = in.readLine();

				if("quit".equals(line)){
					break;
				}else if("file".equals(line)){
					System.out.print("file input\nPlease input file path:");
					BufferedReader in_path = new BufferedReader(new InputStreamReader(System.in));
					String path = in_path.readLine();

					System.out.print("Please input graphID:");
					BufferedReader in_id = new BufferedReader(new InputStreamReader(System.in));
					String id = in_id.readLine();

					loadRDFFile(path, id);

					System.out.println("success");
				}else if(TDBManager.judgeSearch(line)){
					System.out.println(executeQuery(line, null));
				}else{
					executeUpdate(line);
					System.out.println("update is success.");
				}
			}catch(Exception e) {e.printStackTrace(); }
		}

		return;
	}
}
