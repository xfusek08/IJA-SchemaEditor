/**
 * @file:     TestPorts.java
 * @package:  schemaeditor.tests
 * @author    Jaromir Franek
 * @date      11.04.2018
 */
package schemaeditor.tests;

import schemaeditor.app.SchemaEditor;
import java.lang.reflect.Modifier;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import schemaeditor.model.blocks.arithmetics.*;
import schemaeditor.model.ports.*;
import java.util.HashMap;

/**
 * Test class
 */
public class TestPorts
{
  NumberPort inputPort;
  BoolPort boolPort;
  NumberPort outputPort;
  HashMap data;

  @Before
  public void setUp()
  {
    inputPort = new NumberPort();
    boolPort = new BoolPort();
    outputPort = new NumberPort();
    data = new HashMap<String, Double>();
  }

  /**
  * Zakladni test implementace ports.
  */
  @Test
  public void testPorts()
  {
    inputPort.SetValueByName("number", 1.0);
    assertEquals(1.0, inputPort.GetValueByName("number"), 0.0);
    inputPort.SetValueByName("number", 2.0);
    assertEquals(2.0, inputPort.GetValueByName("number"), 0.0);
    inputPort.SetValueByName("number", -5.0);
    assertEquals(-5.0, inputPort.GetValueByName("number"), 0.0);
    inputPort.SetValueByName("number", -5.4);
    assertEquals(-5.4, inputPort.GetValueByName("number"), 0.0);
    inputPort.SetValueByName("number", 1.2);
    assertEquals(1.2, inputPort.GetValueByName("number"), 0.0);
  }

  /**
   * Zakladni test implementace Compatible.
    */
  @Test
  public void testCompatible()
  {
    inputPort.SetValueByName("number", 1.0);
    data.put("number", 1.0);
    assertEquals(inputPort.Compatible(data), true);
    inputPort.SetValueByName("number", 1.0);
    data.put("notNumber", 1.0);
    assertEquals(inputPort.Compatible(data), false);
  }

  /**
  * Zakladni test implementace add.
  */
  @Test
  public void SetValFromPort()
  {
    inputPort.SetValueByName("number", 5.3);
    outputPort.SetValueFromPort(inputPort);
    assertEquals(5.3, inputPort.GetValueByName("number"), 0.0);
    boolPort.SetValueByName("bool", 1.0);
    outputPort.SetValueFromPort(inputPort);
    assertEquals(1.0, boolPort.GetValueByName("bool"), 0.0);
  }
}