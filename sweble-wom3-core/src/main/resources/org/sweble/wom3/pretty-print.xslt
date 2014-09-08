<?xml version="1.1" encoding="UTF-8"?>
<!--

    Copyright 2011 The Open Source Research Group,
                   University of Erlangen-Nürnberg

-->
<xsl:stylesheet
	version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xalan="http://xml.apache.org/xslt"
	xmlns:wom="http://sweble.org/schema/wom30">

	<xsl:output 
		omit-xml-declaration="no"
		standalone="no"
		method="xml"
        version="1.1"
		encoding="UTF-8"
		indent="yes"
		xalan:indent-amount="2" />

	<xsl:strip-space elements="*" />
	<xsl:preserve-space elements="wom:text wom:rtd" />

	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>
	</xsl:template>

</xsl:stylesheet>
