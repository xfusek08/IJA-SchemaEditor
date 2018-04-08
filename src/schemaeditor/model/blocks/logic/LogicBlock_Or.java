/**
 * @file:     LogicBlock_Or.java
 * @package:  safemanager.model.blocks.logic
 * @author    Petr Fusek
 * @date      08.04.2018
 */
package schemaeditor.model.blocks.logic;

import schemaeditor.model.base.Block;
import schemaeditor.model.ports.*;
import java.util.UUID;

/**
 * Block providing logical operation OR
 */
public class LogicBlock_Or extends Block
{
  public LogicBlock_Or(UUID id)
  {
    super(id, "Or");
  }

  public LogicBlock_Or()
  {
    super(UUID.randomUUID(), "Or");
  }

  public void DefinePorts()
  {
    InputPorts.add(new BoolPort());
    InputPorts.add(new BoolPort());

    OutputPorts.add(new BoolPort());
  }

  public void Calculate()
  {
    // TODO: do stuff
  }
}