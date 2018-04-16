/**
 * @file:     NumberBlock_Add.java
 * @package:  schemaeditor.model.blocks.arithmetics
 * @author    Jaromir Franek
 * @date      08.04.2018
 */
package schemaeditor.model.blocks.arithmetics;

import schemaeditor.model.base.Block;
import schemaeditor.model.base.enums.EState;
import schemaeditor.model.ports.*;
import java.util.*;

/**
 * Class reprezenting one block
 */
public class NumberBlock_Add extends Block
{
  public static final String NAME = "Add";

  /** Constructor */
  public NumberBlock_Add(UUID ID)
  {
    super(ID, NAME);
  }

  /** Constructor */
  public NumberBlock_Add()
  {
    super(UUID.randomUUID(), NAME);
  }

  public void Calculate()
  {
    double value1 = InputPorts.get(0).GetValueByName("number");
    double value2 = InputPorts.get(1).GetValueByName("number");
    double result = value1 + value2;
    OutputPorts.get(0).SetValueByName("number", result);
    _status.State = EState.Finished;
  }

  protected void DefinePorts()
  {
    InputPorts.add(new NumberPort());
    InputPorts.add(new NumberPort());

    OutputPorts.add(new NumberPort());
  }
}