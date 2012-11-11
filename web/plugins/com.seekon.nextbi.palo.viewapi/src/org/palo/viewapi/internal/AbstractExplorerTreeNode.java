/*     */package org.palo.viewapi.internal;

/*     */
/*     */import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.palo.api.exceptions.PaloIOException;
import org.palo.viewapi.AuthUser;
import org.palo.viewapi.PaloConnection;
import org.palo.viewapi.Role;
import org.palo.viewapi.User;

/*     */
/*     */public abstract class AbstractExplorerTreeNode extends GuardedObjectImpl
/*     */implements ExplorerTreeNode
/*     */{
  /*     */protected String name;

  /*     */protected ExplorerTreeNode parent;

  /*     */protected ArrayList<ExplorerTreeNode> children;

  /*     */protected String connectionId;

  /*     */
  /*     */protected AbstractExplorerTreeNode(String prefix)
  /*     */{
    /* 89 */super(prefix + UUID.randomUUID().toString());
    /* 90 */this.children = new ArrayList();
    /*     */}

  /*     */
  /*     */protected AbstractExplorerTreeNode(String id, boolean internal)
  /*     */{
    /* 100 */super(id);
    /* 101 */this.children = new ArrayList();
    /*     */}

  /*     */
  /*     */protected AbstractExplorerTreeNode(String prefix, ExplorerTreeNode parent)
  /*     */{
    /* 114 */super(prefix + UUID.randomUUID().toString());
    /* 115 */this.parent = parent;
    /* 116 */this.children = new ArrayList();
    /*     */}

  /*     */
  /*     */protected AbstractExplorerTreeNode(String prefix,
    ExplorerTreeNode parent, String name)
  /*     */{
    /* 131 */super(prefix + UUID.randomUUID().toString());
    /* 132 */this.name = name;
    /* 133 */this.parent = parent;
    /* 134 */this.children = new ArrayList();
    /*     */}

  /*     */
  /*     */protected AbstractExplorerTreeNode(ExplorerTreeNode parent, String id,
    String name)
  /*     */{
    /* 148 */super(id);
    /* 149 */this.name = name;
    /* 150 */this.parent = parent;
    /* 151 */this.children = new ArrayList();
    /*     */}

  /*     */
  /*     */public String getName()
  /*     */{
    /* 171 */return this.name;
    /*     */}

  /*     */
  /*     */public void setName(String newName)
  /*     */{
    /* 180 */this.name = newName;
    /*     */}

  /*     */
  /*     */public ExplorerTreeNode getRoot()
  /*     */{
    /* 189 */ExplorerTreeNode node = this;
    /* 190 */while (node.getParent() != null) {
      /* 191 */node = node.getParent();
      /*     */}
    /* 193 */return node;
    /*     */}

  /*     */
  /*     */public ExplorerTreeNode getParent()
  /*     */{
    /* 202 */return this.parent;
    /*     */}

  /*     */
  /*     */public void setParent(ExplorerTreeNode newParent)
  /*     */{
    /* 211 */if (this.parent != null) {
      /* 212 */this.parent.removeChildById(getId());
      /*     */}
    /* 214 */this.parent = newParent;
    /* 215 */if (this.parent != null)
      /* 216 */this.parent.addChild(this);
    /*     */}

  /*     */
  /*     */public ExplorerTreeNode[] getChildren()
  /*     */{
    /* 227 */return (ExplorerTreeNode[]) this.children
      .toArray(new ExplorerTreeNode[0]);
    /*     */}

  /*     */
  /*     */public boolean addChild(ExplorerTreeNode child)
  /*     */{
    /* 238 */if (!this.children.contains(child)) {
      /* 239 */return this.children.add(child);
      /*     */}
    /* 241 */return false;
    /*     */}

  /*     */
  /*     */public boolean removeChild(ExplorerTreeNode child)
  /*     */{
    /* 253 */return this.children.remove(child);
    /*     */}

  /*     */
  /*     */public boolean removeChildById(String id)
  /*     */{
    /* 264 */int removeIndex = -1;
    /* 265 */int i = 0;
    for (int n = this.children.size(); i < n; ++i) {
      /* 266 */ExplorerTreeNode node = (ExplorerTreeNode) this.children.get(i);
      /* 267 */if (node.getId().equals(id)) {
        /* 268 */removeIndex = i;
        /* 269 */break;
        /*     */}
      /*     */}
    /* 272 */if (removeIndex > -1) {
      /* 273 */this.children.remove(removeIndex);
      /* 274 */return true;
      /*     */}
    /* 276 */return false;
    /*     */}

  /*     */
  /*     */public boolean removeChildByName(String name)
  /*     */{
    /* 287 */ExplorerTreeNode removeNode = null;
    /* 288 */for (ExplorerTreeNode node : this.children) {
      /* 289 */if (node.getName().equals(name)) {
        /* 290 */removeNode = node;
        /* 291 */break;
        /*     */}
      /*     */}
    /* 294 */if (removeNode != null) {
      /* 295 */return this.children.remove(removeNode);
      /*     */}
    /* 297 */return false;
    /*     */}

  /*     */
  /*     */public boolean clearAllChildren()
  /*     */{
    /* 306 */this.children.clear();
    /* 307 */return true;
    /*     */}

  /*     */
  /*     */public boolean equals(Object other)
  /*     */{
    /* 315 */if (other == null) {
      /* 316 */return false;
      /*     */}
    /* 318 */if (!(other instanceof ExplorerTreeNode)) {
      /* 319 */return false;
      /*     */}
    /* 321 */return getId().equals(((ExplorerTreeNode) other).getId());
    /*     */}

  /*     */
  /*     */public int hashCode()
  /*     */{
    /* 328 */int hash = 7;
    /* 329 */hash = 31 * hash + getId().hashCode();
    /* 330 */return hash;
    /*     */}

  /*     */
  /*     */public void addParameterValue(String parameterName, Object parameterValue) {
    /* 334 */if (parameterValue == null) {
      /* 335 */return;
      /*     */}
    /* 337 */Object o = getParameterValue(parameterName);
    /* 338 */if (o == null) {
      /* 339 */setParameter(parameterName, parameterValue);
      /*     */}
    /* 341 */else if (o instanceof List) {
      /* 342 */if (!((List) o).contains(parameterValue)) {
        /* 343 */((List) o).add(parameterValue);
        /* 344 */setParameter(parameterName, o);
        /*     */}
      /* 346 */} else if (o instanceof Object[]) {
      /* 347 */Object[] result = (Object[]) o;
      /* 348 */Object[] nVal = new Object[result.length + 1];
      /* 349 */for (int i = 0; i < result.length; ++i) {
        /* 350 */nVal[i] = result[i];
        /*     */}
      /* 352 */nVal[result.length] = parameterValue;
      /* 353 */setParameter(parameterName, nVal);
      /*     */} else {
      /* 355 */Object[] nVal = new Object[2];
      /* 356 */nVal[0] = o;
      /* 357 */nVal[1] = parameterValue;
      /* 358 */setParameter(parameterName, nVal);
      /*     */}
    /*     */}

  /*     */
  /*     */public String getConnectionId()
  /*     */{
    /* 364 */return "1";
    /*     */}

  /*     */
  /*     */final void setConnectionId(String connectionId) {
    /* 368 */this.connectionId = connectionId;
    /*     */}

  /*     */
  /*     */protected String modify(String x)
  /*     */{
    /* 482 */x = x.replaceAll("&", "&amp;");
    /* 483 */x = x.replaceAll("\"", "&quot;");
    /* 484 */x = x.replaceAll("'", "&apos;");
    /* 485 */x = x.replaceAll("<", "&lt;");
    /* 486 */x = x.replaceAll(">", "&gt;");
    /* 487 */return x;
    /*     */}

  /*     */
  /*     */public static final class Builder
  /*     */{
    /*     */public final String id;

    /*     */public String xml;

    /*     */public String name;

    /*     */public User owner;

    /*     */public int type;

    /*     */public PaloConnection paloConn;

    /* 383 */public Set<Role> roles = new HashSet();

    /*     */public String parentId;

    /* 386 */static HashMap<User, ExplorerTreeNode> roots = new HashMap();

    /*     */
    /*     */public Builder(String id) {
      /* 389 */AccessController.checkAccess(ExplorerTreeNode.class);
      /* 390 */this.id = id;
      /*     */}

    /*     */
    /*     */public Builder definition(String xml) {
      /* 394 */this.xml = xml;
      /* 395 */return this;
      /*     */}

    /*     */
    /*     */public Builder parent(String parentId) {
      /* 399 */this.parentId = parentId;
      /* 400 */return this;
      /*     */}

    /*     */
    /*     */public Builder name(String name) {
      /* 404 */this.name = name;
      /* 405 */return this;
      /*     */}

    /*     */
    /*     */public Builder owner(User owner) {
      /* 409 */this.owner = owner;
      /* 410 */return this;
      /*     */}

    /*     */
    /*     */public Builder type(String type) {
      /* 414 */this.type = Integer.parseInt(type);
      /* 415 */return this;
      /*     */}

    /*     */
    /*     */public Builder connection(PaloConnection paloConn) {
      /* 419 */this.paloConn = paloConn;
      /* 420 */return this;
      /*     */}

    /*     */
    /*     */public Builder add(Role role) {
      /* 424 */this.roles.add(role);
      /* 425 */return this;
      /*     */}

    /*     */
    /*     */private final ExplorerTreeNode findNode(ExplorerTreeNode root,
      String id) {
      /* 429 */if ((root == null) || (id == null)) {
        /* 430 */return null;
        /*     */}
      /* 432 */if (id.equals(root.getId())) {
        /* 433 */return root;
        /*     */}
      /* 435 */for (ExplorerTreeNode kid : root.getChildren()) {
        /* 436 */ExplorerTreeNode f = findNode(kid, id);
        /* 437 */if (f != null) {
          /* 438 */return f;
          /*     */}
        /*     */}
      /* 441 */return null;
      /*     */}

    /*     */
    /*     */public ExplorerTreeNode build(AuthUser user) {
      /* 445 */if ((!roots.containsKey(user)) || (roots.get(user) == null)) {
        /*     */try {
          /* 447 */roots.put(user, FolderModel.getInstance().loadPure(user));
          /*     */} catch (PaloIOException e) {
          /* 449 */e.printStackTrace();
          /* 450 */return null;
          /*     */}
        /*     */}
      /* 453 */AbstractExplorerTreeNode node = (AbstractExplorerTreeNode) findNode(
        (ExplorerTreeNode) roots.get(user), this.id);
      /* 454 */if (node == null) {
        /*     */try {
          /* 456 */roots.put(user, FolderModel.getInstance().loadPure(user));
          /* 457 */node = (AbstractExplorerTreeNode) findNode(
            (ExplorerTreeNode) roots.get(user), this.id);
          /*     */} catch (PaloIOException e) {
          /* 459 */e.printStackTrace();
          /*     */}
        /*     */}
      /* 462 */if (node == null) {
        /* 463 */if (this.type == 2)
          /* 464 */node = new StaticFolder((ExplorerTreeNode) roots.get(user),
            this.id, this.name);
        /* 465 */else if (this.type == 3) {
          /* 466 */node = new FolderElement((ExplorerTreeNode) roots.get(user),
            this.id, this.name);
          /*     */}
        /*     */}
      /* 469 */if (node != null) {
        /* 470 */node.setOwner(this.owner);
        /* 471 */if (this.paloConn != null)
          /* 472 */node.setConnectionId(this.paloConn.getId());
        /*     */else {
          /* 474 */node.setConnectionId("");
          /*     */}
        /*     */}
      /* 477 */return node;
      /*     */}
    /*     */
  }
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.internal.AbstractExplorerTreeNode
 * JD-Core Version: 0.5.4
 */