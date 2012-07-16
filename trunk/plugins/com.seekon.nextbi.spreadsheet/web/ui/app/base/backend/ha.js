Jedox.wss.backend.ha = function(stub) {
	if (typeof stub != "function") {
		return Jedox.wss.backend.ha = undefined
	}
	var HA = function() {
		HA.parent.constructor.call(this);
		this.loadWorkbook = function(id, group, hierarchy, ext) {
			return this.load_workbook(id, group == undefined ? null : group,
					hierarchy == undefined ? null : hierarchy,
					ext == undefined ? null : ext)
		};
		this.saveWorkbook = function(group, hierarchy, name, parentId) {
			return this.save_workbook(group, hierarchy, name,
					parentId == undefined ? null : parentId)
		}
	};
	Jedox.util.extend(HA, stub);
	return Jedox.wss.backend.ha = new HA()
};