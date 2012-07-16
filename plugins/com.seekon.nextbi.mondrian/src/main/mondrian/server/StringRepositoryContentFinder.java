/*
// $Id: //open/mondrian/src/main/mondrian/server/StringRepositoryContentFinder.java#2 $
// This software is subject to the terms of the Eclipse Public License v1.0
// Agreement, available at the following URL:
// http://www.eclipse.org/legal/epl-v10.html.
// Copyright (C) 2010-2011 Julian Hyde
// All Rights Reserved.
// You must accept the terms of that agreement to use this software.
 */
package mondrian.server;

/**
 * Implementation of {@link mondrian.server.RepositoryContentFinder} that always
 * returns a constant string.
 * 
 * @version $Id:
 *          //open/mondrian/src/main/mondrian/server/StringRepositoryContentFinder
 *          .java#2 $
 * @author Julian Hyde
 */
public class StringRepositoryContentFinder implements RepositoryContentFinder {
	private final String content;

	public StringRepositoryContentFinder(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public void shutdown() {
		// nothing to do
	}
}

// End StringRepositoryContentFinder.java
