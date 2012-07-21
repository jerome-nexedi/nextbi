Jedox.wss.app.load = function(cmd, args) {
	args = args || [ 0 ];
	function execFunc() {
		var func = Jedox.wss;
		for (path = cmd[1], i = 0; i < path.length; i++) {
			func = func[path[i]]
		}
		if (Jedox.wss.app.browser === "ie") {
			func[cmd[2]].apply(new Object(), args)
		} else {
			func[cmd[2]].apply(this, args)
		}
	}
	if (cmd[3] && cmd[4] && cmd[2].length > 0) {
		execFunc();
		return
	}
	var that = this, url = cmd[5];
	allScripts = document.getElementsByTagName("script");
	for ( var path = cmd[1], i = 0; i < path.length; i++) {
		url = url.concat("/", path[i])
	}
	url = url.concat("/", cmd[0]);
	for (i = 0; i < allScripts.length; i++) {
		if (allScripts[i].src && allScripts[i].src.indexOf(url) != -1) {
			cmd[4] = true;
			if (cmd[2].length > 0) {
				execFunc()
			}
			return
		}
	}
	var newJS = document.createElement("script");
	newJS.type = "text/javascript";
	newJS.src = url;
	newJS.id = url;
	newJS.onload = newJS.onreadystatechange = function() {
		if (this.readyState && this.readyState == "loading") {
			return
		} else {
			cmd[4] = true;
			if (cmd[2].length > 0) {
				execFunc()
			}
		}
	};
	newJS.onerror = function() {
		docBody.removeChild(newJS);
		cmd[4] = false;
		console.log("Loading error: ".concat(url))
	};
	var docBody = document.getElementsByTagName("body")[0];
	docBody.appendChild(newJS)
};
Jedox.wss.app.dynJSAutoload = function() {
	var dynJSReg = Jedox.wss.app.dynJSRegistry, docBody = document
			.getElementsByTagName("body")[0];
	for ( var dynjs in dynJSReg) {
		if (dynJSReg[dynjs][3]) {
			for ( var url = dynJSReg[dynjs][5], path = dynJSReg[dynjs][1], i = 0; i < path.length; i++) {
				url = url.concat("/", path[i])
			}
			url = url.concat("/", dynJSReg[dynjs][0]);
			var newJS = document.createElement("script");
			newJS.type = "text/javascript";
			newJS.src = url;
			newJS.id = url;
			docBody.appendChild(newJS);
			dynJSReg[dynjs][4] = true
		}
	}
};
Jedox.wss.app.unload = function(cmd) {
	if (cmd[3] || !cmd[4]) {
		return
	}
	var url = cmd[5], allScripts = document.getElementsByTagName("script");
	for ( var path = cmd[1], i = 0; i < path.length; i++) {
		url = url.concat("/", path[i])
	}
	url = url.concat("/", cmd[0]);
	for ( var i = 0; i < allScripts.length; i++) {
		if (allScripts[i].id == url) {
			document.getElementsByTagName("body")[0].removeChild(allScripts[i]);
			cmd[4] = false;
			return
		}
	}
};