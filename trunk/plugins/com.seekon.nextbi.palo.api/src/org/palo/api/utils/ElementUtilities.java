/*    */package org.palo.api.utils;

/*    */
/*    */import java.util.ArrayList; /*    */
import java.util.List; /*    */
import org.palo.api.Element;

/*    */
/*    */public class ElementUtilities
/*    */{
  /*    */public static final String getPaths(Element element)
  /*    */{
    /* 65 */ArrayList paths = new ArrayList();
    /* 66 */collectPaths(element, element.getId(), paths);
    /* 67 */StringBuffer pathsStr = new StringBuffer();
    /* 68 */int lastPath = paths.size() - 1;
    /* 69 */int i = 0;
    for (int n = paths.size(); i < n; ++i) {
      /* 70 */pathsStr.append(paths.get(i));
      /* 71 */if (i < lastPath)
        /* 72 */pathsStr.append(":");
      /*    */}
    /* 74 */return pathsStr.toString();
    /*    */}

  /*    */
  /*    */private static final void collectPaths(Element element, String part,
    List paths)
  /*    */{
    /* 79 */Element[] parents = element.getParents();
    /* 80 */if ((parents == null) || (parents.length == 0)) {
      /* 81 */paths.add(part);
      /* 82 */return;
      /*    */}
    /*    */
    /* 85 */for (int i = 0; i < parents.length; ++i) {
      /* 86 */StringBuffer newPath = new StringBuffer(part);
      /* 87 */newPath.insert(0, ",");
      /* 88 */newPath.insert(0, parents[i].getId());
      /* 89 */collectPaths(parents[i], newPath.toString(), paths);
      /*    */}
    /*    */}
  /*    */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.utils.ElementUtilities JD-Core Version: 0.5.4
 */