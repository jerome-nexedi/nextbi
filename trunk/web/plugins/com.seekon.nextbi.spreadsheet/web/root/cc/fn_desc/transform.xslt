<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xml:space="default">
  <xsl:strip-space elements="*" />
  <xsl:template match="/">
	  $funcdata = array(
		  'cats' <xsl:text disable-output-escaping="yes">=></xsl:text> array(
				<xsl:for-each select="root/groups/group">
					array(
						"<xsl:value-of select="@id" />",
						htmlspecialchars_decode("<xsl:value-of select="translation"></xsl:value-of>")
					),
				</xsl:for-each>
			),
		  'funcs' <xsl:text disable-output-escaping="yes">=></xsl:text> array(
				<xsl:for-each select="root/function">
				  array(
						"<xsl:value-of select="@category" />",
						"<xsl:value-of select="translation" />",
						"<xsl:value-of select="description/syntax" />",
						preg_replace('/ {2,}/', ' ', preg_replace('/[\n\r\t]/', '', trim("<xsl:value-of select="description/short" disable-output-escaping="yes" />"))),
						array(<xsl:apply-templates select="description/parameters" />)
				   ),
				</xsl:for-each>
			)
		);
	</xsl:template>
	<xsl:template match="parameter">
	  array(
			't' <xsl:text disable-output-escaping="yes">=></xsl:text> "<xsl:value-of select="@type" />",
			'n' <xsl:text disable-output-escaping="yes">=></xsl:text> "<xsl:value-of select="@name" />",
			'd' <xsl:text disable-output-escaping="yes">=></xsl:text> preg_replace('/ {2,}/', ' ', preg_replace('/[\n\r\t]/', '', trim("<xsl:value-of select="." disable-output-escaping="yes" />")))
	   ),
	</xsl:template>
	<xsl:template match="sequence">
	  array(
			't' <xsl:text disable-output-escaping="yes">=></xsl:text> 'sequence',
			'c' <xsl:text disable-output-escaping="yes">=></xsl:text> (<xsl:value-of select="@to" /> - <xsl:value-of select="@from" /> + 1),
			'm' <xsl:text disable-output-escaping="yes">=></xsl:text> array(<xsl:apply-templates select="parameter" />)
	   ),
	</xsl:template>
</xsl:stylesheet>