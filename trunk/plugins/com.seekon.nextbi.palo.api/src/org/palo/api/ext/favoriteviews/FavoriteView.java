package org.palo.api.ext.favoriteviews;

import org.palo.api.CubeView;

public abstract interface FavoriteView extends FavoriteViewObject
{
  public abstract void setName(String paramString);

  public abstract int getPosition();

  public abstract void setPosition(int paramInt);

  public abstract CubeView getCubeView();
}

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.ext.favoriteviews.FavoriteView
 * JD-Core Version:    0.5.4
 */