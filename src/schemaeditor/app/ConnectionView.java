/**
 * @file:     ConnectionView.java
 * @package:  schemaeditor.app
 * @author    Petr Fusek
 * @date      29.04.2018
 */
package schemaeditor.app;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.CubicCurve;
import jdk.nashorn.internal.ir.SetSplitState;
import schemaeditor.model.base.Connection;
import schemaeditor.model.base.Port;

/**
 * Class representing connection in schema
 */
public class ConnectionView extends Pane
{
  @FXML Pane root_pane;
  @FXML CubicCurve Curve;

  protected Point2D _start;
  protected Point2D _end;
  protected boolean _fromOut;
  protected Connection _connection;

  /**
   * Constructor
   * @param conn connection to be viewed
   * @param start starting point
   * @param end ending point
   * @param fromOut boolean defining if from out port
   */
  public ConnectionView(Connection conn, Point2D start, Point2D end, boolean fromOut)
  {
    _start = start;
    _end = end;
    _fromOut = fromOut;
    _connection = conn;
    FXMLLoader fxmlLoader = new FXMLLoader(
        getClass().getResource("resources/ConnectionView.fxml")
    );
    fxmlLoader.setRoot(this);
    fxmlLoader.setController(this);
    try
    {
      fxmlLoader.load();
    }
    catch (IOException exception)
    {
      throw new RuntimeException(exception);
    }
  }

  /**
   * Initialization of connection
   */
  @FXML
  private void initialize()
  {
    SetStart(_start);
    SetEnd(_end);
  }

  /**
   * Get boolean to chceck if from out port
   * @return true if is draged from out port
   */
  public boolean isFromOut()
  {
    return _fromOut;
  }

  /**
   * Get connection
   * @return connection of its block are not null
   */
  public Connection GetConnection()
  {
    if (_connection.SourceBlockID != null && _connection.DestBlockID != null)
      return _connection;
    return null;
  }

  /**
   * Set starting point of connection
   * @param point starting point
   */
  public void SetStart(Point2D point)
  {
    _start = point;
    Curve.setStartX(_start.getX());
    Curve.setStartY(_start.getY());
    Curve.setControlY1(_start.getY());
    if (_fromOut)
      Curve.setControlX1(_start.getX() + 50);
    else
      Curve.setControlX1(_start.getX() - 50);
  }

  /**
   * Set ending point of connection
   * @param point end point
   */
  public void SetEnd(Point2D point)
  {
    _end = point;
    Curve.setEndX(_end.getX());
    Curve.setEndY(_end.getY());
    if (_start.getX() > _end.getX())
      Curve.setControlX2(_end.getX() + 50);
    else
      Curve.setControlX2(_end.getX() - 50);
    Curve.setControlY2(_end.getY());
  }

  /**
   * Set source block and source port number
   * @param blockID source block ID
   * @param portNumber source port number
   */
  public void setSource(UUID blockID, int portNumber)
  {
    UnSetRed();
    _connection.SourceBlockID = blockID;
    _connection.SourcePortNumber = portNumber;
  }

  /**
   * Set destination block and destination port 
   * @param blockID destination block ID
   * @param portNumber destination port number
   */
  public void setDest(UUID blockID, int portNumber)
  {
    UnSetRed();
    _connection.DestBlockID = blockID;
    _connection.DestPortNumber = portNumber;
  }

  /**
   * Set port
   * @param isOutput boolean to determine if port if out port
   * @param blockID block ID
   * @param portNumber port number
   */
  public void SetPort(boolean isOutput, UUID blockID, int portNumber)
  {
    if (isOutput)
      setSource(blockID, portNumber);
    else
      setDest(blockID, portNumber);
  }

  /**
   * Set message red
   */
  public void SetRed()
  {
    Curve.setStroke(Color.RED);
  }

  /**
   * Unset message red
   */
  public void UnSetRed()
  {
    Curve.setStroke(Color.BLACK);
  }
}