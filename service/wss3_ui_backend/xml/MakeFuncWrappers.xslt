<?xml version="1.0" encoding="utf-8"?>
<stylesheet version="1.0"
            xmlns="http://www.w3.org/1999/XSL/Transform"
            xmlns:doc="http://tempuri.org/Palo/SpreadsheetFuncs/Documentation.xsd">
  <output method="text" indent="no"/>

  <template match="doc:Function">
    <if test="./doc:ExcelSpecific and not(./doc:ExcelSpecific[@xl_helper='true'] or ./doc:ExcelSpecific[@xl_helper='1'])">
        <text disable-output-escaping="yes">
PALO_FUNCTION_WRAPPER(</text>
      <value-of disable-output-escaping="yes" select="@c_name"/>
      <text disable-output-escaping="yes">,</text>
      <value-of disable-output-escaping="yes" select="@internal_name"/>

      <text disable-output-escaping="yes">);</text>
     </if>
  </template>
</stylesheet>