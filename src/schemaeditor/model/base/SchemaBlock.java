/**
 * @file:     SchemaBlock.java
 * @package:  schemaeditor.model.base
 * @author    Petr Fusek
 * @date      16.04.2018
 */
package schemaeditor.model.base;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

class SchemaBlock
{
  protected Set<UUID> _precedestors;
  protected Set<Integer> _freeInPorts;
  protected Set<Integer> _freeOutPorts;

  protected Block _block;

  public SchemaBlock(Block block)
  {
    _block = block;
    _precedestors = new HashSet<UUID>();
    _freeInPorts = new HashSet<Integer>();
    _freeOutPorts = new HashSet<Integer>();
  }

  public Set<UUID> GetPrecedestors()
  {
    return _precedestors;
  }

  public Set<Integer> GetFreeInPorts()
  {
    return _freeInPorts;
  }

  public Set<Integer> GetFreeOutPorts()
  {
    return _freeOutPorts;
  }

  public Block GetBlock()
  {
    return _block;
  }

  /** True pokud nebyl jiz zapojen */
  public boolean ConnectInPort(int portNum, Set<UUID> sourcePrecedestors)
  {
    if (_freeInPorts.add(portNum))
    {
      // connected unconnected port
      _precedestors.addAll(sourcePrecedestors);
      return true;
    }
    _precedestors.clear();
    return false;
  }

  /** True pokud nebyl jiz zapojen */
  public boolean ConnectOutPort(int portNum)
  {
    return _freeOutPorts.add(portNum);
  }

  public void Clean()
  {
    _freeInPorts.clear();
    _freeInPorts.clear();
    _precedestors.clear();
  }

  public boolean equals(Object obj)
  {
    if (obj instanceof SchemaBlock)
      return _block.equals(((SchemaBlock)obj).GetBlock());
    return false;
  }

  public int hashCode()
  {
    return _block.hashCode();
  }
}
