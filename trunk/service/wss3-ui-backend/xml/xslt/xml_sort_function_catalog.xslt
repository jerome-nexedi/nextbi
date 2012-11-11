<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:fn="http://www.w3.org/2005/xpath-functions">
	<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes" />

	<xsl:template match="/root">
		<root>
			<xsl:attribute name="lang" select="attribute(lang)"/>
			<xsl:apply-templates select="*"/>
			<xsl:apply-templates select="//function" mode="function_only">
				<xsl:sort select="attribute(name)"/>
			</xsl:apply-templates>
		</root>
	</xsl:template>

	<xsl:template match="function" mode="function_only">
		<xsl:copy-of select=".">
			<xsl:apply-templates/>
		</xsl:copy-of>
	</xsl:template>
	
	<xsl:template match="*">
	<xsl:choose >
		<xsl:when test="name() =  'function'"></xsl:when>
			<xsl:otherwise>
				<xsl:copy-of select=".">
					<xsl:apply-templates/>
				</xsl:copy-of>
			</xsl:otherwise>
	</xsl:choose>
	</xsl:template>
	
</xsl:stylesheet>
