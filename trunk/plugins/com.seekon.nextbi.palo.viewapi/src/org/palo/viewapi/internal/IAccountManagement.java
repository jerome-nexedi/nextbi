package org.palo.viewapi.internal;

import java.sql.SQLException;
import java.util.List;
import org.palo.viewapi.Account;
import org.palo.viewapi.PaloConnection;
import org.palo.viewapi.User;

public abstract interface IAccountManagement extends IDomainObjectManagement
{
  public abstract List<Account> findAll()
    throws SQLException;

  public abstract Account findBy(User paramUser, PaloConnection paramPaloConnection)
    throws SQLException;

  public abstract Account findBy(String paramString, PaloConnection paramPaloConnection)
    throws SQLException;

  public abstract List<Account> getAccountsBy(String paramString)
    throws SQLException;

  public abstract List<Account> getAccounts(User paramUser)
    throws SQLException;

  public abstract List<Account> getAccounts(String paramString)
    throws SQLException;

  public abstract void delete(PaloConnection paramPaloConnection)
    throws SQLException;
}

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi.jar
 * Qualified Name:     org.palo.viewapi.internal.IAccountManagement
 * JD-Core Version:    0.5.4
 */