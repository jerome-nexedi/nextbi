<?xml version="1.0" encoding="UTF-8"?><structure version="7" cssmode="quirks" relativeto="*SPS" encodinghtml="UTF-8" encodingrtf="ISO-8859-1" encodingpdf="UTF-8" embed-images="1">	<parameters/>	<schemasources>		<namespaces/>		<schemasources>			<xsdschemasource name="$XML" main="1" schemafile="example1.dtd" workingxmlfile="example1.xml">				<xmltablesupport/>				<textstateicons/>			</xsdschemasource>		</schemasources>	</schemasources>	<modules/>	<flags>		<scripts/>		<globalparts/>		<designfragments/>		<pagelayouts/>	</flags>	<scripts>		<script language="javascript"/>	</scripts>	<globalstyles/>	<mainparts>		<children>			<globaltemplate match="/" matchtype="named" parttype="main">				<children>					<template match="$XML" matchtype="schemasource">						<editorproperties editable="1" markupmode="large" adding="mandatory" autoaddname="1" elementstodisplay="1"/>						<children>							<newline/>							<table>								<properties cellpadding="0" cellspacing="0"/>								<children>									<tablebody>										<children>											<template match="worksheet" matchtype="schemagraphitem">												<editorproperties elementstodisplay="1"/>												<children>													<template match="grid" matchtype="schemagraphitem">														<editorproperties elementstodisplay="1"/>														<children>															<template match="rows" matchtype="schemagraphitem">																<editorproperties elementstodisplay="1"/>																<children>																	<template match="row" matchtype="schemagraphitem">																		<editorproperties elementstodisplay="1"/>																		<children>																			<tablerow>																				<styles _xfont-weight="@height" _xheight="@height"/>																				<children>																					<tablecell>																						<children>																							<template match="cells" matchtype="schemagraphitem">																								<editorproperties elementstodisplay="1"/>																								<children>																									<table>																										<properties cellpadding="0" cellspacing="0"/>																										<styles height="100%"/>																										<children>																											<tablebody>																												<children>																													<tablerow>																														<children>																															<template match="cell" matchtype="schemagraphitem">																																<editorproperties elementstodisplay="1"/>																																<children>																																	<tablecell>																																		<styles _xbackground-color="@background-color" _xcolor="@color" _xfont-family="@font-family" _xfont-size="@font-size" _xfont-style="@font-style" _xfont-weight="@font-weight" _xheight="../../@height" _xtext-align="@text-align" _xtext-decoration="@text-decoration" _xwidth="@width"/>																																		<children>																																			<template match="value" matchtype="schemagraphitem">																																				<editorproperties elementstodisplay="1"/>																																				<children>																																					<content/>																																				</children>																																			</template>																																		</children>																																	</tablecell>																																</children>																															</template>																														</children>																													</tablerow>																												</children>																											</tablebody>																										</children>																									</table>																								</children>																							</template>																						</children>																					</tablecell>																				</children>																			</tablerow>																		</children>																	</template>																</children>															</template>														</children>													</template>												</children>											</template>										</children>									</tablebody>								</children>							</table>						</children>					</template>				</children>			</globaltemplate>		</children>	</mainparts>	<globalparts/>	<pagelayout/>	<designfragments/></structure>