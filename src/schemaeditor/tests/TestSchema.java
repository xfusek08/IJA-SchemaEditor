/**
 * @file:     TestSchema.java
 * @package:  safemanager.tests
 * @author    Petr Fusek
 * @date      08.04.2018
 */
package schemaeditor.tests;

import java.lang.reflect.Modifier;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import schemaeditor.model.base.*;
import schemaeditor.model.blocks.arithmetics.*;

public class TestSchema
{
  private Schema _schema;
  private boolean _schemaOk;
  private Block bl1, bl2, bl3;

  @Before
  public void setUp()
  {
    bl1 = new NumberBlock_Abs();
    bl2 = new NumberBlock_Add();
    bl3 = new NumberBlock_Sub();

    Connection conn1 = new Connection(bl2.ID, 0, bl3.ID, 0);
    Connection conn2 = new Connection(bl3.ID, 0, bl1.ID, 0);

    _schema = new Schema();
    _schema.AddBlock(bl1);
    _schema.AddBlock(bl2);
    _schema.AddBlock(bl3);

    _schemaOk = _schema.AddConnection(conn1) != null;
    _schemaOk = _schemaOk && _schema.AddConnection(conn2) != null;
  }

  @Test
  public void Test_GetInputPorts()
  {
    assertTrue("Invalid schema setup.", _schemaOk);
    List<Port> inPorts = new ArrayList<Port>(_schema.GetInputPorts());
    assertEquals(inPorts.size(), 3);
    assertTrue(inPorts.contains(bl3.InputPorts.get(1)));
    for (Port port : bl2.InputPorts)
      assertTrue(inPorts.contains(port));
  }
}