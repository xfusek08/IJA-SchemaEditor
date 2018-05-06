/**
 * @file:     ConversionBlock_Less.java
 * @package:  schemaeditor.model.blocks.conversion
 * @author    Jaromir Franek
 * @date      08.04.2018
 */
package schemaeditor.model.blocks.conversion;

import schemaeditor.model.base.Block;
import schemaeditor.model.base.enums.EState;
import schemaeditor.model.ports.*;
import java.util.*;

/**
 * Block providing operation less
 */
public class ConversionBlock_Less extends Block
{
  public static final String NAME = "Less";

  /**
   * Constructor
   * @param id id of block
   */
  public ConversionBlock_Less(UUID id)
  {
    super(id, NAME);
  }

  /** Constructor */
  public ConversionBlock_Less()
  {
    super(UUID.randomUUID(), NAME);
  }

  /** Define ports of block */
  protected void DefinePorts()
  {
    InputPorts.add(new NumberPort());
    InputPorts.add(new NumberPort());

    OutputPorts.add(new BoolPort());
  }

  /** Calculated values in ports */
  public void Calculate()
  {
    double value1 = InputPorts.get(0).GetValueByName("number");
    double value2 = InputPorts.get(1).GetValueByName("number");
    if(value1 < value2)
      OutputPorts.get(0).SetValueByName("bool", 1.0);
    else
      OutputPorts.get(0).SetValueByName("bool", 0.0);
    _status.setState(EState.Finished);
  }
}