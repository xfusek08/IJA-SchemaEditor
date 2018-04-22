/**
 * @file:     SchemaXMLLoader.java
 * @package:  safemanager.model.safemanager
 * @author    Petr Fusek
 * @date      04.04.2018
 */
package safemanager.model.safemanager;

import schemaeditor.model.safemanager.ISchemaLoader;
import schemaeditor.model.base.Schema;

/**
 * Loader saving and loading schemas as XML files.
 */
public class SchemaXMLLoader implements ISchemaLoader
{
  /**
   * Loads and returns schema from XML file.
   *
   * @param filename Name of xml file where schema is stored
   */
  public Schema LoadSchema(String fileName)
  {
    return new Schema();
  }

  /**
   * Saves schema into a XML file.
   *
   * @param filename Name of xml file where schema will be stored
   */
  public void SaveSchema(Schema schema, String fileName)
  {

  }
}