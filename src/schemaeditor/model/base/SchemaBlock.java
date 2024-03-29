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

/**
 * Class representing SchemaBlock
 */
class SchemaBlock
{
  protected Set<UUID> _Predecessors;
  protected Set<Integer> _freeInPorts;
  protected Set<Integer> _freeOutPorts;
  protected Block _block;

  /**
   * Constructor
   * @param block block held in schemaBlock
   */
  public SchemaBlock(Block block)
  {
    _block = block;
    _Predecessors = new HashSet<UUID>();
    _freeInPorts = new HashSet<Integer>();
    _freeOutPorts = new HashSet<Integer>();
  }

  /**
   * Return all Predecessors of block
   * @return _Predecessors
   */
  public Set<UUID> GetPredecessors()
  {
    return _Predecessors;
  }

  /**
   * Add Predecessor to Predecessors
   * @param ID ID of Predecessor
   */
  public void AddPredecessor(UUID ID)
  {
    _Predecessors.add(ID);
  }

  /**
   * Add all Predecessor to Predecessors
   * @param IDs set of all predecestors ID to be added
   */
  public void AddAllPredecessor(Set<UUID> IDs)
  {
    _Predecessors.addAll(IDs);
  }

  /**
   * Return iterable of free in ports
   * @return Set of _freeInPorts
   */
  public Set<Integer> GetFreeInPorts()
  {
    return _freeInPorts;
  }

  /**
   * Return iterable of free out ports
   * @return Set of _freeOutPorts
   */
  public Set<Integer> GetFreeOutPorts()
  {
    return _freeOutPorts;
  }

  /**
   * Return block
   * @return Block that is held inside schemaBlock
   */
  public Block GetBlock()
  {
    return _block;
  }

  /**
   * Link InPort with another port
   * @param portNum port to be connected
   * @param sourcePredecessors Predecessors of connected block
   * @return true if was not connect
   */
  public boolean ConnectInPort(int portNum, Set<UUID> sourcePredecessors)
  {
    if (_freeInPorts.add(portNum))
    {
      // connected unconnected port
      _Predecessors.addAll(sourcePredecessors);
      return true;
    }
    _Predecessors.clear();
    return false;
  }

  /**
   * Link outPort with another port
   * @param portNum port to be connected
   * @return true if was not connect
   */
  public boolean ConnectOutPort(int portNum)
  {
    return _freeOutPorts.add(portNum);
  }

  /**
   * Clear values of ports and Predecessors
   * @note this method does not clear _block
   */
  public void Clean()
  {
    _freeInPorts.clear();
    _freeOutPorts.clear();
    _Predecessors.clear();
  }

  /**
   * Compares two schemaBlocks
   * @param obj object that will be compared
   * @return true if block are equal
   */
  public boolean equals(Object obj)
  {
    if (obj instanceof SchemaBlock)
      return _block.equals(((SchemaBlock)obj).GetBlock());
    return false;
  }

  /**
   * Return block hash code
   * @return hashCode of block inside schemaBlock
   */
  public int hashCode()
  {
    return _block.hashCode();
  }
}
