package org.palo.viewapi.services;

import java.util.List;

import org.palo.viewapi.Account;
import org.palo.viewapi.Group;
import org.palo.viewapi.PaloConnection;
import org.palo.viewapi.Right;
import org.palo.viewapi.Role;
import org.palo.viewapi.User;
import org.palo.viewapi.exceptions.OperationFailedException;

public abstract interface AdministrationService extends Service {
  public abstract User getUser(String paramString);

  public abstract List<User> getUsers();

  public abstract User createUser(String paramString1, String paramString2,
    String paramString3, String paramString4) throws OperationFailedException;

  public abstract void delete(User paramUser) throws OperationFailedException;

  public abstract void save(User paramUser) throws OperationFailedException;

  public abstract void setFirstname(String paramString, User paramUser);

  public abstract void setLastname(String paramString, User paramUser);

  public abstract void setLoginName(String paramString, User paramUser);

  public abstract void setPassword(String paramString, User paramUser);

  public abstract void add(User paramUser, Group paramGroup)
    throws OperationFailedException;

  public abstract void remove(User paramUser, Group paramGroup)
    throws OperationFailedException;

  public abstract void setUsers(List<User> paramList, Group paramGroup)
    throws OperationFailedException;

  public abstract void setGroups(List<Group> paramList, User paramUser)
    throws OperationFailedException;

  public abstract void add(Role paramRole, User paramUser)
    throws OperationFailedException;

  public abstract void remove(Role paramRole, User paramUser)
    throws OperationFailedException;

  public abstract void setRoles(List<Role> paramList, User paramUser)
    throws OperationFailedException;

  public abstract Group getGroup(String paramString);

  public abstract List<Group> getGroups();

  public abstract List<Group> getGroups(User paramUser);

  public abstract Group createGroup(String paramString)
    throws OperationFailedException;

  public abstract void delete(Group paramGroup) throws OperationFailedException;

  public abstract void save(Group paramGroup) throws OperationFailedException;

  public abstract void add(Role paramRole, Group paramGroup)
    throws OperationFailedException;

  public abstract void remove(Role paramRole, Group paramGroup)
    throws OperationFailedException;

  public abstract void setName(String paramString, Group paramGroup);

  public abstract void setDescription(String paramString, Group paramGroup);

  public abstract void setRoles(List<Role> paramList, Group paramGroup)
    throws OperationFailedException;

  public abstract Role getRole(String paramString);

  public abstract Role getRoleByName(String paramString);

  public abstract List<Role> getRoles();

  public abstract List<Role> getRoles(User paramUser);

  public abstract Role createRole(String paramString)
    throws OperationFailedException;

  public abstract Role createRole(String paramString, Right paramRight)
    throws OperationFailedException;

  public abstract void delete(Role paramRole) throws OperationFailedException;

  public abstract void save(Role paramRole) throws OperationFailedException;

  public abstract void setDescription(String paramString, Role paramRole);

  public abstract void setName(String paramString, Role paramRole);

  public abstract void setPermission(Right paramRight, Role paramRole);

  public abstract PaloConnection getConnection(String paramString);

  public abstract List<PaloConnection> getConnections();

  public abstract PaloConnection createConnection(String paramString1,
    String paramString2, String paramString3, int paramInt)
    throws OperationFailedException;

  public abstract void delete(PaloConnection paramPaloConnection)
    throws OperationFailedException;

  public abstract void save(PaloConnection paramPaloConnection)
    throws OperationFailedException;

  public abstract void setDescription(String paramString,
    PaloConnection paramPaloConnection);

  public abstract void setName(String paramString, PaloConnection paramPaloConnection);

  public abstract void setHost(String paramString, PaloConnection paramPaloConnection);

  public abstract void setService(String paramString,
    PaloConnection paramPaloConnection);

  public abstract void setType(int paramInt, PaloConnection paramPaloConnection);

  public abstract Account getAccount(String paramString);

  public abstract List<Account> getAccounts();

  public abstract List<Account> getAccounts(User paramUser);

  public abstract Account createAccount(String paramString1, String paramString2,
    User paramUser, PaloConnection paramPaloConnection)
    throws OperationFailedException;

  public abstract void delete(Account paramAccount) throws OperationFailedException;

  public abstract void save(Account paramAccount) throws OperationFailedException;

  public abstract void setConnection(PaloConnection paramPaloConnection,
    Account paramAccount);

  public abstract void setLoginName(String paramString, Account paramAccount);

  public abstract void setPassword(String paramString, Account paramAccount);

  public abstract void setUser(User paramUser, Account paramAccount);
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.services.AdministrationService JD-Core
 * Version: 0.5.4
 */