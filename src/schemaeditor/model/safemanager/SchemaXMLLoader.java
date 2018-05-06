/**
 * @file:     SchemaXMLLoader.java
 * @package:  safemanager.model.safemanager
 * @author    Petr Fusek
 * @date      04.04.2018
 */

package schemaeditor.model.safemanager;

import schemaeditor.model.safemanager.*;
import schemaeditor.model.base.*;
import java.util.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Loader saving and loading schemas as XML files.
 */
public class SchemaXMLLoader implements ISchemaLoader
{
  /**
   * Loads and returns schema from XML file.
   *
   * @param fileName Name of xml file where schema is stored
   * @throws JAXBException exception
   * @throws IOException exception
   * @return schema
   */
  public Schema LoadSchema(String fileName) throws JAXBException, IOException
  {
    JAXBContext context = JAXBContext.newInstance(SaveSchema.class);
    Unmarshaller read = context.createUnmarshaller();
    SaveSchema rSchema = (SaveSchema) read.unmarshal(new FileReader(fileName));
    Schema _schema = new Schema();
    for(SaveBlock block : rSchema.getBlock())
    {
      Block saveBlock;
      saveBlock = block.getFromSave();
      _schema.AddBlock(saveBlock);
    }
    for(SaveConnection conn : rSchema.getConn())
    {
      Connection saveConn;
      saveConn = conn.getFromSave();
      _schema.AddConnection(saveConn);
    }
    return _schema;
  }

  /**
   * Saves schema into a XML file.
   *
   * @param fileName Name of xml file where schema will be stored
   * @throws JAXBException exception
   * @throws IOException exception
   */
  public void SaveSchema(Schema schema, String fileName) throws JAXBException, IOException
  {
    List<SaveBlock> blocks = new ArrayList<>();
    for(Block block :  schema.GetBlocks())
    {
      SaveBlock saveBlock = new SaveBlock();
      saveBlock.setFromSchema(block);
      blocks.add(saveBlock);
    }
    Set<SaveConnection> conns = new HashSet<SaveConnection>();
    for(Connection conn : schema.GetConnections())
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
    m.marshal(sSchema, new File(fileName));
  }
}