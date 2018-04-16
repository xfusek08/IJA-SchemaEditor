/**
 * @file:     ConversionBlock_Greater.java
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
public class ConversionBlock_Greater extends Block
{
  public static final String NAME = "Greater";

  /** Constructor */
  public ConversionBlock_Greater(UUID ID)
  {
    super(ID, NAME);
  }

  public ConversionBlock_Greater()
  {
    super(UUID.randomUUID(), NAME);
  }

  public void Calculate()
  {
    double value1 = InputPorts.get(0).GetValueByName("number");
    double value2 = InputPorts.get(1).GetValueByName("number");
    if(value1 > value2)
      OutputPorts.get(0).SetValueByName("bool", 1.0);
    else
      OutputPorts.get(0).SetValueByName("bool", 0.0);
  }

  protected void DefinePorts()
  {
    InputPorts.add(new NumberPort());
    InputPorts.add(new NumberPort());

    OutputPorts.add(new BoolPort());
  }
}