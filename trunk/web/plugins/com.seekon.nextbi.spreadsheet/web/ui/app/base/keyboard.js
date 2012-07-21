if (!Jedox.wss.app.currentDialogControl) {
	Jedox.wss.keyboard.checkGlobalInput = function(myEvent) {
		if (!Jedox.wss.app.loaded) {
			return
		}
		if (document.all) {
			myEvent = window.event
		}
		var myKeyPressed = ((document.all) ? myEvent.keyCode : myEvent.which);
		Jedox.wss.app.lastKeyPressed = myKeyPressed;
		var result = true;
		var newMode;
		var tmp;
		var env = Jedox.wss.app.environment;
		var activeBook = Jedox.wss.app.activeBook;
		var ctrlpressed = false;
		if (env.inputMode != Jedox.wss.grid.GridMode.DIALOG
				&& env.editingDirectly) {
			var lastInputField = Jedox.wss.app.lastInputField;
			var selCellCoords = env.selectedCellCoords;
			if (env.viewMode != Jedox.wss.grid.viewMode.USER
					&& (lastInputField == Jedox.wss.app.currFormula || !activeBook
							.isCellVisible(selCellCoords[0], selCellCoords[1]))) {
				Jedox.wss.app.currFormula.focus();
				Jedox.wss.app.fromFormulaField = true
			} else {
				lastInputField.focus()
			}
		}
		if (myEvent.ctrlKey && !Jedox.wss.app.ctrlKeyPressed) {
			Jedox.wss.app.ctrlKeyPressed = true
		}
		if (env.viewMode != Jedox.wss.grid.viewMode.USER
				&& !Jedox.wss.app.ctrlKeyPressed) {
			env.defaultSelection.show();
			Jedox.wss.hb.setAllNormal()
		}
		switch (myKeyPressed) {
		case 9:
		case 13:
			if (env.inputMode == Jedox.wss.grid.GridMode.EDIT
					|| env.inputMode == Jedox.wss.grid.GridMode.INPUT) {
				result = Jedox.wss.keyboard
						.sendInput(env.inputField, 13, (myKeyPressed == 13
								&& myEvent.ctrlKey && myEvent.shiftKey))
			}
			if ((env.inputMode != Jedox.wss.grid.GridMode.CNTRL)
					&& (env.inputMode != Jedox.wss.grid.GridMode.DIALOG)) {
				if (result) {
					Jedox.wss.keyboard.cancelInput((myKeyPressed == 9) ? false
							: true);
					try {
						Jedox.wss.app.currFormula.blur()
					} catch (e) {
					}
					if (myKeyPressed == 9) {
						Jedox.wss.keyboard.moveCursor(
								Jedox.wss.grid.ScrollDirection.RIGHT,
								myEvent.shiftKey, 1, myKeyPressed)
					} else {
						if (!(myEvent.ctrlKey && myEvent.shiftKey)) {
							Jedox.wss.keyboard.moveCursor(
									Jedox.wss.grid.ScrollDirection.DOWN,
									myEvent.shiftKey, 1, myKeyPressed)
						}
					}
				} else {
					alert("Invalid input!".localize());
					Jedox.wss.general.focusInputField()
				}
			}
			break;
		case 27:
			if (env.viewMode == Jedox.wss.grid.viewMode.DESIGNER
					&& env.inputMode == Jedox.wss.grid.GridMode.EDIT) {
				Jedox.wss.app.currFormula.blur()
			}
			Jedox.wss.keyboard.cancelInput();
			env.editingDirectly = false;
			break;
		case 36:
			if (myEvent.ctrlKey) {
				Jedox.wss.keyboard.preventKeyEvent(myEvent);
				activeBook.scrollTo( [ this, setDefaultRange, [ 1, 1, 1, 1 ] ],
						1, 1, true, false);
				return
			}
			break;
		case 35:
			if (myEvent.ctrlKey) {
				Jedox.wss.keyboard.preventKeyEvent(myEvent);
				var fuc = activeBook.getFarthestUsedCell();
				activeBook.scrollTo( [ this, setDefaultRange,
						[ fuc[0], fuc[1], fuc[0], fuc[1] ] ], fuc[0], fuc[1],
						true, false);
				return
			}
			break;
		case 33:
		case 34:
		case 37:
		case 38:
		case 39:
		case 40:
			if (env.inputMode == Jedox.wss.grid.GridMode.INPUT) {
				var value = env.inputField.value;
				if ((value.length > 0) && (value.substr(0, 1) == "=")) {
					var elemCoords;
					if (env.formulaSelection.activeToken != null) {
						var point, area = env.formulaSelection.getActiveRange(), areaCorners = area
								.getCorners();
						elemCoords = Jedox.wss.keyboard.calcCursorRng(
								areaCorners[0].clone(), areaCorners[1].clone(),
								area.getAnchorCell().clone(), myKeyPressed,
								myEvent.shiftKey);
						area.set(elemCoords[0], elemCoords[1]);
						if (elemCoords[0].equals(elemCoords[1])) {
							area.setAnchorCell(elemCoords[0])
						}
						area.formulaUpdate();
						area.draw()
					} else {
						var defRngActCell = env.defaultSelection
								.getActiveRange().getActiveCell().clone();
						elemCoords = Jedox.wss.keyboard.calcCursorRng(
								defRngActCell, defRngActCell, defRngActCell,
								myKeyPressed, myEvent.shiftKey);
						if (!activeBook.isCellVisible(elemCoords[2].getX(),
								elemCoords[2].getY())
								&& activeBook._scrollOpPending) {
							return
						}
						var currFormula = Jedox.wss.app.currFormula, cursorPos = Jedox.util
								.getSelection(Jedox.wss.app.fromFormulaField ? currFormula
										: env.inputField).start;
						if (cursorPos < currFormula.value.length
								|| value.substr(-1, 1).match(/^[a-z0-9]$/i) != null) {
							if (env.oldValue != value) {
								Jedox.wss.keyboard.sendInput(env.inputField,
										1000)
							} else {
								Jedox.wss.keyboard.cancelInput()
							}
							return Jedox.wss.keyboard.checkGlobalInput(myEvent)
						}
						var area_id = env.formulaSelection
								.addRange(new Jedox.wss.cls.Point(elemCoords[0]
										.getX(), elemCoords[0].getY())) - 1;
						env.formulaSelection.setActiveRange(area_id);
						var area = env.formulaSelection.getActiveRange(), areaVal = area
								.getValue();
						env.inputField.value = currFormula.value = value
								.concat(areaVal);
						var refs = Jedox.wss.formula.parse(currFormula.value,
								Jedox.wss.app.activePane.getCellNFs(
										elemCoords[0].getX(), elemCoords[0]
												.getY()));
						env.formulaSelection.lastParseRes = refs;
						env.formulaSelection.activeToken = area.formulaToken = refs[refs.length - 1];
						env.formulaSelection
								.setState(Jedox.wss.range.AreaState.NEW);
						area.draw();
						Jedox.wss.keyboard.setFieldSize();
						env.lastInputValue = currFormula.value
					}
					if (!activeBook.isCellVisible(elemCoords[2].getX(),
							elemCoords[2].getY())) {
						Jedox.wss.keyboard.preventKeyEvent(myEvent);
						activeBook.scrollTo( [ this, kbdHandlerEnd ],
								elemCoords[2].getX(), elemCoords[2].getY(),
								true, false);
						return
					}
				} else {
					result = Jedox.wss.keyboard.sendInput(env.inputField, 13);
					if (result) {
						Jedox.wss.keyboard.cancelInput(false);
						try {
							Jedox.wss.app.currFormula.blur()
						} catch (e) {
						}
						Jedox.wss.keyboard.handleCursorKey(myEvent)
					} else {
						alert("Invalid input!".localize());
						Jedox.wss.general.focusInputField()
					}
				}
				Jedox.wss.keyboard.preventKeyEvent(myEvent)
			} else {
				if (!env.editingDirectly
						&& (env.inputMode != Jedox.wss.grid.GridMode.CNTRL)
						&& (env.inputMode != Jedox.wss.grid.GridMode.DIALOG)) {
					Jedox.wss.keyboard.handleCursorKey(myEvent)
				}
				if ((env.inputMode == Jedox.wss.grid.GridMode.DIALOG)
						&& Jedox.wss.app.currentDialogControl != null) {
					var selCmp = Ext.getCmp(Jedox.wss.app.currentDialogControl);
					if (selCmp.getXType() == "dataview") {
						var currIndex = selCmp.getSelectedIndexes()[0];
						switch (myKeyPressed) {
						case 33:
						case 37:
						case 38:
							selCmp
									.select((currIndex > 0) ? --currIndex
											: Jedox.wss.app.currentDialogControlItemsCnt - 1);
							break;
						case 34:
						case 39:
						case 40:
							selCmp
									.select((currIndex < Jedox.wss.app.currentDialogControlItemsCnt - 1) ? ++currIndex
											: 0);
							break
						}
						Jedox.wss.keyboard.preventKeyEvent(myEvent)
					}
				}
			}
			break;
		case 46:
			if ((env.inputMode != Jedox.wss.grid.GridMode.INPUT)
					&& (env.inputMode != Jedox.wss.grid.GridMode.EDIT)
					&& (env.inputMode != Jedox.wss.grid.GridMode.CNTRL)
					&& (env.inputMode != Jedox.wss.grid.GridMode.DIALOG)
					&& (!env.editingDirectly)) {
				if (env.viewMode == Jedox.wss.grid.viewMode.USER) {
					env.inputField.value = "";
					env.selectedCellValue = "";
					env.cursorField.innerHTML = "";
					var cellCoords = env.lastRangeStartCoord;
					activeBook.clrRange( [ cellCoords[0], cellCoords[1],
							cellCoords[0], cellCoords[1] ])
				} else {
					env.defaultSelection.emptyCellContent()
				}
			}
			break;
		case 113:
			if ((env.inputMode != Jedox.wss.grid.GridMode.EDIT)
					&& (env.inputMode != Jedox.wss.grid.GridMode.INPUT)) {
				Jedox.wss.general.setInputMode(Jedox.wss.grid.GridMode.EDIT);
				Jedox.wss.general.showInputField(null, false, true)
			} else {
				newMode = (env.inputMode == Jedox.wss.grid.GridMode.EDIT) ? Jedox.wss.grid.GridMode.INPUT
						: Jedox.wss.grid.GridMode.EDIT;
				Jedox.wss.general.setInputMode(newMode)
			}
			break;
		case 120:
			if (env.inputMode != Jedox.wss.grid.GridMode.EDIT
					&& env.inputMode != Jedox.wss.grid.GridMode.INPUT) {
				Jedox.wss.app.activeBook.recalc()
			}
			break;
		case 122:
			return;
			break;
		case 17:
			var mousePos = env.mousePosition;
			if (mousePos == "rngBorder" || mousePos == "magicDot") {
				env.defaultSelection
						.setCursor(mousePos,
								(mousePos == "rngBorder") ? "rng_copy"
										: "md_curr_plus")
			}
			break;
		case 67:
			if (myEvent.ctrlKey) {
				Jedox.wss.action.copy(true);
				if (env.inputMode == Jedox.wss.grid.GridMode.READY
						|| env.inputMode == Jedox.wss.grid.GridMode.DIALOG) {
					var copyToClp = true
				}
			} else {
				handleDefault()
			}
			break;
		case 71:
			if (myEvent.ctrlKey) {
				Jedox.wss.app.load(Jedox.wss.app.dynJSRegistry.goTo)
			} else {
				handleDefault()
			}
			break;
		case 86:
			if (myEvent.ctrlKey
					&& env.inputMode == Jedox.wss.grid.GridMode.READY) {
				if (Jedox.wss.app.clipboard == null) {
					var pomdiv = document.createElement("div");
					pomdiv.setAttribute("id", "pomdiv");
					pomdiv.style.width = "1px";
					pomdiv.style.height = "1px";
					pomdiv.style.position = "relative";
					pomdiv.style.overflow = "hidden";
					var _pinput = document.createElement("textarea");
					_pinput.setAttribute("id", "_paste_field_");
					_pinput.setAttribute("name", "_paste_field_");
					_pinput
							.setAttribute("style",
									"position: float; width: 1px; height: 1px; z-index: 999; overflow: hidden;");
					pomdiv.appendChild(_pinput);
					document.body.appendChild(pomdiv);
					document.getElementById("_paste_field_").focus();
					document.getElementById("_paste_field_").select();
					var pasteFromClp = true
				} else {
					Jedox.wss.action.paste()
				}
			} else {
				handleDefault()
			}
			break;
		case 88:
			if (myEvent.ctrlKey) {
				Jedox.wss.action.cut(true);
				if (env.inputMode == Jedox.wss.grid.GridMode.READY
						|| env.inputMode == Jedox.wss.grid.GridMode.DIALOG) {
					var copyToClp = true
				}
			} else {
				handleDefault()
			}
			break;
		case 89:
			if (myEvent.ctrlKey) {
				Jedox.wss.sheet.redo();
				Jedox.wss.keyboard.preventKeyEvent(myEvent)
			} else {
				handleDefault()
			}
			break;
		case 90:
			if (myEvent.ctrlKey) {
				Jedox.wss.sheet.undo();
				Jedox.wss.keyboard.preventKeyEvent(myEvent)
			} else {
				handleDefault()
			}
			break;
		default:
			handleDefault()
		}
		function handleDefault() {
			if (myKeyPressed == 8 || myKeyPressed == 32
					|| (myKeyPressed > 41 && myKeyPressed <= 90)
					|| (myKeyPressed > 96 && myKeyPressed <= 107)
					|| (myKeyPressed > 108 && myKeyPressed <= 111)
					|| (myKeyPressed >= 187 && myKeyPressed <= 226)) {
				if (env.inputMode == Jedox.wss.grid.GridMode.READY) {
					if (env.viewMode == Jedox.wss.grid.viewMode.USER) {
						var selCoords = env.selectedCellCoords;
						if (Jedox.wss.app.activeBook.isCellLocked(selCoords[0],
								selCoords[1])) {
							return
						}
					}
					env.selectedCellValue = "";
					env.selectedCellFormula = "";
					Jedox.wss.general
							.setInputMode(Jedox.wss.grid.GridMode.INPUT);
					Jedox.wss.general.showInputField(null, false, true, false);
					Jedox.wss.app.updateUndoState( [ 1, 0 ], false)
				} else {
					if (env.inputMode == Jedox.wss.grid.GridMode.EDIT
							|| env.inputMode == Jedox.wss.grid.GridMode.INPUT) {
						Jedox.wss.app.updateUndoState( [ 1, 0 ], false)
					}
				}
			}
		}
		function resetFocus() {
			Jedox.wss.general.setInputMode(Jedox.wss.app.lastInputModeDlg);
			Jedox.wss.app.lastInputMode = Jedox.wss.grid.GridMode.READY
		}
		function setDefaultRange(rng) {
			var defSel = env.defaultSelection;
			if (defSel) {
				defSel.jumpTo(rng)
			}
			kbdHandlerEnd()
		}
		function kbdHandlerEnd() {
			if (pasteFromClp == undefined
					&& env.inputMode != Jedox.wss.grid.GridMode.EDIT
					&& env.inputMode != Jedox.wss.grid.GridMode.INPUT
					&& env.inputMode != Jedox.wss.grid.GridMode.CNTRL
					&& env.inputMode != Jedox.wss.grid.GridMode.DIALOG) {
				Jedox.wss.keyboard.preventKeyEvent(myEvent)
			}
			if (env.viewMode == Jedox.wss.grid.viewMode.USER) {
				Jedox.wss.keyboard
						.handleUMFocus(myKeyPressed, myEvent.shiftKey)
			}
			if (copyToClp != undefined && copyToClp) {
				resetFocus()
			}
			if (pasteFromClp != undefined && pasteFromClp) {
				setTimeout("Jedox.wss.action.paste()", 1);
				resetFocus()
			}
		}
		kbdHandlerEnd()
	}
}
Jedox.wss.keyboard.calcCursorRng = function(ul, lr, act, key, shiftPressed) {
	var defMaxCoords = Jedox.wss.grid.defMaxCoords, focusCell;
	switch (key) {
	case 37:
		if (shiftPressed) {
			if (ul.equalsX(lr)) {
				ul.setX((ul.getX() > 1) ? ul.getX() - 1 : ul.getX());
				focusCell = ul
			} else {
				if (ul.equalsX(act)) {
					lr.setX((lr.getX() > 1) ? lr.getX() - 1 : lr.getX());
					focusCell = lr
				} else {
					ul.setX((ul.getX() > 1) ? ul.getX() - 1 : ul.getX());
					focusCell = ul
				}
			}
		} else {
			var newX = (act.getX() > 1) ? act.getX() - 1 : act.getX();
			var newY = act.getY();
			ul.setX(newX);
			ul.setY(newY);
			lr.setX(newX);
			lr.setY(newY);
			focusCell = ul
		}
		break;
	case 39:
		if (shiftPressed) {
			if (ul.equalsX(lr)) {
				lr.setX((lr.getX() < defMaxCoords[0]) ? lr.getX() + 1 : lr
						.getX());
				focusCell = ul
			} else {
				if (lr.equalsX(act)) {
					ul.setX((ul.getX() < defMaxCoords[0]) ? ul.getX() + 1 : ul
							.getX());
					focusCell = ul
				} else {
					lr.setX((lr.getX() < defMaxCoords[0]) ? lr.getX() + 1 : lr
							.getX());
					focusCell = lr
				}
			}
		} else {
			var newX = (act.getX() < defMaxCoords[0]) ? act.getX() + 1 : act
					.getX();
			var newY = act.getY();
			ul.setX(newX);
			ul.setY(newY);
			lr.setX(newX);
			lr.setY(newY);
			focusCell = ul
		}
		break;
	case 33:
	case 38:
		if (shiftPressed) {
			if (ul.equalsY(lr)) {
				ul.setY((ul.getY() > 1) ? ul.getY() - 1 : ul.getY());
				focusCell = ul
			} else {
				if (ul.equalsY(act)) {
					lr.setY((lr.getY() > 1) ? lr.getY() - 1 : lr.getY());
					focusCell = lr
				} else {
					ul.setY((ul.getY() > 1) ? ul.getY() - 1 : ul.getY());
					focusCell = ul
				}
			}
		} else {
			var newX = act.getX();
			var newY = (act.getY() > 1) ? act.getY() - 1 : act.getY();
			ul.setX(newX);
			ul.setY(newY);
			lr.setX(newX);
			lr.setY(newY);
			focusCell = ul
		}
		break;
	case 34:
	case 40:
		if (shiftPressed) {
			if (ul.equalsY(lr)) {
				lr.setY((lr.getY() < defMaxCoords[1]) ? lr.getY() + 1 : lr
						.getY());
				focusCell = lr
			} else {
				if (lr.equalsY(act)) {
					ul.setY((ul.getY() < defMaxCoords[1]) ? ul.getY() + 1 : ul
							.getY());
					focusCell = ul
				} else {
					lr.setY((lr.getY() < defMaxCoords[1]) ? lr.getY() + 1 : lr
							.getY());
					focusCell = lr
				}
			}
		} else {
			var newX = act.getX();
			var newY = (act.getY() < defMaxCoords[1]) ? act.getY() + 1 : act
					.getY();
			ul.setX(newX);
			ul.setY(newY);
			lr.setX(newX);
			lr.setY(newY);
			focusCell = ul
		}
		break
	}
	return [ ul, lr, focusCell ]
};
Jedox.wss.keyboard.cursorKeys = {
	16 : true,
	33 : true,
	34 : true,
	37 : true,
	38 : true,
	39 : true,
	40 : true
};
Jedox.wss.keyboard.setFieldContent = function(ev) {
	var env = Jedox.wss.app.environment, currFormula = Jedox.wss.app.currFormula, changed;
	if (Jedox.wss.app.fromFormulaField) {
		env.inputField.value = currFormula.value
	} else {
		currFormula.value = env.inputField.value
	}
	if (changed = env.lastInputValue != currFormula.value) {
		Jedox.wss.keyboard.setFieldSize();
		env.lastInputValue = currFormula.value
	}
	if (currFormula.value.substr(0, 1) != "=") {
		return
	}
	var keyPressed = document.all ? (typeof window.event == "object" ? window.event.keyCode
			: -1)
			: (typeof ev == "object" ? ev.which : -1);
	if (!(keyPressed in Jedox.wss.keyboard.cursorKeys)) {
		env.formulaSelection.activeToken = null
	}
	if (changed) {
		Jedox.wss.range.drawDependingCells(currFormula.value)
	}
};
Jedox.wss.keyboard.setFieldSize = function() {
	var env = Jedox.wss.app.environment;
	var selCellWidth = env.selectedCell.offsetWidth, selCellHeight = env.selectedCell.offsetHeight;
	var activeBook = Jedox.wss.app.activeBook;
	var inputField = env.inputField;
	var tm = Ext.util.TextMetrics.createInstance(inputField.id);
	var rightMargin = tm.getWidth("WWW");
	var contWidth = tm.getWidth(inputField.value.replace(/ /g, "t"));
	var contLineHeight = tm.getHeight("W");
	var fieldWidth = contWidth;
	var fieldHeight = contLineHeight;
	var lastVisCellCoords = activeBook.getCoordsLastVCell();
	var lastVisCell = activeBook.getCellByCoords(lastVisCellCoords[0],
			lastVisCellCoords[1]);
	var maxDims = {
		width : lastVisCell.offsetLeft + lastVisCell.offsetWidth
				- inputField.offsetLeft - 2,
		height : lastVisCell.parentNode.offsetTop + lastVisCell.offsetHeight
				- inputField.offsetTop
	};
	if (contWidth < selCellWidth - 8) {
		fieldWidth = selCellWidth
				- ((env.viewMode == Jedox.wss.grid.viewMode.USER) ? 3 : 2)
	} else {
		if (contWidth < maxDims.width) {
			fieldWidth += rightMargin
		} else {
			fieldWidth = maxDims.width;
			contWidth *= 1.3;
			fieldHeight = Math.round(contWidth / maxDims.width)
					* contLineHeight
					+ ((contWidth % maxDims.width > 0) ? contLineHeight : 0);
			if (fieldHeight > maxDims.height) {
				fieldHeight = maxDims.height
			}
		}
	}
	inputField.style.width = fieldWidth + "px";
	if (fieldHeight > selCellHeight) {
		inputField.style.height = fieldHeight + "px"
	}
};
Jedox.wss.keyboard.sendInput = function(myInputBox, myKeyCode, isArrFrml) {
	var env = Jedox.wss.app.environment, activeBook = Jedox.wss.app.activeBook, cellValue = myInputBox.value, selCellCoords = env.selectedCellCoords;
	if (env.viewMode == Jedox.wss.grid.viewMode.USER && !cellValue.search(/^=/)) {
		return true
	} else {
		if (isArrFrml === undefined || !isArrFrml) {
			if (cellValue == env.oldValue) {
				return true
			}
			var hdata = Jedox.wss.hl.get(selCellCoords);
			if (hdata) {
				if (hdata.dyn) {
					if (cellValue.search(/^=HYPERLINK\(/) != 0) {
						activeBook.clrRange( [ selCellCoords[0],
								selCellCoords[1], selCellCoords[0],
								selCellCoords[1] ],
								Jedox.wss.range.ContentType.ATTRS)
					}
				} else {
					Jedox.wss.hl.updateText( [ selCellCoords[0],
							selCellCoords[1], selCellCoords[0],
							selCellCoords[1] ], cellValue);
					cellValue = Jedox.wss.general
							.filterHLTags(selCellCoords[0], selCellCoords[1],
									cellValue, true)
				}
			}
			env.selectedCellValue = cellValue;
			activeBook.setCellValue(selCellCoords[0], selCellCoords[1],
					cellValue)
		} else {
			activeBook.setArrayFormula(env.defaultSelection.getActiveRange()
					.getCoords(), cellValue);
			var selCellValue = activeBook.getCellValue(selCellCoords[0],
					selCellCoords[1]);
			env.selectedCellValue = (selCellValue == undefined) ? ""
					: selCellValue;
			var selCellFormula = activeBook.getCellFormula(selCellCoords[0],
					selCellCoords[1]);
			env.selectedCellFormula = (selCellFormula == undefined) ? ""
					: selCellFormula;
			Jedox.wss.app.currFormula.value = env.selectedCellFormula
		}
	}
	Jedox.wss.keyboard.cancelInput();
	return true
};
Jedox.wss.keyboard.cancelInput = function(delMarkRng) {
	var env = Jedox.wss.app.environment;
	var inputField = env.inputField;
	var isUserMode = (env.viewMode == Jedox.wss.grid.viewMode.USER);
	env.editingDirectly = false;
	if (!isUserMode) {
		var selCellCoords = env.selectedCellCoords;
		Jedox.wss.app.currFormula.value = ((env.selectedCellFormula != "") && (env.selectedCellFormula != "null")) ? env.selectedCellFormula
				: Jedox.wss.general.filterHLTags(selCellCoords[0],
						selCellCoords[1], env.selectedCellValue, false)
	}
	Jedox.wss.general.setInputMode(Jedox.wss.grid.GridMode.READY);
	inputField.style.zIndex = "37";
	inputField.style.display = "none";
	env.lastInputValue = "";
	env.activeNewArea = false;
	if (!isUserMode) {
		env.cursorField.style.visibility = "";
		env.formulaSelection.removeAll();
		if (delMarkRng == undefined || delMarkRng) {
			env.copySelection.removeAll();
			if (Jedox.wss.app.clipboard != null
					&& Jedox.wss.app.clipboard.id != null) {
				Jedox.wss.app.clipboard = null;
				Jedox.wss.action.togglePaste(false)
			}
		}
	} else {
		Jedox.wss.app.clipboard = null
	}
};
Jedox.wss.keyboard.shiftDirection = function(direction) {
	switch (direction) {
	case Jedox.wss.grid.ScrollDirection.DOWN:
		direction = Jedox.wss.grid.ScrollDirection.UP;
		break;
	case Jedox.wss.grid.ScrollDirection.UP:
		direction = Jedox.wss.grid.ScrollDirection.DOWN;
		break;
	case Jedox.wss.grid.ScrollDirection.LEFT:
		direction = Jedox.wss.grid.ScrollDirection.RIGHT;
		break;
	case Jedox.wss.grid.ScrollDirection.RIGHT:
		direction = Jedox.wss.grid.ScrollDirection.LEFT;
		break
	}
	return direction
};
Jedox.wss.keyboard.preventKeyEvent = function(myKeyEvent) {
	if (Jedox.wss.app.environment.inputMode != Jedox.wss.grid.GridMode.EDIT) {
		if (document.all) {
			window.event.returnValue = false;
			window.event.cancelBubble = true
		} else {
			myKeyEvent.preventDefault()
		}
	}
	return false
};
Jedox.wss.keyboard.handleCursorKey = function(keyEvent) {
	var keyPressed = ((document.all) ? keyEvent.keyCode : keyEvent.which);
	var amount = 1;
	switch (keyPressed) {
	case 37:
		if (Jedox.wss.app.environment.selectedAbsRowNameNumeric > 1) {
			Jedox.wss.keyboard.moveCursor(Jedox.wss.grid.ScrollDirection.LEFT,
					keyEvent.shiftKey, amount, keyPressed)
		}
		break;
	case 33:
	case 38:
		if (Jedox.wss.app.environment.selectedAbsColumnName > 1) {
			Jedox.wss.keyboard.moveCursor(Jedox.wss.grid.ScrollDirection.UP,
					keyEvent.shiftKey, amount, keyPressed)
		}
		break;
	case 39:
		if (Jedox.wss.app.environment.selectedAbsRowNameNumeric < Jedox.wss.grid.defMaxCoords[0]) {
			Jedox.wss.keyboard.moveCursor(Jedox.wss.grid.ScrollDirection.RIGHT,
					keyEvent.shiftKey, amount, keyPressed)
		}
		break;
	case 34:
	case 40:
		if (Jedox.wss.app.environment.selectedAbsColumnName < Jedox.wss.grid.defMaxCoords[1]) {
			Jedox.wss.keyboard.moveCursor(Jedox.wss.grid.ScrollDirection.DOWN,
					keyEvent.shiftKey, amount, keyPressed)
		}
		break
	}
};
Jedox.wss.keyboard.isCursorkey = function(keyCode) {
	return ((keyCode >= 37) && (keyCode <= 40))
};
Jedox.wss.keyboard.moveCursor = function(direction, shiftKey, amount, keyCode) {
	if (Jedox.wss.app.environment.viewMode == Jedox.wss.grid.viewMode.USER) {
		return
	}
	var range = (Jedox.wss.app.environment.activeNewArea) ? Jedox.wss.app.environment.formulaSelection
			: Jedox.wss.app.environment.defaultSelection, activeBook = Jedox.wss.app.activeBook, activeCell = range
			.getActiveRange().getActiveCell(), lastActiveCell = range
			.getActiveRange().getLastActiveCell(), coords = activeCell.clone(), lastCell = activeCell
			.clone(), isCursorKey = Jedox.wss.keyboard.isCursorkey(keyCode), i = 0, activeSheet = Jedox.wss.app.activeSheet;
	if (!Jedox.wss.app.environment.defaultSelection.isSingleCell()
			&& !(keyCode == 13 || keyCode == 9) && !shiftKey) {
		range.set(activeCell, activeCell)
	}
	if (shiftKey) {
		if (isCursorKey) {
			coords = lastActiveCell.clone()
		} else {
			direction = Jedox.wss.keyboard.shiftDirection(direction)
		}
	}
	var rangeSingleCell = range.isSingleCell(), chkHiddenColRow = Jedox.wss.general.chkHiddenColRow;
	switch (direction) {
	case Jedox.wss.grid.ScrollDirection.DOWN:
		if (rangeSingleCell || (shiftKey && isCursorKey)) {
			var newYCoord = chkHiddenColRow(true, coords.getY(), amount, true), newXCoord = chkHiddenColRow(
					false, coords.getX(), 0, true), mergeInfoSrc = activeBook
					.getMergeInfo(coords.getX(), coords.getY()), mergeInfo = activeBook
					.getMergeInfo(newXCoord, newYCoord);
			if (!mergeInfoSrc) {
				range.setRefActiveCell(coords)
			}
			if (mergeInfo && !mergeInfo[0]) {
				var mergeInfoMa = mergeInfoSrc && mergeInfoSrc[0] ? mergeInfoSrc
						: activeBook.getMergeInfo(mergeInfo[1], mergeInfo[2]);
				if (mergeInfoMa && mergeInfoMa[0]) {
					if (mergeInfoSrc && mergeInfoSrc[0]) {
						if (rangeSingleCell) {
							newYCoord = mergeInfo[2] + mergeInfoMa[2]
						}
						if (!shiftKey) {
							var refCell = range.getRefActiveCell();
							if (refCell
									&& refCell.getX() >= mergeInfo[1]
									&& refCell.getX() <= (mergeInfo[1]
											+ mergeInfoMa[1] - 1)) {
								newXCoord = refCell.getX()
							}
							var mergeInfoShiftCell = activeBook.getMergeInfo(
									newXCoord, newYCoord);
							if (mergeInfoShiftCell && !mergeInfoShiftCell[0]) {
								newXCoord = mergeInfoShiftCell[1];
								newYCoord = mergeInfoShiftCell[2]
							}
						}
					} else {
						newXCoord = mergeInfo[1];
						newYCoord = mergeInfo[2]
					}
				}
			}
			if (newXCoord <= Jedox.wss.grid.defMaxCoords[0]
					&& newYCoord <= Jedox.wss.grid.defMaxCoords[1]) {
				coords.setX(newXCoord);
				coords.setY(newYCoord)
			}
		} else {
			for (i = 0; i < amount; i++) {
				range.nextY()
			}
		}
		break;
	case Jedox.wss.grid.ScrollDirection.UP:
		if (rangeSingleCell || (shiftKey && isCursorKey)) {
			var newYCoord = chkHiddenColRow(true, coords.getY(), amount, false), newXCoord = chkHiddenColRow(
					false, coords.getX(), 0, false), mergeInfoSrc = activeBook
					.getMergeInfo(coords.getX(), coords.getY()), mergeInfo = activeBook
					.getMergeInfo(newXCoord, newYCoord);
			if (!mergeInfoSrc) {
				range.setRefActiveCell(coords)
			}
			if (!shiftKey || (shiftKey && !isCursorKey)) {
				if (!mergeInfo) {
					if (mergeInfoSrc && mergeInfoSrc[0]) {
						var refCell = range.getRefActiveCell();
						if (refCell
								&& refCell.getX() >= coords.getX()
								&& refCell.getX() <= (coords.getX()
										+ mergeInfoSrc[1] - 1)) {
							newXCoord = refCell.getX()
						}
						var mergeInfoShiftCell = activeBook.getMergeInfo(
								newXCoord, newYCoord);
						if (mergeInfoShiftCell && !mergeInfoShiftCell[0]) {
							newXCoord = mergeInfoShiftCell[1];
							newYCoord = mergeInfoShiftCell[2]
						}
					}
				} else {
					if (!mergeInfo[0]) {
						newXCoord = mergeInfo[1];
						newYCoord = mergeInfo[2]
					}
				}
			}
			if (newXCoord >= 1 && newYCoord >= 1) {
				coords.setX(newXCoord);
				coords.setY(newYCoord)
			}
		} else {
			for (i = 0; i < amount; i++) {
				range.prevY()
			}
		}
		break;
	case Jedox.wss.grid.ScrollDirection.RIGHT:
		if (rangeSingleCell || (shiftKey && isCursorKey)) {
			var newYCoord = chkHiddenColRow(true, coords.getY(), 0, true), newXCoord = chkHiddenColRow(
					false, coords.getX(), amount, true), mergeInfoSrc = activeBook
					.getMergeInfo(coords.getX(), coords.getY()), mergeInfo = activeBook
					.getMergeInfo(newXCoord, newYCoord);
			if (!mergeInfoSrc) {
				range.setRefActiveCell(coords)
			}
			if (mergeInfo && !mergeInfo[0]) {
				var mergeInfoMa = mergeInfoSrc && mergeInfoSrc[0] ? mergeInfoSrc
						: activeBook.getMergeInfo(mergeInfo[1], mergeInfo[2]);
				if (mergeInfoMa && mergeInfoMa[0]) {
					if (mergeInfoSrc && mergeInfoSrc[0]) {
						if (rangeSingleCell) {
							newXCoord = mergeInfo[1] + mergeInfoMa[1]
						}
						if (!shiftKey) {
							var refCell = range.getRefActiveCell();
							if (refCell
									&& refCell.getY() >= mergeInfo[2]
									&& refCell.getY() <= (mergeInfo[2]
											+ mergeInfoMa[2] - 1)) {
								newYCoord = refCell.getY()
							}
							var mergeInfoShiftCell = activeBook.getMergeInfo(
									newXCoord, newYCoord);
							if (mergeInfoShiftCell && !mergeInfoShiftCell[0]) {
								newXCoord = mergeInfoShiftCell[1];
								newYCoord = mergeInfoShiftCell[2]
							}
						}
					} else {
						newXCoord = mergeInfo[1];
						if (!shiftKey) {
							newYCoord = mergeInfo[2]
						}
					}
				}
			}
			if (newXCoord <= Jedox.wss.grid.defMaxCoords[0]
					&& newYCoord <= Jedox.wss.grid.defMaxCoords[1]) {
				coords.setX(newXCoord);
				coords.setY(newYCoord)
			}
		} else {
			for (i = 0; i < amount; i++) {
				range.next()
			}
		}
		break;
	case Jedox.wss.grid.ScrollDirection.LEFT:
		if (rangeSingleCell || (shiftKey && isCursorKey)) {
			var newYCoord = chkHiddenColRow(true, coords.getY(), 0, false), newXCoord = chkHiddenColRow(
					false, coords.getX(), amount, false), mergeInfoSrc = activeBook
					.getMergeInfo(coords.getX(), coords.getY()), mergeInfo = activeBook
					.getMergeInfo(newXCoord, newYCoord);
			if (!mergeInfoSrc) {
				range.setRefActiveCell(coords)
			}
			if (!shiftKey || (shiftKey && !isCursorKey)) {
				if (!mergeInfo) {
					if (mergeInfoSrc && mergeInfoSrc[0]) {
						var refCell = range.getRefActiveCell();
						if (refCell != undefined
								&& refCell.getY() >= coords.getY()
								&& refCell.getY() <= (coords.getY()
										+ mergeInfoSrc[2] - 1)) {
							newYCoord = refCell.getY()
						}
						var mergeInfoShiftCell = activeBook.getMergeInfo(
								newXCoord, newYCoord);
						if (mergeInfoShiftCell && !mergeInfoShiftCell[0]) {
							newXCoord = mergeInfoShiftCell[1];
							newYCoord = mergeInfoShiftCell[2]
						}
					}
				} else {
					if (!mergeInfo[0]) {
						newXCoord = mergeInfo[1];
						newYCoord = mergeInfo[2]
					}
				}
			}
			if (newXCoord >= 1 && newYCoord >= 1) {
				coords.setX(newXCoord);
				coords.setY(newYCoord)
			}
		} else {
			for (i = 0; i < amount; i++) {
				range.prev()
			}
		}
		break;
	default:
		console.error("Invalid cursor direction!");
		return
	}
	if ((rangeSingleCell || (shiftKey && isCursorKey))
			&& !activeBook.isCellVisible(coords.getX(), coords.getY())
			&& activeBook._scrollOpPending) {
		return
	}
	if (shiftKey && isCursorKey) {
		range.expandToCell(coords, false)
	} else {
		if (rangeSingleCell) {
			range.set(coords, coords)
		} else {
			range._setLegacyVars()
		}
	}
	var scrollMap = (activeBook.getScrollDirMap())[direction], cbScrollTo = function() {
		range.draw()
	};
	if ((rangeSingleCell || (shiftKey && isCursorKey))
			&& !activeBook.isCellVisible(coords.getX(), coords.getY())) {
		activeBook.scrollTo( [ this, cbScrollTo ], coords.getX(),
				coords.getY(), true, false)
	} else {
		range.draw()
	}
};
Jedox.wss.keyboard.preventEvent = function(ev) {
	if (Jedox.wss.app.environment.inputMode != Jedox.wss.grid.GridMode.EDIT) {
		if (document.all) {
			window.event.returnValue = false;
			window.event.cancelBubble = true
		} else {
			ev.preventDefault();
			ev.stopPropagation()
		}
	}
	return false
};
Jedox.wss.keyboard.focusInput = function(currX, currY, direction) {
	var activeBook = Jedox.wss.app.activeBook;
	var iterMode = Jedox.wss.grid.IterationMode;
	var realRng = activeBook.getRealGridRange();
	function iterForHor(currX, currY) {
		var x = currX + 1, y = currY;
		for (; !(x == currX && y == currY); ((++y > realRng[1][1]) ? y = realRng[0][1]
				: y), x = realRng[0][0]) {
			for (; x <= realRng[1][0]; ++x) {
				if (!activeBook.isCellLocked(x, y)) {
					Jedox.wss.mouse.mimicCellMouseEvent(x, y, "mousedown");
					return
				}
			}
		}
	}
	function iterBackHor(currX, currY) {
		var x = currX - 1, y = currY;
		for (; !(x == currX && y == currY); ((--y < realRng[0][1]) ? y = realRng[1][1]
				: y), x = realRng[1][0]) {
			for (; x >= realRng[0][0]; --x) {
				if (!activeBook.isCellLocked(x, y)) {
					Jedox.wss.mouse.mimicCellMouseEvent(x, y, "mousedown");
					return
				}
			}
		}
	}
	function iterForVert(currX, currY) {
		var x = currX, y = currY + 1;
		for (; !(x == currX && y == currY); ((++x > realRng[1][0]) ? x = realRng[0][0]
				: x), y = realRng[0][1]) {
			for (; y <= realRng[1][1]; ++y) {
				if (!activeBook.isCellLocked(x, y)) {
					Jedox.wss.mouse.mimicCellMouseEvent(x, y, "mousedown");
					return
				}
			}
		}
	}
	function iterBackVert(currX, currY) {
		var x = currX, y = currY - 1;
		for (; !(x == currX && y == currY); ((--x < realRng[0][0]) ? x = realRng[1][0]
				: x), y = realRng[1][1]) {
			for (; y >= realRng[0][1]; --y) {
				if (!activeBook.isCellLocked(x, y)) {
					Jedox.wss.mouse.mimicCellMouseEvent(x, y, "mousedown");
					return
				}
			}
		}
	}
	switch (direction) {
	case iterMode.NEXTX:
		iterForHor(currX, currY);
		break;
	case iterMode.PREVX:
		iterBackHor(currX, currY);
		break;
	case iterMode.NEXTY:
		iterForVert(currX, currY);
		break;
	case iterMode.PREVY:
		iterBackVert(currX, currY);
		break;
	case iterMode.FIRST:
		iterForHor(realRng[1][0], realRng[1][1]);
		break;
	case iterMode.LAST:
		iterBackHor(realRng[0][0], realRng[0][1]);
		break
	}
};
Jedox.wss.keyboard.handleUMFocus = function(keyCode, shiftKey) {
	var selCellCoords = Jedox.wss.app.environment.selectedCellCoords;
	if (selCellCoords.length == 0) {
		selCellCoords = [ 1, 1 ]
	}
	switch (keyCode) {
	case 9:
		Jedox.wss.keyboard.focusInput(selCellCoords[0], selCellCoords[1],
				shiftKey ? Jedox.wss.grid.IterationMode.PREVX
						: Jedox.wss.grid.IterationMode.NEXTX);
		break;
	case 13:
		Jedox.wss.keyboard.focusInput(selCellCoords[0], selCellCoords[1],
				shiftKey ? Jedox.wss.grid.IterationMode.PREVY
						: Jedox.wss.grid.IterationMode.NEXTY);
		break;
	case 39:
		Jedox.wss.keyboard.focusInput(selCellCoords[0], selCellCoords[1],
				Jedox.wss.grid.IterationMode.NEXTX);
		break;
	case 37:
		Jedox.wss.keyboard.focusInput(selCellCoords[0], selCellCoords[1],
				Jedox.wss.grid.IterationMode.PREVX);
		break;
	case 40:
		Jedox.wss.keyboard.focusInput(selCellCoords[0], selCellCoords[1],
				Jedox.wss.grid.IterationMode.NEXTY);
		break;
	case 38:
		Jedox.wss.keyboard.focusInput(selCellCoords[0], selCellCoords[1],
				Jedox.wss.grid.IterationMode.PREVY);
		break
	}
};
Jedox.wss.keyboard.fetchGlobalKeyUp = function(ev) {
	if (document.all) {
		ev = window.event
	}
	if (!ev.ctrlKey) {
		Jedox.wss.app.ctrlKeyPressed = false;
		var env = Jedox.wss.app.environment;
		var mousePos = env.mousePosition;
		if (mousePos == "rngBorder" || mousePos == "magicDot") {
			env.defaultSelection.setCursor(mousePos,
					(mousePos == "rngBorder") ? "rng_move" : "md_curr")
		}
	}
};
Jedox.wss.keyboard.genKbdEvent = function(keyCode, ctrlKey, altKey, shiftKey,
		metaKey) {
	if (window.KeyEvent) {
		var evObj = document.createEvent("KeyEvents");
		evObj.initKeyEvent("keydown", true, true, window, ctrlKey, altKey,
				shiftKey, metaKey, keyCode, 0);
		fireOnThis.dispatchEvent(evObj)
	} else {
		var evObj = document.createEventObject();
		evObj.ctrlKey = true;
		evObj.keyCode = keyCode;
		document.fireEvent("onkeydown", evObj)
	}
};