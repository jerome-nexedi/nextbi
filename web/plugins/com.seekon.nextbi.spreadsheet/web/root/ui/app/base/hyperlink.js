Jedox.wss.hl=new function(){var that=this;this.hlTag={begin:'<span class="hl" onmouseover="Jedox.wss.hl.toolTip(event, true);" onmouseout="Jedox.wss.hl.toolTip(event, false);">',end:"</span>",oldBegin:'<span class="hl">'};var _dData={dyn:true,link:["abc.wss","abc.wss hl"],text:["Some hyperlink text","Some hyperlink text"],tip:["D3","Some tip"],trans:[{src:["A2:B4",[2,3,4,5]],dst:"A2:B4"},{src:["@var1",50],dst:"A5"},{src:["Sheet1!aaa",50],dst:"Sheet1!A5"},{src:["['value1', 'value2', 'value3']","['value1', 'value2', 'value3']"],dst:"Some const"}]},_sData={dyn:false,link:{type:"obj",value:{type:"local",target:{path:"/Jedox/Financials/DeveLabs-praznici-odmori.pdf",ghnt:{g:"fgrp1",h:"h1",n:"n36",t:"pdf"},sheet:null,range:null}}},text:{type:"ref",value:"D1"},tip:{type:"string",value:"Some tip"},trans:[{src:{type:"range",value:"B7"},dst:{type:"nrange",value:"testnr"}},{src:{type:"range",value:"A2"},dst:{type:"range",value:"Sheet1!A2:B3"}}]};function _sDataToFunc(data){var loc,txt,tip,trans="";function convStrRef(val){var isString=val.type=="string";return(isString?'"':"").concat(val.value,isString?'"':"")}function convSrcDst(type,val){var res="",isDst=type=="dst";switch(val.type){case"range":case"nrange":var rngElems=val.value.split("!",2);if(rngElems.length>1){res=(rngElems[0].indexOf(" ")>=0?"'".concat(rngElems[0],"'"):rngElems[0]).concat("!",rngElems[1])}else{res=val.value}break;case"var":res=(val.value.indexOf("@")?"@":"").concat(val.value);break;case"cval":return'"'.concat(val.value,'"');case"clist":return"\"['".concat(val.value.join("','"),"']\"")}return(isDst?'"':"").concat(res,isDst?'"':"")}if(data.link.type=="ref"){loc=data.link.value}else{switch(data.link.value.type){case"local":switch(data.link.value.target.ghnt.t){case"workbook":var sheetExists=data.link.value.target.sheet!=null,rangeExists=data.link.value.target.range!=null;loc='"'.concat(sheetExists||rangeExists?"[":"",data.link.value.target.path,sheetExists||rangeExists?"]":"",sheetExists?(data.link.value.target.sheet.indexOf(" ")>=0?"'".concat(data.link.value.target.sheet,"'"):data.link.value.target.sheet).concat("!"):"",rangeExists?data.link.value.target.range:"",'"');break;default:loc='"'.concat(data.link.value.target.path,'"');break}break;case"url":loc='"'.concat(data.link.value.target,'"');break}}txt=convStrRef(data.text);tip=convStrRef(data.tip);var sep=Jedox.wss.i18n.separators[2];for(var i=0,trns=data.trans,trnsLen=trns.length;i<trnsLen;i++){if(!trns[i].src||!trns[i].dst){continue}trans=trans.concat(sep,convSrcDst("src",trns[i].src),sep,convSrcDst("dst",trns[i].dst))}return"=HYPERLINK(".concat(loc,sep,txt,sep,tip,trans,")")}function _convertSDataLink(link){var re_inSquareBracket=/^\[[\w\W]*\]/,wbType=link.match(re_inSquareBracket),path=wbType==null?link:wbType[0].substring(1,wbType[0].length-1),isSelf=!path.search(/^[\w\W]*.wss$/)||path.toUpperCase()=="SELF",isLocal=wbType!=null||!path.search(/^\/[\w]*/)||isSelf,locTarget=!isLocal?path:{path:path,ghnt:isSelf?null:Jedox.wss.backend.ha.resolveNodePath(path)};if(isLocal&&!isSelf&&locTarget.ghnt==null){throw"follHLInvalidWB"}if(!isLocal&&!locTarget.length){throw"follHLInvalidURL"}loc={type:"obj",value:{type:isLocal?"local":"url",target:locTarget}};if(isLocal&&(isSelf||loc.value.target.ghnt.t=="workbook")){var selStr=wbType==null?"":link.substr(wbType[0].length);if(selStr.length){var selArr=selStr.split("!");if(selArr.length>1){loc.value.target.sheet=!selArr[0].search(/^'[^']*'$/)?selArr[0].substring(1,selArr[0].length-1):selArr[0];loc.value.target.range=selArr[1]}else{loc.value.target.sheet=null;loc.value.target.range=selArr[0]}}else{loc.value.target.sheet=null;loc.value.target.range=null}}return loc}function _dDataToSData(attr,incTrans){var conn=Jedox.wss.backend.conn,activeBook=Jedox.wss.app.activeBook;function resolveRef(refStr){if(!refStr.indexOf("@")){var wbVar=conn.cmd(0,["gvar"],[refStr.substr(1)]);if(!wbVar[0][0]||!wbVar[0][1].length){throw"badRef"}return wbVar[0][1]}else{var ref=Jedox.wss.formula.parse(refStr);if(!ref.length){throw"badRef"}ref=ref[0];return activeBook.getCellValue(ref.rng[0],ref.rng[1])}}function getPairVal(val){try{return val[0]==val[1]?val[0]:resolveRef(val[0])}catch(e){return val[0]}}function parseType(val){if(val[0]==val[1]){return"cval"}else{if(!val[0].search(/^\{[\w\W]*\}/)){return"clist"}else{return !val[0].search(/^@/)?"var":(formula.parse(val[0]).length?"range":"nrange")}}}var re_inSquareBracket=/^\[[\w\W]*\]/,loc=attr.link[0]==attr.link[1]?_convertSDataLink(getPairVal(attr.link)):{type:"ref",value:attr.link[0]};var txt=attr.text[0]==attr.text[1]?{type:"string",value:getPairVal(attr.text)}:{type:"ref",value:attr.text[0]};var tip=attr.tip[0]==attr.tip[1]?{type:"string",value:getPairVal(attr.tip)}:{type:"ref",value:attr.tip[0]};var trans=[];if(incTrans===true){for(var i=0,trns=attr.trans,trnsLen=trns.length,inSqareBrack,trnsPair={},formula=Jedox.wss.formula;i<trnsLen;i++,trnsPair={}){if(!trns[i].src||!trns[i].dst||trns[i].src.length<=1||!trns[i].src[0].length){continue}trnsPair.src={type:parseType(trns[i].src),value:trns[i].src[0],rvalue:trns[i].src[1]};trnsPair.dst={type:parseType(trns[i].dst),value:trns[i].dst[0],rvalue:trns[i].dst[1]};trans.push(trnsPair)}}return{dyn:false,link:loc,text:txt,tip:tip,trans:trans}}function _resolveFormula(frm){var activeBook=Jedox.wss.app.activeBook,conn=activeBook._conn,actSheet=activeBook.getSheetSelector().getActiveSheetName(),tmpName="tmp_".concat(Math.ceil(Math.random()*10000000000)),res=conn.cmd(0,["nadd"],[[1,1,{name:tmpName,refers_to:(!frm.search(/^=/)?"":"=").concat(frm),scope:actSheet,comment:"Temporary Named Range"}]]);if(res[0][0]){conn.cmd(0,["ndel"],[res[0][1][0].uuid])}return res[0][0]?[res[0][0],res[0][1][0].uuid,res[0][1][0].value]:res[0]}function _resolveSData(data,incTrans){var resTarget=[],resData=[];if(data.link.type=="ref"){resTarget.push([data.link,"rvalue"]),resData.push(data.link.value)}if(data.text.type=="ref"){resTarget.push([data.text,"rvalue"]),resData.push(data.text.value)}if(data.tip.type=="ref"){resTarget.push([data.tip,"rvalue"]),resData.push(data.tip.value)}if(incTrans===true){for(var i=0,trns=data.trans,trnsLen=trns.length,resTypes="range,nrange,var",parseDst=data.link.type=="obj"&&data.link.value.type=="url",resT=resTarget,resD=resData;i<trnsLen;i++){if(!trns[i].src||!trns[i].dst){continue}if(resTypes.search(trns[i].src.type)>=0){resT.push([trns[i].src,"rvalue"]),resD.push(trns[i].src.value)}if(parseDst&&resTypes.search(trns[i].dst.type)>=0){resT.push([trns[i].dst,"rvalue"]),resD.push(trns[i].dst.value)}}}if(resData.length){var rslVal=_resolveFormula("=RESOLVE(".concat(resData.join(Jedox.wss.i18n.separators[2]),")"));if(!rslVal[0]){throw"resolveError"}for(var j=0,rT=resTarget,rVals=Ext.util.JSON.decode(rslVal[2]),rTLen=resT.length;j<rTLen;j++){rT[j][0][rT[j][1]]=rVals[j]}}if(data.link.type=="ref"){data.link.rvalue=_convertSDataLink(data.link.rvalue)["value"]}return rslVal?rslVal[1]:null}this.set=function(range,hdata){var activeBook=Jedox.wss.app.activeBook,general=Jedox.wss.general,conn=activeBook._conn,cell=activeBook._aPane.getCell(range[0],range[1]),txt="Hyperlink",rslVal=[false];if(hdata.text.type=="string"){txt=hdata.text.value}else{rslVal=_resolveFormula(hdata.text.value);if(rslVal[0]){txt=rslVal[2]}}hdata=["hl",hdata];var rngUpd={a:{mousedown:hdata}};if(cell==undefined||(!cell.t&&(cell.m||cell.s))||"s,h,e".search(cell.t)>=0){rngUpd.v=this.hlTag.begin.concat(txt,this.hlTag.end)}rngUpd.s=Jedox.wss.style.hyperlinkStyle;range.push(rngUpd);conn.createBatch();if(rslVal[0]){conn.cmd(null,["ndel"],[rslVal[1]])}conn.cmd(null,["cdrn",{cm:true}],[range]);conn.sendBatch([activeBook,activeBook.exec],activeBook.getRealGridRange(),false,false,conn.Q_VALUE|conn.Q_STYLE|conn.Q_ATTRS);Jedox.wss.app.environment.selectedCellValue=this.hlTag.begin.concat(txt,this.hlTag.end)};this.get=function(range){var cell=Jedox.wss.app.activePane.getCell(range[0],range[1]);if(typeof cell=="object"&&("a" in cell)&&("mousedown" in cell.a)&&cell.a.mousedown[0]=="hl"){return cell.a.mousedown[1]}return undefined};this.remove=function(range){var activeBook=Jedox.wss.app.activeBook,general=Jedox.wss.general,conn=activeBook._conn,cell=activeBook._aPane.getCell(range[0],range[1]);if(!cell){return}var txt=Jedox.wss.general.filterHLTags(range[0],range[1],activeBook.getCellValue(range[0],range[1]),false),rngUpd={s:Jedox.wss.style.delHyperlinkStyle};if(cell.t=="h"){rngUpd.v=txt}range.push(rngUpd);conn.createBatch();conn.cmd(null,["clat"],[[range[0],range[1],range[0],range[1]]]);conn.cmd(null,["cdrn",{cm:true}],[range]);conn.sendBatch([activeBook,activeBook.exec],activeBook.getRealGridRange(),false,false,conn.Q_VALUE|conn.Q_FORMULA|conn.Q_STYLE|conn.Q_ATTRS)};this.updateText=function(range,txt){if(Jedox.wss.app.activeBook.getCellType(range[0],range[1])=="h"){var hdata=this.get(range);if(hdata==undefined){return}hdata.text.type="string";hdata.text.value=txt;range.push({a:{mousedown:["hl",hdata]}});Jedox.wss.backend.conn.cmd(0,["cdrn",{cm:true}],[range])}};this.followURL=function(data,target){if(!data.link.value.target.search(/^mailto:*/i)){window.location=data.link.value.target;return false}function getSDVals(val){var resTypes="range,nrange,var,clist",res=val[resTypes.search(val.type)>=0?"rvalue":"value"];return Ext.isArray(res)?res:[res]}var transData={};for(var i=0,trns=data.trans,trnsLen=trns.length,dest;i<trnsLen;i++){if(!trns[i].src||!trns[i].dst||!(dest=getSDVals(trns[i].dst)).length){continue}for(var j=0,k=0,tData=transData,src=getSDVals(trns[i].src),dst=dest,srcLen=src.length;j<srcLen;j++,k++){tData[src[j]]=!dst[k]?dst[k=0]:dst[k]}}transData=Ext.urlEncode(transData);var url=(data.link.value.target.search(/:\/\//)<0?"http://".concat(data.link.value.target):data.link.value.target).replace(/\/$/,"").concat(transData.length?"?".concat(transData):"");if(Jedox.wss.app.standalone){window.open(url,"winURL","directories=yes,menubar=yes,toolbar=yes,location=yes,status=yes,resizable=yes,scrollbars=yes")}else{try{for(var triggers=Jedox.wss.events.triggers.openURL,i=triggers.length-1,hlURL=url;i>=0;i--){triggers[i][0]["openURL"].call(parent,triggers[i][1],hlURL,target,data.text[data.text.type=="string"?"value":"rvalue"],true)}}catch(e){Jedox.wss.general.showMsg("Application Error".localize(),e.localize(),Ext.MessageBox.WARNING)}}return false};this.followOther=function(link,target){if(Jedox.wss.app.standalone){Jedox.wss.general.showMsg("follHLInvalidRef".localize(),"follHLNotSuppInStandalone".localize(),Ext.MessageBox.WARNING)}else{try{for(var triggers=Jedox.wss.events.triggers.openOther,i=triggers.length-1,ghnt=link.target.ghnt;i>=0;i--){triggers[i][0]["openOther"].call(parent,triggers[i][1],ghnt,target,true)}}catch(e){Jedox.wss.general.showMsg("Application Error".localize(),e.localize(),Ext.MessageBox.WARNING)}}return false};this.followWb=function(data){var activeBook=Jedox.wss.app.activeBook,showMsg=Jedox.wss.general.showMsg,linkVal=data.link.type=="ref"?data.link.rvalue:data.link.value,rng=[],ext={cbkey:"hl",appmode:Jedox.wss.app.appMode},srcWsId=Jedox.wss.app.activeSheet.getUid();if(linkVal.target.sheet!=null){if(Jedox.wss.app.UPRestrictMode&&linkVal.target.sheet!=activeBook._sheetSelector.getActiveSheetName()){showMsg("follHLTmpDisabledRef".localize(),"follHLTmpDisabledWS".localize(),Ext.MessageBox.WARNING);return false}ext.sheet_name=linkVal.target.sheet}if(linkVal.target.range==null){rng=[1,1,1,1]}else{var rngParsed=Jedox.wss.formula.parse(linkVal.target.range);if(!rngParsed.length){ext.nrange=linkVal.target.range}else{if(rngParsed.length>1){showMsg("follHLInvalidRef".localize(),"follHLInvalidRng".localize(),Ext.MessageBox.WARNING);return false}rng=rngParsed[0].rng}}var transExec=function(params,cb){var conn=Jedox.wss.backend.conn;if(!params[linkVal.target.range]){if(!rng.length){rng=[1,1,1,1]}}else{var paramsNR=!params[linkVal.target.range].search(/^=/)?params[linkVal.target.range].substr(1):params[linkVal.target.range],rngP=Jedox.wss.formula.parse(paramsNR);rng=rngP.length?rngP[0].rng:[1,1,1,1]}if(!data.trans.length){if(cb instanceof Array&&cb.length>1){cb[1].call(cb[0])}return}var sheetPool={},namedRangePool=[],sheets={},sheetList=Jedox.wss.backend.ha.getSheets(),actSheetId=sheetList[1],actSheetName;for(var i=0,sheetListLen=sheetList[0].length;i<sheetListLen;i+=2){sheets[sheetList[0][i+1]]=sheetList[0][i];if(sheetList[0][i]==sheetList[1]){actSheetName=sheetList[0][i+1]}}var resolveRange=function(range){var refs=Jedox.wss.formula.parse(range);return refs.length==1?refs:false},genSrcData=function(src,len,plVal){var srcVal=src.type=="cval"||src.type=="clist"?src.value:src.rvalue;srcVal=srcVal==null?"":srcVal;srcVal=Ext.isArray(srcVal)?srcVal:[srcVal];var srcObjs=[],res=[];for(var i=0,sArr=srcVal,srcLen=sArr.length;i<srcLen;i++){srcObjs.push(plVal?(sArr[i]==null?"":sArr[i]):{v:sArr[i]==null?"":sArr[i]})}while(res.length<len){res=res.concat(srcObjs)}return res.slice(0,len)},splitRange=function(fullRng){var rngElems=fullRng.split("!",2);return rngElems.length>1?{sheet:rngElems[0],range:rngElems[1]}:{sheet:actSheetName,range:rngElems[0]}},appendRange=function(dstRng,src){dstRng=!dstRng.search(/^=/)?dstRng.substr(1):dstRng;var rngElems=splitRange(dstRng);if(!sheets[rngElems.sheet]){return}var parsedDstRng=resolveRange(rngElems.range);if(!parsedDstRng){return}var dataArr=parsedDstRng[0].rng;dataArr=dataArr.concat(genSrcData(src,(dataArr[2]-dataArr[0]+1)*(dataArr[3]-dataArr[1]+1),false));if(sheetPool[sheets[rngElems.sheet]]){sheetPool[sheets[rngElems.sheet]].rngs.push(dataArr)}else{sheetPool[sheets[rngElems.sheet]]={rngs:[dataArr],vars:[]}}};for(var i=0,trns=data.trans,trnsLen=trns.length,shts=sheets,splitRng=splitRange,sPool=sheetPool,nPool=namedRangePool,genSData=genSrcData,actSId=actSheetId,parsedDstRng,dataArr,rngElems;i<trnsLen;i++){if(!trns[i].src||!trns[i].dst){continue}switch(trns[i].dst.type){case"cval":if(srcWsId!=Jedox.wss.app.activeSheet.getUid()){break}case"range":appendRange(trns[i].dst.value,trns[i].src);break;case"nrange":rngElems=splitRng(trns[i].dst.value);nPool.push({name:rngElems.range,uuid:shts[rngElems.sheet],src:trns[i].src});break;case"var":dataArr=[!trns[i].dst.value.search(/^@/)?trns[i].dst.value.substr(1):trns[i].dst.value,genSData(trns[i].src,1,true)[0]];if(sPool[actSId]){sPool[actSId].vars.push(dataArr)}else{sPool[actSId]={rngs:[],vars:[dataArr]}}break}}if(namedRangePool.length){conn.createBatch();for(var i=0,nrPool=namedRangePool,nrPoolLen=nrPool.length;i<nrPoolLen;i++){conn.cmd(0,["nget"],[[1,1,nrPool[i].name,nrPool[i].uuid==undefined?"":nrPool[i].uuid,true]]);conn.cmd(0,["nget"],[[1,1,nrPool[i].name,"",true]])}for(var i=0,nrRes=conn.sendBatch(),nrResLen=nrRes.length,nrPool=namedRangePool,refTo,refToElems;i<nrResLen;i+=2){if(!nrRes[i][0]&&!nrRes[i+1][0]){continue}try{refTo=nrRes[nrRes[i][0]?i:i+1][1][0].refers_to;appendRange(refTo,nrPool[i/2].src)}catch(e){}}}var execTrans=false;for(var sPoolLen in sheetPool){execTrans=true;break}if(execTrans){conn.createBatch();if(sheetPool[actSheetId]){for(var rngs=sheetPool[actSheetId].rngs,i=rngs.length-1;i>=0;i--){conn.cmd(0,["cdrn",{cm:true}],[rngs[i]])}for(var vars=sheetPool[actSheetId].vars,i=vars.length-1;i>=0;i--){conn.cmd(0,["svar"],vars[i])}delete (sheetPool[actSheetId])}var sheetChanged=false;for(var oSheet in sheetPool){conn.cmd(0,["osel"],[2,oSheet]);sheetChanged=true;for(var rngs=sheetPool[oSheet].rngs,i=rngs.length-1;i>=0;i--){conn.cmd(0,["cdrn",{cm:true}],[rngs[i]])}}if(sheetChanged){conn.cmd(0,["osel"],[2,actSheetId])}if(srcWsId==actSheetId){conn.sendBatch([activeBook,activeBook.exec,cb],activeBook.getRealGridRange(),true,true,conn.Q_VALUE|conn.Q_FORMULA|conn.Q_STYLE|conn.Q_ATTRS)}else{conn.sendBatch(cb)}}};function makeSelection(cb){var env=Jedox.wss.app.environment;if(env.viewMode==Jedox.wss.grid.viewMode.DESIGNER){var defSel=env.defaultSelection;defSel.set(new Jedox.wss.cls.Point(rng[0],rng[1]),new Jedox.wss.cls.Point(rng[2],rng[3]));defSel.draw()}if(cb instanceof Array&&cb.length>1){cb[1].call(cb[0])}}try{var ghnt=linkVal.target.ghnt;if(ghnt==null){var sheetSelector=activeBook.getSheetSelector(),path=linkVal.target.path,sheetId;if(!path.search(/^[\w\W]*.wss$/)&&path!=Jedox.wss.workspace.getMetaByWinId(Jedox.wss.app.activeBook.getWinId()).name){throw"follHLInvalidWB"}var cbTransExec=function(){if(ext.nrange){var conn=Jedox.wss.backend.conn,targSheetId=ext.sheet_name?sheetSelector.getIdByName(ext.sheet_name):sheetSelector.getActiveSheetId();if(!targSheetId){throw"follHLInvalidSheet"}conn.createBatch();conn.cmd(0,["nget"],[[1,1,ext.nrange,targSheetId,true]]);conn.cmd(0,["nget"],[[1,1,ext.nrange,"",true]]);var ngetRes=conn.sendBatch();if(!ngetRes[0][0]&&!ngetRes[1][0]){throw"follHLInvTrgNRange"}var nrRef=ngetRes[ngetRes[0][0]?0:1][1][0].refers_to;if(!nrRef.search(/^=/)){nrRef=nrRef.substr(1)}var nrRefSplit=(!nrRef.search(/^=/)?nrRef.substr(1):nrRef).split("!",2);if(nrRefSplit.length>1){sheetId=sheetSelector.getIdByName(nrRefSplit[0]);nrRefRng=nrRefSplit[1]}else{nrRefRng=nrRefSplit[0]}rngParsed=Jedox.wss.formula.parse(nrRefRng);if(!rngParsed.length){throw"follHLInvTrgNRange"}else{if(rngParsed.length>1){throw"follHLInvTrgNRange"}rng=rngParsed[0].rng}if(nrRefSplit.length>1&&sheetSelector.getActiveSheetId()!=sheetId){if(!sheetId){throw"follHLInvalidSheet"}Jedox.wss.sheet.select([this,makeSelection],sheetId,activeBook._wbId,true)}else{makeSelection()}}else{if(ext.sheet_name){sheetId=sheetSelector.getIdByName(ext.sheet_name);if(!sheetId){throw"follHLInvalidSheet"}if(sheetSelector.getActiveSheetId()!=sheetId){Jedox.wss.sheet.select([this,makeSelection],sheetId,activeBook._wbId,true)}else{makeSelection()}}else{makeSelection()}}};transExec({},[this,cbTransExec])}else{if(Jedox.wss.app.UPRestrictMode){var actBookMetaData=Jedox.wss.workspace.getMetaByWinId(activeBook._winId).ghn;if(actBookMetaData.n!=ghnt.n||actBookMetaData.h!=ghnt.h||actBookMetaData.g!=ghnt.g){showMsg("follHLTmpDisabledRef".localize(),"follHLTmpDisabledWB".localize(),Ext.MessageBox.WARNING);return false}}for(var triggers=Jedox.wss.events.triggers.openWorkbook_before,i=triggers.length-1;i>=0;i--){triggers[i][0]["openWorkbook_before"].call(parent,triggers[i][1],ghnt,"",true)}function afterWbOpen(){for(var triggers=Jedox.wss.events.triggers.openWorkbook_after,i=triggers.length-1;i>=0;i--){triggers[i][0]["openWorkbook_after"].call(parent,triggers[i][1],ghnt,Jedox.wss.workspace.getMetaByWinId(Jedox.wss.app.activeBook.getWinId()).name)}}if(!Jedox.wss.book.load([this,makeSelection,[this,afterWbOpen]],ghnt.n,ghnt.g,ghnt.h,ext,{tag:"hl",func:transExec,scope:this,params:[]})){return false}}}catch(e){showMsg("follHLInvalidRef".localize(),e.localize(),Ext.MessageBox.WARNING);return false}};this.follow=function(ev,data){if(ev.button==2||(Ext.isMac&&ev.button==0&&Jedox.wss.app.ctrlKeyPressed)){return true}var targEl=(Ext.isGecko2)?ev.explicitOriginalTarget.parentNode:document.elementFromPoint(ev.clientX,ev.clientY);if(targEl==undefined||targEl.tagName.toUpperCase()!="SPAN"||targEl.className!="hl"){return true}try{if(data.dyn){data=_dDataToSData(data,true)}else{var tmpNRId=_resolveSData(data,true)}}catch(e){Jedox.wss.general.showMsg("follHLInvalidRef".localize(),e.localize(),Ext.MessageBox.WARNING);return false}var linkVal=data.link.type=="ref"?data.link.rvalue:data.link.value;if(linkVal.type=="url"){return Jedox.wss.hl.followURL(data,"tab")}if(linkVal.type=="local"){return linkVal.target.ghnt==null||linkVal.target.ghnt.t=="workbook"?Jedox.wss.hl.followWb(data):Jedox.wss.hl.followOther(linkVal,"tab")}return false};this.rangeContextMenu=function(ev){var env=Jedox.wss.app.environment,selStartCoord=env.lastRangeStartCoord,data=this.get([selStartCoord[0],selStartCoord[1]]),dynData=data!=undefined&&data.dyn,insEditCapt=data?"Edit Hyperlink":"Hyperlink",targEl=(Ext.isGecko2)?ev.explicitOriginalTarget.parentNode:document.elementFromPoint(ev.clientX,ev.clientY),isTargHL=!(targEl==undefined||targEl.tagName.toUpperCase()!="SPAN"||targEl.className!="hl"),cntx=["-"];if(data&&isTargHL){if(dynData){data=_dDataToSData(data)}else{var tmpNRId=_resolveSData(data)}var linkVal=data.link.type=="ref"?data.link.rvalue:data.link.value;if(linkVal.type=="url"){if(!Jedox.wss.app.standalone){cntx.push({text:"HLCntxNewTab".localize(),iconCls:"ico_hl_target_tab",handler:function(){Jedox.wss.hl.followURL(data,"tab")}})}cntx.push({text:"HLCntxNewWin".localize(),iconCls:"ico_hl_target_win",handler:function(){Jedox.wss.hl.followURL(data,"win")}})}if(linkVal.type=="local"){if(linkVal.target.ghnt==null||linkVal.target.ghnt.t=="workbook"){cntx.push({text:"Open".localize(),iconCls:"ico_hl_open",handler:function(){Jedox.wss.hl.followWb(data)}})}else{if(!Jedox.wss.app.standalone){if(Jedox.wss.app.fileTypesReg[linkVal.target.ghnt.t]||linkVal.target.ghnt.t=="hyperlink"){cntx.push({text:"HLCntxNewTab".localize(),iconCls:"ico_hl_target_tab",handler:function(){Jedox.wss.hl.followOther(linkVal,"tab")}});cntx.push({text:"HLCntxNewWin".localize(),iconCls:"ico_hl_target_win",handler:function(){Jedox.wss.hl.followOther(linkVal,"win")}})}if("hyperlink,ahview".search(linkVal.target.ghnt.t)<0){cntx.push({text:"Export".localize().concat("..."),handler:function(){Jedox.wss.hl.followOther(linkVal,"export")}})}}}}}if(env.viewMode!=Jedox.wss.grid.viewMode.USER){if(!dynData){cntx.push({text:insEditCapt.localize().concat("..."),iconCls:"ico_hl_insert",handler:function(){Jedox.wss.app.load(Jedox.wss.app.dynJSRegistry.openHL)}})}if(data){cntx.push({text:"HLCntxRemove".localize(),iconCls:"ico_hl_remove",handler:function(){Jedox.wss.hl.remove([selStartCoord[0],selStartCoord[1],selStartCoord[0],selStartCoord[1]])}})}}return cntx};this.toolTip=function(ev,show){if(document.all){ev=window.event}var el=document.all?ev.srcElement:ev.target;if(show){var activeBook=Jedox.wss.app.activeBook,elParent=el.parentNode,rng=elParent.id.search(/_cursorField$/)>=0?Jedox.wss.app.environment.selectedCellCoords:activeBook.getCoordsByCell(elParent),data=this.get(rng),tipTitle,tipText;if(!data){return}try{if(data.dyn){data=_dDataToSData(data)}else{var tmpNRId=_resolveSData(data)}tipTitle=data.tip[data.tip.type=="ref"?"rvalue":"value"];if(!tipTitle){tipTitle=data.text[data.text.type=="ref"?"rvalue":"value"]}var linkSrc=data.link.type=="ref"?"rvalue":"value";if(data.link[linkSrc].type=="local"){tipText=!data.link[linkSrc].target.path?"Place in This Document".localize().concat(" - ",!data.link[linkSrc].target.sheet?activeBook.getSheetSelector().getActiveSheetName():data.link[linkSrc].target.sheet):data.link[linkSrc].target.path}else{tipText=data.link[linkSrc].target}}catch(e){tipTitle=tipTitle?tipTitle:"follHLInvalidRef".localize();tipText="HLInvalidRefNotice".localize()}el.tooltip=new Ext.ToolTip({target:el,renderTo:Jedox.wss.app.activePane._domId.concat("_IC"),title:tipTitle,html:tipText,anchor:"left",trackMouse:true,autoDestroy:true,dismissDelay:10000});el.tooltip.anchorEl.hide();el.tooltip.showAt([ev.clientX,ev.clientY])}else{if(el.tooltip){el.tooltip.destroy()}}}};