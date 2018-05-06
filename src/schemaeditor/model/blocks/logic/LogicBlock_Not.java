/**
 * @file:     LogicBlock_Not.java
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
 * Block providing logical operation NOT
 */
public class LogicBlock_Not extends Block
{
  public static final String NAME = "Not";

  /**
   * Constructor
   * @param id id of block
   */
  public LogicBlock_Not(UUID id)
  {
    super(id, NAME);
  }

  /** Constructor */
  public LogicBlock_Not()
  {
    super(UUID.randomUUID(), NAME);
  }

  /** Define ports of block */
  public void DefinePorts()
  {
    InputPorts.add(new BoolPort());
    OutputPorts.add(new BoolPort());
  }

  /** Calculated values in ports */
  public void Calculate()
  {
    double value = InputPorts.get(0).GetValueByName("bool");
    value = -(value - 1.0);
    OutputPorts.get(0).SetValueByName("bool", value);
    _status.setState(EState.Finished);
  }
}