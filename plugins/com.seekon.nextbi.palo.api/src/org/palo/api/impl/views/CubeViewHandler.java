/*     */package org.palo.api.impl.views;

/*     */
/*     */import com.tensegrity.palojava.PaloException; /*     */
import java.util.ArrayList; /*     */
import java.util.Collection; /*     */
import java.util.HashMap; /*     */
import org.palo.api.Axis; /*     */
import org.palo.api.CubeView; /*     */
import org.palo.api.Database; /*     */
import org.palo.api.Dimension; /*     */
import org.palo.api.Element; /*     */
import org.palo.api.Hierarchy; /*     */
import org.palo.api.PaloAPIException; /*     */
import org.palo.api.impl.PersistenceErrorImpl; /*     */
import org.palo.api.impl.xml.IPaloEndHandler; /*     */
import org.palo.api.impl.xml.IPaloStartHandler; /*     */
import org.palo.api.persistence.PersistenceError;

/*     */
/*     */abstract class CubeViewHandler
/*     */{
  /*     */static final String LEGACY = "legacy";

  /* 69 */private final ArrayList errors = new ArrayList();

  /* 70 */private final HashMap endHandlers = new HashMap();

  /* 71 */private final HashMap startHandlers = new HashMap();

  /*     */protected final Database database;

  /*     */protected Axis currAxis;

  /*     */protected CubeView cubeView;

  /*     */
  /*     */CubeViewHandler(Database database)
  /*     */{
    /* 78 */this.database = database;
    /* 79 */registerHandlers();
    /*     */}

  /*     */
  /*     */protected abstract void registerStartHandlers();

  /*     */
  /*     */protected abstract void registerEndHandlers();

  /*     */
  /*     */final boolean hasErrors() {
    /* 87 */return !this.errors.isEmpty();
    /*     */}

  /*     */
  /*     */final PersistenceError[] getErrors() {
    /* 91 */return (PersistenceError[]) this.errors
      .toArray(new PersistenceError[this.errors.size()]);
    /*     */}

  /*     */
  /*     */final void addError(PersistenceError error) {
    /* 95 */if (!this.errors.contains(error))
      /* 96 */this.errors.add(error);
    /*     */}

  /*     */
  /*     */CubeView getCubeView() {
    /* 100 */return this.cubeView;
    /*     */}

  /*     */
  /*     */final IPaloEndHandler[] getEndHandlers() {
    /* 104 */return (IPaloEndHandler[]) this.endHandlers.values().toArray(
    /* 105 */new IPaloEndHandler[this.endHandlers.size()]);
    /*     */}

  /*     */
  /*     */final IPaloStartHandler[] getStartHandlers() {
    /* 109 */return (IPaloStartHandler[]) this.startHandlers.values().toArray(
    /* 110 */new IPaloStartHandler[this.startHandlers.size()]);
    /*     */}

  /*     */
  /*     */protected final void addError(String msg, String srcId, Object src,
    Object causeParent, String causeId, int type, Object section, int sectionType)
  /*     */{
    /* 116 */PersistenceErrorImpl err = new PersistenceErrorImpl(msg, srcId, src,
    /* 117 */causeParent, causeId, type, section, sectionType);
    /* 118 */addError(err);
    /*     */}

  /*     */
  /*     */protected final Element[] getPath(String path, Hierarchy hier,
    Object section, int sectionType)
  /*     */throws PaloAPIException
  /*     */{
    /* 124 */String[] paths = path.split("///");
    /* 125 */Element[] elements = new Element[paths.length];
    /* 126 */for (int i = 0; i < paths.length; ++i) {
      /* 127 */elements[i] = hier.getElementByName(paths[i]);
      /* 128 */if (elements[i] == null) {
        /* 129 */addError("CubeViewReader: unknown element id '" + paths[i] +
        /* 130 */"'!!", this.cubeView.getId(), this.cubeView, hier, paths[i],
        /* 131 */1, section, sectionType);
        /*     */}
      /*     */}
    /* 134 */return elements;
    /*     */}

  /*     */
  /*     */protected final Element[] getPathById(String path, Dimension dim,
    Hierarchy hier, Object section, int sectionType) throws PaloAPIException {
    /* 138 */String[] paths = path.split("///");
    /* 139 */Element[] elements = new Element[paths.length];
    /* 140 */for (int i = 0; i < paths.length; ++i) {
      /*     */try {
        /* 142 */if (hier != null)
          /* 143 */elements[i] = hier.getElementById(paths[i]);
        /*     */else
          /* 145 */elements[i] = dim.getElementById(paths[i]);
        /*     */}
      /*     */catch (PaloException e) {
        /* 148 */elements[i] = null;
        /*     */}
      /* 150 */if (elements[i] == null) {
        /* 151 */addError("CubeViewReader: unknown element id '" + paths[i] +
        /* 152 */"'!!", this.cubeView.getId(), this.cubeView, hier, paths[i],
        /* 153 */1, section, sectionType);
        /*     */}
      /*     */}
    /* 156 */return elements;
    /*     */}

  /*     */
  /*     */protected final void registerStartHandler(IPaloStartHandler startHandler)
  /*     */{
    /* 161 */this.startHandlers.put(startHandler.getPath(), startHandler);
    /*     */}

  /*     */
  /*     */protected final void unregisterStartHandler(String handlerPath) {
    /* 165 */this.startHandlers.remove(handlerPath);
    /*     */}

  /*     */
  /*     */protected final void clearStartHandlers() {
    /* 169 */this.startHandlers.clear();
    /*     */}

  /*     */
  /*     */protected final void registerEndHandler(IPaloEndHandler endHandler) {
    /* 173 */this.endHandlers.put(endHandler.getPath(), endHandler);
    /*     */}

  /*     */
  /*     */protected final void unregisterEndHandler(String handlerPath) {
    /* 177 */this.endHandlers.remove(handlerPath);
    /*     */}

  /*     */
  /*     */protected final void clearEndHandlers() {
    /* 181 */this.endHandlers.clear();
    /*     */}

  /*     */
  /*     */private final void registerHandlers() {
    /* 185 */registerStartHandlers();
    /* 186 */registerEndHandlers();
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.impl.views.CubeViewHandler JD-Core Version:
 * 0.5.4
 */