/*
 * Copyright 2007 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.ja-sig.org/products/cas/overview/license/
 */
package org.jasig.cas.ticket;

import org.jasig.cas.authentication.Authentication;
import org.jasig.cas.authentication.principal.Service;

/**
 * Interface for a Service Ticket. A service ticket is used to grant access to a
 * specific service for a principal. A Service Ticket is generally a one-time
 * use ticket.
 * 
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.0
 */
public interface ServiceTicket extends Ticket {

  /** Prefix generally applied to unique ids generated by UniqueIdGenenerator. */
  String PREFIX = "ST";

  /**
   * Retrieve the service this ticket was given for.
   * 
   * @return the server.
   */
  Service getService();

  /**
   * Determine if this ticket was created at the same time as a
   * TicketGrantingTicket.
   * 
   * @return true if it is, false otherwise.
   */
  boolean isFromNewLogin();

  boolean isValidFor(Service service);

  /**
   * Method to grant a TicketGrantingTicket from this service to the
   * authentication. Analogous to the ProxyGrantingTicket.
   * 
   * @param id The unique identifier for this ticket.
   * @param authentication The Authentication we wish to grant a ticket for.
   * @return The ticket granting ticket.
   */
  TicketGrantingTicket grantTicketGrantingTicket(String id,
    Authentication authentication, ExpirationPolicy expirationPolicy);
}
