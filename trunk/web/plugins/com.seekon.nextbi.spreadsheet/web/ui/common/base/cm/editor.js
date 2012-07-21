function makeWhiteSpace(n) {
	var buffer = [], nb = true;
	for (; n > 0; n--) {
		buffer.push((nb || n == 1) ? nbsp : " ");
		nb = !nb
	}
	return buffer.join("")
}
function fixSpaces(string) {
	if (string.charAt(0) == " ") {
		string = nbsp + string.slice(1)
	}
	return string.replace(/\t/g, function() {
		return makeWhiteSpace(indentUnit)
	}).replace(/[ \u00a0]{2,}/g, function(s) {
		return makeWhiteSpace(s.length)
	})
}
function cleanText(text) {
	return text.replace(/\u00a0/g, " ").replace(/\u200b/g, "")
}
function makePartSpan(value, doc) {
	var text = value;
	if (value.nodeType == 3) {
		text = value.nodeValue
	} else {
		value = doc.createTextNode(text)
	}
	var span = doc.createElement("SPAN");
	span.isPart = true;
	span.appendChild(value);
	span.currentText = text;
	return span
}
var webkitLastLineHack = webkit ? function(container) {
	var last = container.lastChild;
	if (!last || !last.isPart || last.textContent != "\u200b") {
		container.appendChild(makePartSpan("\u200b", container.ownerDocument))
	}
} : function() {
};
var Editor = (function() {
	var newlineElements = {
		P : true,
		DIV : true,
		LI : true
	};
	function asEditorLines(string) {
		var tab = makeWhiteSpace(indentUnit);
		return map(string.replace(/\t/g, tab).replace(/\u00a0/g, " ").replace(
				/\r\n?/g, "\n").split("\n"), fixSpaces)
	}
	function simplifyDOM(root) {
		var doc = root.ownerDocument;
		var result = [];
		var leaving = true;
		function simplifyNode(node) {
			if (node.nodeType == 3) {
				var text = node.nodeValue = fixSpaces(node.nodeValue.replace(
						/[\r\u200b]/g, "").replace(/\n/g, " "));
				if (text.length) {
					leaving = false
				}
				result.push(node)
			} else {
				if (node.nodeName == "BR" && node.childNodes.length == 0) {
					leaving = true;
					result.push(node)
				} else {
					forEach(node.childNodes, simplifyNode);
					if (!leaving
							&& newlineElements.hasOwnProperty(node.nodeName)) {
						leaving = true;
						result.push(doc.createElement("BR"))
					}
				}
			}
		}
		simplifyNode(root);
		return result
	}
	function traverseDOM(start) {
		function yield(value, c) {
			cc = c;
			return value
		}
		function push(fun, arg, c) {
			return function() {
				return fun(arg, c)
			}
		}
		function stop() {
			cc = stop;
			throw StopIteration
		}
		var cc = push(scanNode, start, stop);
		var owner = start.ownerDocument;
		var nodeQueue = [];
		function pointAt(node) {
			var parent = node.parentNode;
			var next = node.nextSibling;
			return function(newnode) {
				parent.insertBefore(newnode, next)
			}
		}
		var point = null;
		function insertPart(part) {
			var text = "\n";
			if (part.nodeType == 3) {
				select.snapshotChanged();
				part = makePartSpan(part, owner);
				text = part.currentText
			}
			part.dirty = true;
			nodeQueue.push(part);
			point(part);
			return text
		}
		function writeNode(node, c) {
			var toYield = [];
			forEach(simplifyDOM(node), function(part) {
				toYield.push(insertPart(part))
			});
			return yield(toYield.join(""), c)
		}
		function partNode(node) {
			if (node.isPart && node.childNodes.length == 1
					&& node.firstChild.nodeType == 3) {
				node.currentText = node.firstChild.nodeValue;
				return !/[\n\t\r]/.test(node.currentText)
			}
			return false
		}
		function scanNode(node, c) {
			if (node.nextSibling) {
				c = push(scanNode, node.nextSibling, c)
			}
			if (partNode(node)) {
				nodeQueue.push(node);
				return yield(node.currentText, c)
			} else {
				if (node.nodeName == "BR") {
					nodeQueue.push(node);
					return yield("\n", c)
				} else {
					point = pointAt(node);
					removeElement(node);
					return writeNode(node, c)
				}
			}
		}
		return {
			next : function() {
				return cc()
			},
			nodes : nodeQueue
		}
	}
	function nodeSize(node) {
		if (node.nodeName == "BR") {
			return 1
		} else {
			return node.currentText.length
		}
	}
	function startOfLine(node) {
		while (node && node.nodeName != "BR") {
			node = node.previousSibling
		}
		return node
	}
	function endOfLine(node, container) {
		if (!node) {
			node = container.firstChild
		} else {
			if (node.nodeName == "BR") {
				node = node.nextSibling
			}
		}
		while (node && node.nodeName != "BR") {
			node = node.nextSibling
		}
		return node
	}
	function time() {
		return new Date().getTime()
	}
	function scrubPasted(container, start, start2) {
		var end = select.selectionTopNode(container, true), doc = container.ownerDocument;
		if (start != null && start.parentNode != container) {
			start = start2
		}
		if (start === false) {
			start = null
		}
		if (start == end || !end || !container.firstChild) {
			return
		}
		var clear = traverseDOM(start ? start.nextSibling
				: container.firstChild);
		while (end.parentNode == container) {
			try {
				clear.next()
			} catch (e) {
				break
			}
		}
		forEach(clear.nodes, function(node) {
			var newNode = node.nodeName == "BR" ? doc.createElement("BR")
					: makePartSpan(node.currentText, doc);
			container.replaceChild(newNode, node)
		})
	}
	function SearchCursor(editor, string, fromCursor) {
		this.editor = editor;
		this.history = editor.history;
		this.history.commit();
		this.atOccurrence = false;
		this.fallbackSize = 15;
		var cursor;
		if (fromCursor && (cursor = select.cursorPos(this.editor.container))) {
			this.line = cursor.node;
			this.offset = cursor.offset
		} else {
			this.line = null;
			this.offset = 0
		}
		this.valid = !!string;
		var target = string.split("\n"), self = this;
		this.matches = (target.length == 1) ? function() {
			var match = cleanText(
					self.history.textAfter(self.line).slice(self.offset))
					.indexOf(string);
			if (match > -1) {
				return {
					from : {
						node : self.line,
						offset : self.offset + match
					},
					to : {
						node : self.line,
						offset : self.offset + match + string.length
					}
				}
			}
		} : function() {
			var firstLine = cleanText(self.history.textAfter(self.line).slice(
					self.offset));
			var match = firstLine.lastIndexOf(target[0]);
			if (match == -1 || match != firstLine.length - target[0].length) {
				return false
			}
			var startOffset = self.offset + match;
			var line = self.history.nodeAfter(self.line);
			for ( var i = 1; i < target.length - 1; i++) {
				if (cleanText(self.history.textAfter(line)) != target[i]) {
					return false
				}
				line = self.history.nodeAfter(line)
			}
			if (cleanText(self.history.textAfter(line)).indexOf(
					target[target.length - 1]) != 0) {
				return false
			}
			return {
				from : {
					node : self.line,
					offset : startOffset
				},
				to : {
					node : line,
					offset : target[target.length - 1].length
				}
			}
		}
	}
	SearchCursor.prototype = {
		findNext : function() {
			if (!this.valid) {
				return false
			}
			this.atOccurrence = false;
			var self = this;
			if (this.line && !this.line.parentNode) {
				this.line = null;
				this.offset = 0
			}
			function saveAfter(pos) {
				if (self.history.textAfter(pos.node).length < pos.offset) {
					self.line = pos.node;
					self.offset = pos.offset + 1
				} else {
					self.line = self.history.nodeAfter(pos.node);
					self.offset = 0
				}
			}
			while (true) {
				var match = this.matches();
				if (match) {
					this.atOccurrence = match;
					saveAfter(match.from);
					return true
				}
				this.line = this.history.nodeAfter(this.line);
				this.offset = 0;
				if (!this.line) {
					this.valid = false;
					return false
				}
			}
		},
		select : function() {
			if (this.atOccurrence) {
				select.setCursorPos(this.editor.container,
						this.atOccurrence.from, this.atOccurrence.to);
				select.scrollToCursor(this.editor.container)
			}
		},
		replace : function(string) {
			if (this.atOccurrence) {
				var end = this.editor.replaceRange(this.atOccurrence.from,
						this.atOccurrence.to, string);
				this.line = end.node;
				this.offset = end.offset;
				this.atOccurrence = false
			}
		}
	};
	function Editor(options) {
		this.options = options;
		window.indentUnit = options.indentUnit;
		this.parent = parent;
		this.doc = document;
		var container = this.container = this.doc.body;
		this.win = window;
		this.history = new History(container, options.undoDepth,
				options.undoDelay, this, options.onChange);
		var self = this;
		if (!Editor.Parser) {
			throw "No parser loaded."
		}
		if (options.parserConfig && Editor.Parser.configure) {
			Editor.Parser.configure(options.parserConfig)
		}
		if (!options.readOnly) {
			select.setCursorPos(container, {
				node : null,
				offset : 0
			})
		}
		this.dirty = [];
		if (options.content) {
			this.importCode(options.content)
		} else {
			container.appendChild(this.doc.createElement("BR"))
		}
		if (!options.readOnly) {
			if (options.continuousScanning !== false) {
				this.scanner = this.documentScanner(options.passTime);
				this.delayScanning()
			}
			function setEditable() {
				if (document.body.contentEditable != undefined
						&& internetExplorer) {
					document.body.contentEditable = "true"
				} else {
					document.designMode = "on"
				}
				document.documentElement.style.borderWidth = "0";
				if (!options.textWrapping) {
					container.style.whiteSpace = "nowrap"
				}
			}
			try {
				setEditable()
			} catch (e) {
				var focusEvent = addEventHandler(document, "focus", function() {
					focusEvent();
					setEditable()
				}, true)
			}
			addEventHandler(document, "keydown", method(this, "keyDown"));
			addEventHandler(document, "keypress", method(this, "keyPress"));
			addEventHandler(document, "keyup", method(this, "keyUp"));
			function cursorActivity() {
				self.cursorActivity(false)
			}
			addEventHandler(document.body, "mouseup", cursorActivity);
			addEventHandler(document.body, "paste", function(event) {
				cursorActivity();
				if (internetExplorer) {
					var text = null;
					try {
						text = window.clipboardData.getData("Text")
					} catch (e) {
					}
					if (text != null) {
						self.replaceSelection(text);
						event.stop()
					} else {
						var start = select.selectionTopNode(self.container,
								true), start2 = start && start.previousSibling;
						setTimeout(function() {
							scrubPasted(self.container, start, start2)
						}, 0)
					}
				}
			});
			addEventHandler(document.body, "cut", cursorActivity);
			if (this.options.autoMatchParens) {
				addEventHandler(document.body, "click", method(this,
						"scheduleParenBlink"))
			}
		} else {
			if (!options.textWrapping) {
				container.style.whiteSpace = "nowrap"
			}
		}
	}
	function isSafeKey(code) {
		return (code >= 16 && code <= 18) || (code >= 33 && code <= 40)
	}
	Editor.prototype = {
		importCode : function(code) {
			this.history.push(null, null, asEditorLines(code));
			this.history.reset()
		},
		getCode : function() {
			if (!this.container.firstChild) {
				return ""
			}
			var accum = [];
			select.markSelection(this.win);
			forEach(traverseDOM(this.container.firstChild), method(accum,
					"push"));
			webkitLastLineHack(this.container);
			select.selectMarked();
			return cleanText(accum.join(""))
		},
		checkLine : function(node) {
			if (node === false
					|| !(node == null || node.parentNode == this.container)) {
				throw parent.CodeMirror.InvalidLineHandle
			}
		},
		cursorPosition : function(start) {
			if (start == null) {
				start = true
			}
			var pos = select.cursorPos(this.container, start);
			if (pos) {
				return {
					line : pos.node,
					character : pos.offset
				}
			} else {
				return {
					line : null,
					character : 0
				}
			}
		},
		firstLine : function() {
			return null
		},
		lastLine : function() {
			if (this.container.lastChild) {
				return startOfLine(this.container.lastChild)
			} else {
				return null
			}
		},
		nextLine : function(line) {
			this.checkLine(line);
			var end = endOfLine(line, this.container);
			return end || false
		},
		prevLine : function(line) {
			this.checkLine(line);
			if (line == null) {
				return false
			}
			return startOfLine(line.previousSibling)
		},
		selectLines : function(startLine, startOffset, endLine, endOffset) {
			this.checkLine(startLine);
			var start = {
				node : startLine,
				offset : startOffset
			}, end = null;
			if (endOffset !== undefined) {
				this.checkLine(endLine);
				end = {
					node : endLine,
					offset : endOffset
				}
			}
			select.setCursorPos(this.container, start, end);
			select.scrollToCursor(this.container)
		},
		lineContent : function(line) {
			this.checkLine(line);
			var accum = [];
			for (line = line ? line.nextSibling : this.container.firstChild; line
					&& line.nodeName != "BR"; line = line.nextSibling) {
				accum.push(nodeText(line))
			}
			return cleanText(accum.join(""))
		},
		setLineContent : function(line, content) {
			this.history.commit();
			this.replaceRange( {
				node : line,
				offset : 0
			}, {
				node : line,
				offset : this.history.textAfter(line).length
			}, content);
			this.addDirtyNode(line);
			this.scheduleHighlight()
		},
		insertIntoLine : function(line, position, content) {
			var before = null;
			if (position == "end") {
				before = endOfLine(line, this.container)
			} else {
				for ( var cur = line ? line.nextSibling
						: this.container.firstChild; cur; cur = cur.nextSibling) {
					if (position == 0) {
						before = cur;
						break
					}
					var text = (cur.innerText || cur.textContent
							|| cur.nodeValue || "");
					if (text.length > position) {
						before = cur.nextSibling;
						content = text.slice(0, position) + content
								+ text.slice(position);
						removeElement(cur);
						break
					}
					position -= text.length
				}
			}
			var lines = asEditorLines(content), doc = this.container.ownerDocument;
			for ( var i = 0; i < lines.length; i++) {
				if (i > 0) {
					this.container
							.insertBefore(doc.createElement("BR"), before)
				}
				this.container
						.insertBefore(makePartSpan(lines[i], doc), before)
			}
			this.addDirtyNode(line);
			this.scheduleHighlight()
		},
		selectedText : function() {
			var h = this.history;
			h.commit();
			var start = select.cursorPos(this.container, true), end = select
					.cursorPos(this.container, false);
			if (!start || !end) {
				return ""
			}
			if (start.node == end.node) {
				return h.textAfter(start.node).slice(start.offset, end.offset)
			}
			var text = [ h.textAfter(start.node).slice(start.offset) ];
			for ( var pos = h.nodeAfter(start.node); pos != end.node; pos = h
					.nodeAfter(pos)) {
				text.push(h.textAfter(pos))
			}
			text.push(h.textAfter(end.node).slice(0, end.offset));
			return cleanText(text.join("\n"))
		},
		replaceSelection : function(text) {
			this.history.commit();
			var start = select.cursorPos(this.container, true), end = select
					.cursorPos(this.container, false);
			if (!start || !end) {
				return
			}
			end = this.replaceRange(start, end, text);
			select.setCursorPos(this.container, start, end)
		},
		replaceRange : function(from, to, text) {
			var lines = asEditorLines(text);
			lines[0] = this.history.textAfter(from.node).slice(0, from.offset)
					+ lines[0];
			var lastLine = lines[lines.length - 1];
			lines[lines.length - 1] = lastLine
					+ this.history.textAfter(to.node).slice(to.offset);
			var end = this.history.nodeAfter(to.node);
			this.history.push(from.node, end, lines);
			return {
				node : this.history.nodeBefore(end),
				offset : lastLine.length
			}
		},
		getSearchCursor : function(string, fromCursor) {
			return new SearchCursor(this, string, fromCursor)
		},
		reindent : function() {
			if (this.container.firstChild) {
				this.indentRegion(null, this.container.lastChild)
			}
		},
		reindentSelection : function(direction) {
			if (!select.somethingSelected(this.win)) {
				this.indentAtCursor(direction)
			} else {
				var start = select.selectionTopNode(this.container, true), end = select
						.selectionTopNode(this.container, false);
				if (start === false || end === false) {
					return
				}
				this.indentRegion(start, end, direction)
			}
		},
		grabKeys : function(eventHandler, filter) {
			this.frozen = eventHandler;
			this.keyFilter = filter
		},
		ungrabKeys : function() {
			this.frozen = "leave";
			this.keyFilter = null
		},
		setParser : function(name) {
			Editor.Parser = window[name];
			if (this.container.firstChild) {
				forEach(this.container.childNodes, function(n) {
					if (n.nodeType != 3) {
						n.dirty = true
					}
				});
				this.addDirtyNode(this.firstChild);
				this.scheduleHighlight()
			}
		},
		keyDown : function(event) {
			if (this.frozen == "leave") {
				this.frozen = null
			}
			if (this.frozen
					&& (!this.keyFilter || this.keyFilter(event.keyCode))) {
				event.stop();
				this.frozen(event);
				return
			}
			var code = event.keyCode;
			this.delayScanning();
			if (this.options.autoMatchParens) {
				this.scheduleParenBlink()
			}
			if (code == 13) {
				if (event.ctrlKey && !event.altKey) {
					this.reparseBuffer()
				} else {
					select.insertNewlineAtCursor(this.win);
					this.indentAtCursor();
					select.scrollToCursor(this.container)
				}
				event.stop()
			} else {
				if (code == 9 && this.options.tabMode != "default") {
					this.handleTab(!event.ctrlKey && !event.shiftKey);
					event.stop()
				} else {
					if (code == 32 && event.shiftKey
							&& this.options.tabMode == "default") {
						this.handleTab(true);
						event.stop()
					} else {
						if (code == 36 && !event.shiftKey) {
							if (this.home()) {
								event.stop()
							}
						} else {
							if ((code == 219 || code == 221) && event.ctrlKey
									&& !event.altKey) {
								this.blinkParens(event.shiftKey);
								event.stop()
							} else {
								if (event.metaKey && !event.shiftKey
										&& (code == 37 || code == 39)) {
									var cursor = select
											.selectionTopNode(this.container);
									if (cursor === false
											|| !this.container.firstChild) {
										return
									}
									if (code == 37) {
										select.focusAfterNode(
												startOfLine(cursor),
												this.container)
									} else {
										var end = endOfLine(cursor,
												this.container);
										select
												.focusAfterNode(
														end ? end.previousSibling
																: this.container.lastChild,
														this.container)
									}
									event.stop()
								} else {
									if ((event.ctrlKey || event.metaKey)
											&& !event.altKey) {
										if ((event.shiftKey && code == 90)
												|| code == 89) {
											select.scrollToNode(this.history
													.redo());
											event.stop()
										} else {
											if (code == 90 || code == 8) {
												select
														.scrollToNode(this.history
																.undo());
												event.stop()
											} else {
												if (code == 83
														&& this.options.saveFunction) {
													this.options.saveFunction();
													event.stop()
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		},
		keyPress : function(event) {
			var electric = /indent|default/.test(this.options.tabMode)
					&& Editor.Parser.electricChars;
			if ((this.frozen && (!this.keyFilter || this
					.keyFilter(event.keyCode)))
					|| event.code == 13
					|| (event.code == 9 && this.options.tabMode != "default")
					|| (event.keyCode == 32 && event.shiftKey && this.options.tabMode == "default")) {
				event.stop()
			} else {
				if (electric && electric.indexOf(event.character) != -1) {
					this.parent.setTimeout(method(this, "indentAtCursor"), 0)
				}
			}
		},
		keyUp : function(event) {
			this.cursorActivity(isSafeKey(event.keyCode))
		},
		indentLineAfter : function(start, direction) {
			var whiteSpace = start ? start.nextSibling
					: this.container.firstChild;
			if (whiteSpace && !hasClass(whiteSpace, "whitespace")) {
				whiteSpace = null
			}
			var firstText = whiteSpace ? whiteSpace.nextSibling
					: (start ? start.nextSibling : this.container.firstChild);
			var nextChars = (start && firstText && firstText.currentText) ? firstText.currentText
					: "";
			var newIndent = 0, curIndent = whiteSpace ? whiteSpace.currentText.length
					: 0;
			if (direction != null && this.options.tabMode == "shift") {
				newIndent = direction ? curIndent + indentUnit : Math.max(0,
						curIndent - indentUnit)
			} else {
				if (start) {
					newIndent = start.indentation(nextChars, curIndent,
							direction)
				} else {
					if (Editor.Parser.firstIndentation) {
						newIndent = Editor.Parser.firstIndentation(nextChars,
								curIndent, direction)
					}
				}
			}
			var indentDiff = newIndent - curIndent;
			if (indentDiff < 0) {
				if (newIndent == 0) {
					if (firstText) {
						select.snapshotMove(whiteSpace.firstChild,
								firstText.firstChild, 0)
					}
					removeElement(whiteSpace);
					whiteSpace = null
				} else {
					select.snapshotMove(whiteSpace.firstChild,
							whiteSpace.firstChild, indentDiff, true);
					whiteSpace.currentText = makeWhiteSpace(newIndent);
					whiteSpace.firstChild.nodeValue = whiteSpace.currentText
				}
			} else {
				if (indentDiff > 0) {
					if (whiteSpace) {
						whiteSpace.currentText = makeWhiteSpace(newIndent);
						whiteSpace.firstChild.nodeValue = whiteSpace.currentText
					} else {
						whiteSpace = makePartSpan(makeWhiteSpace(newIndent),
								this.doc);
						whiteSpace.className = "whitespace";
						if (start) {
							insertAfter(whiteSpace, start)
						} else {
							this.container.insertBefore(whiteSpace,
									this.container.firstChild)
						}
					}
					if (firstText) {
						select.snapshotMove(firstText.firstChild,
								whiteSpace.firstChild, curIndent, false, true)
					}
				}
			}
			if (indentDiff != 0) {
				this.addDirtyNode(start)
			}
			return whiteSpace
		},
		highlightAtCursor : function() {
			var pos = select.selectionTopNode(this.container, true);
			var to = select.selectionTopNode(this.container, false);
			if (pos === false || to === false) {
				return
			}
			select.markSelection(this.win);
			if (this.highlight(pos, endOfLine(to, this.container), true, 20) === false) {
				return false
			}
			select.selectMarked();
			return true
		},
		handleTab : function(direction) {
			if (this.options.tabMode == "spaces") {
				select.insertTabAtCursor(this.win)
			} else {
				this.reindentSelection(direction)
			}
		},
		home : function() {
			var cur = select.selectionTopNode(this.container, true), start = cur;
			if (cur === false || !(!cur || cur.isPart || cur.nodeName == "BR")
					|| !this.container.firstChild) {
				return false
			}
			while (cur && cur.nodeName != "BR") {
				cur = cur.previousSibling
			}
			var next = cur ? cur.nextSibling : this.container.firstChild;
			if (next && next != start && next.isPart
					&& hasClass(next, "whitespace")) {
				select.focusAfterNode(next, this.container)
			} else {
				select.focusAfterNode(cur, this.container)
			}
			return true
		},
		scheduleParenBlink : function() {
			if (this.parenEvent) {
				this.parent.clearTimeout(this.parenEvent)
			}
			var self = this;
			this.parenEvent = this.parent.setTimeout(function() {
				self.blinkParens()
			}, 300)
		},
		blinkParens : function(jump) {
			if (!window.select) {
				return
			}
			if (this.parenEvent) {
				this.parent.clearTimeout(this.parenEvent)
			}
			this.parenEvent = null;
			function paren(node) {
				if (node.currentText) {
					var match = node.currentText
							.match(/^[\s\u00a0]*([\(\)\[\]{}])[\s\u00a0]*$/);
					return match && match[1]
				}
			}
			function forward(ch) {
				return /[\(\[\{]/.test(ch)
			}
			var ch, self = this, cursor = select.selectionTopNode(
					this.container, true);
			if (!cursor || !this.highlightAtCursor()) {
				return
			}
			cursor = select.selectionTopNode(this.container, true);
			if (!(cursor && ((ch = paren(cursor)) || (cursor = cursor.nextSibling)
					&& (ch = paren(cursor))))) {
				return
			}
			var className = cursor.className, dir = forward(ch), match = matching[ch];
			function tryFindMatch() {
				var stack = [], ch, ok = true;
				for ( var runner = cursor; runner; runner = dir ? runner.nextSibling
						: runner.previousSibling) {
					if (runner.className == className
							&& runner.nodeName == "SPAN"
							&& (ch = paren(runner))) {
						if (forward(ch) == dir) {
							stack.push(ch)
						} else {
							if (!stack.length) {
								ok = false
							} else {
								if (stack.pop() != matching[ch]) {
									ok = false
								}
							}
						}
						if (!stack.length) {
							break
						}
					} else {
						if (runner.dirty || runner.nodeName != "SPAN"
								&& runner.nodeName != "BR") {
							return {
								node : runner,
								status : "dirty"
							}
						}
					}
				}
				return {
					node : runner,
					status : runner && ok
				}
			}
			function blink(node, ok) {
				node.style.fontWeight = "bold";
				node.style.color = ok ? "#8F8" : "#F88";
				self.parent.setTimeout(function() {
					node.style.fontWeight = "";
					node.style.color = ""
				}, 500)
			}
			while (true) {
				var found = tryFindMatch();
				if (found.status == "dirty") {
					this.highlight(found.node, endOfLine(found.node));
					found.node.dirty = false;
					continue
				} else {
					blink(cursor, found.status);
					if (found.node) {
						blink(found.node, found.status);
						if (jump) {
							select.focusAfterNode(found.node.previousSibling,
									this.container)
						}
					}
					break
				}
			}
		},
		indentAtCursor : function(direction) {
			if (!this.container.firstChild) {
				return
			}
			if (!this.highlightAtCursor()) {
				return
			}
			var cursor = select.selectionTopNode(this.container, false);
			if (cursor === false) {
				return
			}
			var lineStart = startOfLine(cursor);
			var whiteSpace = this.indentLineAfter(lineStart, direction);
			if (cursor == lineStart && whiteSpace) {
				cursor = whiteSpace
			}
			if (cursor == whiteSpace) {
				select.focusAfterNode(cursor, this.container)
			}
		},
		indentRegion : function(start, end, direction) {
			var current = (start = startOfLine(start)), before = start
					&& startOfLine(start.previousSibling);
			if (end.nodeName != "BR") {
				end = endOfLine(end, this.container)
			}
			do {
				var next = endOfLine(current, this.container);
				if (current) {
					this.highlight(before, next, true)
				}
				this.indentLineAfter(current, direction);
				before = current;
				current = next
			} while (current != end);
			select.setCursorPos(this.container, {
				node : start,
				offset : 0
			}, {
				node : end,
				offset : 0
			})
		},
		cursorActivity : function(safe) {
			if (internetExplorer) {
				this.container.createTextRange().execCommand("unlink");
				this.selectionSnapshot = select.selectionCoords(this.win)
			}
			var activity = this.options.cursorActivity;
			if (!safe || activity) {
				var cursor = select.selectionTopNode(this.container, false);
				if (cursor === false || !this.container.firstChild) {
					return
				}
				cursor = cursor || this.container.firstChild;
				if (activity) {
					activity(cursor)
				}
				if (!safe) {
					this.scheduleHighlight();
					this.addDirtyNode(cursor)
				}
			}
		},
		reparseBuffer : function() {
			forEach(this.container.childNodes, function(node) {
				node.dirty = true
			});
			if (this.container.firstChild) {
				this.addDirtyNode(this.container.firstChild)
			}
		},
		addDirtyNode : function(node) {
			node = node || this.container.firstChild;
			if (!node) {
				return
			}
			for ( var i = 0; i < this.dirty.length; i++) {
				if (this.dirty[i] == node) {
					return
				}
			}
			if (node.nodeType != 3) {
				node.dirty = true
			}
			this.dirty.push(node)
		},
		scheduleHighlight : function() {
			var self = this;
			this.parent.clearTimeout(this.highlightTimeout);
			this.highlightTimeout = this.parent.setTimeout(function() {
				self.highlightDirty()
			}, this.options.passDelay)
		},
		getDirtyNode : function() {
			while (this.dirty.length > 0) {
				var found = this.dirty.pop();
				try {
					while (found && found.parentNode != this.container) {
						found = found.parentNode
					}
					if (found && (found.dirty || found.nodeType == 3)) {
						return found
					}
				} catch (e) {
				}
			}
			return null
		},
		highlightDirty : function(force) {
			if (!window.select) {
				return
			}
			if (!this.options.readOnly) {
				select.markSelection(this.win)
			}
			var start, endTime = force ? null : time() + this.options.passTime;
			while (time() < endTime && (start = this.getDirtyNode())) {
				var result = this.highlight(start, endTime);
				if (result && result.node && result.dirty) {
					this.addDirtyNode(result.node)
				}
			}
			if (!this.options.readOnly) {
				select.selectMarked()
			}
			if (start) {
				this.scheduleHighlight()
			}
			return this.dirty.length == 0
		},
		documentScanner : function(passTime) {
			var self = this, pos = null;
			return function() {
				if (!window.select) {
					return
				}
				if (pos && pos.parentNode != self.container) {
					pos = null
				}
				select.markSelection(self.win);
				var result = self.highlight(pos, time() + passTime, true);
				select.selectMarked();
				var newPos = result ? (result.node && result.node.nextSibling)
						: null;
				pos = (pos == newPos) ? null : newPos;
				self.delayScanning()
			}
		},
		delayScanning : function() {
			if (this.scanner) {
				this.parent.clearTimeout(this.documentScan);
				this.documentScan = this.parent.setTimeout(this.scanner,
						this.options.continuousScanning)
			}
		},
		highlight : function(from, target, cleanLines, maxBacktrack) {
			var container = this.container, self = this, active = this.options.activeTokens;
			var endTime = (typeof target == "number" ? target : null);
			if (!container.firstChild) {
				return
			}
			while (from && (!from.parserFromHere || from.dirty)) {
				if (maxBacktrack != null && from.nodeName == "BR"
						&& (--maxBacktrack) < 0) {
					return false
				}
				from = from.previousSibling
			}
			if (from && !from.nextSibling) {
				return
			}
			function correctPart(token, part) {
				return !part.reduced && part.currentText == token.value
						&& part.className == token.style
			}
			function shortenPart(part, minus) {
				part.currentText = part.currentText.substring(minus);
				part.reduced = true
			}
			function tokenPart(token) {
				var part = makePartSpan(token.value, self.doc);
				part.className = token.style;
				return part
			}
			function maybeTouch(node) {
				if (node) {
					if (lineDirty || node.nextSibling != node.oldNextSibling) {
						self.history.touch(node)
					}
					node.oldNextSibling = node.nextSibling
				} else {
					if (lineDirty
							|| self.container.firstChild != self.container.oldFirstChild) {
						self.history.touch(node)
					}
					self.container.oldFirstChild = self.container.firstChild
				}
			}
			var traversal = traverseDOM(from ? from.nextSibling
					: container.firstChild), stream = stringStream(traversal), parsed = from ? from
					.parserFromHere(stream)
					: Editor.Parser.make(stream);
			var parts = {
				current : null,
				get : function() {
					if (!this.current) {
						this.current = traversal.nodes.shift()
					}
					return this.current
				},
				next : function() {
					this.current = null
				},
				remove : function() {
					container.removeChild(this.get());
					this.current = null
				},
				getNonEmpty : function() {
					var part = this.get();
					while (part && part.nodeName == "SPAN"
							&& part.currentText == "") {
						var old = part;
						this.remove();
						part = this.get();
						select.snapshotMove(old.firstChild, part
								&& (part.firstChild || part), 0)
					}
					return part
				}
			};
			var lineDirty = false, prevLineDirty = true, lineNodes = 0;
			forEach(parsed,
					function(token) {
						var part = parts.getNonEmpty();
						if (token.value == "\n") {
							if (part.nodeName != "BR") {
								throw "Parser out of sync. Expected BR."
							}
							if (part.dirty || !part.indentation) {
								lineDirty = true
							}
							maybeTouch(from);
							from = part;
							part.parserFromHere = parsed.copy();
							part.indentation = token.indentation;
							part.dirty = false;
							if (endTime == null && part == target) {
								throw StopIteration
							}
							if ((endTime != null && time() >= endTime)
									|| (!lineDirty && !prevLineDirty
											&& lineNodes > 1 && !cleanLines)) {
								throw StopIteration
							}
							prevLineDirty = lineDirty;
							lineDirty = false;
							lineNodes = 0;
							parts.next()
						} else {
							if (part.nodeName != "SPAN") {
								throw "Parser out of sync. Expected SPAN."
							}
							if (part.dirty) {
								lineDirty = true
							}
							lineNodes++;
							if (correctPart(token, part)) {
								part.dirty = false;
								parts.next()
							} else {
								lineDirty = true;
								var newPart = tokenPart(token);
								container.insertBefore(newPart, part);
								if (active) {
									active(newPart, token, self)
								}
								var tokensize = token.value.length;
								var offset = 0;
								while (tokensize > 0) {
									part = parts.get();
									var partsize = part.currentText.length;
									select.snapshotReplaceNode(part.firstChild,
											newPart.firstChild, tokensize,
											offset);
									if (partsize > tokensize) {
										shortenPart(part, tokensize);
										tokensize = 0
									} else {
										tokensize -= partsize;
										offset += partsize;
										parts.remove()
									}
								}
							}
						}
					});
			maybeTouch(from);
			webkitLastLineHack(this.container);
			return {
				node : parts.getNonEmpty(),
				dirty : lineDirty
			}
		}
	};
	return Editor
})();
addEventHandler(window, "load", function() {
	var CodeMirror = window.frameElement.CodeMirror;
	CodeMirror.editor = new Editor(CodeMirror.options);
	this.parent.setTimeout(method(CodeMirror, "init"), 0)
});