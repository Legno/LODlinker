<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="lodlinker.*"%>
<%
	request.setCharacterEncoding("UTF-8");
	String get = request.getParameter("get");
	String output = new String();

	//System.out.println(reqtype);

	if("CKAN".equals(get)){
		LODGetter.getCKAN();
		output = "登録が完了しました";
	}else {
		output = "読み込み先を指定してください: CKAN";
	}
%>
<%=output %>