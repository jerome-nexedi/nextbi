<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fn="http://www.w3.org/2005/xpath-functions" xmlns:xdt="http://www.w3.org/2005/xpath-datatypes" xmlns:xs="http://www.w3.org/2001/XMLSchema">
	<xsl:output version="1.0" method="html" indent="no" encoding="UTF-8"/>
	<xsl:param name="SV_OutputFormat" select="'HTML'"/>
	<xsl:variable name="XML" select="/"/>
	<xsl:template match="/">
		<html>
			<head>
				<title/>
			</head>
			<body>
				<xsl:for-each select="$XML">
					<table border="1">
						<tbody>
							<xsl:for-each select="grid">
								<xsl:for-each select="table">
									<xsl:for-each select="tbody">
										<tr>
											<td style="height:{../@height}; ">
												<xsl:for-each select="tr">
													<table border="1">
														<tbody>
															<tr>
																<xsl:for-each select="td">
																	<td style="height:{../@height}; width:{@width}; ">
																		<xsl:apply-templates/>
																	</td>
																</xsl:for-each>
															</tr>
														</tbody>
													</table>
												</xsl:for-each>
											</td>
										</tr>
									</xsl:for-each>
								</xsl:for-each>
							</xsl:for-each>
						</tbody>
					</table>
				</xsl:for-each>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>
