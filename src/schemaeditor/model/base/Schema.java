/**
 * @file:     Schema.java
 * @package:  safemanager.model.base
 * @author    Petr Fusek
 * @date      08.04.2018
 */
package schemaeditor.model.base;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for object reprezenting one Schema
 *
 * Schema is composed from Blocks and connections
 * @TODO: Schema shoul be implementing observable object to notify view of changes inside structure
 */
public class Schema // extends Observable
{
  protected List<SchemaBlock> _blocks;
  protected List<Connection> _connections;

  public enum EAddStatus
  {
    Ok,
    OutSourcePortNotFound,
    InDestPortNotfoud,
    SourcePortIncopatible,
    DestPortIncopatible,
    ConnectionCuseesCycles,
    OtherError;
  }

  /** Constructor */
  public Schema()
  {
    _blocks = new ArrayList<SchemaBlock>();
    _connections = new ArrayList<Connection>();
  }

  /** Adds block instance into schema */
  public Block AddBlock(Block block)
  {
    _blocks.add(new SchemaBlock(block));
    return block;
  }

  /** Removes block instance from schema */
  public void RemoveBlock(Block block)
  {
    // _blocks.remove(block);
  }


  /**
   * Adds block instance into schema, checks if connection does not broke any schema rule
   * @param connection new instance of connection to be added and to schema
   * @return True if connection is valid, false otherwise
   */
  public Connection AddConnection(Connection connection)
  {
    Connection newConn = null;
    if (TryValidateConnection(connection) == EAddStatus.Ok)
      if (_connections.add(connection))
        newConn =  connection;
    return newConn;
  }

  /**
   * Tries to determine if connection is valid
   * @param connection concection instance which will be validated in schema
   * @return EAddStatus result of attepmt
   * @note this method does not adds connection to schema.Use AddConnection after this method.
   */
  public EAddStatus TryValidateConnection(Connection connection)
  {
    return EAddStatus.OtherError;
  }

  /** Removes block instance from schema */
  public void RemoveConnection(Connection connection)
  {
    _connections.remove(connection);
  }

  // Iterators of collections

  /** Get Block list iterator */
  public List<Block> GetBlocks()
  {
    List<Block> list = new ArrayList<Block>();
    for (SchemaBlock schemaBlock : _blocks)
      list.add(schemaBlock.GetBlock());
    return list;
  }

  /** Get Block list iterator */
  public List<Connection> GetConnections()
  {
    return _connections;
  }

  /** Gets list of input (unconnected) ports of schema. */
  public List<Port> GetInputPorts()
  {
    List<Port> inPortList = new ArrayList<Port>();
    for (Block block : GetBlocks())
    {
      int portnum = 0;
      for (Port port : block.InputPorts)
      {
        boolean connected = false;
        for (Connection conn : _connections)
          if (connected = (conn.DestBlockID == block.ID && conn.DestPortNumber == portnum))
            break;
        if (!connected)
          inPortList.add(port);
        portnum++;
      }
    }
    return inPortList;
  }

  /** Gets list of output (unconnected) ports of schema. */
  public List<Port> GetOutPorts()
  {
    List<Port> inPortList = new ArrayList<Port>();
    // for(int i = 0; i < _blocks.size(); i++)
    // {
    //   inPortList.addAll(_blocks.get(i).OutputPorts);
    // }
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