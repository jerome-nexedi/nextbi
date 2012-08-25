Jedox.wss.cndfmt=new function(){var that=this,_borderTypes=["top","bottom","left","right"];this.SCOPE_CURR_SEL=0;this.SCOPE_CURR_WKS=1;this.SCOPE_RULE_IDS=2;this.set=function(rules){var defSel=Jedox.wss.app.environment.defaultSelection,conn=Jedox.wss.backend.conn,fParser=Jedox.wss.formula,jwstyle=Jedox.wss.style,activeBook=Jedox.wss.app.activeBook,rule,style,borders,btype,border,ranges,key,rng,rngs,sets=[],set,j,i=-1;while((rule=rules[++i])!==undefined){set={rule:rule.type.concat("(",rule.operator,rule.sit?";1;)":";0;)")};if("format" in rule&&typeof rule.format=="string"&&rule.format!=""){set.format=rule.format}if("style" in rule&&(style=jwstyle.convJStoCSS(rule.style))!=""){set.style=style}if("lock" in rule&&typeof rule.lock=="boolean"){set.lock=rule.lock}if("borders" in rule&&rule.borders instanceof Object&&!("length" in rule.borders)){borders={};for(btype in rule.borders){if(_borderTypes.indexOf(btype)!=-1&&(border=jwstyle.borderStyle2CSS(rule.borders[btype]))!=""){borders[btype]=border}}for(btype in borders){set.border=borders;break}}if("operands" in rule&&rule.operands instanceof Array&&rule.operands.length){set.operands=rule.operands}ranges=[];if("applies_to" in rule){if("id" in rule){set.id=rule.id}rngs=fParser.parse(rule.applies_to);j=-1;while((rng=rngs[++j])!==undefined){ranges.push(rng.rng)}}else{if("id" in rule){continue}rngs=defSel.getRanges(),j=-1;while((rng=rngs[++j])!==undefined){ranges.push(rng.getCoords())}}if(!ranges.length){continue}set.ranges=ranges;sets.push(set)}if(sets.length){conn.cmd([activeBook,activeBook.exec],["cfset"],defSel.getActiveRange().getActiveCell().getCoords().concat(sets),activeBook.getRealGridRange(),false,false,conn.Q_STYLE|conn.Q_FMT_VAL|conn.Q_FMT|conn.Q_LOCK)}};this.get=function(scope){var defSel=Jedox.wss.app.environment.defaultSelection,conn=Jedox.wss.backend.conn,jwstyle=Jedox.wss.style,params=[],i=-1,rule,rules,ruletmp,css;switch(scope){case that.SCOPE_CURR_SEL:case undefined:var rngs=defSel.getRanges(),ranges=[],rng;while((rng=rngs[++i])!==undefined){ranges.push(rng.getCoords())}if(ranges.length){params.push("",ranges)}break;case that.SCOPE_CURR_WKS:break;default:if(typeof scope=="string"){params.push(scope)}break}rules=conn.cmd(null,["cfget"],params);if(rules[0][0]!==true||!(rules[0][1] instanceof Array)){return[]}rules=rules[0][1];i=-1;while((rule=rules[++i])!==undefined){ruletmp=rule.rule.split("(");delete rule.rule;rule.type=ruletmp[0];ruletmp=ruletmp[1].split(";");rule.operator=ruletmp[0];rule.sit=ruletmp[1]=="1"?true:false;if(typeof rule.style=="string"){rule.style=jwstyle.convCSStoJS(rule.style)}if(rule.format===""){rule.format=null}if(!("border" in rule)){continue}for(btype in rule.border){if((css=rule.border[btype])!=""&&(css=css.split(" ")).length==3){rule.border[btype]={width:css[0],type:css[1],color:css[2]}}else{delete rule.border[btype]}}for(btype in rule.border){rule.borders=rule.border;break}delete rule.border}return rules};this.remove=function(scope,ids){var defSel=Jedox.wss.app.environment.defaultSelection,conn=Jedox.wss.backend.conn,activeBook=Jedox.wss.app.activeBook,params=[];switch(scope){case that.SCOPE_CURR_SEL:case undefined:var rngs=defSel.getRanges(),ranges=[],i=-1,rng;while((rng=rngs[++i])!==undefined){ranges.push(rng.getCoords())}if(ranges.length){params.push("",ranges)}break;case that.SCOPE_CURR_WKS:break;case that.SCOPE_RULE_IDS:if(ids instanceof Array){params.push("",[],ids)}break;default:if(typeof scope=="string"){params.push(scope)}break}conn.cmd([activeBook,activeBook.exec],["cfdel"],params,activeBook.getRealGridRange(),false,false,conn.Q_STYLE|conn.Q_FMT_VAL|conn.Q_FMT|conn.Q_LOCK)}};