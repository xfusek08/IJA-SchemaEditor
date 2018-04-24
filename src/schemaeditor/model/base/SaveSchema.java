/**
 * @file:     SaveSchema.java
 * @package:  safemanager.model.base
 * @author    Jaromír Franěk
 * @date      23.04.2018
 */
package schemaeditor.model.base;

import schemaeditor.model.base.enums.EAddStatus;

import java.util.*;
import schemaeditor.model.base.SchemaBlock;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * Class for object reprezenting one Schema
 *
 * Schema is composed from Blocks and connections
 */
@XmlRootElement(namespace = "schemaeditor.model.base")
@XmlAccessorType (XmlAccessType.FIELD)
public class SaveSchema
{
  @XmlElementWrapper(name = "_blocks")
  @XmlElement(name = "SchemaBlock")
  private Set<SaveSchemaBlock> blocks = null;
  @XmlElementWrapper(name = "_connections")
  @XmlElement(name = "connection")
  private Set<SaveConnection> connection = null;

  public void setBlock(Set<SaveSchemaBlock> blocks) 
  {
    this.blocks = blocks;
  }

  public Set<SaveSchemaBlock> getBlock() 
  {
    return blocks;
  }

  public void setConn(Set<SaveConnection> conn) 
  {
    this.connection = conn;
  }

  public Set<SaveConnection> getConn() 
  {
    return connection;
  }

}