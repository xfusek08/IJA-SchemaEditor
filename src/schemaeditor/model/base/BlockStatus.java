/**
 * @file:     BlockStatus.java
 * @package:  schemaeditor.model.base
 * @author    Jaromir Franek
 * @date      08.04.2018
 */
package schemaeditor.model.base;

import java.util.UUID;
import schemaeditor.model.base.enums.EState;

/**
 * Class representing status
 */
public class BlockStatus
{
  public EState State;
  public String Message;

  /** Constructor */
  public BlockStatus()
  {
    this.State = EState.Error;
    this.Message = "";
  }
}