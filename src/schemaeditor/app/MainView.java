package schemaeditor.app;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.Set;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import schemaeditor.app.BlockView;
import schemaeditor.model.base.Block;
import schemaeditor.model.base.Connection;
import schemaeditor.model.base.Port;
import schemaeditor.model.base.Schema;
import schemaeditor.model.base.enums.EAddStatus;
import schemaeditor.model.blocks.arithmetics.*;
import schemaeditor.model.blocks.complex.*;
import schemaeditor.model.blocks.conversion.*;
import schemaeditor.model.blocks.logic.*;

public class MainView extends AnchorPane implements Observer
{
  @FXML AnchorPane root_pane;
  @FXML AnchorPane SchemaPane;
  @FXML Accordion BlockSelectMenu;
  @FXML MenuBar MainMenuBar;
  @FXML Label BlockNumber;
  @FXML Label ConnNumber;
  @FXML GridPane InputTable;

  protected ConnectionView draggConnection;
  protected Schema _schema;
  protected Label _errorMessage;
  protected List<ConnectionView> _displayConns;

  private EventHandler<DragEvent> connectionDragDroppedHandle;
  private EventHandler<DragEvent> connectionDragOverHandle;
  private EventHandler<DragEvent> connectionDragDropHandle;
  private EventHandler<DragEvent> connectionDragExitedHandler;

  private BlockInfoBoard _infotab;

  public MainView()
  {
    _schema = new Schema();
    _schema.addObserver(this);
    _displayConns = new ArrayList<ConnectionView>();

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
    CreateHandlers();
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

      case "AddBlockBt_CplCom" : newBlock = new CplexBlock_Complement(); break;
      case "AddBlockBt_CplAdd" : newBlock = new CplexBlock_Add(); break;
      case "AddBlockBt_CplSub" : newBlock = new CplexBlock_Sub(); break;
      case "AddBlockBt_CplDiv" : newBlock = new CplexBlock_Div(); break;
      case "AddBlockBt_CplMul" : newBlock = new CplexBlock_Mul(); break;
      case "AddBlockBt_CplAbs" : newBlock = new CplexBlock_Abs(); break;

      case "AddBlockBt_and"  :  newBlock = new LogicBlock_And(); break;
      case "AddBlockBt_or"   :  newBlock = new LogicBlock_Or(); break;
      case "AddBlockBt_not"  :  newBlock = new LogicBlock_Not(); break;
      case "AddBlockBt_xor"  :  newBlock = new LogicBlock_Xor(); break;
      case "AddBlockBt_xnor" :  newBlock = new LogicBlock_Xnor(); break;

      case "AddBlockBt_booltonum" :  newBlock = new ConversionBlock_BoolToNumber(); break;
      case "AddBlockBt_equals"    :  newBlock = new ConversionBlock_Equal(); break;
      case "AddBlockBt_greater"   :  newBlock = new ConversionBlock_Greater(); break;
      case "AddBlockBt_less"      :  newBlock = new ConversionBlock_Less(); break;
    }
    if (newBlock != null)
    {
      newBlock.X = 100;
      newBlock.Y = 100;
      BlockView newBlockView = new BlockView(_schema.AddBlock(newBlock));
      SchemaPane.getChildren().add(newBlockView);
      SetConnectionEvents(newBlockView);
    }
    UpdateSchemaStats();
  }

  @FXML
  private void RunAction(ActionEvent event)
  {
    _schema.RunCalculation();
  }
  @FXML
  private void ResetAction(ActionEvent event)
  {
    _schema.StopCalculation();
  }
  @FXML
  private void StepAction(ActionEvent event)
  {
    if(!_schema.isCalculating())
      _schema.StartCalculation();
    else
      _schema.StepCalculation();
  }

  public void update(Observable obs, Object obj)
  {
    UpdateSchemaStats();
  }

  public void RemoveBlockView(BlockView blockView)
  {
    List<ConnectionView> toremove = new ArrayList<ConnectionView>();
    for (ConnectionView conn : _displayConns)
      if (conn.GetConnection().DestBlockID == blockView.GetBlock().ID || conn.GetConnection().SourceBlockID == blockView.GetBlock().ID)
        toremove.add(conn);
    for (ConnectionView conn : toremove)
      RemoveDisplConn(conn);
    SchemaPane.getChildren().remove(blockView);
    _schema.RemoveBlock(blockView.GetBlock());
  }

  protected void CreateHandlers()
  {
    connectionDragOverHandle = new EventHandler<DragEvent>() {
      @Override public void handle(DragEvent event) {
        event.acceptTransferModes(TransferMode.ANY);
        draggConnection.SetEnd(
          SchemaPane.sceneToLocal(new Point2D(event.getSceneX(), event.getSceneY()))
        );
        event.consume();
      }
    };

    connectionDragDroppedHandle = new EventHandler<DragEvent>() {
      @Override public void handle(DragEvent event) {
        System.err.printf(" dropped [%f, %f]\n", event.getX(), event.getY());
        DeleteErrorMessage();
        setOnDragOver(null);
        setOnDragDropped(null);
        setOnDragExited(null);
        RemoveDisplConn(draggConnection);
        draggConnection = null;
        event.setDropCompleted(false);
        event.consume();
      }
    };

    connectionDragExitedHandler = new EventHandler<DragEvent>() {
      @Override public void handle(DragEvent event) {
        System.err.printf(" exited [%f, %f])\n", event.getX(), event.getY());
        DeleteErrorMessage();
        setOnDragOver(null);
        setOnDragDropped(null);
        setOnDragExited(null);
        RemoveDisplConn(draggConnection);
        draggConnection = null;
        event.setDropCompleted(false);
        event.consume();
        }
    };
  }

  protected void SetConnectionEvents(BlockView blockView)
  {
    for (PortView pw : blockView.GetAllPorts())
    {
      pw.Aura.setOnDragOver( new EventHandler<DragEvent>() {
        @Override public void handle(DragEvent event) {
          event.acceptTransferModes(TransferMode.ANY);
          if (pw.IsOutput() != draggConnection.isFromOut() &&
              !blockView.GetBlock().ID.toString().equals(event.getDragboard().getString()))
          {
            event.consume();
          }
        }
      });

      pw.Aura.setOnDragExited( new EventHandler<DragEvent>() {
        @Override public void handle(DragEvent event) {
          if (draggConnection != null)
          {
            DeleteErrorMessage();
            event.acceptTransferModes(TransferMode.ANY);
            if (pw.IsOutput() != draggConnection.isFromOut() &&
                !blockView.GetBlock().ID.toString().equals(event.getDragboard().getString()))
            {
              pw.UnSetHover();
              draggConnection.SetPort(pw.IsOutput(), null, 0);
              event.consume();
            }
          }
        }
      });

      pw.Aura.setOnDragEntered( new EventHandler<DragEvent>() {
        @Override public void handle(DragEvent event) {
          System.err.printf("DragEntered\n");
          event.acceptTransferModes(TransferMode.ANY);
          if (pw.IsOutput() != draggConnection.isFromOut() &&
              !blockView.GetBlock().ID.toString().equals(event.getDragboard().getString()))
          {
            pw.SetHover();
            DeleteErrorMessage();
            draggConnection.SetEnd(SchemaPane.sceneToLocal(pw.GetTip()));
            draggConnection.SetPort(pw.IsOutput(), blockView.GetBlock().ID, pw.GetPortNum());
            String errorMsg = ConnectConnectionView(draggConnection, true);
            if (errorMsg != "") CreateErrorMessage(errorMsg, pw.GetTip());
            event.consume();
          }
        }
      });

      pw.Aura.setOnDragDropped( new EventHandler<DragEvent>() {
        @Override public void handle(DragEvent event) {
          System.err.printf("Drop\n");
          event.acceptTransferModes(TransferMode.ANY);
          if (pw.IsOutput() != draggConnection.isFromOut() &&
              !blockView.GetBlock().ID.toString().equals(event.getDragboard().getString()))
          {
            setOnDragOver(null);
            setOnDragDropped(null);
            setOnDragExited(null);
            event.consume();

            pw.UnSetHover();
            draggConnection.SetPort(pw.IsOutput(), blockView.GetBlock().ID, pw.GetPortNum());
            if (ConnectConnectionView(draggConnection, false) == "")
            {
              System.err.printf("Drop on valid port\n");
              RegisterConnOnPort(pw, draggConnection, false);
              event.setDropCompleted(true);
            }
            else
            {
              System.err.printf("Drop on invalid port\n");
              RemoveDisplConn(draggConnection);
              event.setDropCompleted(false);
            }
            System.err.printf("draggConnection nulled\n");
            DeleteErrorMessage();
            draggConnection = null;
          }
        }
      });

      pw.Aura.setOnDragDetected(new EventHandler<MouseEvent>() {
        @Override public void handle(MouseEvent event) {
          System.err.printf("Drag detected: %s (%s) : %d (%s)\n",
            blockView.GetBlock().ID.toString(),
            blockView.GetBlock().DisplayName,
            pw.GetPortNum(),
            pw.IsOutput() ? "output" : "input"
          );

          setOnDragOver(null);
          setOnDragDropped(null);
          setOnDragExited(null);
          setOnDragOver(connectionDragOverHandle);
          setOnDragDropped(connectionDragDroppedHandle);
          setOnDragExited(connectionDragExitedHandler);

          draggConnection = new ConnectionView(
            SchemaPane.sceneToLocal(pw.GetTip()),
            SchemaPane.sceneToLocal(event.getSceneX(), event.getSceneY()),
            pw.IsOutput()
          );
          draggConnection.SetPort(pw.IsOutput(), blockView.GetBlock().ID, pw.GetPortNum());
          AddDisplConn(draggConnection);

          Connection toremove = RegisterConnOnPort(pw, draggConnection, true);
          if (toremove != null)
            _schema.RemoveConnection(toremove);
          ClipboardContent content = new ClipboardContent();
          content.putString(blockView.GetBlock().ID.toString());
          startDragAndDrop(TransferMode.ANY).setContent(content);
          event.consume();
        }
      });
    }

    blockView.BlockBody.setOnMousePressed(new EventHandler<MouseEvent>() {
      @Override public void handle(MouseEvent event)
      {
        blockView.toFront();
        if (event.getButton() == MouseButton.MIDDLE)
        {
          _infotab = new BlockInfoBoard(blockView.GetBlock());
          getChildren().add(_infotab);
          Point2D coords = SchemaPane.localToScene(blockView.getLayoutX(), blockView.getLayoutY());
          _infotab.relocate(coords.getX() + 80, coords.getY());
          event.consume();
        }
      }
    });

    blockView.BlockBody.setOnMouseReleased(new EventHandler<MouseEvent>() {
      @Override public void handle(MouseEvent event)
      {
        if (event.getButton() == MouseButton.MIDDLE)
        {
          getChildren().remove(_infotab);
          _infotab = null;
          event.consume();
        }
      }
    });

    blockView.DeleteButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override public void handle(MouseEvent event)
      {
        if (event.getButton() == MouseButton.PRIMARY)
        {
          RemoveBlockView(blockView);
          event.consume();
        }
      }
    });
  }
  protected Connection RegisterConnOnPort(PortView port, ConnectionView conn, boolean isStart)
  {
    ConnectionView toremove = port.RegisterConn(conn, isStart);
    if (toremove != null)
    {
      RemoveDisplConn(toremove);
      return toremove.GetConnection();
    }
    return null;
  }

  protected String ConnectConnectionView(ConnectionView connView, boolean justTry)
  {
    Connection conn = connView.GetConnection();
    String msg = "";
    if (conn == null) return "Unconnected";

    System.err.printf("Connecting %s (%d) -> %s (%d)%s",
      conn.SourceBlockID,
      conn.SourcePortNumber,
      conn.DestBlockID,
      conn.DestPortNumber,
      justTry ? " (justTry)\n" : ""
    );

    EAddStatus resStatus = _schema.TryValidateConnection(conn);

    if (resStatus == EAddStatus.Ok)
    {
      if (!justTry)
      {
        _schema.AddConnection(conn);
        System.err.printf("  ... Connected\n");
      }
    }
    else
    {
      connView.SetRed();
      switch (resStatus)
      {
        case OutSourcePortNotFound :  msg = "Source port not found."; break;
        case InDestPortNotfoud :      msg = "Destination port not found."; break;
        case PortsIncopatible :       msg = "Incompatible ports."; break;
        case ConnectionCuseesCycles : msg = "Connection causes cycles."; break;
        default:                      msg = "Unknown."; break;
      }
      System.err.printf(" ... can't connect (%s)\n", msg);
    }
    return msg;
  }

  protected void CreateErrorMessage(String msg, Point2D position)
  {
    DeleteErrorMessage();
    _errorMessage = new Label(msg);
    _errorMessage.setMouseTransparent(true);
    _errorMessage.setTextFill(Color.WHITE);
    _errorMessage.setStyle("-fx-background-color: red; -fx-border-color: yellow; -fx-border-width: 1px; -fx-padding: 3px; -fx-font-size: 15px;");
    _errorMessage.setLayoutX(position.getX());
    _errorMessage.setLayoutY(position.getY() - 40);
    getChildren().add(_errorMessage);
  }

  protected void DeleteErrorMessage()
  {
    if (_errorMessage != null)
    {
      getChildren().remove(_errorMessage);
      _errorMessage = null;
    }
  }

  protected void UpdateSchemaStats()
  {
    Set<Connection> conns = _schema.GetConnections();
    BlockNumber.setText(String.valueOf(_schema.GetBlocks().size()));
    ConnNumber.setText(String.valueOf(conns.size()));

    InputTable.getChildren().clear();
    for (Port port : _schema.GetInputPorts())
    {
      Label label = new Label(String.valueOf(port.GetInputNumber()));
      label.setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
      GridPane portValues = new GridPane();
      int row = 0;
      for (String name : port.GetValuesNames())
      {
        TextField input = new TextField(String.valueOf(port.GetValueByName(name)));
        input.setPrefWidth(50);
        portValues.add(new Label(name), 0, row);
        portValues.add(input, 1, row);
        row++;
        input.setOnMouseClicked(new EventHandler<MouseEvent>() {
          @Override public void handle(MouseEvent envent) {
            input.selectAll();
          }
        });
        input.setOnAction(new EventHandler<ActionEvent>() {
          @Override public void handle(ActionEvent envent)
          {
            Double val;
            try {
              val = Double.parseDouble(input.getText());
            } catch (NumberFormatException e) {
              val = Double.NaN;
            }
            port.SetValueByName(name, val);
            input.setText(String.valueOf(val));
          }
        });
      }
      InputTable.add(label, 0, port.GetInputNumber());
      InputTable.add(portValues, 1, port.GetInputNumber());
    }
  }
  protected void RemoveDisplConn(ConnectionView conn)
  {
    SchemaPane.getChildren().remove(conn);
    _displayConns.remove(conn);
  }
  protected void AddDisplConn(ConnectionView conn)
  {
    SchemaPane.getChildren().add(conn);
    _displayConns.add(conn);
  }
}