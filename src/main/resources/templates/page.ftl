<#macro generateDoc doc>
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
	</head>
	
	<body>
		<@generateDocTree docTree=doctree/>
	</body>
</html>