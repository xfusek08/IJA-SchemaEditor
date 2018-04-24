/**
 * @file:     SaveConnection.java
 * @package:  safemanager.model.base
 * @author    Jaromir Franek
 * @date      24.04.2018
 */
package schemaeditor.model.base;

import java.util.Arrays;
import java.util.UUID;

/**
 * Class reprezenting one connection between output and input port of two blocks
 */
public class SaveConnection
{
  private int DestPortNumber;
  private int SourcePortNumber;
  private UUID DestBlockID;
  private UUID SourceBlockID;

  public void setDestPort(int DestPortNumber) 
  {
    this.DestPortNumber = DestPortNumber;
  }

  public int getDestPort() 
  {
    return DestPortNumber;
  }

  public void setSourcePort(int SourcePortNumber) 
  {
    this.SourcePortNumber = SourcePortNumber;
  }

  public int getSourcePort() 
  {
    return SourcePortNumber;
  }

  public void setDestBlock(UUID DestBlockID) 
  {
    this.DestBlockID = DestBlockID;
  }

  public UUID getDestBlock() 
  {
    return DestBlockID;
  }

  public void setSourceBlock(UUID SourceBlockID) 
  {
    this.SourceBlockID = SourceBlockID;
  }

  public UUID getSourceBlock() 
  {
    return SourceBlockID;
  }

  public void setFromSchema(Connection connection)
  {
    this.DestPortNumber = connection.DestPortNumber;
    this.SourcePortNumber = connection.SourcePortNumber;
    this.DestBlockID = connection.DestBlockID;
    this.SourceBlockID = connection.SourceBlockID;
  }

  public Connection getFromSave()
  {
    Connection connection = new Connection(this.SourceBlockID, this.SourcePortNumber, this.DestBlockID, this.DestPortNumber);
    return connection;
  }

}