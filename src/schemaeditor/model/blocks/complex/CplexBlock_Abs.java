/**
 * @file:     CplexBlock_Abs.java
 * @package:  safemanager.model.blocks.complex
 * @author    Petr Fusek
 * @date      08.04.2018
 */
package schemaeditor.model.blocks.complex;

import schemaeditor.model.base.Block;
import schemaeditor.model.base.enums.EState;
import schemaeditor.model.ports.*;
import java.util.UUID;

/**
 * Block calculating Absolute value of complex number
 */
public class CplexBlock_Abs extends Block
{
  public static final String NAME = "Complex Abs";

  /**
   * Constructor
   * @param id ID of block
  */
  public CplexBlock_Abs(UUID id)
  {
    super(id, NAME);
  }

  /** Constructor */
  public CplexBlock_Abs()
  {
    super(UUID.randomUUID(), NAME);
  }

  /** Define ports of block */
  public void DefinePorts()
  {
    InputPorts.add(new ComplexPort());
    OutputPorts.add(new NumberPort());
  }

  /** Calculated values in ports */
  public void Calculate()
  {
    double value1 = InputPorts.get(0).GetValueByName("real");
    double value2 = InputPorts.get(0).GetValueByName("imaginary");
    double result = Math.sqrt((value1 * value1) + (value2 * value2));
    OutputPorts.get(0).SetValueByName("number", result);
    _status.setState(EState.Finished);
  }
}