/*     */package org.palo.viewapi.uimodels.axis;

/*     */
/*     */import java.io.PrintStream; /*     */
import java.util.ArrayList; /*     */
import org.palo.api.Hierarchy; /*     */
import org.palo.viewapi.Axis; /*     */
import org.palo.viewapi.AxisHierarchy;

/*     */
/*     */public class AxisFlatModel extends AxisTreeModel
/*     */{
  /*     */public static final int VERTICAL = 1;

  /*     */public static final int HORIZONTAL = 2;

  /*     */private static final int UNDEFINED = -1;

  /*     */private final int orientation;

  /*     */private AxisItem[][] model;

  /*     */private final boolean reversed;

  /*     */
  /*     */public AxisFlatModel(Axis axis, boolean isReversed)
  /*     */{
    /* 67 */this(axis, 2, isReversed);
    /*     */}

  /*     */
  /*     */public AxisFlatModel(Axis axis, int orientation, boolean isReversed)
  /*     */{
    /* 79 */super(axis);
    /* 80 */this.model = init(orientation, isReversed);
    /* 81 */this.orientation = orientation;
    /* 82 */this.reversed = isReversed;
    /*     */}

  /*     */
  /*     */public final AxisItem[][] getModel()
  /*     */{
    /* 89 */return this.model;
    /*     */}

  /*     */
  /*     */protected final void notifyExpanded(AxisItem source, AxisItem[][] items) {
    /* 93 */int hierIndex = getIndex(source.getHierarchy());
    /* 94 */int hierCount = getAxisHierarchyCount() - hierIndex;
    /* 95 */if (hierIndex != -1)
    /*     */{
      /* 97 */ArrayList[] delta = getDelta(hierCount, items);
      /*     */
      /* 99 */items = new AxisItem[delta.length][];
      /* 100 */for (int i = 0; i < delta.length; ++i) {
        /* 101 */items[i] = ((AxisItem[]) delta[i].toArray(new AxisItem[0]));
        /*     */}
      /*     */
      /* 104 */if (isHorizontal())
        /* 105 */expandHorizontal(source, hierIndex, items);
      /*     */else {
        /* 107 */expandVertical(source, hierIndex, items);
        /*     */}
      /*     */}
    /* 110 */super.notifyExpanded(source, items);
    /*     */}

  /*     */
  /*     */protected final void notifyCollapsed(AxisItem source, AxisItem[][] items) {
    /* 114 */int hierIndex = getIndex(source.getHierarchy());
    /* 115 */int hierCount = getAxisHierarchyCount() - hierIndex;
    /* 116 */if (hierIndex != -1)
    /*     */{
      /* 118 */ArrayList[] delta = getDelta(hierCount, items);
      /*     */
      /* 120 */items = new AxisItem[delta.length][];
      /* 121 */for (int i = 0; i < delta.length; ++i) {
        /* 122 */items[i] = ((AxisItem[]) delta[i].toArray(new AxisItem[0]));
        /*     */}
      /* 124 */if (isHorizontal())
        /* 125 */collapseHorizontal(hierIndex, items);
      /*     */else
        /* 127 */collapseVertical(source, hierIndex, items);
      /*     */}
    /* 129 */super.notifyCollapsed(source, items);
    /*     */}

  /*     */
  /*     */protected void notifyStructureChange() {
    /* 133 */long t0 = System.currentTimeMillis();
    /*     */
    /* 135 */this.model = init(this.orientation, this.reversed);
    /* 136 */long t1 = System.currentTimeMillis();
    /* 137 */System.err.println("AxisFlatModel#notifyStructureChange(): "
      + (t1 - t0) + "ms");
    /* 138 */super.notifyStructureChange();
    /*     */}

  /*     */
  /*     */private final AxisItem[][] init(int orientation, boolean isReversed)
  /*     */{
    /* 146 */if (isReversed) {
      /* 147 */return reverseInit(orientation);
      /*     */}
    /* 149 */return normalInit(orientation);
    /*     */}

  /*     */
  /*     */private final AxisItem[][] reverseInit(int orientation)
  /*     */{
    /* 154 */AxisItem[][] model = (AxisItem[][]) null;
    /* 155 */AxisHierarchy[] hierarchies = this.axis.getAxisHierarchies();
    /* 156 */final ArrayList[] items = new ArrayList[hierarchies.length];
    /*     */
    /* 158 */AxisTreeFlattenVisitor visitor = new AxisTreeFlattenVisitor() {
      /*     */public final void visit(AxisItem item, int dimIndex) {
        /* 160 */if (items[dimIndex] == null)
          /* 161 */items[dimIndex] = new ArrayList();
        /* 162 */items[dimIndex].add(item);
        /*     */}
      /*     */
    };
    /* 166 */AxisItem[] roots = getRoots();
    /* 167 */for (AxisItem root : roots) {
      /* 168 */reverseFlatten(root, 0, visitor);
      /*     */}
    /* 170 */if (orientation == 2) {
      /* 171 */model = new AxisItem[hierarchies.length][];
      /* 172 */for (int i = 0; i < model.length; ++i)
        /* 173 */model[i] = ((items[i] != null) ? (AxisItem[]) items[i]
          .toArray(new AxisItem[0]) :
        /* 174 */new AxisItem[0]);
      /*     */} else {
      /* 176 */int lastDim = items.length - 1;
      /* 177 */int lastDimSize = items[lastDim].size();
      /* 178 */model = new AxisItem[lastDimSize][hierarchies.length];
      /* 179 */for (int k = 0; k < lastDimSize; ++k) {
        /* 180 */AxisItem item = (AxisItem) items[lastDim].get(k);
        /* 181 */model[k][lastDim] = item;
        /*     */
        /* 184 */int prevDim = lastDim - 1;
        /*     */AxisItem parent;
        /* 185 */while ((parent = item.getParentInPrevHierarchy()) != null)
        /*     */{
          /* 186 */model[k][prevDim] = parent;
          /* 187 */item = parent;
          /* 188 */--prevDim;
          /*     */}
        /*     */}
      /*     */}
    /* 192 */return model;
    /*     */}

  /*     */
  /*     */private final AxisItem[][] normalInit(int orientation) {
    /* 196 */AxisItem[][] model = (AxisItem[][]) null;
    /* 197 */AxisHierarchy[] hierarchies = this.axis.getAxisHierarchies();
    /* 198 */final ArrayList[] items = new ArrayList[hierarchies.length];
    /*     */
    /* 200 */AxisTreeFlattenVisitor visitor = new AxisTreeFlattenVisitor() {
      /*     */public final void visit(AxisItem item, int dimIndex) {
        /* 202 */if (items[dimIndex] == null)
          /* 203 */items[dimIndex] = new ArrayList();
        /* 204 */items[dimIndex].add(item);
        /*     */}
      /*     */
    };
    /* 208 */AxisItem[] roots = getRoots();
    /* 209 */for (AxisItem root : roots) {
      /* 210 */flatten(root, 0, visitor);
      /*     */}
    /* 212 */if (orientation == 2) {
      /* 213 */model = new AxisItem[hierarchies.length][];
      /* 214 */for (int i = 0; i < model.length; ++i)
        /* 215 */model[i] = ((items[i] != null) ? (AxisItem[]) items[i]
          .toArray(new AxisItem[0]) :
        /* 216 */new AxisItem[0]);
      /*     */} else {
      /* 218 */int lastDim = items.length - 1;
      /* 219 */int lastDimSize = items[lastDim].size();
      /* 220 */model = new AxisItem[lastDimSize][hierarchies.length];

      /* 221 */for (int k = 0; k < lastDimSize; ++k) {
        /* 222 */AxisItem item = (AxisItem) items[lastDim].get(k);
        /* 223 */model[k][lastDim] = item;
        /*     */
        /* 226 */int prevDim = lastDim - 1;
        /*     */AxisItem parent;
        /* 227 */while ((parent = item.getParentInPrevHierarchy()) != null)
        /*     */{
          /* 228 */model[k][prevDim] = parent;
          /* 229 */item = parent;
          /* 230 */--prevDim;
          /*     */}
        /*     */}
      /*     */}
    /* 234 */return model;
    /*     */}

  /*     */
  /*     */private final void flatten(AxisItem item, int hierIndex,
    AxisTreeFlattenVisitor visitor)
  /*     */{
    /* 244 */if (item.hasRootsInNextHierarchy()) {
      /* 245 */AxisItem[] rootsInNextHier = item.getRootsInNextHierarchy();
      /* 246 */for (AxisItem root : rootsInNextHier) {
        /* 247 */flatten(root, hierIndex + 1, visitor);
        /*     */}
      /*     */}
    /* 250 */visitor.visit(item, hierIndex);
    /*     */
    /* 252 */if ((item.hasChildren()) && (item.hasState(2))) {
      /* 253 */AxisItem[] children = item.getChildren();
      /* 254 */for (AxisItem child : children)
        /* 255 */flatten(child, hierIndex, visitor);
      /*     */}
    /*     */}

  /*     */
  /*     */private final void reverseFlatten(AxisItem item, int hierIndex,
    AxisTreeFlattenVisitor visitor)
  /*     */{
    /* 262 */if ((item.hasChildren()) && (item.hasState(2))) {
      /* 263 */AxisItem[] children = item.getChildren();
      /* 264 */for (AxisItem child : children) {
        /* 265 */reverseFlatten(child, hierIndex, visitor);
        /*     */}
      /*     */}
    /* 268 */if (item.hasRootsInNextHierarchy()) {
      /* 269 */AxisItem[] rootsInNextHier = item.getRootsInNextHierarchy();
      /* 270 */for (AxisItem root : rootsInNextHier) {
        /* 271 */reverseFlatten(root, hierIndex + 1, visitor);
        /*     */}
      /*     */}
    /*     */
    /* 275 */visitor.visit(item, hierIndex);
    /*     */}

  /*     */
  /*     */private final int getIndex(Hierarchy hierarchy) {
    /* 279 */Hierarchy[] hierarchies = getHierarchies();
    /* 280 */for (int i = 0; i < hierarchies.length; ++i) {
      /* 281 */if (hierarchies[i].equals(hierarchy))
        /* 282 */return i;
      /*     */}
    /* 284 */return -1;
    /*     */}

  /*     */
  /*     */private final int getIndex(AxisItem item, int inHierarchy) {
    /* 288 */if (inHierarchy < 0) {
      /* 289 */return inHierarchy;
      /*     */}
    /* 291 */int index = -1;
    /* 292 */String itemPath = item.getPath();
    /* 293 */if (isHorizontal()) {
      /* 294 */for (int i = 0; i < this.model[inHierarchy].length; ++i) {
        /* 295 */if (this.model[inHierarchy][i].getPath().equals(itemPath)) {
          /* 296 */index = i;
          /* 297 */return index;
          /*     */}
        /*     */}
      /*     */}
    /*     */else {
      /* 302 */for (int i = 0; i < this.model.length; ++i) {
        /* 303 */if (this.model[i][inHierarchy].getPath().equals(itemPath)) {
          /* 304 */index = i;
          /* 305 */break;
          /*     */}
        /*     */}
      /*     */}
    /* 309 */return index;
    /*     */}

  /*     */
  /*     */private final ArrayList<AxisItem>[] getDelta(final int hierCount,
    final AxisItem[][] items)
  /*     */{
    /* 314 */final ArrayList[] delta = new ArrayList[hierCount];
    /* 315 */if ((items != null) && (items.length > 0)) {
      /* 316 */AxisTreeFlattenVisitor visitor = new AxisTreeFlattenVisitor() {
        /*     */public final void visit(AxisItem item, int dimIndex) {
          /* 318 */if (delta[dimIndex] == null)
            /* 319 */delta[dimIndex] = new ArrayList();
          /* 320 */delta[dimIndex].add(item);
          /*     */}
        /*     */
      };
      /* 324 */if (this.reversed)
        /* 325 */for (AxisItem child : items[0])
          /* 326 */reverseFlatten(child, 0, visitor);
      /*     */else {
        /* 328 */for (AxisItem child : items[0])
          /* 329 */flatten(child, 0, visitor);
        /*     */}
      /*     */}
    /* 332 */return delta;
    /*     */}

  /*     */
  /*     */private final boolean isHorizontal() {
    /* 336 */return this.orientation == 2;
    /*     */}

  /*     */
  /*     */private final void expandHorizontal(AxisItem item, int hierIndex,
    AxisItem[][] delta)
  /*     */{
    /* 341 */int index = 0;
    /* 342 */while ((item != null) && (index < delta.length))
    /*     */{
      /* 345 */int modelIndex = hierIndex + index;
      /* 346 */int startIndex = getIndex(item, modelIndex) + 1;
      /* 347 */AxisItem[] newModel =
      /* 348 */new AxisItem[this.model[modelIndex].length + delta[index].length];
      /*     */
      /* 350 */System.arraycopy(this.model[modelIndex], 0, newModel, 0, startIndex);
      /*     */
      /* 352 */System.arraycopy(delta[index], 0, newModel, startIndex,
      /* 353 */delta[index].length);
      /*     */
      /* 355 */int endIndex = startIndex + delta[index].length;
      /* 356 */if (endIndex < newModel.length)
      /*     */{
        /* 358 */System.arraycopy(this.model[modelIndex], startIndex, newModel,
        /* 359 */endIndex, this.model[modelIndex].length - startIndex);
        /*     */}
      /* 361 */this.model[modelIndex] = newModel;
      /* 362 */++index;
      /* 363 */item = getLastChildInNextHierarchy(item);
      /*     */}
    /*     */}

  /*     */
  /*     */private final void expandVertical(AxisItem item, int hierIndex,
    AxisItem[][] delta) {
    /* 368 */int hierCount = getAxisHierarchyCount();
    /* 369 */int srcIndex = getIndex(item, hierIndex);
    /* 370 */int lastDim = delta.length - 1;
    /*     */
    /* 372 */int offset = srcIndex + delta[lastDim].length;
    /* 373 */AxisItem[][] newModel =
    /* 374 */new AxisItem[delta[lastDim].length + this.model.length][hierCount];
    /*     */
    /* 376 */for (int i = 0; i < this.model.length; ++i) {
      /* 377 */if (i <= srcIndex)
        /* 378 */newModel[i] = this.model[i];
      /*     */else {
        /* 380 */newModel[(i + offset)] = this.model[i];
        /*     */}
      /*     */}
    /* 383 */for (int i = 0; i < delta[lastDim].length; ++i)
    /*     */{
      /* 385 */item = delta[lastDim][i];
      /* 386 */newModel[(srcIndex + 1 + i)][lastDim] = item;
      /*     */
      /* 389 */int prevDim = lastDim - 1;
      /*     */AxisItem parent;
      /* 390 */while ((parent = item.getParentInPrevHierarchy()) != null)
      /*     */{
        /* 391 */newModel[i][prevDim] = parent;
        /* 392 */item = parent;
        /* 393 */--prevDim;
        /*     */}
      /*     */}
    /*     */
    /* 397 */this.model = newModel;
    /*     */}

  /*     */
  /*     */private final void collapseHorizontal(int hierIndex, AxisItem[][] delta)
  /*     */{
    /* 403 */int i = 0;
    for (int n = this.model.length - hierIndex; i < n; ++i) {
      /* 404 */int modelIndex = hierIndex + i;
      /*     */
      /* 406 */AxisItem[] newModel =
      /* 407 */new AxisItem[this.model[modelIndex].length - delta[i].length];
      /* 408 */int startIndex = getIndex(delta[i][0], modelIndex);
      /*     */
      /* 410 */System.arraycopy(this.model[modelIndex], 0, newModel, 0, startIndex);
      /*     */
      /* 412 */int endIndex =
      /* 413 */getIndex(delta[i][(delta[i].length - 1)], modelIndex) + 1;
      /* 414 */if (endIndex < this.model[modelIndex].length)
      /*     */{
        /* 416 */System.arraycopy(this.model[modelIndex], endIndex, newModel,
        /* 417 */startIndex, this.model[modelIndex].length - endIndex);
        /*     */}
      /* 419 */this.model[modelIndex] = newModel;
      /*     */}
    /*     */}

  /*     */
  /*     */private final void collapseVertical(AxisItem item, int hierIndex,
    AxisItem[][] delta)
  /*     */{
    /* 425 */int srcIndex = getIndex(item, hierIndex);
    /* 426 */int lastDim = delta.length - 1;
    /*     */
    /* 428 */int offset = srcIndex + delta[lastDim].length;
    /* 429 */AxisItem[][] newModel =
    /* 430 */new AxisItem[this.model.length - delta[lastDim].length][];
    /*     */
    /* 432 */for (int i = 0; i < this.model.length; ++i) {
      /* 433 */if (i <= srcIndex)
        /* 434 */newModel[i] = this.model[i];
      /*     */else {
        /* 436 */newModel[(i + offset)] = this.model[i];
        /*     */}
      /*     */
      /*     */}
    /*     */
    /* 452 */this.model = newModel;
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.uimodels.axis.AxisFlatModel JD-Core
 * Version: 0.5.4
 */