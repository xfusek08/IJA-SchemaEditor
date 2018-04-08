/**
 * @file:     LogicBlock_And.java
 * @package:  safemanager.model.blocks.logic
 * @author    Petr Fusek
 * @date      08.04.2018
 */
package schemaeditor.model.blocks.logic;

import schemaeditor.model.base.Block;
import schemaeditor.model.ports.*;
import java.util.UUID;

/**
 * Block providing logical operation AND
 */
public class LogicBlock_And extends Block
{
  public LogicBlock_And(UUID id)
  {
    super(id, "And");
  }

  public LogicBlock_And()
  {
    super(UUID.randomUUID(), "And");
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