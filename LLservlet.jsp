<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="lodlinker.*"%>
<%

	response.setHeader("Access-Control-Allow-Origin", "*");
	response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, DELETE, OPTIONS");
	response.setHeader("Access-Control-Allow-Headers", request.getHeader("Access-Control-Request-Headers"));
	response.setHeader("Access-Control-Max-Age", "-1");

	request.setCharacterEncoding("UTF-8");
	String reqtype = request.getParameter("reqtype");
	String output = new String();

	System.out.println(reqtype);

	if("set_rdf".equals(reqtype)){
		output = FunctionRoot.setRDF(request);
	}else if("query".equals(reqtype) || reqtype == null){
		output = FunctionRoot.executeQuery(request);
	}else if("set_ep".equals(reqtype)){
		output = FunctionRoot.setEP(request);
		//System.out.println(output);
	}
%>
<%=output %>