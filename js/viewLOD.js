var conn = new connector();
var pageURL = location.protocol + "//" + location.host + location.pathname;
var n;

$(function(){
	var output = getParameter("output");
	var limit = getParameter("limit");
	var offset = getParameter("offset");

	n = $("#shadowTextarea").val();

	if(limit == undefined){
		limit = 50;
	}
	if(offset == undefined){
		offset = 0;
	}

	//conn.executeQuery(query,"HTML", maketable2, getErrorMsg, service);

	getResult(limit, offset);
});

function limioff(flag){
	var temp_offset = 0;
	if(flag != undefined){
		var offset = Number(getParameter("offset"));

		if(!judgeParam(offset)){
			offset = 0;
		}

		if(flag == 1){
			temp_offset = offset - 50;
		}else if(flag == 2){
			temp_offset = offset + 50;
		}
	}

	var param = "&limit=50&offset="+ temp_offset;

	return param;
}

function goResult(obj, flag){
	var param = "";
	param += "?output=HTML";
	param += limioff(flag);

	//console.log(location.href);
	location.href = pageURL + param;
}

function goResult_lod(obj, flag){
	var param = "?lod="+ encodeURIComponent(obj.prev().attr("href"));
	param += "&output=HTML";
	param += limioff(flag);

	//console.log(location.href);
	location.href = pageURL.substring(0, pageURL.lastIndexOf("/")) + param;
}

function getResult(limit, offset){

	var sparql = "select ?lod ?label ?type where{{?lod a lodcu:rdfFILE.}union{?lod a lodcu:SPARQLEndPoint}"+
				"?lod rdfs:label ?label; a ?type.}ORDER BY ?label";

	if(judgeParam(limit)){
		sparql += " limit "+ limit;
	}
	if(judgeParam(offset)){
		sparql += " offset "+ offset;
	}

	console.log(sparql);

	goQuery(sparql);
}

function goQuery(query, output){
	$(".title").children("div").children("p").empty();
	$("#table").append("<img src='img/loading.gif'></img>");

	if(output == "HTML" || output == undefined || output == null){
		conn.executeQuery(query,"HTML", maketable, getErrorMsg);
	}
}

function querySend(){
	var stext = $("#query_sparql").val();
	var service = $("#mess1").children("a").attr("href");

	var param = "?query="+ encodeURIComponent(stext);
	param += "&service="+ encodeURIComponent(service);
	param += "&output=HTML";

	//console.log(location.href);
	location.href = pageURL + param;
}

function maketable(re){
	$("#table").empty();
	var uri = getParameter("uri");
	var key = getParameter("keyword");
	var offset = Number(getParameter("offset"));
	//console.log(re);

	var str = new String("<h4>Search Result</h4><table class='table table-striped table-bordered'><thead><tr>");
	str += "<th>LODのURL</th><th>LODのタイトル</th><th>LODのタイプ(エンドポイントもしくはRDFファイル)</th>"
	str += "</tr></thead><tbody>";

	while(re.next()){
		var type = re.getValue("type");
		//console.log(llabel.indexOf("#"));
		str += "<tr>";

		str += "<td class='td_uri'><a href='"+re.getValue("lod") +"' target='_blank'>"+re.getValue("lod")+"</a><a style='color:red;font-size: 12px; float:right;display : block; text-align:right' href='javascript:void(0)' onclick='goResult_lod($(this))'>クラス一覧</a></td>";
		str += "<td>"+re.getValue("label")+"</td>";
		str += "<td>"+type.substring(type.indexOf("#")+1)+"</td>";
		str += "</tr>";
	}
	str += "</tbody></table>";


	if(!judgeParam(offset)){
		offset = 0;
	}

	if(offset > 0){
		str += "<a href='javascript:void(0)' onclick='goResult($(this), 1)'><i class='icon-3x icon-backward'></i></a>";
	}

	str += "<a href='javascript:void(0)' onclick='goResult($(this), 2)'><i class='icon-forward icon-3x'></i></a>";

	//console.log(str);
	$("#table").append(str);
}

function changeSelect(){
	var type = $("#modal_select").val();

	if(type == "SPARQLEndPoint"){
		$("#ftype_s").attr("class", "control-group hide");
	}else if(type == "rdfFILE"){
		$("#ftype_s").attr("class", "control-group");
	}
}

function addLOD(){
	var type = $("#modal_select").val();
	//alert("aa");

	if(type == "SPARQLEndPoint"){
		var url = $("#inputURL").val();
		var label = $("#inputTitle").val();

		$("#mbody2").empty();
		$("#mbody2").append("<img src='img/loading.gif'></img>");

		conn.getRDFep(url, label, function(){
			$("#mbody2").empty();
			confirm("登録が完了しました");
			location.href = pageURL;
		}, getErrorMsg2);
	}else if(type == "rdfFILE"){
		var url = $("#inputURL").val();
		var label = $("#inputTitle").val();
		var ftype = $("#inputFtype").val();

		$("#mbody2").empty();
		$("#mbody2").append("<img src='img/loading.gif'></img>");

		conn.getRDFfile(url, label, ftype, function(){
			$("#mbody2").empty();
			confirm("登録が完了しました");
			location.href = pageURL;
		}, getErrorMsg2);
	}
}

function getErrorMsg(eobj){
	alert(eobj.errorMsg+"\n\n"+eobj.errorInfo);
	history.back();
}

function getErrorMsg2(eobj){
	alert(eobj.errorMsg+"\n\n"+eobj.errorInfo);
	location.href = pageURL;
}

function judgeParam(obj){
	if(obj != undefined && obj != "null" && obj != "" && !isNaN(obj)){
		return true;
	}else{
		return false;
	}
}

function getParameter(key) {
	var str = location.search.split("?");
	if (str.length < 2) {
		return undefined;
	}

	var params = str[1].split("&");
	for (var i = 0; i < params.length; i++) {
		var keyVal = params[i].split("=");
		if (keyVal[0] == key && keyVal.length == 2) {
			return decodeURIComponent(keyVal[1]);
		}
	}
	return undefined;
}