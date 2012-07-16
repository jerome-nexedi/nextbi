/*     */ package org.palo.viewapi.internal;
/*     */ 
/*     */ import java.sql.SQLException;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.palo.api.Hierarchy;
/*     */ import org.palo.api.exceptions.PaloIOException;
/*     */ import org.palo.api.parameters.ParameterReceiver;
/*     */ import org.palo.api.subsets.Subset2;
/*     */ import org.palo.viewapi.Account;
/*     */ import org.palo.viewapi.AuthUser;
/*     */ import org.palo.viewapi.CubeView;
/*     */ import org.palo.viewapi.Group;
/*     */ import org.palo.viewapi.PaloConnection;
/*     */ import org.palo.viewapi.Right;
/*     */ import org.palo.viewapi.Role;
/*     */ import org.palo.viewapi.User;
/*     */ import org.palo.viewapi.View;
/*     */ import org.palo.viewapi.exceptions.NoAccountException;
/*     */ import org.palo.viewapi.exceptions.OperationFailedException;
/*     */ import org.palo.viewapi.internal.dbmappers.MapperRegistry;
/*     */ import org.palo.viewapi.services.FolderService;
/*     */ import org.palo.viewapi.services.ServiceProvider;
/*     */ 
/*     */ public class FolderServiceImpl extends InternalService
/*     */   implements FolderService
/*     */ {
/*     */   FolderServiceImpl(AuthUser user)
/*     */   {
/*  61 */     super(user);
/*  62 */     IFolderManagement folderMgmt = 
/*  63 */       MapperRegistry.getInstance().getFolderManagement();
/*  64 */     folderMgmt.setUser(user);
/*     */   }
/*     */ 
/*     */   public void add(Role role, ExplorerTreeNode toNode) throws OperationFailedException
/*     */   {
/*  69 */     if (toNode.hasRole(role))
/*     */       return;
/*  71 */     AbstractExplorerTreeNode node = (AbstractExplorerTreeNode)toNode;
/*  72 */     node.add(role);
/*     */     try {
/*  74 */       getFolderManagement().update(node);
/*     */     }
/*     */     catch (SQLException e) {
/*  77 */       node.remove(role);
/*  78 */       throw new OperationFailedException(
/*  79 */         "Failed to modify tree node", e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public DynamicFolder createDynamicFolder(String name, ExplorerTreeNode parent, Hierarchy sourceHierarchy, Subset2 sourceSubset, PaloConnection con)
/*     */     throws OperationFailedException
/*     */   {
/*  88 */     AccessController.checkPermission(Right.CREATE, this.user);
/*     */     try
/*     */     {
/*  91 */       DynamicFolder folder = new DynamicFolder(parent, sourceHierarchy, 
/*  92 */         sourceSubset, name);
/*  93 */       folder.setOwner(this.user);
/*     */ 
/*  95 */       folder.setConnectionId("");
/*     */ 
/*  97 */       Role ownerRole = (Role)getRoleManagement().findByName("owner");
/*  98 */       if (ownerRole != null) {
/*  99 */         this.user.add(ownerRole);
/*     */       }
/* 101 */       getFolderManagement().insert(folder);
/* 102 */       return folder;
/*     */     } catch (SQLException e) {
/* 104 */       throw new OperationFailedException(
/* 105 */         "Failed to create dynamic folder", e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public FolderElement createFolderElement(String name, ExplorerTreeNode parent, PaloConnection con)
/*     */     throws OperationFailedException
/*     */   {
/*     */     try
/*     */     {
/* 115 */       FolderElement folder = new FolderElement(parent, name);
/* 116 */       folder.setOwner(this.user);
/*     */ 
/* 118 */       folder.setConnectionId("");
/*     */ 
/* 120 */       Role ownerRole = (Role)getRoleManagement().findByName("owner");
/* 121 */       if (ownerRole != null) {
/* 122 */         this.user.add(ownerRole);
/*     */       }
/* 124 */       getFolderManagement().insert(folder);
/* 125 */       return folder;
/*     */     } catch (SQLException e) {
/* 127 */       throw new OperationFailedException(
/* 128 */         "Failed to create folder element", e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public StaticFolder createStaticFolder(String name, ExplorerTreeNode parent, PaloConnection con)
/*     */     throws OperationFailedException
/*     */   {
/* 135 */     AccessController.checkPermission(Right.CREATE, this.user);
/* 136 */     return createStaticFolderUnchecked(name, parent, con);
/*     */   }
/*     */ 
/*     */   private StaticFolder createStaticFolderUnchecked(String name, ExplorerTreeNode parent, PaloConnection con) throws OperationFailedException
/*     */   {
/*     */     try
/*     */     {
/* 143 */       StaticFolder folder = new StaticFolder(parent, name);
/* 144 */       folder.setOwner(this.user);
/*     */ 
/* 146 */       folder.setConnectionId("");
/*     */ 
/* 148 */       Role ownerRole = (Role)getRoleManagement().findByName("owner");
/* 149 */       if (ownerRole != null) {
/* 150 */         this.user.add(ownerRole);
/*     */       }
/* 152 */       getFolderManagement().insert(folder);
/* 153 */       return folder;
/*     */     } catch (SQLException e) {
/* 155 */       throw new OperationFailedException("Failed to create view", e);
/*     */     }
/*     */   }
/*     */ 
/*     */   private StaticFolder createStaticFolderUnchecked(String name, ExplorerTreeNode parent, String id)
/*     */     throws OperationFailedException
/*     */   {
/*     */     try
/*     */     {
/* 164 */       StaticFolder folder = StaticFolder.internalCreate(parent, id, name);
/* 165 */       folder.setOwner(this.user);
/*     */ 
/* 167 */       folder.setConnectionId("");
/*     */ 
/* 169 */       Role ownerRole = (Role)getRoleManagement().findByName("owner");
/* 170 */       if (ownerRole != null) {
/* 171 */         this.user.add(ownerRole);
/*     */       }
/* 173 */       getFolderManagement().insert(folder);
/* 174 */       return folder;
/*     */     } catch (SQLException e) {
/* 176 */       throw new OperationFailedException("Failed to create view", e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void delete(ExplorerTreeNode node) throws OperationFailedException {
/* 181 */     if (!doesTreeNodeExist(node)) {
/* 182 */       return;
/*     */     }
/* 184 */     AccessController.checkPermission(Right.DELETE, node, this.user);
/*     */     try
/*     */     {
/* 187 */       for (ExplorerTreeNode n : node.getChildren()) {
/* 188 */         delete(n);
/*     */       }
/* 190 */       if (node.getParent() != null) {
/* 191 */         node.getParent().removeChild(node);
/*     */       }
/* 193 */       getFolderManagement().delete(node);
/*     */     } catch (SQLException e) {
/* 195 */       throw new OperationFailedException("Failed to delete tree node", e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean doesTreeNodeExist(ExplorerTreeNode node) {
/* 200 */     return (node != null) && (getTreeNode(node.getId()) != null);
/*     */   }
/*     */ 
/*     */   public ExplorerTreeNode getTreeNode(String id) {
/* 204 */     AccessController.checkPermission(Right.READ, this.user);
/*     */     try {
/* 206 */       return (ExplorerTreeNode)getFolderManagement().find(id);
/*     */     } catch (SQLException localSQLException) {
/*     */     }
/* 209 */     return null;
/*     */   }
/*     */ 
/*     */   private final void getAllViews(ExplorerTreeNode root, Set<String> allViews) {
/* 213 */     if (root instanceof FolderElement) {
/* 214 */       ParameterReceiver pr = ((FolderElement)root).getSourceObject();
/* 215 */       if (pr instanceof CubeView)
/* 216 */         allViews.add(((CubeView)pr).getId());
/* 217 */       else if (pr instanceof View) {
/* 218 */         allViews.add(((View)pr).getId());
/*     */       }
/*     */     }
/* 221 */     for (ExplorerTreeNode nd : root.getChildren())
/* 222 */       getAllViews(nd, allViews);
/*     */   }
/*     */ 
/*     */   private final void addAllViews(ExplorerTreeNode rootNode)
/*     */   {
/* 227 */     Set allViews = new LinkedHashSet();
/* 228 */     getAllViews(rootNode, allViews);
/*     */ 
/* 230 */     ExplorerTreeNode targetNode = null;
/*     */ 
/* 232 */     List<View> existingViews = null;
/*     */     try {
/* 234 */       existingViews = getViewManagement().listViews();
/*     */     } catch (SQLException e) {
/* 236 */       e.printStackTrace();
/*     */     }
/* 238 */     HashSet userCons = new HashSet();
/* 239 */     for (Account a : this.user.getAccounts()) {
/* 240 */       userCons.add(a.getConnection().getId());
/*     */     }
/*     */ 
/* 243 */     if (existingViews != null) {
/* 244 */       for (View v : existingViews) {
/* 245 */         if (!allViews.contains(v.getId())) {
/* 246 */           String conId = v.getAccount().getConnection().getId();
/* 247 */           if (userCons.contains(conId)) {
/* 248 */             allViews.add(v.getId());
/*     */             try
/*     */             {
/* 251 */               if (targetNode == null) {
/* 252 */                 ExplorerTreeNode node = findTreeNode(rootNode, this.user.getId() + "foreignViews");
/* 253 */                 if (node == null)
/*     */                   try {
/* 255 */                     node = createStaticFolderUnchecked("Recently created views", rootNode, this.user.getId() + "foreignViews");
/*     */                   }
/*     */                   catch (OperationFailedException localOperationFailedException1) {
/*     */                   }
/* 259 */                 targetNode = (node == null) ? rootNode : node;
/*     */               }
/* 261 */               FolderElement fe = createFolderElement(v.getName(), targetNode, null);
/* 262 */               fe.setSourceObject(v);
/*     */             } catch (OperationFailedException e) {
/* 264 */               e.printStackTrace();
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     try
/*     */     {
/* 272 */       save(rootNode);
/*     */     } catch (OperationFailedException e) {
/* 274 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   private final ExplorerTreeNode findTreeNode(ExplorerTreeNode root, String id) {
/* 278 */     if (root == null) {
/* 279 */       return null;
/*     */     }
/* 281 */     if (root.getId().equals(id)) {
/* 282 */       return root;
/*     */     }
/* 284 */     for (ExplorerTreeNode kid : root.getChildren()) {
/* 285 */       ExplorerTreeNode res = findTreeNode(kid, id);
/* 286 */       if (res != null) {
/* 287 */         return res;
/*     */       }
/*     */     }
/* 290 */     return null;
/*     */   }
/*     */ 
/*     */   private final void addOwnerViews(ExplorerTreeNode rootNode) {
/* 294 */     Set allViews = new LinkedHashSet();
/* 295 */     getAllViews(rootNode, allViews);
/*     */ 
/* 297 */     ExplorerTreeNode targetNode = null;
/* 298 */     HashSet userCons = new HashSet();
/* 299 */     for (Account a : this.user.getAccounts()) {
/* 300 */       userCons.add(a.getConnection().getId());
/*     */     }
/*     */ 
/* 303 */     List<View> roleViews = null;
/*     */     try {
/* 305 */       roleViews = getViewManagement().findViews(this.user);
/*     */     } catch (SQLException e) {
/* 307 */       e.printStackTrace();
/*     */     }
/* 309 */     if (roleViews != null)
/* 310 */       for (View v : roleViews)
/* 311 */         if (!allViews.contains(v.getId())) {
/* 312 */           String conId = v.getAccount().getConnection().getId();
/* 313 */           if (userCons.contains(conId)) {
/* 314 */             allViews.add(v.getId());
/*     */             try
/*     */             {
/* 317 */               if (targetNode == null) {
/* 318 */                 ExplorerTreeNode node = findTreeNode(rootNode, this.user.getId() + "foreignViews");
/* 319 */                 if (node == null)
/*     */                   try {
/* 321 */                     node = createStaticFolderUnchecked("Recently created views", rootNode, this.user.getId() + "foreignViews");
/*     */                   }
/*     */                   catch (OperationFailedException localOperationFailedException1) {
/*     */                   }
/* 325 */                 targetNode = (node == null) ? rootNode : node;
/*     */               }
/* 327 */               FolderElement fe = createFolderElement(v.getName(), targetNode, null);
/* 328 */               fe.setSourceObject(v);
/*     */             } catch (OperationFailedException e) {
/* 330 */               e.printStackTrace();
/*     */             }
/*     */           }
/*     */         }
/*     */   }
/*     */ 
/*     */   private final void addRoleViews(ExplorerTreeNode rootNode)
/*     */   {
/* 344 */     Set allViews = new LinkedHashSet();
/* 345 */     getAllViews(rootNode, allViews);
/*     */ 
/* 347 */     ExplorerTreeNode targetNode = null;
/* 348 */     HashSet<Role> allRoles = new HashSet();
/*     */ 
/* 350 */     for (Role r : this.user.getRoles())
/* 351 */       allRoles.add(r);
/* 353 */     for (Group g : this.user.getGroups()) {
/* 354 */       for (Iterator localIterator2 = g.getRoles().iterator(); localIterator2.hasNext(); ) { Role r = (Role)localIterator2.next();
/* 355 */         allRoles.add(r); }
/*     */ 
/*     */     }
/* 358 */     HashSet userCons = new HashSet();
/* 359 */     for (Account a : this.user.getAccounts()) {
/* 360 */       userCons.add(a.getConnection().getId());
/*     */     }
/*     */ 
/* 363 */     for (Role r : allRoles) {
/* 364 */       List<View> roleViews = null;
/*     */       try {
/* 366 */         roleViews = getViewManagement().findViews(r);
/*     */       } catch (SQLException e) {
/* 368 */         e.printStackTrace();
/*     */       }
/* 370 */       if (roleViews != null) {
/* 371 */         for (View v : roleViews)
/* 372 */           if (!allViews.contains(v.getId())) {
/* 373 */             String conId = v.getAccount().getConnection().getId();
/* 374 */             if (userCons.contains(conId)) {
/* 375 */               allViews.add(v.getId());
/*     */               try
/*     */               {
/* 378 */                 if (targetNode == null) {
/* 379 */                   ExplorerTreeNode node = findTreeNode(rootNode, this.user.getId() + "foreignViews");
/* 380 */                   if (node == null)
/*     */                     try {
/* 382 */                       node = createStaticFolderUnchecked("Recently created views", rootNode, this.user.getId() + "foreignViews");
/*     */                     }
/*     */                     catch (OperationFailedException localOperationFailedException1) {
/*     */                     }
/* 386 */                   targetNode = (node == null) ? rootNode : node;
/*     */                 }
/* 388 */                 FolderElement fe = createFolderElement(v.getName(), targetNode, null);
/* 389 */                 fe.setSourceObject(v);
/*     */               } catch (OperationFailedException e) {
/* 391 */                 e.printStackTrace();
/*     */               }
/*     */             }
/*     */           }
/*     */       }
/*     */     }
/*     */     try
/*     */     {
/* 399 */       save(rootNode);
/*     */     } catch (OperationFailedException e) {
/* 401 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public ExplorerTreeNode getTreeRoot() {
/* 406 */     AccessController.checkPermission(Right.READ, this.user);
/* 407 */     ExplorerTreeNode rootNode = null;
/*     */     try
/*     */     {
/* 411 */       getFolderManagement().getFolders(this.user);
/*     */       try
/*     */       {
/* 414 */         rootNode = FolderModel.getInstance().load(this.user);
/*     */       } catch (PaloIOException localPaloIOException) {
/*     */       }
/*     */     } catch (SQLException e) {
/* 418 */       e.printStackTrace();
/*     */     }
/* 420 */     if (rootNode == null) {
/* 421 */       String fn = this.user.getFirstname();
/* 422 */       String ln = this.user.getLastname();
/* 423 */       String name = (((fn == null) ? "" : fn) + " " + ((ln == null) ? "" : ln))
/* 424 */         .trim();
/* 425 */       if (name.length() == 0)
/* 426 */         name = this.user.getLoginName();
/*     */       try
/*     */       {
/* 429 */         rootNode = createStaticFolderUnchecked(name, null, "");
/*     */       }
/*     */       catch (OperationFailedException localOperationFailedException) {
/*     */       }
/*     */     }
/* 434 */     addOwnerViews(rootNode);
/* 435 */     if (ServiceProvider.isAdmin(this.user))
/* 436 */       addAllViews(rootNode);
/*     */     else {
/* 438 */       addRoleViews(rootNode);
/*     */     }
/*     */ 
/* 448 */     return rootNode;
/*     */   }
/*     */ 
/*     */   public void remove(Role role, ExplorerTreeNode fromNode) throws OperationFailedException
/*     */   {
/* 453 */     if (!fromNode.hasRole(role)) {
/*     */       return;
/*     */     }
/* 456 */     AbstractExplorerTreeNode node = (AbstractExplorerTreeNode)fromNode;
/* 457 */     node.remove(role);
/*     */     try {
/* 459 */       getFolderManagement().update(node);
/*     */     }
/*     */     catch (SQLException e) {
/* 462 */       node.add(role);
/* 463 */       throw new OperationFailedException("Failed to modify node", e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void save(ExplorerTreeNode root) throws OperationFailedException
/*     */   {
/*     */     try
/*     */     {
/*     */       try
/*     */       {
/* 473 */         FolderModel.getInstance().save(this.user, root);
/*     */       } catch (PaloIOException e) {
/* 475 */         throw new OperationFailedException("Failed to save node", e);
/*     */       }
/* 477 */       getFolderManagement().update(root);
/*     */     } catch (SQLException e) {
/* 479 */       throw new OperationFailedException("Failed to save node", e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setName(String name, ExplorerTreeNode ofNode)
/*     */   {
/* 485 */     AbstractExplorerTreeNode node = (AbstractExplorerTreeNode)ofNode;
/* 486 */     node.setName(name);
/*     */   }
/*     */ 
/*     */   public void setOwner(User owner, ExplorerTreeNode ofNode)
/*     */   {
/* 491 */     ((AbstractExplorerTreeNode)ofNode).setOwner(owner);
/*     */   }
/*     */ 
/*     */   private final Account getAccount(PaloConnection forConnection) throws SQLException
/*     */   {
/* 496 */     Account account = getAccountManagement().findBy(this.user, forConnection);
/* 497 */     if (account == null)
/* 498 */       throw new NoAccountException(this.user, forConnection, "User '" + 
/* 499 */         this.user.getLastname() + "' has no account on '" + 
/* 500 */         forConnection.getHost() + "'");
/* 501 */     return account;
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi.jar
 * Qualified Name:     org.palo.viewapi.internal.FolderServiceImpl
 * JD-Core Version:    0.5.4
 */