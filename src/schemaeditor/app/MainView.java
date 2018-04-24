package schemaeditor.app;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TitledPane;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import schemaeditor.app.BlockView;
import schemaeditor.model.base.Block;
import schemaeditor.model.blocks.arithmetics.*;

public class MainView extends AnchorPane
{
  @FXML AnchorPane root_pane;
  @FXML AnchorPane SchemaPane;
  @FXML Accordion BlockSelectMenu;
  @FXML MenuBar MainMenuBar;

  public MainView()
  {
    FXMLLoader fxmlLoader = new FXMLLoader(
      getClass().getResource("resources/MainView.fxml")
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

  @FXML
  private void AddBlock(MouseEvent event)
  {
    Block newBlock = null;
    switch(((HBox)event.getSource()).getId())
    {
      case "AddBlockBt_add": newBlock = new NumberBlock_Add(); break;
      case "AddBlockBt_sub": newBlock = new NumberBlock_Sub(); break;
      case "AddBlockBt_mul": newBlock = new NumberBlock_Mul(); break;
      case "AddBlockBt_div": newBlock = new NumberBlock_Div(); break;
      case "AddBlockBt_abs": newBlock = new NumberBlock_Abs(); break;
    }
    if (newBlock != null)
    {
      newBlock.X = 100;
      newBlock.Y = 100;
      BlockView newBlockView = new BlockView(newBlock);
      SchemaPane.getChildren().add(newBlockView);
      MakeDraggableBlock(newBlockView);
    }
  }

  protected void MakeDraggableBlock(BlockView blockView)
  {

  }
}