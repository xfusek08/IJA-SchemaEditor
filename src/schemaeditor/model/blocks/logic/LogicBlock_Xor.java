/**
 * @file:     LogicBlock_Xor.java
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
 * Block providing logical operation XOR
 */
public class LogicBlock_Xor extends Block
{
  public static final String NAME = "Xor";

  /** Constructor */
  public LogicBlock_Xor(UUID id)
  {
    super(id, NAME);
  }

  /** Constructor */
  public LogicBlock_Xor()
  {
    super(UUID.randomUUID(), NAME);
  }

  /** Define ports of block */
  public void DefinePorts()
  {
    InputPorts.add(new BoolPort());
    InputPorts.add(new BoolPort());

    OutputPorts.add(new BoolPort());
  }

  /** Calculated values in ports */
  public void Calculate()
  {
    double value1 = InputPorts.get(0).GetValueByName("bool");
    double value2 = InputPorts.get(1).GetValueByName("bool");
    if(value1 != value2)
      OutputPorts.get(0).SetValueByName("bool", 1.0);
    else
      OutputPorts.get(0).SetValueByName("bool", 0.0);
    _status.setState(EState.Finished);
  }
}