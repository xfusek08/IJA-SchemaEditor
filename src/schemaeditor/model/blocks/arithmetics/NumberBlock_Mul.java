/**
 * @file:     NumberBlock_Mul.java
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
 * Block calculating multiplication of two numbers
 */
public class NumberBlock_Mul extends Block
{
  public static final String NAME = "Mul";

  /**
   * Constructor
   * @param ID ID of block
  */
  public NumberBlock_Mul(UUID ID)
  {
    super(ID, NAME);
  }

  /** Constructor */
  public NumberBlock_Mul()
  {
    super(UUID.randomUUID(), NAME);
  }

  /** Define ports of block */
  protected void DefinePorts()
  {
    InputPorts.add(new NumberPort());
    InputPorts.add(new NumberPort());

    OutputPorts.add(new NumberPort());
  }

  /** Calculated values in ports */
  public void Calculate()
  {
    double value1 = InputPorts.get(0).GetValueByName("number");
    double value2 = InputPorts.get(1).GetValueByName("number");
    double result = value1 * value2;
    OutputPorts.get(0).SetValueByName("number", result);
    _status.setState(EState.Finished);
  }
}