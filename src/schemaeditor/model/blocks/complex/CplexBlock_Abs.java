/**
 * @file:     CplexBlock_Abs.java
 * @package:  safemanager.model.blocks.complex
 * @author    Petr Fusek
 * @date      08.04.2018
 */
package schemaeditor.model.blocks.complex;

import schemaeditor.model.base.Block;
import schemaeditor.model.ports.*;
import java.util.UUID;

/**
 * Block calculating Absolute value of complex number
 */
public class CplexBlock_Abs extends Block
{
  public static final String NAME = "Absolute value";

  public CplexBlock_Abs(UUID id)
  {
    super(id, NAME);
  }

  public CplexBlock_Abs()
  {
    super(UUID.randomUUID(), NAME);
  }

  public void DefinePorts()
  {
    InputPorts.add(new ComplexPort());
    OutputPorts.add(new NumberPort());
  }

  public void Calculate()
  {
    // TODO: do stuff
  }
}