/**
 * @file:     PortView.java
 * @package:  schemaeditor.app
 * @author    Petr Fusek
 * @date      06.05.2018
 */
package schemaeditor.app;

import java.awt.Point;
import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.UUID;

import com.sun.glass.ui.GestureSupport;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import schemaeditor.model.base.Block;
import schemaeditor.model.base.Port;
import schemaeditor.model.blocks.arithmetics.*;

/**
 * Controller class from port model.
 */
public class PortView extends AnchorPane implements Observer
{
  @FXML AnchorPane root_pane;
  @FXML Circle Aura;
  @FXML Line PortLine;

  protected Port _port;
  protected boolean _isOutput;
  protected int _portNum;
  protected ConnectionView _connection;
  protected boolean _isConnectedEnd;
  protected Label _inputNumLabel;

  /**
   * Constructor
   * @param port      port to be observed
   * @param isOutput  flag if fort is output, false - port is input
   * @param portNum   Number of port on block. (index in bloks port array)
  */
  public PortView(Port port, boolean isOutput, int portNum)
  {
    _port = port;
    _isOutput = isOutput;
    _portNum = portNum;
    _connection = null;
    _port.addObserver(this);
    FXMLLoader fxmlLoader = new FXMLLoader(
        getClass().getResource("resources/PortView.fxml")
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
   * Initialize
   */
  @FXML
  private void initialize()
  {
    Aura.toFront();
    if (_isOutput)
    {
      Aura.setLayoutX(Aura.getLayoutX() + 4);
      PortLine.setEndX(20);
    }
    else
    {
      Aura.setLayoutX(Aura.getLayoutX() - 10);
      PortLine.setStartY(0);
    }
    MakeEvents();
  }

  /**
   * Get port
   * @return port
   */
  public Port GetPort()
  {
    return _port;
  }

  /**
   * Get port number
   * @return port number
   */
  public int GetPortNum()
  {
    return _portNum;
  }

  /**
   * Get boolean defining if port is out port
   * @return boolean
   */
  public boolean IsOutput()
  {
    return _isOutput;
  }

  /**
   * Get tip
   * @return position of tip
   */
  public Point2D GetTip()
  {
    return Aura.localToScene(Aura.getCenterX(), Aura.getCenterY());
  }

  /**
   * Set blue color
   */
  public void SetHover()
  {
    Aura.setStroke(Color.SKYBLUE);
  }

  /**
   * Unset hover (color)
   */
  public void UnSetHover()
  {
    Aura.setStroke(Color.TRANSPARENT);
  }

  /**
   * @param connection connection to be registered
   * @param isStart determine if is starting
   * @return old connection
   */
  public ConnectionView RegisterConn(ConnectionView connection, boolean isStart)
  {
    ConnectionView old = _connection;
    _connection = connection;
    _isConnectedEnd = !isStart;
    return old;
  }

  /**
   * Move connection
   * @param Scene scene to be moved
   */
  public void MoveConnection(Parent Scene)
  {
    if (_connection != null)
    {
      if (_isConnectedEnd)
        _connection.SetEnd(Scene.sceneToLocal(GetTip()));
      else
        _connection.SetStart(Scene.sceneToLocal(GetTip()));
    }
  }

  /**
   * Make events
   */
  protected void MakeEvents()
  {
    Aura.setOnMouseEntered(new EventHandler<MouseEvent>() {
      @Override public void handle(MouseEvent evMouseEvent) {
        SetHover();
      }
    });

    Aura.setOnMouseExited(new EventHandler<MouseEvent>() {
      @Override public void handle(MouseEvent evMouseEvent) {
        UnSetHover();
      }
    });
  }

  /**
   * Update object
   * @param obj object to be updated
   */
  public void update(Observable obs, Object obj)
  {
    if (_inputNumLabel != null)
      getChildren().remove(_inputNumLabel);

    if (_port.IsInput())
    {
      _inputNumLabel = new Label(String.valueOf(_port.GetInputNumber()));
      _inputNumLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 20px;");
      _inputNumLabel.setTextFill(Color.SKYBLUE);
      _inputNumLabel.setMouseTransparent(true);
      getChildren().add(_inputNumLabel);
    }
  }
}