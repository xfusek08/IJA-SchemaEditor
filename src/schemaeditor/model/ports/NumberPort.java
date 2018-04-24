/**
 * @file:     NumberPort.java
 * @package:  schemaeditor.model.ports
 * @author    Jaromir Franek
 * @date      08.04.2018
 */
package schemaeditor.model.ports;

import schemaeditor.model.base.Port;

/**
 * Class representing one numeric (double) value
 */
public class NumberPort extends Port
{
  /** Constructor */
  public NumberPort()
  {
    type = "number";
    DefineValue("number", 0.0);
  }
}