/**
 * @file:     Schema.java
 * @package:  safemanager.model.base
 * @author    Petr Fusek
 * @date      08.04.2018
 */
package schemaeditor.model.base;

import schemaeditor.model.base.*;

import java.util.*;
import java.util.stream.Stream;

/**
 * Class for object reprezenting one Schema
 *
 * Schema is composed from Blocks and connections
 * @TODO: Schema should be implementing observable object to notify view of changes inside structure
 */
public class Schema // extends Observable
{
  public Set<SchemaBlock> _blocks;
  public Set<Connection> _connections;

  /** Constructor */
  public Schema()
  {
    _blocks = new HashSet<SchemaBlock>();
    _connections = new HashSet<Connection>();
  }

  /** Constructor to load from save */
  public Schema(SaveSchema save)
  {
    _blocks = new HashSet<SchemaBlock>();
    _connections = new HashSet<Connection>();
    for(SaveSchemaBlock block : save.getBlock())
    {
      SchemaBlock saveBlock = new SchemaBlock();
      saveBlock = block.getFromSave();
      _blocks.add(saveBlock);
    }
    for(SaveConnection conn : save.getConn())
    {
      Connection saveConn = new Connection();
      saveConn = conn.getFromSave();
      _connections.add(saveConn);
    }
  }

  /** Adds block instance into schema */
  public Block AddBlock(Block block)
  {
    SchemaBlock newSBlock = new SchemaBlock(block);
    if (!_blocks.contains(newSBlock))
      _blocks.add(newSBlock);
    return block;
  }

  /** Removes block instance from schema */
  public void RemoveBlock(Block block)
  {
    _blocks.remove(GetSchemaBlockById(block.ID));
    List<Connection> toRemove = new ArrayList<Connection>();
    for (Connection conn : GetConnections())
      if (conn.DestBlockID == block.ID || conn.SourceBlockID == block.ID)
        toRemove.add(conn);
    for (Connection conn : toRemove)
      RemoveConnection(conn);
    CalculateSchemaConnections();
  }

  /**
   * Adds block instance into schema, checks if connection does not broke any schema rule
   * @param connection new instance of connection to be added and to schema
   * @return True if connection is valid, false otherwise
   */
  public Connection AddConnection(Connection connection)
  {
    return AddConnection(connection, true, false);
  }

  /**
   * Tries to determine if connection is valid
   * @param connection concection instance which will be validated in schema
   * @return EAddStatus result of attepmt
   * @note this method does not adds connection to schema.Use AddConnection after this method.
   */
  public EAddStatus TryValidateConnection(Connection connection)
  {
    SchemaBlock prec = null;
    Port in = null;
    Port out = null;
    if(connection == null)
      return EAddStatus.OtherError;
    if(connection.SourceBlockID == null)
      return EAddStatus.OutSourcePortNotFound;
    if(connection.DestBlockID == null)
      return EAddStatus.InDestPortNotfoud;
    for (SchemaBlock schemaBlock : _blocks)
    {
      if(schemaBlock._block.ID == connection.SourceBlockID)
        out = schemaBlock._block.OutputPorts.get(connection.SourcePortNumber);
      else if(schemaBlock._block.ID == connection.DestBlockID)
        in = schemaBlock._block.InputPorts.get(connection.DestPortNumber);
    }
    if(out == null)
      return EAddStatus.OutSourcePortNotFound;
    if(in == null)
      return EAddStatus.InDestPortNotfoud;
    if(!in.Compatible(out._data) || !out.Compatible(out._data))
      return EAddStatus.PortsIncopatible;
    for (SchemaBlock schemaBlock : _blocks)
      if(schemaBlock._block.ID == connection.SourceBlockID)
        prec = schemaBlock;
    for (UUID ID : prec.GetPrecedestors())
      if(ID == connection.DestBlockID)
        return EAddStatus.ConnectionCuseesCycles;
    return EAddStatus.Ok;
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
  public Set<Connection> GetConnections()
  {
    return _connections;
  }

  /** Gets list of input (unconnected) ports of schema. */
  public List<Port> GetInputPorts()
  {
    Dictionary<SchemaBlock, List<Port>> inputdict = GetInputSchemaBlocksWithInputPorts();
    List<Port> inPortList = new ArrayList<Port>();
    Enumeration<List<Port>> values = inputdict.elements();
    while(values.hasMoreElements())
      inPortList.addAll(values.nextElement());
    return inPortList;
  }

  /** Gets list of output (unconnected) ports of schema. */
  public List<Port> GetOutPorts()
  {
    Dictionary<SchemaBlock, List<Port>> outputdict = GetOutpuSchemaBlocksWithOutpuPorts();
    List<Port> outPortList = new ArrayList<Port>();
    Enumeration<List<Port>> values = outputdict.elements();
    while(values.hasMoreElements())
      outPortList.addAll(values.nextElement());
    return outPortList;
  }

  public Set<Block> GetInBlocks()
  {
    Dictionary<SchemaBlock, List<Port>> inputdict = GetInputSchemaBlocksWithInputPorts();
    Set<Block> res = new HashSet<Block>();
    Enumeration<SchemaBlock> keys = inputdict.keys();
    while(keys.hasMoreElements())
      res.add(keys.nextElement().GetBlock());
    return res;
  }

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

  /************************************************ PROTECTED ***************************************************/

  /**
   * Cleans all informations from all schemablocks and use bfs to fill new precedestors to blocks and check validity of connections.
   * If some invalid connection occures then such a connection is thrown away. By invalid is considered connection causing loops.
   * It uses AddConnection method without adding to collection (doNotAdd = false) for update state of scheamblocks.
   */
  protected void CalculateSchemaConnections()
  {
    _blocks.stream().forEach(sb -> sb.Clean());
    List<SchemaBlock> openQueue = new ArrayList<SchemaBlock>();
    Dictionary<SchemaBlock, List<Port>> inputDict = GetInputSchemaBlocksWithInputPorts();
    Set<Connection> connections = GetConnections();
    //Nahrani prvnich blocku do fronty
    for(Enumeration blocks = inputDict.keys(); blocks.hasMoreElements();)
    {
      openQueue.add((SchemaBlock)blocks.nextElement());
    }
    ListIterator<SchemaBlock> queue = openQueue.listIterator();
    while(queue.hasNext())
    {
      SchemaBlock block = queue.next();
      for(Connection connection : connections)
      {
        //Pro kazdy block najdu jeho konekci predchudce a ziskam predchudce
        if(connection.DestBlockID == block.GetBlock().ID)
        {
          SchemaBlock preBlock = null;
          for(SchemaBlock sBlock : openQueue)
            if(connection.SourceBlockID == sBlock.GetBlock().ID)
              preBlock = sBlock;
          if(preBlock != null)
          {
            block._precedestors.addAll(preBlock.GetPrecedestors());
            block._precedestors.add(preBlock.GetBlock().ID);
            queue.set(block);
          }
          AddConnection(connection, true, false);
        }
        //Bloky na ktere vede nahraju do pole
        if(connection.SourceBlockID == block.GetBlock().ID)
        {
          SchemaBlock afBlock = null;
          for(SchemaBlock sBlock : _blocks)
            if(connection.DestBlockID == sBlock.GetBlock().ID)
              afBlock = sBlock;
          if(afBlock != null)
            queue.add(afBlock);
        }
      }
    }
  }

  private SchemaBlock GetSchemaBlockById(UUID id)
  {
    Optional<SchemaBlock> opt  = _blocks.stream().filter(sb -> sb.GetBlock().ID == id).findFirst();
    if (opt.isPresent())
      return opt.get();
    return null;
  }

  protected Dictionary<SchemaBlock, List<Port>> GetInputSchemaBlocksWithInputPorts()
  {
    Dictionary<SchemaBlock, List<Port>> result = new Hashtable<SchemaBlock, List<Port>>();
    for (SchemaBlock block : _blocks)
    {
      List<Port> inPortList = new ArrayList<Port>();
      int portnum = 0;
      for (Port port : block.GetBlock().InputPorts)
      {
        boolean connected = false;
        for (Connection conn : _connections)
          if (connected = (conn.DestBlockID == block.GetBlock().ID && conn.DestPortNumber == portnum))
            break;
        if (!connected)
          inPortList.add(port);
        portnum++;
      }
      if (inPortList.size() > 0)
        result.put(block, inPortList);
    }
    return result;
  }

  protected Dictionary<SchemaBlock, List<Port>> GetOutpuSchemaBlocksWithOutpuPorts()
  {
    Dictionary<SchemaBlock, List<Port>> result = new Hashtable<SchemaBlock, List<Port>>();
    for (SchemaBlock block : _blocks)
    {
      List<Port> outPortList = new ArrayList<Port>();
      int portnum = 0;
      for (Port port : block.GetBlock().OutputPorts)
      {
        boolean connected = false;
        for (Connection conn : _connections)
          if (connected = (conn.SourceBlockID == block.GetBlock().ID && conn.SourcePortNumber == portnum))
            break;
        if (!connected)
          outPortList.add(port);
        portnum++;
      }
      if (outPortList.size() > 0)
        result.put(block, outPortList);
    }
    return result;
  }

  protected Connection AddConnection(Connection connection, boolean recalculate, boolean doNotAdd)
  {
    if (TryValidateConnection(connection) != EAddStatus.Ok)
      return null;

    if (!doNotAdd)
      if (!_connections.add(connection))
        return null;

    SchemaBlock source = GetSchemaBlockById(connection.SourceBlockID);
    SchemaBlock dest = GetSchemaBlockById(connection.DestBlockID);
    boolean isRemoved = false;
    if (!source.ConnectOutPort(connection.SourcePortNumber))
    {
      // remove old obstructed connection leading from source output
      GetConnections().stream()
        .filter(c -> c.SourceBlockID == connection.SourceBlockID && c.SourcePortNumber == connection.SourcePortNumber)
        .forEach(c -> RemoveConnection(c));
      isRemoved = true;
    }
    if (!dest.ConnectInPort(connection.DestPortNumber, source.GetPrecedestors()))
    {
      // remove old obstructed connection leading to destination input
      GetConnections().stream()
        .filter(c -> c.DestBlockID == connection.DestBlockID && c.DestPortNumber == connection.DestPortNumber)
        .forEach(c -> RemoveConnection(c));
      isRemoved = true;
    }

    if (isRemoved && recalculate)
      CalculateSchemaConnections();
    return connection;
  }
}