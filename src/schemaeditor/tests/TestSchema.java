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
import java.util.*;
import schemaeditor.model.base.*;
import schemaeditor.model.blocks.arithmetics.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

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
    //change schema
    Connection conn3 = new Connection(bl1.ID, 0, bl2.ID, 0);
    _schemaOk = _schemaOk && _schema.AddConnection(conn3) != null;
    assertEquals(3, _schema.GetConnections().size());
    //test
    assertFalse("Valid schema setup.", _schemaOk);
    /*List<Port> outPorts = new ArrayList<Port>(_schema.GetOutPorts());
    assertEquals(outPorts.size(), 2);*/
  }

  @Test
  public void Test_SaveAndLoad() throws JAXBException, IOException
  {
    Set<SaveSchemaBlock> blocks = new HashSet<SaveSchemaBlock>();
    for(SchemaBlock block : _schema._blocks)
    {
      SaveSchemaBlock saveBlock = new SaveSchemaBlock();
      saveBlock.setFromSchema(block);
      blocks.add(saveBlock);
    }
    Set<SaveConnection> conns = new HashSet<SaveConnection>();
    for(Connection conn : _schema._connections)
    {
      SaveConnection saveConn = new SaveConnection();
      saveConn.setFromSchema(conn);
      conns.add(saveConn);
    }
    SaveSchema sSchema = new SaveSchema();
    sSchema.setBlock(blocks);
    sSchema.setConn(conns);
    JAXBContext context = JAXBContext.newInstance(SaveSchema.class);
    Marshaller m = context.createMarshaller();
    m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
    m.marshal(sSchema, new File("./Save.xml"));

    Unmarshaller read = context.createUnmarshaller();
    SaveSchema rSchema = (SaveSchema) read.unmarshal(new FileReader("./Save.xml"));
    assertEquals(3, rSchema.getBlock().size());
    assertEquals(2, rSchema.getConn().size());

    Schema lSchema = new Schema(rSchema);
    assertEquals(3, lSchema._blocks.size());
    assertEquals(2, lSchema._connections.size());
  }

}