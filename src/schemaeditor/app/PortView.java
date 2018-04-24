package schemaeditor.app;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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

  public PortView(Port port, boolean isOutput)
  {
    _port = port;
    _isOutput = isOutput;
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
      Aura.setLayoutX(Aura.getLayoutX() + 10);
      PortLine.setEndX(20);
    }
    else
    {
      Aura.setLayoutX(Aura.getLayoutX() - 10);
      PortLine.setStartY(0);
    }
    MakeEvents();
  }

  protected void MakeEvents()
  {
    Aura.setOnMouseEntered(new EventHandler<MouseEvent>() {
      @Override public void handle(MouseEvent evMouseEvent) {
        Aura.setStroke(Color.SKYBLUE);
      }
    });

    Aura.setOnMouseExited(new EventHandler<MouseEvent>() {
      @Override public void handle(MouseEvent evMouseEvent) {
        Aura.setStroke(Color.TRANSPARENT);
      }
    });
  }
}