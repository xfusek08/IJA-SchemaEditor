/**
 * @file:     CplexBlock_Complement.java
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
 * Block calculation complementary value of complex number
 */
public class CplexBlock_Complement extends Block
{
  public static final String NAME = "Complex Complement";

  /**
   * Constructor
   * @param id ID of block
  */
  public CplexBlock_Complement(UUID id)
  {
    super(id, NAME);
  }

  /** Constructor */
  public CplexBlock_Complement()
  {
    super(UUID.randomUUID(), NAME);
  }

  /** Define ports of block */
  public void DefinePorts()
  {
    InputPorts.add(new ComplexPort());
    OutputPorts.add(new ComplexPort());
  }

  /** Calculated values in ports */
  public void Calculate()
  {
    double valueR = InputPorts.get(0).GetValueByName("real");
    double valueI = InputPorts.get(0).GetValueByName("imaginary");
    OutputPorts.get(0).SetValueByName("real", valueR);
    OutputPorts.get(0).SetValueByName("imaginary", -valueI);
    _status.setState(EState.Finished);
  }
}