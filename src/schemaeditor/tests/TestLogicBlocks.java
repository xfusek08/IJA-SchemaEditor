/**
 * @file:     TestLogicBlocks.java
 * @package:  safemanager.tests
 * @author    Petr Fusek
 * @date      08.04.2018
 */
package schemaeditor.tests;

import java.lang.reflect.Modifier;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import schemaeditor.model.blocks.logic.*;

/** Test class for complex blocks */
public class TestLogicBlocks
{
  @Before
  public void setUp()
  {
  }

  @Test
  public void Test_LogicBlock_And()
  {
    LogicBlock_And block = new LogicBlock_And();
    block.InputPorts.get(0).SetValueByName("bool", 0.0);
    block.InputPorts.get(1).SetValueByName("bool", 0.0);
    block.Calculate();
    assertEquals(block.OutputPorts.get(0).GetValueByName("bool"), 0.0);

    block.InputPorts.get(0).SetValueByName("bool", 1.0);
    block.InputPorts.get(1).SetValueByName("bool", 0.0);
    block.Calculate();
    assertEquals(block.OutputPorts.get(0).GetValueByName("bool"), 0.0);

    block.InputPorts.get(0).SetValueByName("bool", 0.0);
    block.InputPorts.get(1).SetValueByName("bool", 1.0);
    block.Calculate();
    assertEquals(block.OutputPorts.get(0).GetValueByName("bool"), 0.0);

    block.InputPorts.get(0).SetValueByName("bool", 1.0);
    block.InputPorts.get(1).SetValueByName("bool", 1.0);
    block.Calculate();
    assertEquals(block.OutputPorts.get(0).GetValueByName("bool"), 1.0);
  }

  @Test
  public void Test_LogicBlock_Or()
  {
    LogicBlock_Or block = new LogicBlock_Or();
    block.InputPorts.get(0).SetValueByName("bool", 0.0);
    block.InputPorts.get(1).SetValueByName("bool", 0.0);
    block.Calculate();
    assertEquals(block.OutputPorts.get(0).GetValueByName("bool"), 0.0);

    block.InputPorts.get(0).SetValueByName("bool", 1.0);
    block.InputPorts.get(1).SetValueByName("bool", 0.0);
    block.Calculate();
    assertEquals(block.OutputPorts.get(0).GetValueByName("bool"), 1.0);

    block.InputPorts.get(0).SetValueByName("bool", 0.0);
    block.InputPorts.get(1).SetValueByName("bool", 1.0);
    block.Calculate();
    assertEquals(block.OutputPorts.get(0).GetValueByName("bool"), 1.0);

    block.InputPorts.get(0).SetValueByName("bool", 1.0);
    block.InputPorts.get(1).SetValueByName("bool", 1.0);
    block.Calculate();
    assertEquals(block.OutputPorts.get(0).GetValueByName("bool"), 1.0);
  }

  @Test
  public void Test_LogicBlock_Xor()
  {
    LogicBlock_Xor block = new LogicBlock_Xor();
    block.InputPorts.get(0).SetValueByName("bool", 0.0);
    block.InputPorts.get(1).SetValueByName("bool", 0.0);
    block.Calculate();
    assertEquals(block.OutputPorts.get(0).GetValueByName("bool"), 0.0);

    block.InputPorts.get(0).SetValueByName("bool", 1.0);
    block.InputPorts.get(1).SetValueByName("bool", 0.0);
    block.Calculate();
    assertEquals(block.OutputPorts.get(0).GetValueByName("bool"), 1.0);

    block.InputPorts.get(0).SetValueByName("bool", 0.0);
    block.InputPorts.get(1).SetValueByName("bool", 1.0);
    block.Calculate();
    assertEquals(block.OutputPorts.get(0).GetValueByName("bool"), 1.0);

    block.InputPorts.get(0).SetValueByName("bool", 1.0);
    block.InputPorts.get(1).SetValueByName("bool", 1.0);
    block.Calculate();
    assertEquals(block.OutputPorts.get(0).GetValueByName("bool"), 0.0);
  }

  @Test
  public void Test_LogicBlock_Xnor()
  {
    LogicBlock_Xor block = new LogicBlock_Xor();
    block.InputPorts.get(0).SetValueByName("bool", 0.0);
    block.InputPorts.get(1).SetValueByName("bool", 0.0);
    block.Calculate();
    assertEquals(block.OutputPorts.get(0).GetValueByName("bool"), 1.0);

    block.InputPorts.get(0).SetValueByName("bool", 1.0);
    block.InputPorts.get(1).SetValueByName("bool", 0.0);
    block.Calculate();
    assertEquals(block.OutputPorts.get(0).GetValueByName("bool"), 0.0);

    block.InputPorts.get(0).SetValueByName("bool", 0.0);
    block.InputPorts.get(1).SetValueByName("bool", 1.0);
    block.Calculate();
    assertEquals(block.OutputPorts.get(0).GetValueByName("bool"), 0.0);

    block.InputPorts.get(0).SetValueByName("bool", 1.0);
    block.InputPorts.get(1).SetValueByName("bool", 1.0);
    block.Calculate();
    assertEquals(block.OutputPorts.get(0).GetValueByName("bool"), 1.0);
  }

  @Test
  public void Test_LogicBlock_Not()
  {
    LogicBlock_Not block = new LogicBlock_Not();
    block.InputPorts.get(0).SetValueByName("bool", 0.0);
    block.Calculate();
    assertEquals(block.OutputPorts.get(0).GetValueByName("bool"), 1.0);

    block.InputPorts.get(0).SetValueByName("bool", 1.0);
    block.Calculate();
    assertEquals(block.OutputPorts.get(0).GetValueByName("bool"), 0.0);
  }
}