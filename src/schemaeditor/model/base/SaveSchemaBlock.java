/**
 * @file:     SaveSchemaBlock.java
 * @package:  schemaeditor.model.base
 * @author    Jaromír Franěk
 * @date      23.04.2018
 */
package schemaeditor.model.base;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import schemaeditor.model.base.*;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * Class representing SchemaBlock
 */
@XmlRootElement(name = "SchemaBlock")
@XmlAccessorType (XmlAccessType.FIELD)
public class SaveSchemaBlock
{
  private Set<UUID> _precedestors = null;
  private Set<Integer> _freeInPorts = null;
  private Set<Integer> _freeOutPorts = null;
  private SaveBlock _sblock = new SaveBlock();

  public void setPrecedestors(Set<UUID> _precedestors)
  {
    this._precedestors = _precedestors;
  }
  
  public Set<UUID> getPrecedestors()
  {
    return _precedestors;
  }

  public void setFreeInPorts(Set<Integer> _freeInPorts)
  {
    this._freeInPorts = _freeInPorts;
  }
  
  public Set<Integer> getFreeInPorts()
  {
    return _freeInPorts;
  }

  public void setFreeOutPorts(Set<Integer> _freeOutPorts)
  {
    this._freeOutPorts = _freeOutPorts;
  }
  
  public Set<Integer> getFreeOutPorts()
  {
    return _freeOutPorts;
  }

  public void setBlock(SaveBlock _sblock)
  {
    this._sblock = _sblock;
  }
  
  public SaveBlock getBlock()
  {
    return _sblock;
  }

  public void setFromSchema(SchemaBlock _block)
  {
    this._precedestors = _block.GetPrecedestors();
    this._freeInPorts = _block.GetFreeInPorts();
    this._freeOutPorts = _block.GetFreeOutPorts();
    this._sblock.setFromSchema(_block.GetBlock());
  }

  public SchemaBlock getFromSave()
  {
    SchemaBlock _block = new SchemaBlock();
    _block._precedestors = this._precedestors;
    _block._freeInPorts = this._freeInPorts;
    _block._freeOutPorts = this._freeOutPorts;
    _block._block = this._sblock.getFromSave();

    return _block;
  }
}
