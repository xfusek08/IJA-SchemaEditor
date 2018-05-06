/**
 * @file:     Schema.java
 * @package:  safemanager.model.base
 * @author    Petr Fusek
 * @date      08.04.2018
 */
package schemaeditor.model.base;

import schemaeditor.model.base.*;
import schemaeditor.model.base.enums.EState;
import schemaeditor.model.safemanager.*;
import schemaeditor.model.base.enums.EAddStatus;

import java.util.*;
import java.util.stream.Stream;

/**
 * Class for object representing one Schema
 *
 * Schema is composed from Blocks and connectionsa
 */
public class Schema extends Observable
{
  protected Set<SchemaBlock> _blocks;
  protected Set<Connection> _connections;
  protected boolean _isCalculating;

  /** Constructor */
  public Schema()
  {
    _blocks = new HashSet<SchemaBlock>();
    _connections = new HashSet<Connection>();
    _isCalculating = false;
  }

  /**
   * Adds block instance into schema
   * @param block block object to be add to schema
   * @return added block
   */
  public Block AddBlock(Block block)
  {
    SchemaBlock newSBlock = new SchemaBlock(block);
    if (!_blocks.contains(newSBlock))
      _blocks.add(newSBlock);
    setChanged();
    notifyObservers();
    return block;
  }

  /**
   * Check if schema is calculating
   * @return added block
   */
  public boolean isCalculating()
  {
    return _isCalculating;
  }

  /**
   * Check if schema has changed
   * @return true
   */
  @Override
  public boolean hasChanged() {
    return true;
  }

  /**
   * Removes block instance from schema
   * @param block block to be removed from list
   */
  public void RemoveBlock(Block block)
  {
    List<Connection> toRemove = new ArrayList<Connection>();
    for (Connection conn : GetConnections())
      if (conn.DestBlockID.equals(block.ID) || conn.SourceBlockID.equals(block.ID))
        toRemove.add(conn);
    for (Connection conn : toRemove)
      RemoveConnection(conn, false);
    _blocks.remove(GetSchemaBlockById(block.ID));
    CalculateSchemaConnections();
    setChanged();
    notifyObservers();
  }

  /**
   * Adds block instance into schema, checks if connection does not broke any schema rule
   * @param connection new instance of connection to be added and to schema
   * @return EAddStatus result of attepmt
   */
  public EAddStatus AddConnection(Connection connection)
  {
    EAddStatus res = AddConnection(connection, true, false);
    setChanged();
    notifyObservers();
    return res;
  }

  /**
   * Removes block instance from schema
   * @param connection connectiont to be removed from list
   */
  public void RemoveConnection(Connection connection)
  {
    RemoveConnection(connection, true);
    setChanged();
    notifyObservers();
  }

  /**
   * Tries to determine if connection is valid
   * @param connection concection instance which will be validated in schema
   * @return EAddStatus result of attepmt
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
      return EAddStatus.InDestPortNotFound;
    for (SchemaBlock schemaBlock : _blocks)
    {
      if(schemaBlock._block.ID.equals(connection.SourceBlockID))
        out = schemaBlock._block.OutputPorts.get(connection.SourcePortNumber);
      else if(schemaBlock._block.ID.equals(connection.DestBlockID))
        in = schemaBlock._block.InputPorts.get(connection.DestPortNumber);
    }
    if(out == null)
      return EAddStatus.OutSourcePortNotFound;
    if(in == null)
      return EAddStatus.InDestPortNotFound;
    if(!in.Compatible(out) || !out.Compatible(out))
      return EAddStatus.PortsIncompatible;
    for (SchemaBlock schemaBlock : _blocks)
      if(schemaBlock._block.ID.equals(connection.SourceBlockID))
        prec = schemaBlock;
    for (UUID ID : prec.GetPredecessors())
      if(ID.equals(connection.DestBlockID))
        return EAddStatus.ConnectionCausesCycles;
    return EAddStatus.Ok;
  }

  // Iterators of collections

  /**
   * Get Block list iterator
   * @return list of blocks
   */
  public List<Block> GetBlocks()
  {
    List<Block> list = new ArrayList<Block>();
    for (SchemaBlock schemaBlock : _blocks)
      list.add(schemaBlock.GetBlock());
    return list;
  }

  /**
   * Get Block list iterator
   * @return connection list
  */
  public Set<Connection> GetConnections()
  {
    return _connections;
  }

  /**
   * Gets list of input (unconnected) ports of schema.
   * @return list of all input ports
   */
  public List<Port> GetInputPorts()
  {
    Dictionary<SchemaBlock, List<Port>> inputdict = GetInputSchemaBlocksWithInputPorts();
    List<Port> inPortList = new ArrayList<Port>();
    Enumeration<List<Port>> values = inputdict.elements();
    while(values.hasMoreElements())
      inPortList.addAll(values.nextElement());
    return inPortList;
  }

  /**
   * Gets list of output (unconnected) ports of schema.
   * @return list of all output ports
   */
  public List<Port> GetOutPorts()
  {
    Dictionary<SchemaBlock, List<Port>> outputdict = GetOutputSchemaBlocksWithOutputPorts();
    List<Port> outPortList = new ArrayList<Port>();
    Enumeration<List<Port>> values = outputdict.elements();
    while(values.hasMoreElements())
      outPortList.addAll(values.nextElement());
    return outPortList;
  }

  /**
   * Gets list of block with unconnected input ports of schema.
   * @return list of all input blocks
   */
  public Set<Block> GetInBlocks()
  {
    Dictionary<SchemaBlock, List<Port>> inputdict = GetInputSchemaBlocksWithInputPorts();
    Set<Block> res = new HashSet<Block>();
    Enumeration<SchemaBlock> keys = inputdict.keys();
    while(keys.hasMoreElements())
      res.add(keys.nextElement().GetBlock());
    return res;
  }

  /**
   * Gets list of block with unconnected output ports of schema.
   * @return list of all output blocks
   */
  public Set<Block> GetOutBlocks()
  {
    Dictionary<SchemaBlock, List<Port>> inputdict = GetOutputSchemaBlocksWithOutputPorts();
    Set<Block> res = new HashSet<Block>();
    Enumeration<SchemaBlock> keys = inputdict.keys();
    while(keys.hasMoreElements())
      res.add(keys.nextElement().GetBlock());
    return res;
  }

  /**
   * Runs calculation
   * @return true of run succesfully
   */
  public boolean RunCalculation()
  {
    _isCalculating = true;
    boolean doCalc = true;
    try
    {
      StartCalculation();
      while(doCalc == true)
        doCalc = StepCalculation();
    }
    catch(Exception e)
    {
      return false;
    }
    return true;
  }

  /**
   * Starts debug calculation and prepares it to stepping
   */
  public void StartCalculation()
  {
    _isCalculating = true;
    Set<Connection> conns = new HashSet<Connection>();
    for(SchemaBlock sBlock : _blocks)
      if(sBlock.GetBlock().isExecutable())
      {
        sBlock.GetBlock().Calculate();
        conns.addAll(getConsToNextBlocks(sBlock));
      }
    for(Connection connect : conns)
      sendVal(connect);
  }

  /**
   * Execute one level of calculation
   * @return true if block calculated
   */
  public boolean StepCalculation()
  {
    boolean calculated = false;
    Set<Connection> conns = new HashSet<Connection>();
    for(SchemaBlock schemaBlock : _blocks)
      if(schemaBlock.GetBlock().GetStatus().getState() == EState.Finished)
        for(Connection conn : _connections)
          if(conn.SourceBlockID.equals(schemaBlock.GetBlock().ID))
            for(SchemaBlock sBlock : _blocks)
              if(sBlock.GetBlock().ID.equals(conn.DestBlockID) && sBlock.GetBlock().GetStatus().getState() == EState.Ready)
              {
                if(sBlock.GetBlock().isExecutable())
                {
                  sBlock.GetBlock().Calculate();
                  conns.addAll(getConsToNextBlocks(sBlock));
                }
                calculated = true;
              }
    for(Connection connect : conns)
      sendVal(connect);
    return calculated;
  }

  /**
   * Send value to connected port
   * @param conn connection to send value by
   */
  private void sendVal(Connection conn)
  {
    HashMap<String, Double> value = GetSchemaBlockById(conn.SourceBlockID).GetBlock().getOutPortVal(conn.SourcePortNumber);
    GetSchemaBlockById(conn.DestBlockID).GetBlock().setInPortVal(conn.DestPortNumber, value);
  }

  /**
   * Find connections to next block
   * @param sBlock block to used in connection
   * @return list of connections leading to next block
   */
  private Set<Connection> getConsToNextBlocks(SchemaBlock sBlock)
  {
    Set<Connection> conns = new HashSet<Connection>();
    for(Connection conn : _connections)
      if(conn.SourceBlockID.equals(sBlock.GetBlock().ID))
        conns.add(conn);
    return conns;
  }

  /** Resets all blocks into initial state before calculation */
  public void StopCalculation()
  {
    List<Block> blocks = GetBlocks();
    for(Block block : blocks)
      block.Reset();
    _isCalculating = false;
  }

  /************************************************ PROTECTED ***************************************************/

  /**
   * Cleans all informations from all schemablocks and use bfs to fill new Predecessors to blocks and check validity of connections.
   * If some invalid connection occures then such a connection is thrown away. By invalid is considered connection causing loops.
   * It uses AddConnection method without adding to collection (doNotAdd = false) for update state of scheamblocks.
   */
  protected void CalculateSchemaConnections()
  {
    _blocks.stream().forEach(sb -> sb.Clean());
    Queue<Block> openQueue = new LinkedList<Block>();

    Dictionary<SchemaBlock, List<Port>> inputdict = GetInputSchemaBlocksWithInputPorts();
    Enumeration<SchemaBlock> keys = inputdict.keys();
    while(keys.hasMoreElements())
    {
      SchemaBlock sblock = keys.nextElement();
      List<Port> ports = inputdict.get(sblock);
      Block block = sblock.GetBlock();
      if (block.InputPorts.size() == ports.size())
      {
        openQueue.add(block);
      }
    }

    while(!openQueue.isEmpty())
    {
      Block first = openQueue.poll();
      for(Connection conn : GetConnections())
        if(conn.SourceBlockID.equals(first.ID))
        {
          AddConnection(conn, false, true);
          openQueue.add(GetSchemaBlockById(conn.DestBlockID).GetBlock());
        }
    }
  }

  /**
   * Find schema block in list
   * @param id ID of block
   * @return block if found, null if not
   */
  private SchemaBlock GetSchemaBlockById(UUID id)
  {
    Optional<SchemaBlock> opt  = _blocks.stream().filter(sb -> sb.GetBlock().ID.equals(id)).findFirst();
    if (opt.isPresent())
      return opt.get();
    return null;
  }

  /**
   * Get dictionary of input block and its input ports
   * @return dictionary of block and ports
   */
  protected Dictionary<SchemaBlock, List<Port>> GetInputSchemaBlocksWithInputPorts()
  {
    Dictionary<SchemaBlock, List<Port>> result = new Hashtable<SchemaBlock, List<Port>>();
    int inputNumber = 0;
    for (SchemaBlock block : _blocks)
    {
      List<Port> inPortList = new ArrayList<Port>();
      int portnum = 0;
      for (Port port : block.GetBlock().InputPorts)
      {
        boolean connected = false;
        for (Connection conn : _connections)
          if (connected = (conn.DestBlockID.equals(block.GetBlock().ID) && conn.DestPortNumber == portnum))
            break;
        if (!connected)
        {
          port.SetInputNumber(inputNumber++);
          inPortList.add(port);
        }
        else
          port.UnsetInput();
        portnum++;
      }
      if (inPortList.size() > 0)
        result.put(block, inPortList);
    }
    return result;
  }

  /**
   * Get dictionary of input block and its input ports
   * @return dictionary of block and ports
   */
  protected Dictionary<SchemaBlock, List<Port>> GetOutputSchemaBlocksWithOutputPorts()
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
          if (connected = (conn.SourceBlockID.equals(block.GetBlock().ID) && conn.SourcePortNumber == portnum))
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

  /**
   * Add connection to block
   * @param connection connection to be add
   * @param recalculate value to determine if recalculate schema
   * @param noadd value to determine if add to schema
   * @return status of operation
   */
  protected EAddStatus AddConnection(Connection connection, boolean recalculate, boolean noadd)
  {
    EAddStatus res = TryValidateConnection(connection);
    if (res != EAddStatus.Ok) return res;

    SchemaBlock source = GetSchemaBlockById(connection.SourceBlockID);
    SchemaBlock dest = GetSchemaBlockById(connection.DestBlockID);
    dest.AddAllPredecessor(source.GetPredecessors());
    dest.AddPredecessor(source.GetBlock().ID);


    if (!noadd)
    {
      List<Connection> toremove = new ArrayList<Connection>();
      GetConnections().stream()
        .filter(c ->
          c.SourceBlockID.equals(connection.SourceBlockID) && c.SourcePortNumber == connection.SourcePortNumber ||
          c.DestBlockID.equals(connection.DestBlockID) && c.DestPortNumber == connection.DestPortNumber
        )
        .forEach(c -> toremove.add(c)
      );

      for (Connection conn : toremove)
        RemoveConnection(conn, false);

      if (!_connections.add(connection))
        return EAddStatus.OtherError;
    }

    if (recalculate)
       CalculateSchemaConnections();

    return EAddStatus.Ok;
  }

  /**
   * Removes block instance from schema
   * @param connection connection to be removed
   * @param recalculate value to determine if recalculate schema
   */
  protected void RemoveConnection(Connection connection, boolean recalculate)
  {
    _connections.remove(connection);
    if(recalculate)
      CalculateSchemaConnections();
  }
}