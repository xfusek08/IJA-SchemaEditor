/**
 * @file:     ConversionBlock_Less.java
 * @package:  schemaeditor.model.blocks.conversion
 * @author    Jaromir Franek
 * @date      08.04.2018
 */
package schemaeditor.model.blocks.conversion;

import schemaeditor.model.base.Block;
import schemaeditor.model.ports.*;
import java.util.*;

/**
 * Class reprezenting one block
 */
public class ConversionBlock_Less extends Block
{
  public static final String NAME = "Less";

  /** Constructor */
  public ConversionBlock_Less(UUID ID)
  {
    super(ID, NAME);
  }

  public ConversionBlock_Less()
  {
    super(UUID.randomUUID(), NAME);
  }

  public void Calculate()
  {

  }

  protected void DefinePorts()
  {
    InputPorts.add(new NumberPort());
    InputPorts.add(new NumberPort());

    OutputPorts.add(new BoolPort());
  }
}