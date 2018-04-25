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

public class ConnectionView extends Pane
{
  @FXML Pane root_pane;
  @FXML CubicCurve Curve;

  protected Point2D _start;
  protected Point2D _end;
  protected boolean _fromOut;
  protected Connection _connection;
  protected Port _sourcePort;
  protected Port _destPort;

  public ConnectionView(Point2D start, Point2D end, boolean fromOut)
  {
    _start = start;
    _end = end;
    _fromOut = fromOut;
    _connection = new Connection();
    _sourcePort = null;
    _destPort = null;
    // System.err.print(getClass().getResource("resources/ConnectionView.fxml"));
    // System.err.print("\n");
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

  @FXML
  private void initialize()
  {
    SetStart(_start);
    SetEnd(_end);
  }

  public boolean isFromOut()
  {
    return _fromOut;
  }

  public Connection GetConnection()
  {
    return _connection;
  }

  public boolean ArePortCompatible()
  {
    if (_sourcePort != null && _destPort != null)
      return _sourcePort.Compatible(_destPort);
    return true;
  }

  public void SetStart(Point2D point)
  {
    _start = point;
    Curve.setStartX(_start.getX());
    Curve.setStartY(_start.getY());
    Curve.setControlY1(_start.getY());
    if (_fromOut)
      Curve.setControlX1(_start.getX() + 100);
    else
      Curve.setControlX1(_start.getX() - 100);
  }

  public void SetEnd(Point2D point)
  {
    _end = point;
    Curve.setEndX(_end.getX());
    Curve.setEndY(_end.getY());
    if (_start.getX() > _end.getX())
      Curve.setControlX2(_end.getX() + 100);
    else
      Curve.setControlX2(_end.getX() - 100);
    Curve.setControlY2(_end.getY());
  }

  public void setSource(Port port, UUID blockID, int portNumber)
  {
    _sourcePort = port;
    _connection.SourceBlockID = blockID;
    _connection.SourcePortNumber = portNumber;
    CheckCompatible();
  }

  public void setDest(Port port, UUID blockID, int portNumber)
  {
    _destPort = port;
    _connection.DestBlockID = blockID;
    _connection.DestPortNumber = portNumber;
    CheckCompatible();
  }

  protected void CheckCompatible()
  {
    if (!ArePortCompatible())
      Curve.setStroke(Color.RED);
    else
      Curve.setStroke(Color.BLACK);
  }
}