/**
 * @file:     TestCplexBlocks.java
 * @package:  safemanager.tests
 * @author    Petr Fusek
 * @date      08.04.2018
 */
package schemaeditor.tests;

import java.lang.reflect.Modifier;
import org.junit.Before;
import org.junit.Test;
import jdk.nashorn.internal.ir.Block;
import static org.junit.Assert.*;

import schemaeditor.model.blocks.complex.*;

/** Test class for complex blocks */
public class TestCplexBlocks
{
  @Before
  public void setUp()
  {
  }

  @Test
  public void Test_CplexBlock_Abs()
  {
    CplexBlock_Abs block = new CplexBlock_Abs();
    block.InputPorts.get(0).SetValueByName("real", 3.0);
    block.InputPorts.get(0).SetValueByName("imaginary", 4.0);
    block.Calculate();
    assertEquals(block.OutputPorts.get(0).GetValueByName("number"), 5.0);
  }

  @Test
  public void Test_CplexBlock_Add()
  {
    CplexBlock_Add block = new CplexBlock_Add();
    block.InputPorts.get(0).SetValueByName("real", 3.0);
    block.InputPorts.get(0).SetValueByName("imaginary", 4.0);
    block.InputPorts.get(1).SetValueByName("real", 2.0);
    block.InputPorts.get(1).SetValueByName("imaginary", -4.0);
    block.Calculate();
    assertEquals(block.OutputPorts.get(0).GetValueByName("real"), 5.0);
    assertEquals(block.OutputPorts.get(0).GetValueByName("imaginary"), 0.0);
  }

  @Test
  public void Test_CplexBlock_Complement()
  {
    CplexBlock_Complement block = new CplexBlock_Complement();
    block.InputPorts.get(0).SetValueByName("real", 3.0);
    block.InputPorts.get(0).SetValueByName("imaginary", 4.0);
    block.Calculate();
    assertEquals(block.OutputPorts.get(0).GetValueByName("real"), 3.0);
    assertEquals(block.OutputPorts.get(0).GetValueByName("imaginary"), -4.0);
  }

  @Test
  public void Test_CplexBlock_Div()
  {
    CplexBlock_Div block = new CplexBlock_Div();
    block.InputPorts.get(0).SetValueByName("real", 1.0);
    block.InputPorts.get(0).SetValueByName("imaginary", -3.0);
    block.InputPorts.get(1).SetValueByName("real", 1.0);
    block.InputPorts.get(1).SetValueByName("imaginary", 2.0);
    block.Calculate();
    assertEquals(block.OutputPorts.get(0).GetValueByName("real"), -1.0);
    assertEquals(block.OutputPorts.get(0).GetValueByName("imaginary"), -1.0);
  }

  @Test
  public void Test_CplexBlock_Mul()
  {
    CplexBlock_Mul block = new CplexBlock_Mul();
    block.InputPorts.get(0).SetValueByName("real", 1.0);
    block.InputPorts.get(0).SetValueByName("imaginary", 4.0);
    block.InputPorts.get(1).SetValueByName("real", 5.0);
    block.InputPorts.get(1).SetValueByName("imaginary", 1.0);
    block.Calculate();
    assertEquals(block.OutputPorts.get(0).GetValueByName("real"), 1.0);
    assertEquals(block.OutputPorts.get(0).GetValueByName("imaginary"), 21.0);
  }

  @Test
  public void Test_CplexBlock_Sub()
  {
    CplexBlock_Sub block = new CplexBlock_Sub();
    block.InputPorts.get(0).SetValueByName("real", 1.0);
    block.InputPorts.get(0).SetValueByName("imaginary", 4.0);
    block.InputPorts.get(1).SetValueByName("real", 5.0);
    block.InputPorts.get(1).SetValueByName("imaginary", 1.0);
    block.Calculate();
    assertEquals(block.OutputPorts.get(0).GetValueByName("real"), -4.0);
    assertEquals(block.OutputPorts.get(0).GetValueByName("imaginary"), 3.0);
  }
}