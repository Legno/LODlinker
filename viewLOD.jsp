<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="lodlinker.*"%>

<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!-->
<html class="no-js">
<!--<![endif]-->
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>LODlinker</title>
<meta name="description" content="">
<meta name="viewport" content="width=device-width, initial-scale=1">

<!-- Place favicon.ico and apple-touch-icon.png in the root directory -->

<link rel="stylesheet" href="css/normalize.css">
<link rel="stylesheet" href="css/bootstrap.css">
<link rel="stylesheet" href="css/responsive.css">
<link rel="stylesheet" href="css/font-awesome.min.css" rel="stylesheet">

<link rel="stylesheet"
	href="content/font/elusive-icons/css/elusive-webfont.min.css">
<link rel="stylesheet" href="css/style.css">

<link rel="stylesheet" href="css/main.css">

<script src="js/vendor/modernizr-2.6.2.min.js"></script>


<script
	src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
<script>
	window.jQuery
			|| document.write(
					'<script src="js/vendor/jquery-1.10.2.min.js"><\/script>')
</script>
<script src="js/bootstrap.js"></script>
<script src="js/plugins.js"></script>
<script src="js/connector.js"></script>
<script src="js/viewLOD.js"></script>
</head>
<body>
	<!--[if lt IE 7]>
            <p class="browsehappy">You are using an <strong>outdated</strong> browser. Please <a href="http://browsehappy.com/">upgrade your browser</a> to improve your experience.</p>
        <![endif]-->

	<!-- Add your site or application content here -->
	<div class="main container text-center">
		<div class="title row">
			<div class="span12">
				<a href="/"><img src="./img/title.png" width="250" class="img"></a><br><br>
			</div>
		</div>
		<div class="search_result row">
			<div class="span10 offset1">
				<div id="table"></div>
			</div>
		</div>

		<div class="row search_form">

		</div>

		<div class="foot row">
			<div class="span12">
				<p>
					<a href="about.html">about</a> | <a href='#addModal' data-toggle='modal'>LODを登録</a> | <a href='viewLOD.jsp'>LOD一覧</a> | <a href='contact.html'>contact</a>
				</p>
			</div>
		</div>
	</div>

	<div class="modal hide fade" id="addModal">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<h4 class="modal-title">LODを登録</h4>
				</div>
				<div class="modal-body span12" id="mbody2">
					<div>
						タイプ <select selected="selected" id="modal_select"
							onchange="changeSelect()"><option value="SPARQLEndPoint">SPARQLエンドポイント</option>
							<option value="rdfFILE">RDFファイル</option>
						</select>
					</div>
					<hr width="450">
					<div id="modal_main">
						<form class="form-horizontal">
							<div class="control-group">
								<label class="control-label" for="inputURL">URL</label>
								<div class="controls">
									<input type="text" id="inputURL" placeholder="LODのURL">
								</div>
							</div>
							<div class="control-group">
								<label class="control-label" for="inputTitle">タイトル</label>
								<div class="controls">
									<input type="text" id="inputTitle" placeholder="タイトル(ラベル)">
								</div>
							</div>
							<div class="control-group hide" id="ftype_s">
								<label class="control-label" for="inputFtype">ファイル形式</label>
								<div class="controls">
									<input type="text" id="inputFtype"
										placeholder="RDF/XML or TTL or N-TRIPLE or N3">
								</div>
							</div>
							<div class="control-group">
								<button type="button" class="btn-primary" onclick="addLOD()">登録</button>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>

	<!-- Google Analytics: change UA-XXXXX-X to be your site's ID. -->
	<script>
		(function(b, o, i, l, e, r) {
			b.GoogleAnalyticsObject = l;
			b[l] || (b[l] = function() {
				(b[l].q = b[l].q || []).push(arguments)
			});
			b[l].l = +new Date;
			e = o.createElement(i);
			r = o.getElementsByTagName(i)[0];
			e.src = '//www.google-analytics.com/analytics.js';
			r.parentNode.insertBefore(e, r)
		}(window, document, 'script', 'ga'));
		ga('create', 'UA-XXXXX-X');
		ga('send', 'pageview');
	</script>

<textarea id="shadowTextarea" style="display:none">

</textarea>
</body>
</html>
