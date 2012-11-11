Jedox.studio.app.initModules = function() {
	var navigation = new Jedox.studio.app.navigation();
	var content = new Jedox.studio.app.content();
	var modules = [], navigationItems = [], contentItems = [], btnItems = [];
	var inits = Jedox.studio.app.modules;
	for (i in inits) {
		modules.push(inits[i]())
	}
	modules.sort(function(module1, module2) {
		return module1.order - module2.order
	});
	for ( var i = 0; i < modules.length; i++) {
		navigationItems.push(modules[i].navigation);
		btnItems.push(modules[i].btn);
		contentItems.push(modules[i].content)
	}
	navigation.addCmp(navigationItems);
	content.addBtn(btnItems);
	content.addCmp(contentItems);
	return [ navigation, content ]
};