/**
 * @file:     SaveConnection.java
 * @package:  safemanager.model.safemanager
 * @author    Jaromir Franek
 * @date      24.04.2018
 */

package schemaeditor.model.safemanager;

import java.util.Arrays;
import java.util.UUID;
import schemaeditor.model.base.*;

/**
 * Class representing one connection between output and input port of two blocks
 */
public class SaveConnection
{
  private int DestPortNumber;
  private int SourcePortNumber;
  private UUID DestBlockID;
  private UUID SourceBlockID;

  /**
   * Set save method
   * @param DestPortNumber destination port number
  */
  public void setDestPort(int DestPortNumber)
  {
    this.DestPortNumber = DestPortNumber;
  }

  /**
   * Get save method
   * @return destination port number
  */
  public int getDestPort()
  {
    return DestPortNumber;
  }

  /**
   * Set save method
   * @param SourcePortNumber source port number
  */
  public void setSourcePort(int SourcePortNumber)
  {
    this.SourcePortNumber = SourcePortNumber;
  }

  /**
   * Get save method
   * @return source port number
  */
  public int getSourcePort()
  {
    return SourcePortNumber;
  }

  /**
   * Set save method
   * @param DestBlockID destination block ID
  */
  public void setDestBlock(UUID DestBlockID)
  {
    this.DestBlockID = DestBlockID;
  }

  /**
   * Get save method
   * @return destination block ID
  */
  public UUID getDestBlock()
  {
    return DestBlockID;
  }

  /**
   * Set save method
   * @param SourceBlockID source block ID
  */
  public void setSourceBlock(UUID SourceBlockID)
  {
    this.SourceBlockID = SourceBlockID;
  }

  /**
   * Get save method
   * @return source block ID
  */
  public UUID getSourceBlock()
  {
    return SourceBlockID;
  }

  /**
   * Set save method
   * @param connection connection to be set
  */
  public void setFromSchema(Connection connection)
  {
    this.DestPortNumber = connection.DestPortNumber;
    this.SourcePortNumber = connection.SourcePortNumber;
    this.DestBlockID = connection.DestBlockID;
    this.SourceBlockID = connection.SourceBlockID;
  }

  /**
   * Get save method
   * @return connection that was saved
  */
  public Connection getFromSave()
  {
    Connection connection = new Connection(this.SourceBlockID, this.SourcePortNumber, this.DestBlockID, this.DestPortNumber);
    return connection;
  }

}