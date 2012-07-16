/*
// $Id: //open/mondrian/src/main/mondrian/util/UtilCompatibleJdk16.java#1 $
// This software is subject to the terms of the Eclipse Public License v1.0
// Agreement, available at the following URL:
// http://www.eclipse.org/legal/epl-v10.html.
// Copyright (C) 2011-2011 Julian Hyde
// All Rights Reserved.
// You must accept the terms of that agreement to use this software.
 */
package mondrian.util;

import mondrian.olap.Util;

import javax.script.*;

// Only in Java6 and above

/**
 * Implementation of {@link mondrian.util.UtilCompatible} that runs in JDK 1.6.
 * 
 * <p>
 * Prior to JDK 1.6, this class should never be loaded. Applications should
 * instantiate this class via {@link Class#forName(String)} or better, use
 * methods in {@link mondrian.olap.Util}, and not instantiate it at all.
 * 
 * @author jhyde
 * @version $Id:
 *          //open/mondrian/src/main/mondrian/util/UtilCompatibleJdk16.java#1 $
 */
public class UtilCompatibleJdk16 extends UtilCompatibleJdk15 {
	public <T> T compileScript(Class<T> iface, String script, String engineName) {
		ScriptEngineManager factory = new ScriptEngineManager();
		ScriptEngine engine = factory.getEngineByName(engineName);
		try {
			engine.eval(script);
			Invocable inv = (Invocable) engine;
			return inv.getInterface(iface);
		} catch (ScriptException e) {
			throw Util.newError(e, "Error while compiling script to implement "
					+ iface + " SPI");
		}
	}
}

// End UtilCompatibleJdk16.java
