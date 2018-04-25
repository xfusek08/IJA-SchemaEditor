package schemaeditor.app;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TitledPane;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import schemaeditor.app.BlockView;
import schemaeditor.model.base.Block;
import schemaeditor.model.base.Connection;
import schemaeditor.model.base.Schema;
import schemaeditor.model.base.enums.EAddStatus;
import schemaeditor.model.blocks.arithmetics.*;
import schemaeditor.model.blocks.complex.*;

public class MainView extends AnchorPane
{
  @FXML AnchorPane root_pane;
  @FXML AnchorPane SchemaPane;
  @FXML Accordion BlockSelectMenu;
  @FXML MenuBar MainMenuBar;

  protected ConnectionView draggConnection;
  protected Schema _schema;
  protected Label _errorMessage;

  private EventHandler<DragEvent> connectionDragDroppedHandle;
  private EventHandler<DragEvent> connectionDragOverHandle;
  private EventHandler<DragEvent> connectionDragDropHandle;
  private EventHandler<DragEvent> connectionDragExitedHandler;

  public MainView()
  {
    _schema = new Schema();
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
    }
    if (newBlock != null)
    {
      newBlock.X = 100;
      newBlock.Y = 100;
      BlockView newBlockView = new BlockView(_schema.AddBlock(newBlock));
      SchemaPane.getChildren().add(newBlockView);
      SetConnectionEvents(newBlockView);
    }
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
        SchemaPane.getChildren().remove(draggConnection);
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
        SchemaPane.getChildren().remove(draggConnection);
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
            event.acceptTransferModes(TransferMode.ANY);
            if (pw.IsOutput() != draggConnection.isFromOut() &&
                !blockView.GetBlock().ID.toString().equals(event.getDragboard().getString()))
            {
              pw.UnSetHover();
              DeleteErrorMessage();
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
            draggConnection.SetEnd(SchemaPane.sceneToLocal(pw.GetTip()));
            draggConnection.SetPort(pw.IsOutput(), blockView.GetBlock().ID, pw.GetPortNum());
            String errorMsg = ConnectConnectionView(draggConnection, true);
            if (errorMsg != "")
              CreateErrorMessage(errorMsg, pw.GetTip());
            pw.SetHover();
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
              event.setDropCompleted(true);
              RegisterConnOnPort(pw, draggConnection, false);
            }
            else
            {
              System.err.printf("Drop on invalid port\n");
              event.setDropCompleted(false);
              SchemaPane.getChildren().remove(draggConnection);
            }
            draggConnection = null;
            System.err.printf("draggConnection nulled\n");
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

          SchemaPane.getChildren().remove(draggConnection);

          draggConnection = new ConnectionView(
            SchemaPane.sceneToLocal(pw.GetTip()),
            SchemaPane.sceneToLocal(event.getSceneX(), event.getSceneY()),
            pw.IsOutput()
          );

          SchemaPane.getChildren().add(draggConnection);
          RegisterConnOnPort(pw, draggConnection, true);
          draggConnection.SetPort(pw.IsOutput(), blockView.GetBlock().ID, pw.GetPortNum());

          ClipboardContent content = new ClipboardContent();
          content.putString(blockView.GetBlock().ID.toString());
          startDragAndDrop(TransferMode.ANY).setContent(content);
          event.consume();
        }
      });
    }
  }
  protected void RegisterConnOnPort(PortView port, ConnectionView conn, boolean isStart)
  {
    ConnectionView toRemove = port.RegisterConn(conn, isStart);
    if (toRemove != null)
    {
      SchemaPane.getChildren().remove(toRemove);
    }
  }

  protected String ConnectConnectionView(ConnectionView connView, boolean justTry)
  {
    Connection conn = connView.GetConnection();
    String msg = "";
    if (conn == null) return msg;

    System.err.printf("Connecting %s (%d) -> %s (%d)%s ... ",
      conn.SourceBlockID,
      conn.SourcePortNumber,
      conn.DestBlockID,
      conn.DestPortNumber,
      justTry ? " (justTry)" : ""
    );
    EAddStatus resStatus = _schema.TryValidateConnection(conn);
    System.err.printf(" Validated ... ");
    if (resStatus == EAddStatus.Ok)
    {
      if (!justTry)
        _schema.AddConnection(conn);
      System.err.printf("Connected ... ");
    }
    else
    {
      connView.SetRed();
      switch (resStatus)
      {
        case OutSourcePortNotFound :  msg = "Source port not found."; break;
        case InDestPortNotfoud :      msg = "Destination port not found."; break;
        case PortsIncopatible :       msg = "Incopatible ports."; break;
        case ConnectionCuseesCycles : msg = "Connection causes cycles."; break;
        default:                      msg = "Unknown."; break;
      }
      System.err.printf("cant connect ... ");
    }
    System.err.printf("end\n");
    return msg;
  }

  public void CreateErrorMessage(String msg, Point2D position)
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

  public void DeleteErrorMessage()
  {
    if (_errorMessage != null)
    {
      getChildren().remove(_errorMessage);
      _errorMessage = null;
    }
  }
}