<?xml version="1.0" encoding="utf-8"?>
<stylesheet version="1.0"
            xmlns="http://www.w3.org/1999/XSL/Transform"
            xmlns:doc="http://tempuri.org/Palo/SpreadsheetFuncs/Documentation.xsd">
  <output method="text" indent="no"/>

  <template match="doc:Function">
    <if test="./doc:ExcelSpecific and not(./doc:ExcelSpecific[@xl_helper='true'] or ./doc:ExcelSpecific[@xl_helper='1'])">
      <text disable-output-escaping="yes">
        REGISTER_PALO_FUNCTION(</text>
      <value-of disable-output-escaping="yes" select="@c_name"/>
      <text disable-output-escaping="yes">, "</text>
      <value-of disable-output-escaping="yes" select="./doc:ExcelSpecific/@name"/>
      <text disable-output-escaping="yes">", </text>

      <variable name="sigs" select="./doc:Signatures/doc:Signature"/>
      <if test="count($sigs) > 1">
        <text disable-output-escaping="yes">0</text>
      </if>
      <if test="./doc:ArgumentPool/doc:Argument/@repeat">
        <text disable-output-escaping="yes">0</text>
      </if>
      <if test="count($sigs) = 1">
        <if test="not(./doc:ArgumentPool/doc:Argument/@repeat)">
          <variable name="args" select="./doc:Signatures/doc:Signature/doc:ArgumentRef"/>
          <value-of select="count($args)"/>
        </if>
      </if>
      <text disable-output-escaping="yes">,</text>
      <choose>
        <when test="./doc:PaloWebSpecific">
          <value-of disable-output-escaping="yes" select="./doc:PaloWebSpecific/@is_view_independent"/>
          <text disable-output-escaping="yes">,</text>
          <value-of disable-output-escaping="yes" select="./doc:PaloWebSpecific/@execute_on_set"/>
        </when>
        <otherwise>
          <text disable-output-escaping="yes">false,false</text>
        </otherwise>
        </choose>
      <text disable-output-escaping="yes">)</text>
    </if>
    
  </template>
</stylesheet>