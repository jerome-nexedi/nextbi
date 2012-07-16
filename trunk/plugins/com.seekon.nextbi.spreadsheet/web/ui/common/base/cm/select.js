var select = {};
(function() {
	select.ie_selection = document.selection
			&& document.selection.createRangeCollection;
	function topLevelNodeAt(node, top) {
		while (node && node.parentNode != top) {
			node = node.parentNode
		}
		return node
	}
	function topLevelNodeBefore(node, top) {
		while (!node.previousSibling && node.parentNode != top) {
			node = node.parentNode
		}
		return topLevelNodeAt(node.previousSibling, top)
	}
	var currentSelection = null;
	var fourSpaces = "\u00a0\u00a0\u00a0\u00a0";
	select.snapshotChanged = function() {
		if (currentSelection) {
			currentSelection.changed = true
		}
	};
	select.snapshotReplaceNode = function(from, to, length, offset) {
		if (!currentSelection) {
			return
		}
		currentSelection.changed = true;
		function replace(point) {
			if (from == point.node) {
				if (length && point.offset > length) {
					point.offset -= length
				} else {
					point.node = to;
					point.offset += (offset || 0)
				}
			}
		}
		replace(currentSelection.start);
		replace(currentSelection.end)
	};
	select.snapshotMove = function(from, to, distance, relative, ifAtStart) {
		if (!currentSelection) {
			return
		}
		currentSelection.changed = true;
		function move(point) {
			if (from == point.node && (!ifAtStart || point.offset == 0)) {
				point.node = to;
				if (relative) {
					point.offset = Math.max(0, point.offset + distance)
				} else {
					point.offset = distance
				}
			}
		}
		move(currentSelection.start);
		move(currentSelection.end)
	};
	if (select.ie_selection) {
		function selectionNode(win, start) {
			var range = win.document.selection.createRange();
			range.collapse(start);
			function nodeAfter(node) {
				var found = null;
				while (!found && node) {
					found = node.nextSibling;
					node = node.parentNode
				}
				return nodeAtStartOf(found)
			}
			function nodeAtStartOf(node) {
				while (node && node.firstChild) {
					node = node.firstChild
				}
				return {
					node : node,
					offset : 0
				}
			}
			var containing = range.parentElement();
			if (!isAncestor(win.document.body, containing)) {
				return null
			}
			if (!containing.firstChild) {
				return nodeAtStartOf(containing)
			}
			var working = range.duplicate();
			working.moveToElementText(containing);
			working.collapse(true);
			for ( var cur = containing.firstChild; cur; cur = cur.nextSibling) {
				if (cur.nodeType == 3) {
					var size = cur.nodeValue.length;
					working.move("character", size)
				} else {
					working.moveToElementText(cur);
					working.collapse(false)
				}
				var dir = range.compareEndPoints("StartToStart", working);
				if (dir == 0) {
					return nodeAfter(cur)
				}
				if (dir == 1) {
					continue
				}
				if (cur.nodeType != 3) {
					return nodeAtStartOf(cur)
				}
				working.setEndPoint("StartToEnd", range);
				return {
					node : cur,
					offset : size - working.text.length
				}
			}
			return nodeAfter(containing)
		}
		select.markSelection = function(win) {
			currentSelection = null;
			var sel = win.document.selection;
			if (!sel) {
				return
			}
			var start = selectionNode(win, true), end = selectionNode(win,
					false);
			if (!start || !end) {
				return
			}
			currentSelection = {
				start : start,
				end : end,
				window : win,
				changed : false
			}
		};
		select.selectMarked = function() {
			if (!currentSelection || !currentSelection.changed) {
				return
			}
			function makeRange(point) {
				var range = currentSelection.window.document.body
						.createTextRange();
				var node = point.node;
				if (!node) {
					range
							.moveToElementText(currentSelection.window.document.body);
					range.collapse(false)
				} else {
					if (node.nodeType == 3) {
						range.moveToElementText(node.parentNode);
						var offset = point.offset;
						while (node.previousSibling) {
							node = node.previousSibling;
							offset += (node.innerText || "").length
						}
						range.move("character", offset)
					} else {
						range.moveToElementText(node);
						range.collapse(true)
					}
				}
				return range
			}
			var start = makeRange(currentSelection.start), end = makeRange(currentSelection.end);
			start.setEndPoint("StartToEnd", end);
			start.select()
		};
		select.selectionTopNode = function(container, start) {
			var selection = container.ownerDocument.selection;
			if (!selection) {
				return false
			}
			var range = selection.createRange();
			range.collapse(start);
			var around = range.parentElement();
			if (around && isAncestor(container, around)) {
				var range2 = range.duplicate();
				range2.moveToElementText(around);
				if (range.compareEndPoints("StartToStart", range2) == -1) {
					return topLevelNodeAt(around, container)
				}
			}
			try {
				range.pasteHTML("<span id='xxx-temp-xxx'></span>")
			} catch (e) {
				return false
			}
			var temp = container.ownerDocument.getElementById("xxx-temp-xxx");
			if (temp) {
				var result = topLevelNodeBefore(temp, container);
				removeElement(temp);
				return result
			}
			return false
		};
		select.focusAfterNode = function(node, container) {
			var range = container.ownerDocument.body.createTextRange();
			range.moveToElementText(node || container);
			range.collapse(!node);
			range.select()
		};
		select.somethingSelected = function(win) {
			var sel = win.document.selection;
			return sel && (sel.createRange().text != "")
		};
		function insertAtCursor(window, html) {
			var selection = window.document.selection;
			if (selection) {
				var range = selection.createRange();
				range.pasteHTML(html);
				range.collapse(false);
				range.select()
			}
		}
		select.insertNewlineAtCursor = function(window) {
			insertAtCursor(window, "<br>")
		};
		select.insertTabAtCursor = function(window) {
			insertAtCursor(window, fourSpaces)
		};
		select.cursorPos = function(container, start) {
			var selection = container.ownerDocument.selection;
			if (!selection) {
				return null
			}
			var topNode = select.selectionTopNode(container, start);
			while (topNode && topNode.nodeName != "BR") {
				topNode = topNode.previousSibling
			}
			var range = selection.createRange(), range2 = range.duplicate();
			range.collapse(start);
			if (topNode) {
				range2.moveToElementText(topNode);
				range2.collapse(false)
			} else {
				try {
					range2.moveToElementText(container)
				} catch (e) {
					return null
				}
				range2.collapse(true)
			}
			range.setEndPoint("StartToStart", range2);
			return {
				node : topNode,
				offset : range.text.length
			}
		};
		select.setCursorPos = function(container, from, to) {
			function rangeAt(pos) {
				var range = container.ownerDocument.body.createTextRange();
				if (!pos.node) {
					range.moveToElementText(container);
					range.collapse(true)
				} else {
					range.moveToElementText(pos.node);
					range.collapse(false)
				}
				range.move("character", pos.offset);
				return range
			}
			var range = rangeAt(from);
			if (to && to != from) {
				range.setEndPoint("EndToEnd", rangeAt(to))
			}
			range.select()
		};
		select.scrollToCursor = function(container) {
			var selection = container.ownerDocument.selection;
			if (!selection) {
				return null
			}
			selection.createRange().scrollIntoView()
		};
		select.scrollToNode = function(node) {
			if (!node) {
				return
			}
			node.scrollIntoView()
		};
		select.selectionCoords = function(win) {
			var selection = win.document.selection;
			if (!selection) {
				return null
			}
			var start = selection.createRange(), end = start.duplicate();
			start.collapse(true);
			end.collapse(false);
			var body = win.document.body;
			return {
				start : {
					x : start.boundingLeft + body.scrollLeft - 1,
					y : start.boundingTop + body.scrollTop
				},
				end : {
					x : end.boundingLeft + body.scrollLeft - 1,
					y : end.boundingTop + body.scrollTop
				}
			}
		};
		select.selectCoords = function(win, coords) {
			if (!coords) {
				return
			}
			var range1 = win.document.body.createTextRange(), range2 = range1
					.duplicate();
			try {
				range1.moveToPoint(coords.start.x, coords.start.y);
				range2.moveToPoint(coords.end.x, coords.end.y);
				range1.setEndPoint("EndToStart", range2);
				range1.select()
			} catch (e) {
				alert(e.message)
			}
		}
	} else {
		select.markSelection = function(win) {
			var selection = win.getSelection();
			if (!selection || selection.rangeCount == 0) {
				return (currentSelection = null)
			}
			var range = selection.getRangeAt(0);
			currentSelection = {
				start : {
					node : range.startContainer,
					offset : range.startOffset
				},
				end : {
					node : range.endContainer,
					offset : range.endOffset
				},
				window : win,
				changed : false
			};
			function normalize(point) {
				while (point.node.nodeType != 3 && point.node.nodeName != "BR") {
					var newNode = point.node.childNodes[point.offset]
							|| point.node.nextSibling;
					point.offset = 0;
					while (!newNode && point.node.parentNode) {
						point.node = point.node.parentNode;
						newNode = point.node.nextSibling
					}
					point.node = newNode;
					if (!newNode) {
						break
					}
				}
			}
			normalize(currentSelection.start);
			normalize(currentSelection.end)
		};
		select.selectMarked = function() {
			if (!currentSelection || !currentSelection.changed) {
				return
			}
			var win = currentSelection.window, range = win.document
					.createRange();
			function setPoint(point, which) {
				if (point.node) {
					if (point.offset == 0) {
						range["set" + which + "Before"](point.node)
					} else {
						range["set" + which](point.node, point.offset)
					}
				} else {
					range.setStartAfter(win.document.body.lastChild
							|| win.document.body)
				}
			}
			setPoint(currentSelection.end, "End");
			setPoint(currentSelection.start, "Start");
			selectRange(range, win)
		};
		function selectRange(range, window) {
			var selection = window.getSelection();
			selection.removeAllRanges();
			selection.addRange(range)
		}
		function selectionRange(window) {
			var selection = window.getSelection();
			if (!selection || selection.rangeCount == 0) {
				return false
			} else {
				return selection.getRangeAt(0)
			}
		}
		select.selectionTopNode = function(container, start) {
			var range = selectionRange(container.ownerDocument.defaultView);
			if (!range) {
				return false
			}
			var node = start ? range.startContainer : range.endContainer;
			var offset = start ? range.startOffset : range.endOffset;
			if (window.opera && !start && range.endContainer == container
					&& range.endOffset == range.startOffset + 1
					&& container.childNodes[range.startOffset]
					&& container.childNodes[range.startOffset].nodeName == "BR") {
				offset--
			}
			if (node.nodeType == 3) {
				if (offset > 0) {
					return topLevelNodeAt(node, container)
				} else {
					return topLevelNodeBefore(node, container)
				}
			} else {
				if (node.nodeName == "HTML") {
					return (offset == 1 ? null : container.lastChild)
				} else {
					if (node == container) {
						return (offset == 0) ? null
								: node.childNodes[offset - 1]
					} else {
						if (offset == node.childNodes.length) {
							return topLevelNodeAt(node, container)
						} else {
							if (offset == 0) {
								return topLevelNodeBefore(node, container)
							} else {
								return topLevelNodeAt(
										node.childNodes[offset - 1], container)
							}
						}
					}
				}
			}
		};
		select.focusAfterNode = function(node, container) {
			var win = container.ownerDocument.defaultView, range = win.document
					.createRange();
			range.setStartBefore(container.firstChild || container);
			if (node && !node.firstChild) {
				range.setEndAfter(node)
			} else {
				if (node) {
					range.setEnd(node, node.childNodes.length)
				} else {
					range.setEndBefore(container.firstChild || container)
				}
			}
			range.collapse(false);
			selectRange(range, win)
		};
		select.somethingSelected = function(win) {
			var range = selectionRange(win);
			return range && !range.collapsed
		};
		function insertNodeAtCursor(window, node) {
			var range = selectionRange(window);
			if (!range) {
				return
			}
			range.deleteContents();
			range.insertNode(node);
			webkitLastLineHack(window.document.body);
			range = window.document.createRange();
			range.selectNode(node);
			range.collapse(false);
			selectRange(range, window)
		}
		select.insertNewlineAtCursor = function(window) {
			insertNodeAtCursor(window, window.document.createElement("BR"))
		};
		select.insertTabAtCursor = function(window) {
			insertNodeAtCursor(window, window.document
					.createTextNode(fourSpaces))
		};
		select.cursorPos = function(container, start) {
			var range = selectionRange(window);
			if (!range) {
				return
			}
			var topNode = select.selectionTopNode(container, start);
			while (topNode && topNode.nodeName != "BR") {
				topNode = topNode.previousSibling
			}
			range = range.cloneRange();
			range.collapse(start);
			if (topNode) {
				range.setStartAfter(topNode)
			} else {
				range.setStartBefore(container)
			}
			return {
				node : topNode,
				offset : range.toString().length
			}
		};
		select.setCursorPos = function(container, from, to) {
			var win = container.ownerDocument.defaultView, range = win.document
					.createRange();
			function setPoint(node, offset, side) {
				if (!node) {
					node = container.firstChild
				} else {
					node = node.nextSibling
				}
				if (!node) {
					return
				}
				if (offset == 0) {
					range["set" + side + "Before"](node);
					return true
				}
				var backlog = [];
				function decompose(node) {
					if (node.nodeType == 3) {
						backlog.push(node)
					} else {
						forEach(node.childNodes, decompose)
					}
				}
				while (true) {
					while (node && !backlog.length) {
						decompose(node);
						node = node.nextSibling
					}
					var cur = backlog.shift();
					if (!cur) {
						return false
					}
					var length = cur.nodeValue.length;
					if (length >= offset) {
						range["set" + side](cur, offset);
						return true
					}
					offset -= length
				}
			}
			to = to || from;
			if (setPoint(to.node, to.offset, "End")
					&& setPoint(from.node, from.offset, "Start")) {
				selectRange(range, win)
			}
		};
		select.scrollToNode = function(element) {
			if (!element) {
				return
			}
			var doc = element.ownerDocument, body = doc.body, win = doc.defaultView, html = doc.documentElement;
			while (element && !element.offsetTop) {
				element = element.previousSibling
			}
			var y = 0, pos = element;
			while (pos && pos.offsetParent) {
				y += pos.offsetTop;
				pos = pos.offsetParent
			}
			var screen_y = y - (body.scrollTop || html.scrollTop || 0);
			if (screen_y < 0 || screen_y > win.innerHeight - 30) {
				win.scrollTo(body.scrollLeft || html.scrollLeft || 0, y)
			}
		};
		select.scrollToCursor = function(container) {
			select.scrollToNode(select.selectionTopNode(container, true)
					|| container.firstChild)
		}
	}
})();