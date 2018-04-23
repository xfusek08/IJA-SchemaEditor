/**
 * @file:     Block.java
 * @package:  schemaeditor.model.base
 * @author    Jaromir Franek
 * @date      08.04.2018
 */
package schemaeditor.model.base;

import schemaeditor.model.base.Port;

import java.util.*;

/**
 * Class reprezenting one block
 */
public abstract class Block
{
  protected BlockStatus _status;
  public UUID ID;
  public List<Port> InputPorts;
  public List<Port> OutputPorts;
  public String DisplayName;
  public double X;
  public double Y;

  /** Constructor */
  public Block(UUID ID, String name)
  {
    this._status = new BlockStatus();
    this.ID = ID;
    this.InputPorts = new ArrayList<>();
    this.OutputPorts = new ArrayList<>();
    this.DisplayName = name;
    this.X = 100;
    this.Y = 100;
    DefinePorts();
  }

  /** Calculated values in ports */
  protected abstract void DefinePorts();

  /** Calculated values in ports */
  public abstract void Calculate();

  /** Check if all input ports are defined */
  public boolean isAllInputsDefined()
  {
    for(int i = 0; i < InputPorts.size(); i++)
    {
      if(InputPorts.get(i).hasDefinedValue() == false)
        return false;
    }
    return true;
  }

  /** Return status of block */
  public BlockStatus GetStatus()
  {
    return this._status;
  }

  /** Reset */
  public void Reset()
  {
    for(int i = 0; i < OutputPorts.size(); i++)
    {
      Iterable<String> names = OutputPorts.get(i).GetValuesNames();
      for(String s : names)
        OutputPorts.get(i).SetValueByName(s, null);
    }
  }
}