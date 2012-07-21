/*     */package org.palo.api.ext.favoriteviews;

/*     */
/*     */import java.util.ArrayList; /*     */
import java.util.List;

/*     */
/*     */public class FavoriteViewTreeNode
/*     */{
  /*     */private FavoriteViewObject userObject;

  /*     */private FavoriteViewTreeNode parent;

  /*     */private List children;

  /*     */
  /*     */public FavoriteViewTreeNode(FavoriteViewObject userObject)
  /*     */{
    /* 105 */this.userObject = userObject;
    /* 106 */this.children = new ArrayList(1);
    /*     */}

  /*     */
  /*     */public FavoriteViewObject getUserObject()
  /*     */{
    /* 119 */return this.userObject;
    /*     */}

  /*     */
  /*     */public void setParent(FavoriteViewTreeNode parent)
  /*     */{
    /* 128 */this.parent = parent;
    /*     */}

  /*     */
  /*     */public FavoriteViewTreeNode getParent()
  /*     */{
    /* 137 */return this.parent;
    /*     */}

  /*     */
  /*     */public int hashCode()
  /*     */{
    /* 145 */if (this.userObject != null) {
      /* 146 */return this.userObject.hashCode();
      /*     */}
    /* 148 */return super.hashCode();
    /*     */}

  /*     */
  /*     */public boolean equals(Object obj)
  /*     */{
    /* 156 */if (obj instanceof FavoriteViewTreeNode) {
      /* 157 */FavoriteViewObject uo1 = this.userObject;
      /* 158 */FavoriteViewObject uo2 = ((FavoriteViewTreeNode) obj)
        .getUserObject();
      /*     */
      /* 160 */if ((uo1 != null) && (uo2 != null)) {
        /* 161 */return uo1.equals(uo2);
        /*     */}
      /*     */}
    /* 164 */return super.equals(obj);
    /*     */}

  /*     */
  /*     */public void addChild(FavoriteViewTreeNode child)
  /*     */{
    /* 177 */this.children.add(child);
    /* 178 */child.setParent(this);
    /*     */}

  /*     */
  /*     */public void insertChild(int index, FavoriteViewTreeNode child)
  /*     */{
    /* 190 */if ((index > this.children.size()) || (index < 0))
      /* 191 */this.children.add(child);
    /*     */else {
      /* 193 */this.children.add(index, child);
      /*     */}
    /* 195 */child.setParent(this);
    /*     */}

  /*     */
  /*     */public void removeChild(FavoriteViewTreeNode child)
  /*     */{
    /* 206 */this.children.remove(child);
    /* 207 */child.setParent(null);
    /*     */}

  /*     */
  /*     */public void removeChildren()
  /*     */{
    /* 214 */for (int i = this.children.size() - 1; i >= 0; --i) {
      /* 215 */FavoriteViewTreeNode child = (FavoriteViewTreeNode) this.children
        .get(i);
      /* 216 */this.children.remove(child);
      /* 217 */child.setParent(null);
      /*     */}
    /*     */}

  /*     */
  /*     */public int indexOfChild(FavoriteViewTreeNode child)
  /*     */{
    /* 229 */return this.children.indexOf(child);
    /*     */}

  /*     */
  /*     */public FavoriteViewTreeNode[] getChildren()
  /*     */{
    /* 238 */return (FavoriteViewTreeNode[]) this.children.toArray(
    /* 239 */new FavoriteViewTreeNode[this.children.size()]);
    /*     */}

  /*     */
  /*     */public int getChildCount()
  /*     */{
    /* 248 */return this.children.size();
    /*     */}

  /*     */
  /*     */public FavoriteViewTreeNode getChildAt(int index)
  /*     */{
    /* 258 */return (FavoriteViewTreeNode) this.children.get(index);
    /*     */}

  /*     */
  /*     */public boolean hasChildren()
  /*     */{
    /* 267 */return this.children.size() > 0;
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.ext.favoriteviews.FavoriteViewTreeNode JD-Core
 * Version: 0.5.4
 */