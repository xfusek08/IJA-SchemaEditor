/**
 * @file:     CplexBlock_Mul.java
 * @package:  safemanager.model.blocks.complex
 * @author    Petr Fusek
 * @date      08.04.2018
 */
package schemaeditor.model.blocks.complex;

import schemaeditor.model.base.Block;
import schemaeditor.model.ports.*;
import java.util.UUID;

/**
 * Block providing multiplication of two complex numbers
 */
public class CplexBlock_Mul extends Block
{
  public static final String NAME = "Complex multiplication";

  public CplexBlock_Mul(UUID id)
  {
    super(id, NAME);
  }

  public CplexBlock_Mul()
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
    // TODO: do stuff
  }
}