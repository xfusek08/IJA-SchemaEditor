/**
 * @file:     BlockView.java
 * @package:  schemaeditor.app
 * @author    Petr Fusek
 * @date      29.04.2018
 */
package schemaeditor.app;

import java.awt.Point;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import schemaeditor.model.base.Block;
import schemaeditor.model.base.Port;

/**
 * Class representing block view
 */
public class BlockView extends AnchorPane implements Observer
{
  @FXML AnchorPane root_pane;

  @FXML GridPane InputPortGrid;
  @FXML GridPane OutputPortGrid;
  @FXML VBox BlockBody;
  @FXML Text DisplayName;
  @FXML Pane DeleteButton;

  protected Block _block;
  protected Point2D _dragOffset;

  private EventHandler <DragEvent> DragOverHandler;
  private EventHandler <DragEvent> DragDroppedHandler;
  private EventHandler <DragEvent> DragExitedHandler;

  protected String _styles;

  /**
   * Constructor
   * @param block block to be view
   */
  public BlockView(Block block)
  {
    _block = block;
    _block.addObserver(this);
    _dragOffset = new Point2D(0,0);

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

  /**
   * Block initialization
   */
  @FXML
  private void initialize()
  {
    _styles = BlockBody.getStyle();
    DisplayName.setText(_block.DisplayName);
    this.setLayoutX(_block.X);
    this.setLayoutY(_block.Y);

    int cnt = 0;
    InputPortGrid.getRowConstraints().clear();
    OutputPortGrid.getRowConstraints().clear();
    for (Port port : _block.InputPorts)
    {
      RowConstraints row = new RowConstraints();
      row.setPercentHeight(100.0 / _block.InputPorts.size());
      InputPortGrid.getRowConstraints().add(row);
      InputPortGrid.add(new PortView(port, false, cnt), 0, cnt);
      cnt++;
    }
    cnt = 0;
    for (Port port : _block.OutputPorts)
    {
      RowConstraints row = new RowConstraints();
      row.setPercentHeight(100 / _block.OutputPorts.size());
      OutputPortGrid.getRowConstraints().add(row);
      OutputPortGrid.add(new PortView(port, true, cnt), 0, cnt);
      cnt++;
    }
    Reload();
    MakeDragEvents();

    DeleteButton.setOnMouseEntered(new EventHandler<MouseEvent>() {
      @Override public void handle(MouseEvent event) { DeleteButton.getChildren().forEach(c -> ((Line)c).setStroke(Color.RED)); }
    });
    DeleteButton.setOnMouseExited(new EventHandler<MouseEvent>() {
      @Override public void handle(MouseEvent event) { DeleteButton.getChildren().forEach(c -> ((Line)c).setStroke(Color.BLACK)); }
    });
  }

  /**
   * Update object
   */
  public void update(Observable obs, Object obj)
  {
    Reload();
  }

  /**
   * Get block
   * @return block
   */
  public Block GetBlock()
  {
    return _block;
  }

  /**
   * Move to point
   * @param point co-ordinates of point to be moved to
   */
  public void MoveToPoint(Point2D point)
  {
    Point2D localCoords = getParent().sceneToLocal(point).subtract(_dragOffset);
    _block.X = localCoords.getX();
    _block.Y = localCoords.getY();
    relocate (
      (int) (localCoords.getX()),
      (int) (localCoords.getY())
    );
    for (PortView port : GetAllPorts())
      port.MoveConnection(getParent());
  }

  /**
   * Get all ports
   * @return list of all ports
   */
  public List<PortView> GetAllPorts()
  {
    List<PortView> res = new ArrayList<PortView>();
    for (Node child : OutputPortGrid.getChildren())
    {
      Integer column = GridPane.getColumnIndex(child);
      Integer row = GridPane.getRowIndex(child);
      if (column != null && row != null)
        res.add((PortView)child);
    }
    for (Node child : InputPortGrid.getChildren())
    {
      Integer column = GridPane.getColumnIndex(child);
      Integer row = GridPane.getRowIndex(child);
      if (column != null && row != null)
        res.add((PortView)child);
    }
    return res;
  }

  /**
   * Gets input port View on index
   * @param index index of input port
   * @return port view if found
   */
  public PortView GetInputPortViewByIndex(int index)
  {
    for (PortView pv : GetAllPorts())
    {
      if (!pv.IsOutput() && pv.GetPortNum() == index)
        return pv;
    }
    return null;
  }

  /**
   * Gets output port View on index
   * @param index index of output port
   * @return port view if found
   */
  public PortView GetOutputPortViewByIndex(int index)
  {
    for (PortView pv : GetAllPorts())
    {
      if (pv.IsOutput() && pv.GetPortNum() == index)
        return pv;
    }
    return null;
  }


  /**
   * Events occuring when draging object
   */
  protected void MakeDragEvents()
  {
    DragOverHandler = new EventHandler<DragEvent>() {
      @Override public void handle(DragEvent event) {
        event.acceptTransferModes(TransferMode.ANY);
        MoveToPoint(new Point2D(event.getSceneX(), event.getSceneY()));
        event.consume();
      }
    };

    DragDroppedHandler = new EventHandler<DragEvent>() {
      @Override public void handle(DragEvent event) {
        System.out.printf(" --> [%f, %f]\n",
          event.getX(),
          event.getY()
        );
        getParent().setOnDragOver(null);
        getParent().setOnDragDropped(null);
        getParent().setOnDragExited(null);
        event.setDropCompleted(true);
        event.consume();
      }
    };

    DragExitedHandler = new EventHandler<DragEvent>() {
      @Override public void handle(DragEvent event) {
        System.out.printf(" --> [%f, %f] (exited)\n",
          event.getX(),
          event.getY()
        );
        event.setDropCompleted(false);
        event.consume();
      }
    };

    BlockBody.setOnDragDetected(new EventHandler<MouseEvent>() {
      @Override public void handle(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY)
        {
          Point2D schemaCoords = getParent().sceneToLocal(event.getSceneX(), event.getSceneY());
          System.out.printf("drag %s (%s): [%f, %f]",
            _block.ID.toString(),
            _block.DisplayName,
            schemaCoords.getX(),
            schemaCoords.getY()
          );

          getParent().setOnDragOver(null);
          getParent().setOnDragDropped(null);
          getParent().setOnDragExited(null);
          getParent().setOnDragOver(DragOverHandler);
          getParent().setOnDragDropped(DragDroppedHandler);
          getParent().setOnDragExited(DragExitedHandler);

          _dragOffset = sceneToLocal(event.getSceneX(), event.getSceneY());

          ClipboardContent content = new ClipboardContent();
          content.putString(_block.ID.toString());
          startDragAndDrop(TransferMode.ANY).setContent(content);
          event.consume();
        }
      }
    });
  }
  protected void Reload()
  {
    switch(_block.GetStatus().getState())
    {
      case Finished: BlockBody.setStyle("-fx-background-color: rgb(200,255,200);" + _styles); break;
      case Error: BlockBody.setStyle("-fx-background-color: rgb(255,200,200);" + _styles); break;
      case Ready: BlockBody.setStyle("-fx-background-color: white;" + _styles); break;
    }
  }
}