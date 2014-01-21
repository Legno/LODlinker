package lodlinker;

import java.util.HashMap;
import java.util.Map;

import net.arnx.jsonic.JSON;

class LLErrorException extends Exception {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private String errorType;
	private String errorMsg;
	private String errorInfo;

	public LLErrorException(){};
	public LLErrorException(String eType){
		errorType = eType;
	};
	public LLErrorException(String eType,String eMsg){
		errorType = eType;
		errorMsg = eMsg;
	};
	public LLErrorException(String eType,String eMsg,String eInfo){
		errorType = eType;
		errorMsg = eMsg;
		errorInfo = eInfo;
	}

	void setType(String eType){
		errorType = eType;
	}
	void setMsg(String eMsg){
		errorMsg = eMsg;
	}
	void setInfo(String eInfo){
		errorInfo = eInfo;
	}
	public String getErrorJson(){
		return makeErrorJson(errorType, errorMsg, errorInfo);
	}

	private String makeErrorJson(String eType,String eMsg,String eInfo){
		String json = new String();

		Map emap = createErrorMap(true, eType, eMsg, eInfo);

		json = JSON.encode(emap);
		//System.out.println(json);

		return json;
	}

	private Map createErrorMap(boolean error, String eType, String eMsg, String eInfo){
		Map map = new HashMap<String, Object>();

		map.put("error", error);
		map.put("errorType", eType);
		map.put("errorMsg", eMsg);
		map.put("errorInfo", eInfo);

		return map;
	}
}

class SparqlSyntaxException extends LLErrorException{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public SparqlSyntaxException(String eInfo){
		super("SparqlSyntaxError","エラーが発生しました","SPARQLの構文ミスです。詳細情報:"+ eInfo);
	}
}

class HTMLConnectionException extends LLErrorException{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public HTMLConnectionException(String eInfo){
		super("HTMLConnectionError","エラーが発生しました","URL先へアクセスできませんでした。詳細情報:"+ eInfo);
	}
}
