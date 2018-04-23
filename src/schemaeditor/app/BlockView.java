package schemaeditor.app;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import schemaeditor.model.base.Block;
import schemaeditor.model.base.Port;

public class BlockView extends AnchorPane
{
  @FXML AnchorPane root_pane;

  @FXML GridPane InputPortGrid;
  @FXML GridPane OutputPortGrid;
  @FXML HBox BlockBody;
  @FXML Label label_DisplayName;

  protected Block _block;

  public BlockView(Block block)
  {
    _block = block;
    System.err.print(getClass().getResource("resources/BlockView.fxml"));
    System.err.print("\n");
    FXMLLoader fxmlLoader = new FXMLLoader(
        getClass().getResource("resources/BlockView.fxml")
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
    label_DisplayName.setText(_block.DisplayName);
    int cnt = 0;
    InputPortGrid.getRowConstraints().clear();
    OutputPortGrid.getRowConstraints().clear();
    for (Port port : _block.InputPorts)
    {
      RowConstraints row = new RowConstraints();
      row.setPercentHeight(100.0 / _block.InputPorts.size());
      InputPortGrid.getRowConstraints().add(row);
      InputPortGrid.add(new PortView(port), 0, cnt++);
    }
    cnt = 0;
    for (Port port : _block.OutputPorts)
    {
      RowConstraints row = new RowConstraints();
      row.setPercentHeight(100 / _block.OutputPorts.size());
      OutputPortGrid.getRowConstraints().add(row);
      OutputPortGrid.add(new PortView(port), 0, cnt++);
    }
  }
}