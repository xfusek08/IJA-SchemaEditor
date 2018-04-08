/**
 * @file:     LogicBlock_Not.java
 * @package:  safemanager.model.blocks.logic
 * @author    Petr Fusek
 * @date      08.04.2018
 */
package schemaeditor.model.blocks.logic;

import schemaeditor.model.base.Block;
import java.util.UUID;

/**
 * Block providing logical operation NOT
 */
public class LogicBlock_Not extends Block
{
  public LogicBlock_Not(UUID id)
  {
    super(id, "Not");
  }

  public LogicBlock_Not()
  {
    super(UUID.randomUUID(), "Not");
  }

  public void DefinePorts()
  {
    InputPorts.add(new BoolPort());
    OutputPorts.add(new BoolPort());
  }

  public void Calculate()
  {

  }
}