/**
 * @file:     EAddStatus.java
 * @package:  schemaeditor.model.base.enums
 * @author    Jaromir Franek
 * @date      16.04.2018
 */

package schemaeditor.model.base.enums;

public enum EAddStatus
{
  Ok,
  OutSourcePortNotFound,
  InDestPortNotFound,
  PortsIncompatible,
  ConnectionCausesCycles,
  OtherError;
}