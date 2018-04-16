/**
 * @file:     LogicBlock_Xnor.java
 * @package:  safemanager.model.blocks.logic
 * @author    Petr Fusek
 * @date      08.04.2018
 */
package schemaeditor.model.blocks.logic;

import schemaeditor.model.base.Block;
import schemaeditor.model.ports.*;
import java.util.UUID;

/**
 * Block providing logical operation XNOR
 */
public class LogicBlock_Xnor extends Block
{
  public static final String NAME = "Xnor";

  public LogicBlock_Xnor(UUID id)
  {
    super(id, NAME);
  }

  public LogicBlock_Xnor()
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
    if(value1 == value2)
      OutputPorts.get(0).SetValueByName("bool", 1.0);
    else
      OutputPorts.get(0).SetValueByName("bool", 0.0);
  }
}