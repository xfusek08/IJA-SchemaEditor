/**
 * @file:     CplexBlock_Div.java
 * @package:  safemanager.model.blocks.complex
 * @author    Petr Fusek
 * @date      08.04.2018
 */
package schemaeditor.model.blocks.complex;

import schemaeditor.model.base.Block;
import schemaeditor.model.ports.*;
import java.util.UUID;

/**
 * Block providing division of two complex numbers
 */
public class CplexBlock_Div extends Block
{
  public static final String NAME = "Complex division";

  public CplexBlock_Div(UUID id)
  {
    super(id, NAME);
  }

  public CplexBlock_Div()
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