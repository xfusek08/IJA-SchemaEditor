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
 * Class for object reprezenting one Schema
 *
 * Schema is composed from Blocks and connections
 * @TODO: Schema should be implementing observable object to notify view of changes inside structure
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

  /** Adds block instance into schema */
  public Block AddBlock(Block block)
  {
    SchemaBlock newSBlock = new SchemaBlock(block);
    if (!_blocks.contains(newSBlock))
      _blocks.add(newSBlock);
    setChanged();
    notifyObservers();
    return block;
  }

  public boolean isCalculating()
  {
    return _isCalculating;
  }

  @Override
  public boolean hasChanged() {
    return true;
  }

  /** Removes block instance from schema */
  public void RemoveBlock(Block block)
  {
    List<Connection> toRemove = new ArrayList<Connection>();
    for (Connection conn : GetConnections())
      if (conn.DestBlockID == block.ID || conn.SourceBlockID == block.ID)
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

  /** Removes block instance from schema */
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
    if(!in.Compatible(out) || !out.Compatible(out))
      return EAddStatus.PortsIncopatible;
    for (SchemaBlock schemaBlock : _blocks)
      if(schemaBlock._block.ID == connection.SourceBlockID)
        prec = schemaBlock;
    for (UUID ID : prec.GetPrecedestors())
    {
      if(ID == connection.DestBlockID)
        return EAddStatus.ConnectionCuseesCycles;
    }
    return EAddStatus.Ok;
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

  public Set<Block> GetOutBlocks()
  {
    Dictionary<SchemaBlock, List<Port>> inputdict = GetOutpuSchemaBlocksWithOutpuPorts();
    Set<Block> res = new HashSet<Block>();
    Enumeration<SchemaBlock> keys = inputdict.keys();
    while(keys.hasMoreElements())
      res.add(keys.nextElement().GetBlock());
    return res;
  }

  /** Runs calculation */
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

  /** Starts debug calculation and prepares it to stepping */
  public void StartCalculation()
  {
    _isCalculating = true;
    //najdu vsechny bloky, ktere maji prazdne porty pomoci metody a zavolam calculate na blocich
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

  /** Execute one level of calculation */
  public boolean StepCalculation()
  {
    //najdu vsechny bloky ktery maji stav finished
    //na kazdy blok zavolam calculate

    boolean calculated = false;
    Set<Connection> conns = new HashSet<Connection>();
    for(SchemaBlock schemaBlock : _blocks)
      if(schemaBlock.GetBlock().GetStatus().getState() == EState.Finished)
        for(Connection conn : _connections)
          if(conn.SourceBlockID == schemaBlock.GetBlock().ID)
            for(SchemaBlock sBlock : _blocks)
              if(sBlock.GetBlock().ID == conn.DestBlockID && sBlock.GetBlock().GetStatus().getState() == EState.Ready)
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

  private void sendVal(Connection conn)
  {
    HashMap<String, Double> value = GetSchemaBlockById(conn.SourceBlockID).GetBlock().getOutPortVal(conn.SourcePortNumber);
    GetSchemaBlockById(conn.DestBlockID).GetBlock().setInPortVal(conn.DestPortNumber, value);
  }

  private Set<Connection> getConsToNextBlocks(SchemaBlock sBlock)
  {
    Set<Connection> conns = new HashSet<Connection>();
    for(Connection conn : _connections)
      if(conn.SourceBlockID == sBlock.GetBlock().ID)
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
   * Cleans all informations from all schemablocks and use bfs to fill new precedestors to blocks and check validity of connections.
   * If some invalid connection occures then such a connection is thrown away. By invalid is considered connection causing loops.
   * It uses AddConnection method without adding to collection (doNotAdd = false) for update state of scheamblocks.
   */
  protected void CalculateSchemaConnections()
  {
    // System.err.printf("Recalculate call:\n");
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
        // System.err.printf("\t inblock: %s %d %d\n", block.ID, block.InputPorts.size(), ports.size());
        openQueue.add(block);
      }
    }

    //Nahrani prvnich blocku do fronty
    while(!openQueue.isEmpty())
    {
      Block first = openQueue.poll();
      // System.err.printf("Check of %s\n", first.ID);
      for(Connection conn : GetConnections())
        if(conn.SourceBlockID == first.ID)
        {
          // System.err.printf("adding %s\n", GetSchemaBlockById(conn.DestBlockID).GetBlock().ID);
          AddConnection(conn, false, true);
          openQueue.add(GetSchemaBlockById(conn.DestBlockID).GetBlock());
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
    int inputNumber = 0;
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

  protected EAddStatus AddConnection(Connection connection, boolean recalculate, boolean noadd)
  {
    EAddStatus res = TryValidateConnection(connection);
    if (res != EAddStatus.Ok) return res;

    SchemaBlock source = GetSchemaBlockById(connection.SourceBlockID);
    SchemaBlock dest = GetSchemaBlockById(connection.DestBlockID);
    // source.ConnectOutPort(connection.SourcePortNumber);
    dest.AddAllPrecedestor(source.GetPrecedestors());
    dest.AddPrecedestor(source.GetBlock().ID);


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

    // System.err.printf("\n");
    // for (Connection conn : GetConnections())
    //   System.err.printf("\t%s (%d) -> %s (%d)\n", conn.SourceBlockID.toString(), conn.SourcePortNumber, conn.DestBlockID.toString(), conn.DestPortNumber);
    // System.err.printf("\n");
    return EAddStatus.Ok;
  }

  /** Removes block instance from schema */
  protected void RemoveConnection(Connection connection, boolean recalculate)
  {
    _connections.remove(connection);
    if(recalculate)
      CalculateSchemaConnections();
  }
}