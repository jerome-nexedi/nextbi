/*     */package com.tensegrity.palojava.http.loader;

/*     */
/*     */import com.tensegrity.palojava.DbConnection; /*     */
import com.tensegrity.palojava.ElementInfo; /*     */
import com.tensegrity.palojava.HierarchyInfo; /*     */
import com.tensegrity.palojava.PaloInfo; /*     */
import com.tensegrity.palojava.loader.ElementLoader; /*     */
import java.util.ArrayList; /*     */
import java.util.Collection;

/*     */
/*     */public class HttpElementLoader extends ElementLoader
/*     */{
  /*     */public HttpElementLoader(DbConnection paloConnection,
    HierarchyInfo hierarchy)
  /*     */{
    /* 58 */super(paloConnection, hierarchy);
    /*     */}

  /*     */
  /*     */public String[] getAllElementIds() {
    /* 62 */if (!this.loaded) {
      /* 63 */reload();
      /* 64 */this.loaded = true;
      /*     */}
    /* 66 */return getLoadedIds();
    /*     */}

  /*     */
  /*     */public final ElementInfo load(int index)
  /*     */{
    /* 71 */String[] elIds = getAllElementIds();
    /* 72 */if ((index < 0) || (index > elIds.length - 1))
      /* 73 */return null;
    /* 74 */return load(elIds[index]);
    /*     */}

  /*     */
  /*     */public ElementInfo loadByName(String name)
  /*     */{
    /* 80 */ElementInfo elInfo = findElement(name);
    /* 81 */if (elInfo == null)
    /*     */{
      /* 83 */reload();
      /* 84 */elInfo = findElement(name);
      /*     */}
    /* 86 */return elInfo;
    /*     */}

  /*     */
  /*     */public final ElementInfo[] getElementsAtDepth(int depth) {
    /* 90 */String[] ids = getAllElementIds();
    /* 91 */ArrayList lvlElements = new ArrayList();
    /* 92 */for (String id : ids) {
      /* 93 */ElementInfo info = load(id);
      /* 94 */if ((info != null) && (info.getDepth() == depth)) {
        /* 95 */lvlElements.add(info);
        /*     */}
      /*     */}
    /* 98 */return (ElementInfo[]) lvlElements.toArray(new ElementInfo[
    /* 99 */lvlElements.size()]);
    /*     */}

  /*     */
  /*     */public final ElementInfo[] getChildren(ElementInfo el) {
    /* 103 */String[] children = el.getChildren();
    /*     */
    /* 107 */ArrayList _children = new ArrayList();
    /* 108 */ArrayList newChildren = new ArrayList();
    /* 109 */for (int i = 0; i < children.length; ++i)
      /*     */try {
        /* 111 */ElementInfo ei = load(children[i]);
        /* 112 */if (ei == el) {
          /* 113 */return new ElementInfo[0];
          /*     */}
        /* 115 */_children.add(ei);
        /* 116 */newChildren.add(children[i]);
        /*     */}
      /*     */catch (Throwable localThrowable) {
        /*     */}
    /* 120 */el.update((String[]) newChildren.toArray(new String[0]));
    /* 121 */return (ElementInfo[]) _children.toArray(new ElementInfo[0]);
    /*     */}

  /*     */
  /*     */protected final void reload()
  /*     */{
    /* 134 */reset();
    /* 135 */ElementInfo[] elInfos = this.paloConnection
      .getElements(this.hierarchy);
    /* 136 */for (ElementInfo elInfo : elInfos)
      /* 137 */loaded(elInfo);
    /*     */}

  /*     */
  /*     */private final ElementInfo findElement(String name)
  /*     */{
    /* 142 */Collection<PaloInfo> infos = getLoaded();
    /* 143 */for (PaloInfo info : infos) {
      /* 144 */if (info instanceof ElementInfo) {
        /* 145 */ElementInfo elInfo = (ElementInfo) info;
        /*     */
        /* 147 */if (elInfo.getName().equalsIgnoreCase(name))
          /* 148 */return elInfo;
        /*     */}
      /*     */}
    /* 151 */return null;
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.paloapi\lib\palo.jar
 * Qualified Name: com.tensegrity.palojava.http.loader.HttpElementLoader JD-Core
 * Version: 0.5.4
 */