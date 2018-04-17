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
  private Schema _schema, _notSchema;
  private boolean _schemaOk, _notSchemaOk;
  private Block bl1, bl2, bl3, bl4, bl5, bl6;

  @Before
  public void setUp()
  {
    bl1 = new NumberBlock_Abs();
    bl2 = new NumberBlock_Add();
    bl3 = new NumberBlock_Sub();

    bl4 = new NumberBlock_Abs();
    bl5 = new NumberBlock_Abs();
    bl6 = new NumberBlock_Abs();

    Connection conn1 = new Connection(bl2.ID, 0, bl3.ID, 0);
    Connection conn2 = new Connection(bl3.ID, 0, bl1.ID, 0);

    Connection conn3 = new Connection(bl4.ID, 0, bl5.ID, 0);
    Connection conn4 = new Connection(bl5.ID, 0, bl4.ID, 0);

    _schema = new Schema();
    _schema.AddBlock(bl1);
    _schema.AddBlock(bl2);
    _schema.AddBlock(bl3);

    _notSchema = new Schema();
    _notSchema.AddBlock(bl4);
    _notSchema.AddBlock(bl5);
    _notSchema.AddBlock(bl6);

    _schemaOk = _schema.AddConnection(conn1) != null;
    _schemaOk = _schemaOk && _schema.AddConnection(conn2) != null;

    _notSchemaOk = _notSchema.AddConnection(conn3) != null;
    _notSchemaOk = _notSchemaOk && _notSchema.AddConnection(conn4) != null;
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

  @Test
  public void Test_GetOutputPorts()
  {
    assertTrue("Invalid schema setup.", _schemaOk);
    List<Port> outPorts = new ArrayList<Port>(_schema.GetOutPorts());
    assertEquals(outPorts.size(), 1);
    assertEquals(outPorts.get(0), bl1.OutputPorts.get(0));
  }


  @Test
  public void Test_GetInBlocks()
  {
    assertTrue("Invalid schema setup.", _schemaOk);
    List<Block> inBlocks = new ArrayList<Block>(_schema.GetInBlocks());
    assertEquals(inBlocks.size(), 2);
    assertTrue(inBlocks.contains(bl3));
    assertTrue(inBlocks.contains(bl2));
  }


  @Test
  public void Test_RemoveBlock_and_GetBlocks()
  {
    List<Block> bList = null;
    _schema.RemoveBlock(bl1);
    bList = _schema.GetBlocks();
    assertEquals(2, _schema.GetBlocks().size());
    assertEquals(1, _schema.GetConnections().size());
  }

  @Test
  public void Test_Cycles()
  {
    assertFalse("Valid schema setup.", _notSchemaOk);
    List<Port> outPorts = new ArrayList<Port>(_notSchema.GetOutPorts());
    assertEquals(outPorts.size(), 2);
  }

}