/**
 * @file:     Block.java
 * @package:  schemaeditor.model.base
 * @author    Jaromir Franek
 * @date      08.04.2018
 */

package schemaeditor.model.base;

import schemaeditor.model.base.enums.EState;
import schemaeditor.model.base.Port;

import java.util.*;

/**
 * Class reprezenting one block
 */
public abstract class Block extends Observable implements Observer
{
  protected BlockStatus _status;
  public UUID ID;
  public List<Port> InputPorts;
  public List<Port> OutputPorts;
  public String DisplayName;
  public double X;
  public double Y;

  /** 
   * Constructor 
   * @param ID block id
   * @param name name of block
   * */
  public Block(UUID ID, String name)
  {
    this._status = new BlockStatus();
    this._status.addObserver(this);
    this.ID = ID;
    this.InputPorts = new ArrayList<>();
    this.OutputPorts = new ArrayList<>();
    this.DisplayName = name;
    this.X = 100;
    this.Y = 100;
    DefinePorts();
    Reset();
  }

  /** Define ports of block */
  protected abstract void DefinePorts();

  /** Calculated values in ports */
  public abstract void Calculate();

  /**
   * Get output value
   * @param number index of port
   * @return data of port on index
   */
  public HashMap<String, Double> getOutPortVal(int number)
  {
    return OutputPorts.get(number).GetData();
  }

  /**
   * Set input value
   * @param number index of port
   * @param value to be set
   */
  public void setInPortVal(int number, HashMap<String, Double> value)
  {
    InputPorts.get(number).SetData(value);
  }

  /**
   * Check if block is executable
   * @return true if all ports have not NaN value
   */
  public boolean isExecutable()
  {
    for(Port port : InputPorts)
      for(String key : port.GetData().keySet())
        if(Double.isNaN(port.GetData().get(key)))
          return false;
    return true;
  }

  /**
   * Check if all input ports are defined
   * @return true if all ports are defined
   */
  public boolean isAllInputsDefined()
  {
    for(int i = 0; i < InputPorts.size(); i++)
    {
      if(InputPorts.get(i).hasDefinedValue() == false)
        return false;
    }
    return true;
  }

  /**
   * Return status of block
   * @return block status
   */
  public BlockStatus GetStatus()
  {
    return this._status;
  }

  /**
   * Update object a notify observers
   * @param obs observer to be notified
   * @param obj to be updated
   */
  public void update(Observable obs, Object obj)
  {
    setChanged();
    notifyObservers();
  }

  /** Reset */
  public void Reset()
  {
    for (Port port : OutputPorts)
    {
      for(String s : port.GetValuesNames())
        port.SetValueByName(s, Double.NaN);
    }
    for (Port port : InputPorts)
    {
      for(String s : port.GetValuesNames())
        port.SetValueByName(s, Double.NaN);
    }
    _status.setState(EState.Ready);
  }

  /**
   * Check if blocks are equal
   * @param obj object to be compared to
   * @return true if are equal
   */
  public boolean equals(Object obj)
  {
    if (obj instanceof Block)
      return ID.equals(((Block)obj).ID);
    return false;
  }

  /**
   * Return block hash code
   * @return hashCode of block
   */
  public int hashCode()
  {
    return ID.hashCode();
  }
}