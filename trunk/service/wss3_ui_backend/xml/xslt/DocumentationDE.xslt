<?xml version="1.0" encoding="utf-8"?>
<stylesheet
  version="1.0"
  xmlns="http://www.w3.org/1999/XSL/Transform"
  xmlns:doc="http://tempuri.org/Palo/SpreadsheetFuncs/Documentation.xsd"
>
  <output
    method="xml"
    version="1.0"
    encoding="utf-8"
    omit-xml-declaration="yes"
    indent="yes"
  />
  <template match="doc:Functions">
  <for-each select="doc:Function">
  <sort select="doc:ExcelSpecific/@name" />
  <if test="doc:ExcelSpecific/@name!=''"><if test="doc:ExcelSpecific/@func_type!='0'">
    <text disable-output-escaping="yes"><![CDATA[<function name="]]></text>
    <value-of select="doc:ExcelSpecific/@name" />
    <text disable-output-escaping="yes"><![CDATA[" category="PALO">
    <translation>]]></text>
    <value-of select="doc:ExcelSpecific/@name" />
    <text disable-output-escaping="yes"><![CDATA[</translation>
    <description>
      <parameters>]]></text>
        <for-each select="doc:ArgumentPool/doc:Argument">
        <text disable-output-escaping="yes"><![CDATA[
        <parameter name="]]></text>
        <value-of select="doc:ShortDescription/doc:Value[@lang='de-DE']" />
        <text disable-output-escaping="yes"><![CDATA[" type="text"><![CDATA[]]></text>
          <value-of select="doc:LongDescription/doc:Value[@lang='de-DE']" />]]<text disable-output-escaping="yes"><![CDATA[>]]></text>
        <text disable-output-escaping="yes"><![CDATA[</parameter>]]></text>
        </for-each>
        <text disable-output-escaping="yes"><![CDATA[
      </parameters>
      <syntax><![CDATA[]]></text>          
        <value-of select="doc:ExcelSpecific/@name" /><text>(</text>
        <for-each select="doc:ArgumentPool/doc:Argument">
          <value-of select="doc:ShortDescription/doc:Value[@lang='de-DE']" />
          <if test="position()!=last()"><text>;</text>
          </if>
        </for-each>)]]<text disable-output-escaping="yes"><![CDATA[>]]></text>
      <text disable-output-escaping="yes"><![CDATA[</syntax>
      <short><![CDATA[]]></text>
        <value-of select="doc:ShortDescription/doc:Value[@lang='de-DE']" />]]<text disable-output-escaping="yes"><![CDATA[>]]></text>
        <text disable-output-escaping="yes"><![CDATA[</short>
      <long><![CDATA[]]></text>
        <value-of select="doc:LongDescription/doc:Value[@lang='de-DE']" />]]<text disable-output-escaping="yes"><![CDATA[>]]></text>
        <text disable-output-escaping="yes"><![CDATA[</long>
    </description>
    <comment><![CDATA[some text for the translator]]></text>]]<text disable-output-escaping="yes"><![CDATA[></comment>
  </function>]]>
  </text>
  </if></if>
  </for-each>
  </template>
</stylesheet>
