/*
// $Id: //open/mondrian/src/main/mondrian/server/RepositoryContentFinder.java#2 $
// This software is subject to the terms of the Eclipse Public License v1.0
// Agreement, available at the following URL:
// http://www.eclipse.org/legal/epl-v10.html.
// Copyright (C) 2010-2011 Julian Hyde
// All Rights Reserved.
// You must accept the terms of that agreement to use this software.
 */
package mondrian.server;

/**
 * Callback to get the content of the repository as an XML string.
 * 
 * <p>
 * Various implementations might use caching or storage in media other than a
 * file system.
 * 
 * @version $Id:
 *          //open/mondrian/src/main/mondrian/server/RepositoryContentFinder
 *          .java#2 $
 * @author Julian Hyde
 */
public interface RepositoryContentFinder {
  String getContent();

  void shutdown();
}

// End RepositoryContentFinder.java
