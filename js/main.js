var conn = new connector();
var pageURL = location.protocol + "//" + location.host + location.pathname;
var n;

$(function(){
	var uri = getParameter("uri");
	var key = getParameter("keyword");
	var output = getParameter("output");
	var service = getParameter("service");
	var limit = getParameter("limit");
	var offset = getParameter("offset");
	var lod = getParameter("lod");
	var query = getParameter("query");

	//console.log(uri);
	n = $("#shadowTextarea").val();

	if(service != undefined || query != undefined){
		//console.log(service);
		$(".title").children("div").children("p").empty();
		$("#table").append("<img src='img/loading.gif'></img>");

		if(output == "HTML" || output == undefined || output == null){
			conn.executeQuery(query,"HTML", maketable2, getErrorMsg, service);
		}
	}else if(uri != undefined){
		//$(".main").empty();
		getResult_uri(uri, output, limit, offset);
	}else if(key != undefined){
		//$(".main").empty();
		getResult_key(key, output, limit, offset);
	}else if(lod != undefined){
		getResult_lod(lod, output, limit, offset);
	}
});

function goResult_uri(obj, flag){
	var param = "?uri="+ encodeURIComponent(obj.prev().val());
	if(flag != undefined){
		param = "?uri="+ encodeURIComponent(getParameter("uri"));
	}
	param += "&output=HTML";
	param += limioff(flag);

	//console.log(location.href);
	location.href = pageURL + param;
}

function goResult_key(obj, flag){
	var param = "?keyword="+ encodeURIComponent(obj.prev().val());
	if(flag != undefined){
		param = "?keyword="+ encodeURIComponent(getParameter("keyword"));
	}
	param += "&output=HTML";
	param += limioff(flag);

	//console.log(location.href);
	location.href = pageURL + param;
}

function goResult_lod(obj, flag){
	var param = "?lod="+ encodeURIComponent(obj.prev().val());
	if(flag != undefined){
		param = "?lod="+ encodeURIComponent(getParameter("lod"));
	}
	param += "&output=HTML";
	param += limioff(flag);

	//console.log(location.href);
	location.href = pageURL + param;
}

function goSubmit_uri(e, obj){
	if (!e) var e = window.event;

    if(e.keyCode == 13){
    	goResult_uri(obj.next("input"));

    	return false;
    }
}

function goSubmit_key(e, obj){
	if (!e) var e = window.event;

    if(e.keyCode == 13){
    	goResult_key(obj.next("input"));

    	return false;
    }
}

function limioff(flag){
	var temp_offset = 0;
	if(flag != undefined){
		var offset = Number(getParameter("offset"));

		if(flag == 1){
			temp_offset = offset - 50;
		}else if(flag == 2){
			temp_offset = offset + 50;
		}
	}

	var param = "&limit=50&offset="+ temp_offset;

	return param;
}

function getResult_uri(uri, output, limit, offset){

	var sparql = "select distinct ?uri ?label ?LOD ?LOD_label ?type where{<"+ uri +"> lodcu:describedIn ?LOD.";
	sparql += "?LOD rdfs:label ?LOD_label;";
	sparql += "a ?type.";
	sparql += "?uri lodcu:describedIn ?LOD FILTER (?uri = <"+ uri +">).";
	sparql += "OPTIONAL {?uri rdfs:label ?label}}";

	if(judgeParam(limit)){
		sparql += "limit "+ limit;
	}
	if(judgeParam(offset)){
		sparql += " offset "+ offset;
	}

	goQuery(sparql, output);
}

function getResult_key(key, output, limit, offset){
	var sparql = "select distinct ?uri ?label ?LOD ?LOD_label ?type where{{?uri lodcu:describedIn ?LOD FILTER regex(STR(?uri),\""+ key +"\",\"i\").}union{";
	sparql += "?uri rdfs:label ?l FILTER regex(?l,\""+ key +"\",\"i\").} ?uri lodcu:describedIn ?LOD.";
	sparql += "?LOD rdfs:label ?LOD_label;";
	sparql += "a ?type.OPTIONAL{?uri rdfs:label ?label}}";

	//console.log(limit);

	if(judgeParam(limit)){
		sparql += "limit "+ limit;
	}
	if(judgeParam(offset)){
		sparql += " offset "+ offset;
	}

	goQuery(sparql, output);
}

function getResult_lod(lod, output, limit, offset){
	var sparql = "select distinct ?uri ?label ?LOD ?LOD_label ?type where{?uri lodcu:describedIn <"+ lod +">.";
	sparql += "?uri lodcu:describedIn ?LOD. FILTER (?LOD=<"+ lod +">)";
	sparql += "?LOD rdfs:label ?LOD_label;";
	sparql += "a ?type.OPTIONAL{?uri rdfs:label ?label}}";

	//console.log(limit);
	//console.log(getParameter("format"));

	if(judgeParam(limit)){
		sparql += "limit "+ limit;
	}
	if(judgeParam(offset)){
		sparql += " offset "+ offset;
	}

	goQuery(sparql, output);
}

function goQuery(query, output){
	$(".title").children("div").children("p").empty();
	$("#table").append("<img src='img/loading.gif'></img>");

	if(output == "HTML" || output == undefined || output == null){
		conn.executeQuery(query,"HTML", maketable, getErrorMsg);
	}
}

function createModal(obj){
	$("#mess1").empty();
	$("#query_sparql").val("");

	var url = obj.prev("a").attr("href");
	//console.log(text);
	var str = "<a href='" +url+ "' target='_blank'>"+url+"</a>へSPARQLクエリを送信します"
	$("#mess1").append(str);

	var uri = obj.parent("td").prevAll(".td_uri").children("a").attr("href");
	var query = "select ?s ?p ?o where{" +n+ "?s a <"+ uri +">."+n+"?s ?p ?o."+ n +"}limit 50";
	console.log(query);

	$("#query_sparql").val(query);
}

function createModal_ins(obj){
	$("#modal_ins_table").empty();
	console.log(obj);

	var uri = obj.prev("a").attr("href");

	var str = "<h5>about &lt;"+ uri +"&gt;</h5>";
	$("#modal_ins_table").append(str);

	//var query = "select ?p ?o where{<"+ uri +"> ?p ?o.}";
	var query = "select ?s ?p ?o where{{<"+ uri +"> ?p ?o.}union{?s ?p <"+ uri +">}}";
	//console.log(query);

	conn.executeQuery(query,"HTML", maketable_ins, getErrorMsg);
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
	var lod = getParameter("lod");
	var offset = Number(getParameter("offset"));
	//console.log(re);

	var str = new String("<h4>Search Result</h4><table class='table table-striped table-bordered'><thead><tr>");
	str += "<th>URI</th><th>URIのラベル</th><th>記述のあるLOD</th><th>LODのタイトル</th><th>LODのタイプ(エンドポイントもしくはRDFファイル)</th>"
	str += "</tr></thead><tbody>";

	while(re.next()){
		var type = re.getValue("type");
		//console.log(llabel.indexOf("#"));
		str += "<tr>";

		str += "<td class='td_uri'><a href='"+re.getValue("uri") +"' target='_blank'>"+re.getValue("uri")+"</a><a style='float:right;display : block; text-align:right; font-size:10px;color:red;' href='#myModal_ins' data-toggle='modal' onclick='createModal_ins($(this))'>More info</a></td>";
		str += "<td>"+re.getValue("label")+"</td>";
		str += "<td><a href='" +re.getValue("LOD")+ "' target='_blank'>"+re.getValue("LOD")+"</a><a style='float:right;display : block; text-align:right;font-size:10px;color:red' href='#myModal' data-toggle='modal' onclick='createModal($(this))'>SPARQL</a></td>";
		str += "<td>"+re.getValue("LOD_label")+"</td>";
		str += "<td>"+type.substring(type.indexOf("#")+1)+"</td>";
		str += "</tr>";
	}
	str += "</tbody></table>";


	if(!judgeParam(offset)){
		offset = 0;
	}

	if(offset > 0){
		if(uri != undefined){
			str += "<a href='javascript:void(0)' onclick='goResult_uri($(this), 1)'><i class='icon-3x icon-backward'></i></a>";
		}else if(key != undefined){
			str += "<a href='javascript:void(0)' onclick='goResult_key($(this), 1)'><i class='icon-backward icon-3x'></i></a>";
		}else if(lod != undefined){
			str += "<a href='javascript:void(0)' onclick='goResult_lod($(this), 1)'><i class='icon-backward icon-3x'></i></a>";
		}
	}

	if(uri != undefined){
		str += "<a href='javascript:void(0)' onclick='goResult_uri($(this), 2)'><i class='icon-forward icon-3x'></i></a>";
	}else if(key != undefined){
		str += "<a href='javascript:void(0)' onclick='goResult_key($(this), 2)'><i class='icon-forward icon-3x'></i></a>";
	}else if(lod != undefined){
		str += "<a href='javascript:void(0)' onclick='goResult_lod($(this), 2)'><i class='icon-forward icon-3x'></i></a>";
	}

	//console.log(str);
	$("#table").append(str);
}

function maketable2(re, obj){

	var table_obj;
	if(obj != undefined){
		table_obj = obj;
	}else{
		table_obj = $("#table");
	}

	table_obj.empty();
	//console.log(re);

	var str = new String("<h4>Search Result</h4><table class='table table-striped table-bordered'><thead><tr>");
	for(var i=0; i<re.getKeyListLength();i++){
		str += "<th>"+re.getKey(i)+"</th>";
	}
	str += "</tr></thead><tbody>";
	while(re.next()){
		str += "<tr>";
		for(var i=0; i < re.getLength();i++){
			str += "<td>"+re.getValue(i)+"</td>";
		}
		str += "</tr>";
	}
	str += "</tbody></table>";

	table_obj.append(str);
}

function maketable_ins(re){
	var table_obj = $("#modal_ins_table");

	//table_obj.empty();
	//console.log(re);

	var str = new String("<table class='table table-striped table-bordered'>");

	str += "<tbody>";
	while(re.next()){
		str += "<tr>";
		/*
		for(var i=0; i < re.getLength();i++){
			str += "<td>"+re.getValue(i)+"</td>";
		}
		*/

		var s = re.getValue("s");
		var p = re.getValue("p");
		var o = re.getValue("o");
		console.log(s);

		if(s == undefined){
			str += "<td>"+p+"</td>";
			str += "<td>"+o+"</td>";
		}else if(o == undefined){
			str += "<td>"+p+" of</td>";
			str += "<td>"+s+"</td>";
		}

		str += "</tr>";
	}
	str += "</tbody></table>";

	table_obj.append(str);
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
			//console.log(decodeURIComponent(keyVal[1]));
			return decodeURIComponent(keyVal[1]);
		}
	}
	return undefined;
}