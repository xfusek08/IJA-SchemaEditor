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
  public LogicBlock_Xnor(UUID id)
  {
    super(id, "Xnor");
  }

  public LogicBlock_Xnor()
  {
    super(UUID.randomUUID(), "Xnor");
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