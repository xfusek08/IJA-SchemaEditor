package schemaeditor.app;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
  @FXML Label IDLabel;

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
    IDLabel.setText(_block.ID.toString());
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
    MakeDragEvents();
  }

  public Block GetBlock()
  {
    return _block;
  }

  public void MoveToPoint(Point2D point)
  {
    Point2D localCoords = getParent().sceneToLocal(point).subtract(_dragOffset);
    relocate (
      (int) (localCoords.getX()),
      (int) (localCoords.getY())
    );
    for (PortView port : GetAllPorts())
      port.MoveConnection(getParent());
  }

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
    });

    BlockBody.setOnMouseEntered(new EventHandler<MouseEvent>() {
      @Override public void handle(MouseEvent event)
      {
        IDLabel.setVisible(true);
      }
    });

    BlockBody.setOnMouseExited(new EventHandler<MouseEvent>() {
      @Override public void handle(MouseEvent event)
      {
        IDLabel.setVisible(false);
      }
    });
  }
}