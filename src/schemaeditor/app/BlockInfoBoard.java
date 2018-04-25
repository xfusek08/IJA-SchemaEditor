package schemaeditor.app;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.UUID;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.CubicCurve;
import javafx.scene.text.Text;
import jdk.nashorn.internal.ir.SetSplitState;
import schemaeditor.model.base.Block;
import schemaeditor.model.base.BlockStatus;
import schemaeditor.model.base.Connection;
import schemaeditor.model.base.Port;
import schemaeditor.model.base.enums.EState;

public class BlockInfoBoard extends AnchorPane implements Observer
{
  @FXML AnchorPane root_pane;
  @FXML Label IDLabel;
  @FXML Label StatusLabel;
  @FXML Label MessageLabel;
  @FXML GridPane InValuesGrid;
  @FXML GridPane OutValuesGrid;

  protected Block _block;

  public BlockInfoBoard(Block block)
  {
    _block = block;
    _block.addObserver(this);
    // System.err.print(getClass().getResource("resources/BlockInfoBoard.fxml"));
    // System.err.print("\n");
    FXMLLoader fxmlLoader = new FXMLLoader(
        getClass().getResource("resources/BlockInfoBoard.fxml")
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
    LoadFromBlock();
  }

  public void update(Observable obs, Object obj)
  {
    LoadFromBlock();
  }

  public void LoadFromBlock()
  {
    IDLabel.setText(_block.ID.toString());
    switch (_block.GetStatus().getState())
    {
      case Finished:
        StatusLabel.setText("Finished");
        setStyle("-fx-background-color: rgb(200,255,200);");
        break;
      case Error:
        StatusLabel.setText("Error");
        setStyle("-fx-background-color: rgb(255,200,200);");
        break;
      case Ready:
        StatusLabel.setText("Ready");
        setStyle("-fx-background-color: white;");
        break;
    }
    MessageLabel.setText(_block.GetStatus().getMessage());

    int portNum = 0;
    for (Port port : _block.InputPorts)
    {
      InValuesGrid.add(new Text(String.valueOf(portNum) + ":"), 0, portNum);
      InValuesGrid.add(new Text(port.GetValueAsString()), 1, portNum);
      portNum++;
    }
    portNum = 0;
    for (Port port : _block.OutputPorts)
    {
      OutValuesGrid.add(new Text(String.valueOf(portNum) + ":"), 0, portNum);
      OutValuesGrid.add(new Text(port.GetValueAsString()), 1, portNum);
      portNum++;
    }
  }
}