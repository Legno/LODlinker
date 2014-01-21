
function connector(server){
	var server = "./LLservlet.jsp";

	if(server != undefined){
		this.server = server;
	}

	this.getRDFfile = function(url_str, label, type, func, error){
		var date = new Date();
		var month = date.getMonth()+1;

		var date_str = date.getFullYear() +"/"+ month +"/"+ date.getDate();

		console.log(date_str);

		var param = {
				url: url_str,
				label: label,
				type: type,
				date: date_str,
				reqtype: "set_rdf"
		};

		this.transport(param, func, error);
	}

	this.getRDFep = function(url_str, label, func, error){
		var date = new Date();
		var month = date.getMonth()+1;

		var date_str = date.getFullYear() +"/"+ month +"/"+ date.getDate();

		var param = {
				url: url_str,
				label: label,
				date: date_str,
				reqtype: "set_ep"
		};

		this.transport(param, func, error);
	}

	this.executeQuery = function(sparql, style, func, error, service){
		console.log(service);

		var param = {
				query: sparql,
				output: style,
				reqtype: "query"
		}

		if(service != undefined){
			param.service = service;
		}

		this.transport(param, function(data){

			console.log(json);
			result = makeResult(json);
			func(result);
		}, error);
	}

	this.forwardResult = function(sparql, style, service){
		var param = {
				query: sparql,
				output: style,
				reqtype: "query",
		}

		if(service != undefined){
			param.service = service;
		}

		var param_str = "?";

		for(key in param){
			param_str += key +"="+ param[key] +"&";
		}

		location.href = server + param_str;

	}

	function makeResult(data){
		var varlist = new Array();
		var dlist = new Array();
		var strtemp = new String();
		var strtype = new String();

		var result = new Object();

		if(data.queryType == "ASK"){
			return data;
		}else if(data.queryType == "CONSTRUCT"){
			return data;
		}else if(data.queryType == "DESCRIBE"){
			return data;
		}

		for(var i=0; i < data.head.vars.length; i++){
			varlist.push(data.head.vars[i]);
		}

		for(var i=0; i < data.results.bindings.length; i++){
			var addlist = new Array();
			var addobj = new Object();
			var obj = data.results.bindings[i];
			for(var j=0; j < varlist.length; j++){
				if((obj[varlist[j]]) != undefined){
					strtemp = obj[varlist[j]].value;
					strtype = obj[varlist[j]].type;
				}else{
					strtemp = undefined;
				}
				if(strtemp != undefined){
					addlist.push({type: strtype ,value:strtemp});
				}else{
					addlist.push({type: undefined ,value:undefined});
				}
			}
			dlist.push(addlist);
		}

		result.keylist = varlist;
		result.valuelist = dlist;
		result.jsonlist = data;
		result.now = -1;
		result.next = function(){
			this.now++;
			if(this.now >= this.getValueListLength()){
				return false;
			}else{
				return true;
			}
		}
		result.getValue = function(v){
			if(this.now == -1){
				console.log("Error. Please use the 'next()' before use 'getValue()'.");
			}else{
				if(typeof v == "string"){
					var varindex = this.keylist.indexOf(v);
					if(varindex == -1){
						return undefined;
					}else{
						return this.valuelist[this.now][varindex].value;
					}
				}else if(typeof v == "number"){
					return this.valuelist[this.now][v].value;
				}
			}
		}
		result.getType = function(v){
			if(this.now == -1){
				console.log("Error. Please use the 'next()' before use 'getType()'.");
			}else{
				if(typeof v == "string"){
					var varindex = this.keylist.indexOf(v);
					if(varindex == -1){
						return undefined;
					}else{
						return this.valuelist[now][varindex].type;
					}
				}else if(typeof v == "number"){
					return this.valuelist[this.now][v].type;
				}
			}
		}
		result.getKey = function(i){
			return this.keylist[i];
		}
		result.getKeyList = function(){
			return this.keylsit;
		}
		result.getList = function(){
			return this.valuelist[this.now];
		}
		result.getValueList = function(){
			return this.valuelist;
		}
		result.getJson = function(){
			return this.jsonlist;
		}
		result.getKeyListLength = function(){
			return this.keylist.length;
		}
		result.getLength = function(){
			if(this.now == -1){
				console.log("Error. Please use the 'next()' before use 'getLength()'.");
			}else{
				return this.valuelist[this.now].length;
			}
		}
		result.getValueListLength = function(){
			return this.valuelist.length;
		}
		result.reset = function(){
			this.now = -1;
		}

		return result;
	}

	this.transport = function(param, func, error){
		$.ajax({
			url: server,
			data: param,
			success: function(data){
				try{
					  // 何らかのプログラム
					json = JSON.parse(data);
					if(json.error){
						error(json);
					}else{
						func(json);
					}
				}catch( e ){
					func();
				}

				//console.log(json);
			},
			error: function(){
				console.log("通信エラー");
			}
		});
	}
}