package schemaeditor.app.resources;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class BlockController extends AnchorPane {

  public BlockController()
  {
    FXMLLoader fxmlLoader = new FXMLLoader(
        getClass().getResource("resources/BlockController.fxml")
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