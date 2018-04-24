package schemaeditor.app;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
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
  protected Point2D _dragOffset;

  private EventHandler <DragEvent> DragOverHandler;
  private EventHandler <DragEvent> DragDroppedHandler;
  private EventHandler <DragEvent> DragExitedHandler;

  public BlockView(Block block)
  {
    _block = block;
    // System.err.print(getClass().getResource("resources/BlockView.fxml"));
    // System.err.print("\n");
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
      InputPortGrid.add(new PortView(port, false), 0, cnt++);
    }
    cnt = 0;
    for (Port port : _block.OutputPorts)
    {
      RowConstraints row = new RowConstraints();
      row.setPercentHeight(100 / _block.OutputPorts.size());
      OutputPortGrid.getRowConstraints().add(row);
      OutputPortGrid.add(new PortView(port, true), 0, cnt++);
    }
    MakeDragEvents();
  }

  protected void MakeDragEvents()
  {
    BlockBody.setOnMousePressed(new EventHandler<MouseEvent>() {
      @Override public void handle(MouseEvent event) {
        toFront();
      }
		});

    DragOverHandler = new EventHandler<DragEvent>() {
      @Override public void handle(DragEvent event) {
        event.acceptTransferModes(TransferMode.ANY);
        // System.out.printf("setOnDragOver: [%f, %f]\n",
        //   event.getX(),
        //   event.getY()
        // );
        MoveToPoint(new Point2D(event.getSceneX(), event.getSceneY()));
        event.consume();
      }
    };

    DragDroppedHandler = new EventHandler<DragEvent>() {
      @Override public void handle(DragEvent event) {
        System.out.printf("dropped: [%f, %f]\n",
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
        System.out.printf("exited: [%f, %f]\n",
          event.getX(),
          event.getY()
        );
        event.consume();
      }
    };

    BlockBody.setOnDragDetected(new EventHandler<MouseEvent>() {
      @Override public void handle(MouseEvent event) {
        Point2D schemaCoords = getParent().sceneToLocal(event.getSceneX(), event.getSceneY());
        System.out.printf("drag %s (%s): [%f, %f]\n",
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
    });
  }

  public void MoveToPoint(Point2D point)
  {
    Point2D localCoords = getParent().sceneToLocal(point).subtract(_dragOffset);
    relocate (
      (int) (localCoords.getX()),
      (int) (localCoords.getY())
    );
  }
}