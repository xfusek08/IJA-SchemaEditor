package schemaeditor.app;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import schemaeditor.model.base.Block;
import schemaeditor.model.base.Port;
import schemaeditor.model.blocks.arithmetics.*;

public class PortView extends AnchorPane
{
  @FXML AnchorPane root_pane;

  protected Port _port;

  public PortView(Port port)
  {
    _port = port;
    System.err.print(getClass().getResource("resources/PortView.fxml"));
    System.err.print("\n");
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
  }
}