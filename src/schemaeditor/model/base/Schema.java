/**
 * @file:     Schema.java
 * @package:  safemanager.model.base
 * @author    Petr Fusek
 * @date      08.04.2018
 */
package schemaeditor.model.base;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Class for object reprezenting one Schema
 *
 * Schema is composed from Blocks and connections
 * @TODO: Schema shoul be implementing observable object to notify view of changes inside structure
 */
public class Schema // extends Observable
{
  protected List<Block> _blocks;
  protected List<Connection> _connections;

  /** Constructor */
  public Schema()
  {
    _blocks = new ArrayList<Block>();
    _connections = new ArrayList<Connection>();
  }

  /** Adds block instance into schema */
  public Block AddBlock(Block block)
  {
    if (!_blocks.contains(block))
      _blocks.add(block);
    return block;
  }

  /** Removes block instance from schema */
  public void RemoveBlock(Block block)
  {
    _blocks.remove(block);
  }


  /** Adds block instance into schema */
  public Connection AddConnection(Connection connection)
  {
    _connections.add(connection);
    return connection;
  }

  /** Removes block instance from schema */
  public void RemoveConnection(Connection connection)
  {
    _connections.remove(connection);
  }

  // Iterators of collections

  /** Get Block list iterator */
  public Iterable<Block> GetBlocks()
  {
    return _blocks;
  }

  /** Get Block list iterator */
  public Iterable<Connection> GetConnections()
  {
    return _connections;
  }

  /** Gets list of input (unconnected) ports of schema. */
  public Iterable<Port> GetInputPorts()
  {
    List<Port> inPortList = new ArrayList<Port>();
    for(int i = 0; i < _blocks.size(); i++)
    {
      inPortList.addAll(_blocks.get(i).InputPorts);
    }
    return inPortList;
  }

  /** Gets list of output (unconnected) ports of schema. */
  public Iterable<Port> GetOutPorts()
  {
    List<Port> inPortList = new ArrayList<Port>();
    for(int i = 0; i < _blocks.size(); i++)
    {
      inPortList.addAll(_blocks.get(i).OutputPorts);
    }
    return inPortList;
  }

  // Calculation controll

  /** Runs calculation */
  public boolean RunCalculation()
  {
    return false;
  }

  /** Starts debug calculation and prepares it to stepping */
  public void StartCalculation()
  {
  }

  /** Execute one level of calculation */
  public boolean StepCalculation()
  {
    return false;
  }

  /** Resets all blocks into initial state before calculation */
  public void StopCalculation()
  {
  }
}