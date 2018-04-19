/**
 * @file:     ConversionBlock_BoolToNumber.java
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
 * Block providing conversion bool to number
 */
public class ConversionBlock_BoolToNumber extends Block
{
  public static final String NAME = "BoolToNumber";

  /** Constructor */
  public ConversionBlock_BoolToNumber(UUID ID)
  {
    super(ID, NAME);
  }

  /** Constructor */
  public ConversionBlock_BoolToNumber()
  {
    super(UUID.randomUUID(), NAME);
  }

  /** Define ports of block */
  protected void DefinePorts()
  {
    InputPorts.add(new BoolPort());
  
    OutputPorts.add(new NumberPort());
  }

  /** Calculated values in ports */
  public void Calculate()
  {
    double value = InputPorts.get(0).GetValueByName("bool");
    OutputPorts.get(0).SetValueByName("number", value);
    _status.State = EState.Finished;
  }
}