package org.jasig.portal.cas.authentication.handler.support;

import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

public class PortalPersonDirUserPasswordDao
  implements UserPasswordDao
{
  private static final String PERSON_DIR_QUERY = "SELECT ENCRPTD_PSWD FROM UP_PERSON_DIR WHERE USER_NAME = ?";
  private DataSource dataSource;
  private SimpleJdbcTemplate simpleJdbcTemplate;

  public DataSource getDataSource()
  {
    return this.dataSource;
  }

  public void setDataSource(DataSource dataSource)
  {
    this.dataSource = dataSource;
    this.simpleJdbcTemplate = new SimpleJdbcTemplate(this.dataSource);
  }

  public String getPasswordHash(String userName)
  {
    try
    {
      return (String)this.simpleJdbcTemplate.queryForObject("SELECT ENCRPTD_PSWD FROM UP_PERSON_DIR WHERE USER_NAME = ?", String.class, new Object[] { userName });
    } catch (EmptyResultDataAccessException e) {
    }
    return null;
  }
}