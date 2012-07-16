Jedox.wss.events = {
	WSSPlugin : new Jedox.util.Interface("WSSPlugin", [ "openWorkbook_before",
			"openWorkbook_after", "closeWorkbook_before",
			"closeWorkbook_after", "switchWorkbook_before",
			"switchWorkbook_after", "saveAsWorkbook_before",
			"saveAsWorkbook_after", "newWorkbook_before", "newWorkbook_after",
			"hideWorkbook_before", "hideWorkbook_after",
			"unhideWorkbook_before", "unhideWorkbook_after",
			"importWorkbook_before", "importWorkbook_after", "openURL",
			"openOther" ]),
	triggers : {
		openWorkbook_before : [],
		openWorkbook_after : [],
		closeWorkbook_before : [],
		closeWorkbook_after : [],
		switchWorkbook_before : [],
		switchWorkbook_after : [],
		saveAsWorkbook_before : [],
		saveAsWorkbook_after : [],
		newWorkbook_before : [],
		newWorkbook_after : [],
		hideWorkbook_before : [],
		hideWorkbook_after : [],
		unhideWorkbook_before : [],
		unhideWorkbook_after : [],
		importWorkbook_before : [],
		importWorkbook_after : [],
		openURL : [],
		openOther : []
	},
	registerPlugin : function(plugin) {
		try {
			Jedox.util.Interface.ensureImplements(plugin, this.WSSPlugin);
			var events = plugin.getTriggerInfo();
			for ( var event in events) {
				if (this.triggers[event] != undefined) {
					this.triggers[event].push( [ plugin, events[event] ])
				}
			}
			return true
		} catch (e) {
			console.log(e);
			return false
		}
	}
};