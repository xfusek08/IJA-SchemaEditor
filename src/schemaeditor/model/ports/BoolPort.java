/**
 * @file:     BoolPort.java
 * @package:  schemaeditor.model.ports
 * @author    Jaromir Franek
 * @date      08.04.2018
 */
package schemaeditor.model.ports;

import schemaeditor.model.base.Port;

/**
 * Port carying boolean value
 */
public class BoolPort extends Port
{
  /** Constructor */
  public BoolPort()
  {
    type = "bool";
    DefineValue("bool", 0.0);
  }
}