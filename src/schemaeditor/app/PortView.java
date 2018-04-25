package schemaeditor.app;

import java.io.IOException;
import java.net.URL;
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

public class PortView extends AnchorPane
{
  @FXML AnchorPane root_pane;
  @FXML Circle Aura;
  @FXML Line PortLine;

  protected Port _port;
  protected boolean _isOutput;
  protected int _portNum;
  protected ConnectionView _connection;
  protected boolean _isConnectedEnd;
  // protected Label _inputNumLabel;

  public PortView(Port port, boolean isOutput, int portNum)
  {
    _port = port;
    _isOutput = isOutput;
    _portNum = portNum;
    _connection = null;
    // System.err.print(getClass().getResource("resources/PortView.fxml"));
    // System.err.print("\n");
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

  public Port GetPort()
  {
    return _port;
  }

  public int GetPortNum()
  {
    return _portNum;
  }

  public boolean IsOutput()
  {
    return _isOutput;
  }

  public Point2D GetTip()
  {
    if (_isOutput)
      return PortLine.localToScene(PortLine.getEndX(), PortLine.getEndY());
    return PortLine.localToScene(PortLine.getStartY(), PortLine.getStartX());
  }

  public void SetHover()
  {
    Aura.setStroke(Color.SKYBLUE);
  }

  public void UnSetHover()
  {
    Aura.setStroke(Color.TRANSPARENT);
  }

  public ConnectionView RegisterConn(ConnectionView connection, boolean isStart)
  {
    // ClearInputNumber();
    ConnectionView old = _connection;
    _connection = connection;
    _isConnectedEnd = !isStart;
    return old;
  }

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

  // public void SetInputLabel()
  // {
  //   getChildren().remove(_inputNumLabel);
  //   if (_port.isInput())
  //   {
  //     _inputNumber = number;
  //     _inputNumLabel = new Label(String.valueOf(_port.getInputNumber()));
  //     _inputNumLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 20px;");
  //     _inputNumLabel.setTextFill(Color.SKYBLUE);
  //     getChildren().add(_inputNumLabel);
  //   }
  // }
}