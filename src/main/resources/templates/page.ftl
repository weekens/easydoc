<#macro generateDoc doc>
	<!-- Generated from ${doc.sourceLink.file.path}:${doc.sourceLink.line} -->
	<p>${doc.text}</p>
</#macro>

<#macro generateDocTree docTree>
	<#list docTree as doc>
		<@generateDoc doc=doc/>
		<#if doc.children??>
			<@generateDocTree docTree=doc.children/>
		</#if>
	</#list>
</#macro>

<html>
	<head>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8">
		<title>Documentation</title>
		
		<style>
			body {
				font-family: "Helvetica Neue",Helvetica,Arial,sans-serif;
				color: #404040;
			}
			
			h1, h2, h3, h4, h5 {
				background-image: linear-gradient(left , rgb(186,227,242) 2%, rgb(255,255,255) 51%, rgb(250,253,255) 85%);
				background-image: -o-linear-gradient(left , rgb(186,227,242) 2%, rgb(255,255,255) 51%, rgb(250,253,255) 85%);
				background-image: -moz-linear-gradient(left , rgb(186,227,242) 2%, rgb(255,255,255) 51%, rgb(250,253,255) 85%);
				background-image: -webkit-linear-gradient(left , rgb(186,227,242) 2%, rgb(255,255,255) 51%, rgb(250,253,255) 85%);
				background-image: -ms-linear-gradient(left , rgb(186,227,242) 2%, rgb(255,255,255) 51%, rgb(250,253,255) 85%);
				
				background-image: -webkit-gradient(
					linear,
					left bottom,
					right bottom,
					color-stop(0.02, rgb(186,227,242)),
					color-stop(0.51, rgb(255,255,255)),
					color-stop(0.85, rgb(250,253,255))
				);
				
				color: #000077;
			}
			
			pre {
				background-image: linear-gradient(left , rgb(245,245,215) 2%, rgb(255,255,255) 51%, rgb(250,253,255) 85%);
				background-image: -o-linear-gradient(left , rgb(245,245,215) 2%, rgb(255,255,255) 51%, rgb(250,253,255) 85%);
				background-image: -moz-linear-gradient(left , rgb(245,245,215) 2%, rgb(255,255,255) 51%, rgb(250,253,255) 85%);
				background-image: -webkit-linear-gradient(left , rgb(245,245,215) 2%, rgb(255,255,255) 51%, rgb(250,253,255) 85%);
				background-image: -ms-linear-gradient(left , rgb(245,245,215) 2%, rgb(255,255,255) 51%, rgb(250,253,255) 85%);
				
				background-image: -webkit-gradient(
					linear,
					left bottom,
					right bottom,
					color-stop(0.02, rgb(245,245,215)),
					color-stop(0.51, rgb(255,255,255)),
					color-stop(0.85, rgb(250,253,255))
				);
				
				font-family: Monaco,Andale Mono,Courier New,monospace;
			}
		</style>
	</head>
	
	<body>
		<@generateDocTree docTree=doctree/>
	</body>
</html>