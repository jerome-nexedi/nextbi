<?xml version="1.0" encoding="utf-8"?>
<stylesheet
  version="1.0"
  xmlns="http://www.w3.org/1999/XSL/Transform"
  xmlns:doc="http://tempuri.org/Palo/SpreadsheetFuncs/Documentation.xsd"
>

  <template match="/root//function">
    <value-of select="translation" /> - <value-of select="description/short" />
  </template>

</stylesheet>