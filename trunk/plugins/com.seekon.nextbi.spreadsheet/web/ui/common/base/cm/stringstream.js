window.stringStream = function(source) {
	var current = "";
	var pos = 0;
	var accum = "";
	function ensureChars() {
		while (pos == current.length) {
			accum += current;
			current = "";
			pos = 0;
			try {
				current = source.next()
			} catch (e) {
				if (e != StopIteration) {
					throw e
				} else {
					return false
				}
			}
		}
		return true
	}
	return {
		peek : function() {
			if (!ensureChars()) {
				return null
			}
			return current.charAt(pos)
		},
		next : function() {
			if (!ensureChars()) {
				if (accum.length > 0) {
					throw "End of stringstream reached without emptying buffer ('"
							+ accum + "')."
				} else {
					throw StopIteration
				}
			}
			return current.charAt(pos++)
		},
		get : function() {
			var temp = accum;
			accum = "";
			if (pos > 0) {
				temp += current.slice(0, pos);
				current = current.slice(pos);
				pos = 0
			}
			return temp
		},
		push : function(str) {
			current = current.slice(0, pos) + str + current.slice(pos)
		},
		lookAhead : function(str, consume, skipSpaces, caseInsensitive) {
			function cased(str) {
				return caseInsensitive ? str.toLowerCase() : str
			}
			str = cased(str);
			var found = false;
			var _accum = accum, _pos = pos;
			if (skipSpaces) {
				this.nextWhileMatches(/[\s\u00a0]/)
			}
			while (true) {
				var end = pos + str.length, left = current.length - pos;
				if (end <= current.length) {
					found = str == cased(current.slice(pos, end));
					pos = end;
					break
				} else {
					if (str.slice(0, left) == cased(current.slice(pos))) {
						accum += current;
						current = "";
						try {
							current = source.next()
						} catch (e) {
							break
						}
						pos = 0;
						str = str.slice(left)
					} else {
						break
					}
				}
			}
			if (!(found && consume)) {
				current = accum.slice(_accum.length) + current;
				pos = _pos;
				accum = _accum
			}
			return found
		},
		more : function() {
			return this.peek() !== null
		},
		applies : function(test) {
			var next = this.peek();
			return (next !== null && test(next))
		},
		nextWhile : function(test) {
			var next;
			while ((next = this.peek()) !== null && test(next)) {
				this.next()
			}
		},
		matches : function(re) {
			var next = this.peek();
			return (next !== null && re.test(next))
		},
		nextWhileMatches : function(re) {
			var next;
			while ((next = this.peek()) !== null && re.test(next)) {
				this.next()
			}
		},
		equals : function(ch) {
			return ch === this.peek()
		},
		endOfLine : function() {
			var next = this.peek();
			return next == null || next == "\n"
		}
	}
};