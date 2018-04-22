/**
 * @file:     ComplexPort.java
 * @package:  schemaeditor.model.ports
 * @author    Jaromir Franek
 * @date      08.04.2018
 */
package schemaeditor.model.ports;

import schemaeditor.model.base.Port;

/**
 * Port carying complex value
 */
public class ComplexPort extends Port
{
  /** Constructor */
  public ComplexPort()
  {
    DefineValue("real", 0.0);
    DefineValue("imaginary", 0.0);
  }
}