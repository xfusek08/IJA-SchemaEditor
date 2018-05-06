/**
 * @file:     NumberBlock_Abs.java
 * @package:  schemaeditor.model.blocks.arithmetics
 * @author    Jaromir Franek
 * @date      08.04.2018
 */
package schemaeditor.model.blocks.arithmetics;

import schemaeditor.model.base.Block;
import schemaeditor.model.base.enums.EState;
import schemaeditor.model.ports.*;
import java.util.*;

/**
 * Block calculating Absolute value of number
 */
public class NumberBlock_Abs extends Block
{
  public static final String NAME = "Abs";

  /**
   * Constructor
   * @param ID ID of block
  */
  public NumberBlock_Abs(UUID ID)
  {
    super(ID, NAME);
  }

  /** Constructor */
  public NumberBlock_Abs()
  {
    super(UUID.randomUUID(), NAME);
  }

  /** Define ports of block */
  protected void DefinePorts()
  {
    InputPorts.add(new NumberPort());

    OutputPorts.add(new NumberPort());
  }

  /** Calculated values in ports */
  public void Calculate()
  {
    double value = InputPorts.get(0).GetValueByName("number");
    if(value < 0)
      value = -value;
    OutputPorts.get(0).SetValueByName("number", value);
    _status.setState(EState.Finished);
  }
}