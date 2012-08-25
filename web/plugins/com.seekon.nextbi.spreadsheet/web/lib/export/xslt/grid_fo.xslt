<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:fn="http://www.w3.org/2005/xpath-functions" xmlns:xdt="http://www.w3.org/2005/xpath-datatypes" xmlns:xs="http://www.w3.org/2001/XMLSchema">
	<xsl:output version="1.0" method="xml" encoding="UTF-8" indent="no"/>
	<xsl:param name="SV_OutputFormat" select="'PDF'"/>
	<xsl:variable name="XML" select="/"/>
	<xsl:variable name="fo:layout-master-set">
		<fo:layout-master-set>
			<fo:simple-page-master master-name="default-page" page-height="11in" page-width="8.5in" margin-left="0.6in" margin-right="0.6in">
				<fo:region-body margin-top="0.79in" margin-bottom="0.79in"/>
			</fo:simple-page-master>
		</fo:layout-master-set>
	</xsl:variable>
	<xsl:template match="/">
		<xsl:variable name="maxwidth" select="7.30000"/>
		<fo:root>
			<xsl:copy-of select="$fo:layout-master-set"/>
			<fo:page-sequence master-reference="default-page" initial-page-number="1" format="1">
				<fo:flow flow-name="xsl-region-body">
					<fo:block>
						<xsl:for-each select="$XML">
							<xsl:variable name="tablewidth0" select="$maxwidth * 1.00000 - 0.01042 - 0.01042"/>
							<xsl:variable name="sumcolumnwidths0" select="0.04167"/>
							<xsl:variable name="defaultcolumns0" select="1"/>
							<xsl:variable name="defaultcolumnwidth0">
								<xsl:choose>
									<xsl:when test="$defaultcolumns0 &gt; 0">
										<xsl:value-of select="($tablewidth0 - $sumcolumnwidths0) div $defaultcolumns0"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="0.000"/>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:variable>
							<xsl:variable name="columnwidth0_0" select="$defaultcolumnwidth0"/>
							<fo:table margin-left="0.0in" margin-right="0.0in" width="{$maxwidth}in">
								<fo:table-column column-width="{0.01042}in"/>
								<fo:table-column column-width="{$tablewidth0}in"/>
								<fo:table-column column-width="{0.01042}in"/>
								<fo:table-body>
									<fo:table-row>
										<fo:table-cell>
											<fo:block/>
										</fo:table-cell>
										<fo:table-cell>
											<fo:block>
												<fo:table width="{$tablewidth0}in" border-top-style="solid" border-top-color="#ECE9D8" border-top-width="0.01042in" border-bottom-style="solid" border-bottom-color="#ACA899" border-bottom-width="0.01042in" border-left-style="solid" border-left-color="#ECE9D8" border-left-width="0.01042in" border-right-style="solid" border-right-color="#ACA899" border-right-width="0.01042in" border-collapse="separate" border-separation="0.04167in" color="black" display-align="center" text-align="left">
													<fo:table-column column-width="{$columnwidth0_0}in"/>
													<fo:table-body>
														<xsl:for-each select="grid">
															<xsl:for-each select="table">
																<xsl:for-each select="tbody">
																	<fo:table-row>
																		<fo:table-cell border-top-style="solid" border-top-color="#ACA899" border-top-width="0.01042in" border-bottom-style="solid" border-bottom-color="#ECE9D8" border-bottom-width="0.01042in" border-left-style="solid" border-left-color="#ACA899" border-left-width="0.01042in" border-right-style="solid" border-right-color="#ECE9D8" border-right-width="0.01042in" padding-top="0.02083in" padding-bottom="0.02083in" padding-left="0.02083in" padding-right="0.02083in">
																			<fo:block padding-top="1pt" padding-bottom="1pt">
																				<xsl:for-each select="tr">
																					<xsl:variable name="tablewidth1" select="$columnwidth0_0 * 1.00000 - 0.01042 - 0.01042 - 0.02083 - 0.02083 - 0.01042 - 0.01042"/>
																					<xsl:variable name="sumcolumnwidths1" select="0.04167 * count(td)"/>
																					<xsl:variable name="defaultcolumns1" select="count(td)"/>
																					<xsl:variable name="defaultcolumnwidth1">
																						<xsl:choose>
																							<xsl:when test="$defaultcolumns1 &gt; 0">
																								<xsl:value-of select="($tablewidth1 - $sumcolumnwidths1) div $defaultcolumns1"/>
																							</xsl:when>
																							<xsl:otherwise>
																								<xsl:value-of select="0.000"/>
																							</xsl:otherwise>
																						</xsl:choose>
																					</xsl:variable>
																					<xsl:variable name="columnwidth1_0" select="$defaultcolumnwidth1"/>
																					<fo:table margin-left="0.0in" margin-right="0.0in" width="{$columnwidth0_0 - 0.01042 - 0.01042 - 0.02083 - 0.02083}in" border-collapse="collapse">
																						<fo:table-column column-width="{0.01042}in"/>
																						<fo:table-column column-width="{$tablewidth1}in"/>
																						<fo:table-column/>
																						<fo:table-body>
																							<fo:table-row>
																								<fo:table-cell>
																									<fo:block/>
																								</fo:table-cell>
																								<fo:table-cell>
																									<fo:block>
																										<fo:table width="{$tablewidth1}in" border-top-style="solid" border-top-color="#ECE9D8" border-top-width="0.01042in" border-bottom-style="solid" border-bottom-color="#ACA899" border-bottom-width="0.01042in" border-left-style="solid" border-left-color="#ECE9D8" border-left-width="0.01042in" border-right-style="solid" border-right-color="#ACA899" border-right-width="0.01042in" border-collapse="separate" border-separation="0.04167in" color="black" display-align="center" text-align="left">
																											<xsl:for-each select="td">
																												<fo:table-column column-width="{$columnwidth1_0}in"/>
																											</xsl:for-each>
																											<fo:table-body>
																												<fo:table-row>
																													<xsl:for-each select="td">
																														<fo:table-cell border-top-style="solid" border-top-color="#ACA899" border-top-width="0.01042in" border-bottom-style="solid" border-bottom-color="#ECE9D8" border-bottom-width="0.01042in" border-left-style="solid" border-left-color="#ACA899" border-left-width="0.01042in" border-right-style="solid" border-right-color="#ECE9D8" border-right-width="0.01042in" padding-top="0.02083in" padding-bottom="0.02083in" padding-left="0.02083in" padding-right="0.02083in">
																															<fo:block padding-top="1pt" padding-bottom="1pt">
																																<fo:inline>
																																	<xsl:apply-templates>
																																		<xsl:with-param name="maxwidth" select="$columnwidth1_0 - 0.01042 - 0.01042 - 0.02083 - 0.02083"/>
																																	</xsl:apply-templates>
																																</fo:inline>
																															</fo:block>
																														</fo:table-cell>
																													</xsl:for-each>
																												</fo:table-row>
																											</fo:table-body>
																										</fo:table>
																									</fo:block>
																								</fo:table-cell>
																								<fo:table-cell>
																									<fo:block/>
																								</fo:table-cell>
																							</fo:table-row>
																						</fo:table-body>
																					</fo:table>
																				</xsl:for-each>
																			</fo:block>
																		</fo:table-cell>
																	</fo:table-row>
																</xsl:for-each>
															</xsl:for-each>
														</xsl:for-each>
													</fo:table-body>
												</fo:table>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell>
											<fo:block/>
										</fo:table-cell>
									</fo:table-row>
								</fo:table-body>
							</fo:table>
						</xsl:for-each>
					</fo:block>
					<fo:block id="SV_RefID_PageTotal"/>
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>
	<xsl:template name="double-backslash">
		<xsl:param name="text"/>
		<xsl:param name="text-length"/>
		<xsl:variable name="text-after-bs" select="substring-after($text, '\')"/>
		<xsl:variable name="text-after-bs-length" select="string-length($text-after-bs)"/>
		<xsl:choose>
			<xsl:when test="$text-after-bs-length = 0">
				<xsl:choose>
					<xsl:when test="substring($text, $text-length) = '\'">
						<xsl:value-of select="concat(substring($text,1,$text-length - 1), '\\')"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$text"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="concat(substring($text,1,$text-length - $text-after-bs-length - 1), '\\')"/>
				<xsl:call-template name="double-backslash">
					<xsl:with-param name="text" select="$text-after-bs"/>
					<xsl:with-param name="text-length" select="$text-after-bs-length"/>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>
