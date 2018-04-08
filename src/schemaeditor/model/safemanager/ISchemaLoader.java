/**
 * @file:     ISchemaLoader.java
 * @package:  safemanager.model.safemanager
 * @author    Petr Fusek
 * @date      04.04.2018
 */
package schemaeditor.model.safemanager;

import schemaeditor.model.base.Schema;

/**
 * Interface providing methods to load and store schema into a file
 */
public interface ISchemaLoader
{
  /**
   * Loads and returns schema from file.
   *
   * @param filename Name of file where schema is stored
   */
  public Schema LoadSchema(String fileName);

  /**
   * Saves schema into a file.
   *
   * @param filename Name of file where schema will be stored
   */
  public void SaveSchema(Schema schema, String fileName);
}