/**
 * @file:     Connection.java
 * @package:  safemanager.model.base
 * @author    Petr Fusek
 * @date      08.04.2018
 */
package schemaeditor.model.base;

import java.util.Arrays;
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

  /** Constructor */
  public Connection(UUID sourceBlockID, int source,  UUID destBlockID, int dest)
  {
    DestPortNumber = dest;
    SourcePortNumber = source;
    DestBlockID = destBlockID;
    SourceBlockID = sourceBlockID;
  }

  /** Constructor */
  public Connection()
  {
  }

  /**
   * Check if all atributes of connection are equals 
   * @param obj another connection
   * @return true if are equal 
   */
  public boolean equals(Object obj)
  {
    if (obj instanceof Connection)
    {
      Connection other = (Connection) obj;
      return
        DestBlockID.equals(other.DestBlockID) &&
        SourceBlockID.equals(other.SourceBlockID) &&
        DestPortNumber == other.DestPortNumber &&
        SourcePortNumber == other.SourcePortNumber;
    }
    return false;
  }

  /**
   * Return hash code of connection
   * @return hashCode  
   */
  public int hashCode()
  {
    return Arrays.hashCode(new Object[] {DestBlockID, SourceBlockID, DestPortNumber, SourcePortNumber});
  }
}