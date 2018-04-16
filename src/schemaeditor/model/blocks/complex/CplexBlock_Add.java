/**
 * @file:     CplexBlock_Add.java
 * @package:  safemanager.model.blocks.complex
 * @author    Petr Fusek
 * @date      08.04.2018
 */
package schemaeditor.model.blocks.complex;

import schemaeditor.model.base.Block;
import schemaeditor.model.ports.*;
import java.util.UUID;

/**
 * Block providing addition of two complex numbers
 */
public class CplexBlock_Add extends Block
{
  public static final String NAME = "Complex addition";

  public CplexBlock_Add(UUID id)
  {
    super(id, NAME);
  }

  public CplexBlock_Add()
  {
    super(UUID.randomUUID(), NAME);
  }

  public void DefinePorts()
  {
    InputPorts.add(new ComplexPort());
    InputPorts.add(new ComplexPort());

    OutputPorts.add(new ComplexPort());
  }

  public void Calculate()
  {
    double valueR1 = InputPorts.get(0).GetValueByName("real");
    double valueI1 = InputPorts.get(0).GetValueByName("imaginary");
    double valueR2 = InputPorts.get(1).GetValueByName("real");
    double valueI2 = InputPorts.get(1).GetValueByName("imaginary");
    double resultR = valueR1 + valueR2;
    double resultI = valueI1 + valueI2;
    OutputPorts.get(0).SetValueByName("real", resultR);
    OutputPorts.get(0).SetValueByName("imaginary", resultI);
  }
}