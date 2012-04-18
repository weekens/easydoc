<#macro generateDoc doc>
	<!-- Generated from ${doc.sourceLink.file.path}:${doc.sourceLink.line} -->
	<p>
		${doc.text}
		<#if sourceBrowser??>
			<p>
				<a class="easydoc-source-link" 
					href="${sourceBrowser.generateUrl(doc)}" 
					target="_blank">source link</a>
			</p>
		</#if>
	</p>
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
		
		<#if css??>
			<style>
				${css}
			</style>
		</#if>
	</head>
	
	<body>
		<@generateDocTree docTree=doctree/>
	</body>
</html>