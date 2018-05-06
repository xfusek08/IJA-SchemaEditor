/**
 * @file:     ISchemaLoader.java
 * @package:  safemanager.model.safemanager
 * @author    Petr Fusek
 * @date      04.04.2018
 */
package schemaeditor.model.safemanager;

import schemaeditor.model.base.Schema;
import javax.xml.bind.JAXBException;
import java.io.IOException;

/**
 * Interface providing methods to load and store schema into a file
 */
public interface ISchemaLoader
{
  /**
   * Loads and returns schema from file.
   * @param filename Name of file where schema is stored
   */
  public Schema LoadSchema(String fileName) throws JAXBException, IOException;

  /**
   * Saves schema into a file.
   *
   * @param schema    Schema to be saved
   * @param filename Name of file where schema will be stored
   */
  public void SaveSchema(Schema schema, String fileName) throws JAXBException, IOException;
}