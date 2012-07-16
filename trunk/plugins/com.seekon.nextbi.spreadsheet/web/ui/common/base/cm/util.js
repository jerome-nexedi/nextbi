var internetExplorer = document.selection && window.ActiveXObject
		&& /MSIE/.test(navigator.userAgent);
var webkit = /AppleWebKit/.test(navigator.userAgent);
function method(obj, name) {
	return function() {
		obj[name].apply(obj, arguments)
	}
}
var StopIteration = {
	toString : function() {
		return "StopIteration"
	}
};
function forEach(iter, f) {
	if (iter.next) {
		try {
			while (true) {
				f(iter.next())
			}
		} catch (e) {
			if (e != StopIteration) {
				throw e
			}
		}
	} else {
		for ( var i = 0; i < iter.length; i++) {
			f(iter[i])
		}
	}
}
function map(iter, f) {
	var accum = [];
	forEach(iter, function(val) {
		accum.push(f(val))
	});
	return accum
}
function matcher(regexp) {
	return function(value) {
		return regexp.test(value)
	}
}
function hasClass(element, className) {
	var classes = element.className;
	return classes && new RegExp("(^| )" + className + "($| )").test(classes)
}
function insertAfter(newNode, oldNode) {
	var parent = oldNode.parentNode;
	parent.insertBefore(newNode, oldNode.nextSibling);
	return newNode
}
function removeElement(node) {
	if (node.parentNode) {
		node.parentNode.removeChild(node)
	}
}
function clearElement(node) {
	while (node.firstChild) {
		node.removeChild(node.firstChild)
	}
}
function isAncestor(node, child) {
	while (child = child.parentNode) {
		if (node == child) {
			return true
		}
	}
	return false
}
var nbsp = "\u00a0";
var matching = {
	"{" : "}",
	"[" : "]",
	"(" : ")",
	"}" : "{",
	"]" : "[",
	")" : "("
};
function normalizeEvent(event) {
	if (!event.stopPropagation) {
		event.stopPropagation = function() {
			this.cancelBubble = true
		};
		event.preventDefault = function() {
			this.returnValue = false
		}
	}
	if (!event.stop) {
		event.stop = function() {
			this.stopPropagation();
			this.preventDefault()
		}
	}
	if (event.type == "keypress") {
		event.code = (event.charCode == null) ? event.keyCode : event.charCode;
		event.character = String.fromCharCode(event.code)
	}
	return event
}
function addEventHandler(node, type, handler, removeFunc) {
	function wrapHandler(event) {
		handler(normalizeEvent(event || window.event))
	}
	if (typeof node.addEventListener == "function") {
		node.addEventListener(type, wrapHandler, false);
		if (removeFunc) {
			return function() {
				node.removeEventListener(type, wrapHandler, false)
			}
		}
	} else {
		node.attachEvent("on" + type, wrapHandler);
		if (removeFunc) {
			return function() {
				node.detachEvent("on" + type, wrapHandler)
			}
		}
	}
}
function nodeText(node) {
	return node.innerText || node.textContent || node.nodeValue || ""
};