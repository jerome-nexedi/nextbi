if (!Array.prototype.indexOf) {
  Array.prototype.indexOf = function(elt) {
    var len = this.length;
    var from = Number(arguments[1]) || 0;
    from = (from < 0) ? Math.ceil(from) : Math.floor(from);
    if (from < 0) {
      from += len
    }
    for (; from < len; from++) {
      if (from in this && this[from] === elt) {
        return from
      }
    }
    return - 1
  }
}
var PHPParser = Editor.Parser = (function() {
  var atomicTypes = {
    atom: true,
    number: true,
    variable: true,
    string: true
  };
  function PHPLexical(indented, column, type, align, prev, info) {
    this.indented = indented;
    this.column = column;
    this.type = type;
    if (align != null) {
      this.align = align
    }
    this.prev = prev;
    this.info = info
  }
  function indentPHP(lexical) {
    return function(firstChars) {
      var firstChar = firstChars && firstChars.charAt(0),
      type = lexical.type;
      var closing = firstChar == type;
      if (type == "form" && firstChar == "{") {
        return lexical.indented
      } else {
        if (type == "stat" || type == "form") {
          return lexical.indented + indentUnit
        } else {
          if (lexical.info == "switch" && !closing) {
            return lexical.indented + (/^(?:case|default)\b/.test(firstChars) ? indentUnit: 2 * indentUnit)
          } else {
            if (lexical.align) {
              return lexical.column - (closing ? 1 : 0)
            } else {
              return lexical.indented + (closing ? 0 : indentUnit)
            }
          }
        }
      }
    }
  }
  function parsePHP(input, basecolumn) {
    var tokens = tokenizePHP(input);
    var cc = [statements];
    var lexical = new PHPLexical((basecolumn || 0) - indentUnit, 0, "block", false);
    var column = 0;
    var indented = 0;
    var consume, marked;
    var parser = {
      next: next,
      copy: copy
    };
    function next() {
      while (cc[cc.length - 1].lex) {
        cc.pop()()
      }
      var token = tokens.next();
      if (token.type == "whitespace" && column == 0) {
        indented = token.value.length
      }
      column += token.value.length;
      if (token.content == "\n") {
        indented = column = 0;
        if (! ("align" in lexical)) {
          lexical.align = false
        }
        token.indentation = indentPHP(lexical)
      }
      if (token.type == "whitespace" || token.type == "comment" || token.type == "string_not_terminated") {
        return token
      }
      if (! ("align" in lexical)) {
        lexical.align = true
      }
      while (true) {
        consume = marked = false;
        var action = cc.pop();
        action(token);
        if (consume) {
          if (marked) {
            token.style = marked
          }
          return token
        }
      }
      return 1
    }
    function copy() {
      var _lexical = lexical,
      _cc = cc.concat([]),
      _tokenState = tokens.state;
      return function copyParser(input) {
        lexical = _lexical;
        cc = _cc.concat([]);
        column = indented = 0;
        tokens = tokenizePHP(input, _tokenState);
        return parser
      }
    }
    function push(fs) {
      for (var i = fs.length - 1; i >= 0; i--) {
        cc.push(fs[i])
      }
    }
    function cont() {
      push(arguments);
      consume = true
    }
    function pass() {
      push(arguments);
      consume = false
    }
    function mark(style) {
      marked = style
    }
    function mark_add(style) {
      marked = marked + " " + style
    }
    function pushlex(type, info) {
      var result = function pushlexing() {
        lexical = new PHPLexical(indented, column, type, null, lexical, info)
      };
      result.lex = true;
      return result
    }
    function poplex() {
      lexical = lexical.prev
    }
    poplex.lex = true;
    function expect(wanted) {
      return function expecting(token) {
        if (token.type == wanted) {
          cont()
        } else {
          cont(arguments.callee)
        }
      }
    }
    function require(wanted, execute) {
      return function requiring(token) {
        var ok;
        var type = token.type;
        if (typeof(wanted) == "string") {
          ok = (type == wanted) - 1
        } else {
          ok = wanted.indexOf(type)
        }
        if (ok >= 0) {
          if (execute && typeof(execute[ok]) == "function") {
            execute[ok](token)
          }
          cont()
        } else {
          if (!marked) {
            mark(token.style)
          }
          mark_add("syntax-error");
          cont(arguments.callee)
        }
      }
    }
    function statements(token) {
      return pass(statement, statements)
    }
    function statement(token) {
      var type = token.type;
      if (type == "keyword a") {
        cont(pushlex("form"), expression, statement, poplex)
      } else {
        if (type == "keyword b") {
          cont(pushlex("form"), statement, poplex)
        } else {
          if (type == "{") {
            cont(pushlex("}"), block, poplex)
          } else {
            if (type == "function") {
              funcdef()
            } else {
              if (type == "class") {
                cont(require("t_string"), expect("{"), pushlex("}"), block, poplex)
              } else {
                if (type == "foreach") {
                  cont(pushlex("form"), require("("), pushlex(")"), expression, require("as"), require("variable"), expect(")"), poplex, statement, poplex)
                } else {
                  if (type == "for") {
                    cont(pushlex("form"), require("("), pushlex(")"), expression, require(";"), expression, require(";"), expression, require(")"), poplex, statement, poplex)
                  } else {
                    if (type == "modifier") {
                      cont(require(["modifier", "variable", "function"], [null, null, funcdef]))
                    } else {
                      if (type == "switch") {
                        cont(pushlex("form"), require("("), expression, require(")"), pushlex("}", "switch"), require([":", "{"]), block, poplex, poplex)
                      } else {
                        if (type == "case") {
                          cont(expression, require(":"))
                        } else {
                          if (type == "default") {
                            cont(require(":"))
                          } else {
                            if (type == "catch") {
                              cont(pushlex("form"), require("("), require("t_string"), require("variable"), require(")"), statement, poplex)
                            } else {
                              if (type == "const") {
                                cont(require("t_string"))
                              } else {
                                if (type == "namespace") {
                                  cont(namespacedef, require(";"))
                                } else {
                                  pass(pushlex("stat"), expression, require(";"), poplex)
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
            }
          }
        }
      }
    }
    function expression(token) {
      var type = token.type;
      if (atomicTypes.hasOwnProperty(type)) {
        cont(maybeoperator)
      } else {
        if (type == "<<<") {
          cont(require("string"), maybeoperator)
        } else {
          if (type == "t_string") {
            cont(maybe_double_colon, maybeoperator)
          } else {
            if (type == "keyword c") {
              cont(expression)
            } else {
              if (type == "(") {
                cont(pushlex(")"), commasep(expression), require(")"), poplex, maybeoperator)
              } else {
                if (type == "operator") {
                  cont(expression)
                }
              }
            }
          }
        }
      }
    }
    function maybeoperator(token) {
      var type = token.type;
      if (type == "operator") {
        if (token.content == "?") {
          cont(expression, require(":"), expression)
        } else {
          cont(expression)
        }
      } else {
        if (type == "(") {
          cont(pushlex(")"), expression, commasep(expression), require(")"), poplex, maybeoperator)
        } else {
          if (type == "[") {
            cont(pushlex("]"), expression, require("]"), maybeoperator, poplex)
          }
        }
      }
    }
    function maybe_double_colon(token) {
      if (token.type == "t_double_colon") {
        cont(require(["t_string", "variable"]), maybeoperator)
      } else {
        pass(expression)
      }
    }
    function funcdef() {
      cont(require("t_string"), require("("), pushlex(")"), commasep(funcarg), require(")"), poplex, block)
    }
    function commasep(what) {
      function proceed(token) {
        if (token.type == ",") {
          cont(what, proceed)
        }
      }
      return function commaSeparated() {
        pass(what, proceed)
      }
    }
    function block(token) {
      if (token.type == "}") {
        cont()
      } else {
        pass(statement, block)
      }
    }
    function maybedefaultparameter(token) {
      if (token.content == "=") {
        cont(expression)
      }
    }
    function funcarg(token) {
      if (token.type == "t_string") {
        cont(require("variable"), maybedefaultparameter)
      } else {
        if (token.type == "variable") {
          cont(maybedefaultparameter)
        }
      }
    }
    function maybe_double_colon_def(token) {
      if (token.type == "t_double_colon") {
        cont(namespacedef)
      }
    }
    function namespacedef(token) {
      pass(require("t_string"), maybe_double_colon_def)
    }
    return parser
  }
  return {
    make: parsePHP,
    electricChars: "{}:"
  }
})();