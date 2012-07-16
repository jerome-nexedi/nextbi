Jedox.wss.app.updateUndoState = function(undoState, updState) {
};
Jedox.wss.app.hideBar = function(barId) {
	var bar = Ext.get(barId);
	bar.setVisibilityMode(Ext.Element.DISPLAY);
	bar.hide();
	Jedox.wss.workspace.resize();
	Jedox.wss.workspace.resizeMaxWindows()
};
Jedox.wss.app.showBar = function(barId) {
	var bar = Ext.get(barId);
	bar.setVisibilityMode(Ext.Element.DISPLAY);
	bar.show();
	Jedox.wss.workspace.resize();
	Jedox.wss.workspace.resizeMaxWindows()
};
Jedox.wss.app.hideToolbar = function(toolbar) {
	Jedox.wss.app.hideBar("Toolbar")
};
Jedox.wss.app.showToolbar = function(toolbar) {
	Jedox.wss.app.showBar("Toolbar")
};
Jedox.wss.app.hideShowToolbar = function(state, toolbar) {
	if (state) {
		Jedox.wss.app.showToolbar(toolbar)
	} else {
		Jedox.wss.app.hideToolbar(toolbar)
	}
};