/**
 * @file:     CplexBlock_Div.java
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
 * Block calculating division of two complex numbers
 */
public class CplexBlock_Div extends Block
{
  public static final String NAME = "Complex Div";

  /**
   * Constructor
   * @param id ID of block
  */
  public CplexBlock_Div(UUID id)
  {
    super(id, NAME);
  }

  /** Constructor */
  public CplexBlock_Div()
  {
    super(UUID.randomUUID(), NAME);
  }

  /** Define ports of block */
  public void DefinePorts()
  {
    InputPorts.add(new ComplexPort());
    InputPorts.add(new ComplexPort());

    OutputPorts.add(new ComplexPort());
  }

  /** Calculated values in ports */
  public void Calculate()
  {
    double valueR1 = InputPorts.get(0).GetValueByName("real");
    double valueI1 = InputPorts.get(0).GetValueByName("imaginary");
    double valueR2 = InputPorts.get(1).GetValueByName("real");
    double valueI2 = InputPorts.get(1).GetValueByName("imaginary");
    double resultR = (valueR1 * valueR2 + valueI1 * valueI2) / (valueR2 * valueR2 + valueI2 * valueI2);
    double resultI = (-valueR1 * valueI2 + valueR1 * valueI1) / (valueR2 * valueR2 + valueI2 * valueI2);
    OutputPorts.get(0).SetValueByName("real", resultR);
    OutputPorts.get(0).SetValueByName("imaginary", resultI);
    _status.setState(EState.Finished);
  }
}