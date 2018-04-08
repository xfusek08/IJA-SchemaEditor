/**
 * @file:     LogicBlock_Xor.java
 * @package:  safemanager.model.blocks.logic
 * @author    Petr Fusek
 * @date      08.04.2018
 */
package schemaeditor.model.blocks.logic;

import schemaeditor.model.base.Block;
import schemaeditor.model.ports.*;
import java.util.UUID;

/**
 * Block providing logical operation XOR
 */
public class LogicBlock_Xor extends Block
{
  public LogicBlock_Xor(UUID id)
  {
    super(id, "Xor");
  }

  public LogicBlock_Xor()
  {
    super(UUID.randomUUID(), "Xor");
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