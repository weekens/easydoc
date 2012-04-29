Quick Start
===========

Setup
-----------

To use Easydoc with Maven, you just need to declare easydoc-maven-plugin in you pom.xml. 
See [this](http://weekens.github.com/easydoc#easydoc-maven) section for more information.

Writing docs
-----------

After you're set up, just start writing docs right inside your source files.

	/*@@easydoc-start@@
	<h1>RESTful API</h1>
	
	The service exposes RESTful API to provide access to it's resources.
	The methods are available under http://company.com/myservice/api
	@@easydoc-end@@*/
	@Controller("/api")
	class RESTController {
	}

or

	<!--@@easydoc-start@@
	<h1>Database</h1>
	
	The service uses database, which is configured in database.xml file. 
	@@easydoc-end@@-->
	<import location="database.xml"/>

All the HTMLs between @@easydoc-start@@ and @@easydoc-end@@ keys are the *docs*. They will get to the resulting
documentation page. This is briefly described [here](http://weekens.github.com/easydoc#easydoc-intro).