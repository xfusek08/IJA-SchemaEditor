/**
 * @file:     CplexBlock_Complement.java
 * @package:  safemanager.model.blocks.complex
 * @author    Petr Fusek
 * @date      08.04.2018
 */
package schemaeditor.model.blocks.complex;

import schemaeditor.model.base.Block;
import schemaeditor.model.ports.*;
import java.util.UUID;

/**
 * Block calculation complementary value of complex number
 */
public class CplexBlock_Complement extends Block
{
  public static final String NAME = "Complementary value";

  public CplexBlock_Complement(UUID id)
  {
    super(id, NAME);
  }

  public CplexBlock_Complement()
  {
    super(UUID.randomUUID(), NAME);
  }

  public void DefinePorts()
  {
    InputPorts.add(new ComplexPort());
    OutputPorts.add(new ComplexPort());
  }

  public void Calculate()
  {
    // TODO: do stuff
  }
}