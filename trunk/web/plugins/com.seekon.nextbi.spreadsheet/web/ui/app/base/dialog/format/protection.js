Jedox.wss.dialog.format.protection = function(callback, _pre, isFromOther) {
	var selCoord = Jedox.wss.app.environment.selectedCellCoords;
	var env = Jedox.wss.app.environment, rngStartCoord = env.lastRangeStartCoord, rngEndCoord = env.lastRangeEndCoord;
	if (!isFromOther) {
		var inputCheckBoxValue = Jedox.wss.app.activeBook.isCellLocked(
				selCoord[0], selCoord[1])
	} else {
		var inputCheckBoxValue = _pre
	}
	var isLockedCB = new Ext.form.Checkbox( {
		hideLabel : true,
		name : "locked",
		checked : inputCheckBoxValue,
		boxLabel : "Locked".localize()
	});
	var protectionTab = new Ext.Panel(
			{
				baseCls : "x-title-f",
				labelWidth : 100,
				labelAlign : "left",
				frame : false,
				bodyStyle : "padding: 10px; color: #000000; font-size: 9pt; background-color: transparent;",
				header : false,
				monitorValid : true,
				autoHeight : true,
				autoWidth : true,
				listeners : {
					doLock : function(callback) {
						var outputCheckBoxValue = isLockedCB.getValue();
						if (!isFromOther) {
							if (inputCheckBoxValue != outputCheckBoxValue) {
								Jedox.wss.app.activeBook.setRangeLock( [
										rngStartCoord[0], rngStartCoord[1],
										rngEndCoord[0], rngEndCoord[1] ],
										outputCheckBoxValue)
							}
						} else {
							callback(outputCheckBoxValue)
						}
					}
				},
				items : [
						isLockedCB,
						new Ext.form.Checkbox( {
							hideLabel : true,
							name : "hidden",
							disabled : true,
							boxLabel : "Hidden".localize()
						}),
						{
							html : "Locking cells or hiding formulas has no effect until you protect the worksheet."
									.localize(),
							baseCls : "x-plain"
						} ]
			});
	callback(protectionTab)
};