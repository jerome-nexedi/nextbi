if (Jedox) {
	if (Jedox.wss) {
		if (Jedox.wss.palo) {
			Jedox.wss.palo.NONE = 0;
			Jedox.wss.palo.GRID = 1;
			Jedox.wss.palo.STUDIO = 2
		} else {
			Jedox.wss.palo = {
				NONE : 0,
				GRID : 1,
				STUDIO : 2
			}
		}
		if (Jedox.studio) {
			Jedox.wss.palo.workIn = 2
		} else {
			Jedox.wss.palo.workIn = 1
		}
	} else {
		Jedox.wss = {
			palo : {
				NONE : 0,
				GRID : 1,
				STUDIO : 2,
				workIn : 0
			}
		}
	}
} else {
	Jedox = {
		wss : {
			palo : {
				NONE : 0,
				GRID : 1,
				STUDIO : 2,
				workIn : 0
			}
		}
	}
}
Jedox.wss.palo.utils = new function() {
	if (Jedox.wss.palo.workIn == Jedox.wss.palo.GRID) {
		var gridModes = Jedox.wss.grid.viewMode;
		this.registerHandlers = function() {
			switch (Jedox.wss.app.appMode) {
			case gridModes.DESIGNER:
				Jedox.wss.grid.cbReg("hnd_dblCpvOpen", [ this,
						Jedox.wss.palo.utils.handleOpenPasteViewDblclick ]);
			case gridModes.USER:
				Jedox.wss.grid.cbReg("hnd_dblCpv", [ this,
						Jedox.wss.palo.utils.handlePasteViewDblclick ]);
				Jedox.wss.grid
						.cbReg(
								"hnd_dblCceOpen",
								[
										this,
										Jedox.wss.palo.utils.handleOpenPasteViewChooseElement ])
			}
		};
		this.handlePasteViewDblclick = function(inObj, relX, relY, pasteViewId,
				dimName, elemName) {
			Jedox.wss.app.activeBook.cb("palo_handlerExpandCollapsePasteView",
					[ [ inObj, relX, relY, pasteViewId, dimName, elemName ] ]);
			return false
		};
		this.handleOpenPasteViewDblclick = function(inObj, pasteViewId, x, y) {
			Jedox.wss.app.load(Jedox.wss.app.dynJSRegistry.pasteView, [
					pasteViewId, x, y ]);
			return false
		};
		this.handleOpenPasteViewChooseElement = function(inObj, confData) {
			confData.dblclick_data = inObj;
			Jedox.wss.app.load(Jedox.wss.app.dynJSRegistry.chooseElement,
					[ confData ]);
			return false
		};
		this.openSubsetEditor = function(conf) {
			Jedox.wss.app.load(Jedox.wss.app.dynJSRegistry.subsetEditor,
					[ conf ])
		}
	}
	this.OnEnterDataViewClass = Ext
			.extend(
					Ext.DataView,
					{
						focusedClass : "x-view-focused",
						focusEl : true,
						afterRender : function() {
							Jedox.wss.palo.utils.OnEnterDataViewClass.superclass.afterRender
									.call(this);
							var that = this;
							if (this.singleSelect || this.multiSelect) {
								if (this.focusEl === true) {
									this.focusEl = this.el.parent().parent()
											.createChild( {
												tag : "a",
												href : "#",
												cls : "x-view-focus",
												tabIndex : "-1"
											});
									this.focusEl.insertBefore(this.el.parent());
									this.focusEl.swallowEvent("click", true);
									this.renderedFocusEl = true
								} else {
									if (this.focusEl) {
										this.focusEl = Ext.get(this.focusEl)
									}
								}
								if (this.focusEl) {
									this.keyNav = new Ext.KeyNav(
											this.focusEl,
											{
												up : function(e) {
													var selection = that
															.getSelectedIndexes();
													if (selection.length > 0) {
														var lastIndex = selection[selection.length - 1];
														if (lastIndex <= selection[0]) {
															if (lastIndex > 0) {
																if (that.multiSelect
																		&& e.shiftKey) {
																	that
																			.select(
																					lastIndex - 1,
																					true,
																					false)
																} else {
																	that
																			.select(lastIndex - 1)
																}
															}
														} else {
															that
																	.deselect(lastIndex)
														}
													}
												},
												down : function(e) {
													var selection = that
															.getSelectedIndexes();
													if (selection.length > 0) {
														var lastIndex = selection[selection.length - 1];
														if (lastIndex >= selection[0]) {
															if (lastIndex < (that
																	.getStore()
																	.getCount() - 1)) {
																if (that.multiSelect
																		&& e.shiftKey) {
																	that
																			.select(
																					lastIndex + 1,
																					true,
																					false)
																} else {
																	that
																			.select(lastIndex + 1)
																}
															}
														} else {
															that
																	.deselect(lastIndex)
														}
													}
												},
												enter : function() {
													if (that.events.onkeyenter) {
														that.events.onkeyenter
																.fire()
													}
												},
												scope : this,
												forceKeyDown : true
											})
								}
							}
						},
						onClick : function(e) {
							var item = e.getTarget(this.itemSelector, this.el);
							if (item) {
								var index = this.indexOf(item);
								if (this.onItemClick(item, index, e) !== false) {
									this.fireEvent("click", this, index, item,
											e);
									this.retainFocus()
								}
							} else {
								if (this.fireEvent("containerclick", this, e) !== false) {
									this.clearSelections();
									this.retainFocus()
								}
							}
						},
						retainFocus : function() {
							var that = this;
							if (this.focusEl) {
								setTimeout(function() {
									that.focusEl.focus()
								}, 0)
							}
						},
						doRetainFocus : function() {
							this.focusEl.focus()
						}
					});
	this.checkPaloName = function(name, type) {
		switch (type) {
		case "database":
		case "db":
			var res = name.match(/^[a-zA-Z0-9_-][a-zA-Z0-9\._-]*/);
			return ((res != null) && (res == name));
			break;
		case "dimension":
		case "dim":
		case "cube":
			var res = name
					.match(/^[^\x00-\x1F\\*:|<>\/?\.\s][^\x00-\x1F\\*:|<>\/?]*/);
			if ((res != null) && (res == name)) {
				res = name
						.match(/[^\x00-\x1F\\*:|<>\/?]*[^\x00-\x1F\\*:|<>\/?\s]$/)
			} else {
				return false
			}
			return ((res != null) && (res == name));
			break;
		case "element":
		case "elem":
			var res = name.match(/^[^\x00-\x1F\s][^\x00-\x1F]*/);
			if ((res != null) && (res == name)) {
				res = name.match(/[^\x00-\x1F]*[^\x00-\x1F\s]$/)
			} else {
				return false
			}
			return ((res != null) && (res == name));
			break
		}
		return false
	}
};
Jedox.wss.palo.config = {
	panelW : 800,
	panelH : 400,
	widthMidButtons : 38,
	imgsPath : "/ui/common/res/img/palo/",
	elementSize : 18,
	numberOfElements : 10000,
	cubeWizWinW : 750,
	cubeWizWinH : 432,
	pvWinW : 750,
	pvWinH : 500,
	fixWidth : 12.57,
	ceWinW : 350,
	ceWinH : 450,
	seWinW : 750,
	seWinH : 500,
	pdfWinW : 350,
	pdfWinH : 450,
	paloWizWinW : 750,
	paloWizWinH : 432,
	sseWinW : 900,
	sseWinH : 550
};