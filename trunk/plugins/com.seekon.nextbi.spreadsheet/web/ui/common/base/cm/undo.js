function History(container, maxDepth, commitDelay, editor, onChange) {
	this.container = container;
	this.maxDepth = maxDepth;
	this.commitDelay = commitDelay;
	this.editor = editor;
	this.parent = editor.parent;
	this.onChange = onChange;
	var initial = {
		text : "",
		from : null,
		to : null
	};
	this.first = initial;
	this.last = initial;
	this.firstTouched = false;
	this.history = [];
	this.redoHistory = [];
	this.touched = []
}
History.prototype = {
	scheduleCommit : function() {
		var self = this;
		this.parent.clearTimeout(this.commitTimeout);
		this.commitTimeout = this.parent.setTimeout(function() {
			self.tryCommit()
		}, this.commitDelay)
	},
	touch : function(node) {
		this.setTouched(node);
		this.scheduleCommit()
	},
	undo : function() {
		this.commit();
		if (this.history.length) {
			var item = this.history.pop();
			this.redoHistory.push(this.updateTo(item, "applyChain"));
			if (this.onChange) {
				this.onChange()
			}
			return this.chainNode(item)
		}
	},
	redo : function() {
		this.commit();
		if (this.redoHistory.length) {
			var item = this.redoHistory.pop();
			this.addUndoLevel(this.updateTo(item, "applyChain"));
			if (this.onChange) {
				this.onChange()
			}
			return this.chainNode(item)
		}
	},
	clear : function() {
		this.history = [];
		this.redoHistory = []
	},
	historySize : function() {
		return {
			undo : this.history.length,
			redo : this.redoHistory.length
		}
	},
	push : function(from, to, lines) {
		var chain = [];
		for ( var i = 0; i < lines.length; i++) {
			var end = (i == lines.length - 1) ? to
					: this.container.ownerDocument.createElement("BR");
			chain.push( {
				from : from,
				to : end,
				text : cleanText(lines[i])
			});
			from = end
		}
		this.pushChains( [ chain ], from == null && to == null)
	},
	pushChains : function(chains, doNotHighlight) {
		this.commit(doNotHighlight);
		this.addUndoLevel(this.updateTo(chains, "applyChain"));
		this.redoHistory = []
	},
	chainNode : function(chains) {
		for ( var i = 0; i < chains.length; i++) {
			var start = chains[i][0], node = start && (start.from || start.to);
			if (node) {
				return node
			}
		}
	},
	reset : function() {
		this.history = [];
		this.redoHistory = []
	},
	textAfter : function(br) {
		return this.after(br).text
	},
	nodeAfter : function(br) {
		return this.after(br).to
	},
	nodeBefore : function(br) {
		return this.before(br).from
	},
	tryCommit : function() {
		if (!window.History) {
			return
		}
		if (this.editor.highlightDirty()) {
			this.commit()
		} else {
			this.scheduleCommit()
		}
	},
	commit : function(doNotHighlight) {
		this.parent.clearTimeout(this.commitTimeout);
		if (!doNotHighlight) {
			this.editor.highlightDirty(true)
		}
		var chains = this.touchedChains(), self = this;
		if (chains.length) {
			this.addUndoLevel(this.updateTo(chains, "linkChain"));
			this.redoHistory = [];
			if (this.onChange) {
				this.onChange()
			}
		}
	},
	updateTo : function(chains, updateFunc) {
		var shadows = [], dirty = [];
		for ( var i = 0; i < chains.length; i++) {
			shadows.push(this.shadowChain(chains[i]));
			dirty.push(this[updateFunc](chains[i]))
		}
		if (updateFunc == "applyChain") {
			this.notifyDirty(dirty)
		}
		return shadows
	},
	notifyDirty : function(nodes) {
		forEach(nodes, method(this.editor, "addDirtyNode"));
		this.editor.scheduleHighlight()
	},
	linkChain : function(chain) {
		for ( var i = 0; i < chain.length; i++) {
			var line = chain[i];
			if (line.from) {
				line.from.historyAfter = line
			} else {
				this.first = line
			}
			if (line.to) {
				line.to.historyBefore = line
			} else {
				this.last = line
			}
		}
	},
	after : function(node) {
		return node ? node.historyAfter : this.first
	},
	before : function(node) {
		return node ? node.historyBefore : this.last
	},
	setTouched : function(node) {
		if (node) {
			if (!node.historyTouched) {
				this.touched.push(node);
				node.historyTouched = true
			}
		} else {
			this.firstTouched = true
		}
	},
	addUndoLevel : function(diffs) {
		this.history.push(diffs);
		if (this.history.length > this.maxDepth) {
			this.history.shift()
		}
	},
	touchedChains : function() {
		var self = this;
		var nullTemp = null;
		function temp(node) {
			return node ? node.historyTemp : nullTemp
		}
		function setTemp(node, line) {
			if (node) {
				node.historyTemp = line
			} else {
				nullTemp = line
			}
		}
		function buildLine(node) {
			var text = [];
			for ( var cur = node ? node.nextSibling : self.container.firstChild; cur
					&& cur.nodeName != "BR"; cur = cur.nextSibling) {
				if (cur.currentText) {
					text.push(cur.currentText)
				}
			}
			return {
				from : node,
				to : cur,
				text : cleanText(text.join(""))
			}
		}
		var lines = [];
		if (self.firstTouched) {
			self.touched.push(null)
		}
		forEach(self.touched, function(node) {
			if (node && node.parentNode != self.container) {
				return
			}
			if (node) {
				node.historyTouched = false
			} else {
				self.firstTouched = false
			}
			var line = buildLine(node), shadow = self.after(node);
			if (!shadow || shadow.text != line.text || shadow.to != line.to) {
				lines.push(line);
				setTemp(node, line)
			}
		});
		function nextBR(node, dir) {
			var link = dir + "Sibling", search = node[link];
			while (search && search.nodeName != "BR") {
				search = search[link]
			}
			return search
		}
		var chains = [];
		self.touched = [];
		forEach(lines, function(line) {
			if (!temp(line.from)) {
				return
			}
			var chain = [], curNode = line.from, safe = true;
			while (true) {
				var curLine = temp(curNode);
				if (!curLine) {
					if (safe) {
						break
					} else {
						curLine = buildLine(curNode)
					}
				}
				chain.unshift(curLine);
				setTemp(curNode, null);
				if (!curNode) {
					break
				}
				safe = self.after(curNode);
				curNode = nextBR(curNode, "previous")
			}
			curNode = line.to;
			safe = self.before(line.from);
			while (true) {
				if (!curNode) {
					break
				}
				var curLine = temp(curNode);
				if (!curLine) {
					if (safe) {
						break
					} else {
						curLine = buildLine(curNode)
					}
				}
				chain.push(curLine);
				setTemp(curNode, null);
				safe = self.before(curNode);
				curNode = nextBR(curNode, "next")
			}
			chains.push(chain)
		});
		return chains
	},
	shadowChain : function(chain) {
		var shadows = [], next = this.after(chain[0].from), end = chain[chain.length - 1].to;
		while (true) {
			shadows.push(next);
			var nextNode = next.to;
			if (!nextNode || nextNode == end) {
				break
			} else {
				next = nextNode.historyAfter || this.before(end)
			}
		}
		return shadows
	},
	applyChain : function(chain) {
		var cursor = select.cursorPos(this.container, false), self = this;
		function removeRange(from, to) {
			var pos = from ? from.nextSibling : self.container.firstChild;
			while (pos != to) {
				var temp = pos.nextSibling;
				removeElement(pos);
				pos = temp
			}
		}
		var start = chain[0].from, end = chain[chain.length - 1].to;
		removeRange(start, end);
		for ( var i = 0; i < chain.length; i++) {
			var line = chain[i];
			if (i > 0) {
				self.container.insertBefore(line.from, end)
			}
			var node = makePartSpan(fixSpaces(line.text),
					this.container.ownerDocument);
			self.container.insertBefore(node, end);
			if (cursor && cursor.node == line.from) {
				var cursordiff = 0;
				var prev = this.after(line.from);
				if (prev && i == chain.length - 1) {
					for ( var match = 0; match < cursor.offset
							&& line.text.charAt(match) == prev.text
									.charAt(match); match++) {
					}
					if (cursor.offset > match) {
						cursordiff = line.text.length - prev.text.length
					}
				}
				select.setCursorPos(this.container, {
					node : line.from,
					offset : Math.max(0, cursor.offset + cursordiff)
				})
			} else {
				if (cursor && (i == chain.length - 1) && cursor.node
						&& cursor.node.parentNode != this.container) {
					select.setCursorPos(this.container, {
						node : line.from,
						offset : line.text.length
					})
				}
			}
		}
		this.linkChain(chain);
		return start
	}
};