/**
 * @file:     Connection.java
 * @package:  safemanager.model.base
 * @author    Petr Fusek
 * @date      08.04.2018
 */
package schemaeditor.model.base;

import java.util.UUID;

/**
 * Class reprezenting one connection between output and input port of two blocks
 */
public class Connection
{
  public int DestPortNumber;
  public int SourcePortNumber;
  public UUID DestBlockID;
  public UUID SourceBlockID;

  public Connection(UUID sourceBlockID, int source,  UUID destBlockID, int dest)
  {
    DestPortNumber = dest;
    SourcePortNumber = source;
    DestBlockID = destBlockID;
    SourceBlockID = sourceBlockID;
  }
}