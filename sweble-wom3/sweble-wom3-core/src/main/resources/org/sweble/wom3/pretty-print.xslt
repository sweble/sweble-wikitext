<?xml version="1.1" encoding="UTF-8"?>
<!--

    Copyright 2011 The Open Source Research Group,
                   University of Erlangen-Nürnberg

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see http://www.gnu.org/licenses/.

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
