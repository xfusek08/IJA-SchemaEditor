/**
 * @file:     LogicBlock_Or.java
 * @package:  safemanager.model.blocks.logic
 * @author    Petr Fusek
 * @date      08.04.2018
 */
package schemaeditor.model.blocks.logic;

import schemaeditor.model.base.Block;
import schemaeditor.model.base.enums.EState;
import schemaeditor.model.ports.*;
import java.util.UUID;

/**
 * Block providing logical operation OR
 */
public class LogicBlock_Or extends Block
{
  public static final String NAME = "Or";

  public LogicBlock_Or(UUID id)
  {
    super(id, NAME);
  }

  public LogicBlock_Or()
  {
    super(UUID.randomUUID(), NAME);
  }

  public void DefinePorts()
  {
    InputPorts.add(new BoolPort());
    InputPorts.add(new BoolPort());

    OutputPorts.add(new BoolPort());
  }

  public void Calculate()
  {
    double value1 = InputPorts.get(0).GetValueByName("bool");
    double value2 = InputPorts.get(1).GetValueByName("bool");
    if(value1 == 1.0 || value2 == 1.0)
      OutputPorts.get(0).SetValueByName("bool", 1.0);
    else
      OutputPorts.get(0).SetValueByName("bool", 0.0);
    _status.State = EState.Finished;
  }
}