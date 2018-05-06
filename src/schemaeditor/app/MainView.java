package schemaeditor.app;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.Set;
import java.io.File;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
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
import javafx.stage.*;
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
import schemaeditor.model.safemanager.*;
import javax.xml.bind.JAXBException;

import org.omg.PortableServer._ServantActivatorStub;

public class MainView extends AnchorPane implements Observer
{
  @FXML AnchorPane root_pane;
  @FXML AnchorPane SchemaPane;
  @FXML Accordion BlockSelectMenu;
  @FXML MenuBar MainMenuBar;
  @FXML Label BlockNumber;
  @FXML Label ConnNumber;
  @FXML GridPane InputTable;

  protected ConnectionView dragConnection;
  protected Schema _schema;
  protected Label _errorMessage;
  protected List<ConnectionView> _displayConns;
  protected String _filePath;

  private EventHandler<DragEvent> connectionDragDroppedHandle;
  private EventHandler<DragEvent> connectionDragOverHandle;
  private EventHandler<DragEvent> connectionDragExitedHandler;

  private BlockInfoBoard _infotab;

  public MainView()
  {
    _schema = new Schema();
    _schema.addObserver(this);
    _displayConns = new ArrayList<ConnectionView>();
    _filePath = null;

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
  private void SaveFile(ActionEvent event) throws JAXBException, IOException
  {
    if (_filePath != null)
    {
      SchemaXMLLoader loader = new SchemaXMLLoader();
      loader.SaveSchema(_schema, _filePath);
    }
    else
    {
      SaveAsFile(event);
    }
  }

  @FXML
  private void SaveAsFile(ActionEvent event) throws JAXBException, IOException
  {
    final Stage stage = new Stage();
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Save File");
    FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
    fileChooser.getExtensionFilters().add(filter);
    File file = fileChooser.showSaveDialog(stage);
    if (file != null)
    {
      SchemaXMLLoader loader = new SchemaXMLLoader();
      loader.SaveSchema(_schema, file.toString());
      _filePath = file.toString();
    }
  }

  @FXML
  private void LoadFile(ActionEvent event) throws JAXBException, IOException
  {
    final Stage stage = new Stage();
    SchemaXMLLoader loader = new SchemaXMLLoader();
    Schema lSchema = new Schema();
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Load File");
    FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
    fileChooser.getExtensionFilters().add(filter);
    File file = fileChooser.showOpenDialog(stage);
    if (file != null)
    {
      lSchema = loader.LoadSchema(file.toString());
      _filePath = file.toString();
      System.err.printf("Block :%d \n", lSchema.GetBlocks().size());
      System.err.printf("Cons :%d \n", lSchema.GetConnections().size());
      LoadSchema(lSchema);
    }
    // TODO: chyba ...
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
      SetEventsOfBlocksAndConnection(newBlockView);
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

  @FXML
  private void NewSchema(ActionEvent event)
  {
    _filePath = null;
    LoadSchema(new Schema());
  }

  @FXML
  private void ExitApp(ActionEvent event)
  {
    System.exit(0);
  }


  /**
   * Update of observer to be notify when schema has changed.
   */
  public void update(Observable obs, Object obj)
  {
    UpdateSchemaStats();
  }

  /**
   * Removes connection view from display collection
   * @param conn connection view to be removed
   */
  protected void RemoveDisplConn(ConnectionView conn)
  {
    SchemaPane.getChildren().remove(conn);
    _displayConns.remove(conn);
  }

  /**
   * Register connection view to display collection
   * @param conn connection view to be added
   */
  protected void AddDisplayConn(ConnectionView conn)
  {
    SchemaPane.getChildren().add(conn);
    _displayConns.add(conn);
  }

  /**
   * Loads schema to screen from model
   * @param schema Schema to be loaded
   */
  protected void LoadSchema(Schema schema)
  {
    if (_schema != null)
      _schema.deleteObserver(this);

    while(_displayConns.size() != 0)
      RemoveDisplConn(_displayConns.get(0));

    List<BlockView> blocksViews = new ArrayList<BlockView>();
    for (Node n : SchemaPane.getChildren())
      if (n instanceof BlockView)
        blocksViews.add((BlockView)n);

    for (BlockView b : blocksViews)
      RemoveBlockView(b);
    blocksViews.clear();

    _schema = schema;

    for (Block block : _schema.GetBlocks())
    {
      System.err.printf("block: %s ports: %d\n", block.ID, block.InputPorts.size() + block.OutputPorts.size());
      BlockView newBlockView = new BlockView(block);
      SchemaPane.getChildren().add(newBlockView);
      blocksViews.add(newBlockView);
      SetEventsOfBlocksAndConnection(newBlockView);
    }

    for (Connection conn : _schema.GetConnections())
    {
      PortView startPort = null;
      PortView endPort = null;

      for (BlockView bv : blocksViews)
      {
        if (bv.GetBlock().ID.equals(conn.SourceBlockID))
          startPort = bv.GetOutputPortViewByIndex(conn.SourcePortNumber);
        else if (bv.GetBlock().ID.equals(conn.DestBlockID))
          endPort = bv.GetInputPortViewByIndex(conn.SourcePortNumber);
      }

      if (startPort == null || endPort == null)
      {
        // TODO: chyba ... vypsat ??
        continue;
      }

      ConnectionView newConnView = new ConnectionView(
        SchemaPane.sceneToLocal(startPort.GetTip()),
        SchemaPane.sceneToLocal(endPort.GetTip()),
        true
      );

      AddDisplayConn(newConnView);
      RegisterConnOnPort(startPort, newConnView, true);
      RegisterConnOnPort(endPort, newConnView, false);
    }
    _schema.addObserver(this);
    UpdateSchemaStats();
  }

  /**
   * Removes block view from screen and from model.
   * @param blockView block view to be removed
  */
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

  /**
   * Registers connection view on port view.
   * @param PortView  Port where connection will be attached.
   * @param conn      Connection to be attached.
   * @param isStart   True if port is connected to start point of connection curve, false if is on end. This is used to orient shape of curve.
   * @return instance of removed connection null if no connection was rewritten.
   */
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

  /**
   * Tries to connect two blocks in model based on connection view description. Connection is created in model if validating was ok.
   * @param connView  Connection view describing new connection in model.
   * @param justTry   Flag. If True method will validate connection and return result, but if connection is ok does not affect model.
   * @return string error message what went wrong in connection, empty string is ok.
   */
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

  /**
   * Creates error message na screen on certain position.
   * @param msg       Message to be displayed.
   * @param position  Position where message will be displayed.
   */
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

  /**
   * Removes error message from screen
   */
  protected void DeleteErrorMessage()
  {
    if (_errorMessage != null)
    {
      getChildren().remove(_errorMessage);
      _errorMessage = null;
    }
  }

  /**
   * Reloads data from schema to be displayed on screen. Input ports, block count, connection count.
   */
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
          @Override public void handle(MouseEvent event) {
            input.selectAll();
          }
        });
        input.setOnAction(new EventHandler<ActionEvent>() {
          @Override public void handle(ActionEvent event)
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

  //===========================================================================
  //=========================  Handling events ================================
  //===========================================================================

  /**
   * Defines handles to object fields to be attach and detach to MainView in various circumstances
  */
  protected void CreateHandlers()
  {
    // drag and stretch connection outside the blocks
    connectionDragOverHandle = new EventHandler<DragEvent>() {
      @Override public void handle(DragEvent event) {
        event.acceptTransferModes(TransferMode.ANY);
        dragConnection.SetEnd(
          SchemaPane.sceneToLocal(new Point2D(event.getSceneX(), event.getSceneY()))
        );
        event.consume();
      }
    };

    // Drop connection when it is outside of port
    connectionDragDroppedHandle = new EventHandler<DragEvent>() {
      @Override public void handle(DragEvent event) {
        System.err.printf(" dropped [%f, %f]\n", event.getX(), event.getY());
        DeleteErrorMessage();
        setOnDragOver(null);
        setOnDragDropped(null);
        setOnDragExited(null);
        RemoveDisplConn(dragConnection);
        dragConnection = null;
        event.setDropCompleted(false);
        event.consume();
      }
    };

    // Drop connection when mouse exits screen
    connectionDragExitedHandler = new EventHandler<DragEvent>() {
      @Override public void handle(DragEvent event) {
        System.err.printf(" exited [%f, %f])\n", event.getX(), event.getY());
        DeleteErrorMessage();
        setOnDragOver(null);
        setOnDragDropped(null);
        setOnDragExited(null);
        RemoveDisplConn(dragConnection);
        dragConnection = null;
        event.setDropCompleted(false);
        event.consume();
        }
    };
  }

  /**
   * Defining events for newly created blockview such as connections displaying information and deleting blocks.
   * @param blockView newly created blockview
   */
  protected void SetEventsOfBlocksAndConnection(BlockView blockView)
  {
    // for each port on block, set events for creating and connection connections
    for (PortView pw : blockView.GetAllPorts())
    {
      // consume dragging connection over port, because it has snapped.
      pw.Aura.setOnDragOver( new EventHandler<DragEvent>() {
        @Override public void handle(DragEvent event) {
          event.acceptTransferModes(TransferMode.ANY);
          if (pw.IsOutput() != dragConnection.isFromOut() &&
              !blockView.GetBlock().ID.toString().equals(event.getDragboard().getString()))
          {
            event.consume();
          }
        }
      });

      // drag and connection over the port and snaps it to port if it is valid connection, eventualy display error warning
      pw.Aura.setOnDragEntered( new EventHandler<DragEvent>() {
        @Override public void handle(DragEvent event) {
          System.err.printf("DragEntered\n");
          event.acceptTransferModes(TransferMode.ANY);
          if (pw.IsOutput() != dragConnection.isFromOut() &&
              !blockView.GetBlock().ID.toString().equals(event.getDragboard().getString()))
          {
            pw.SetHover();
            DeleteErrorMessage();
            dragConnection.SetEnd(SchemaPane.sceneToLocal(pw.GetTip()));
            dragConnection.SetPort(pw.IsOutput(), blockView.GetBlock().ID, pw.GetPortNum());
            String errorMsg = ConnectConnectionView(dragConnection, true);
            if (errorMsg != "") CreateErrorMessage(errorMsg, pw.GetTip());
            event.consume();
          }
        }
      });

      // exit snapping to ports
      pw.Aura.setOnDragExited( new EventHandler<DragEvent>() {
        @Override public void handle(DragEvent event) {
          if (dragConnection != null)
          {
            DeleteErrorMessage();
            event.acceptTransferModes(TransferMode.ANY);
            if (pw.IsOutput() != dragConnection.isFromOut() &&
                !blockView.GetBlock().ID.toString().equals(event.getDragboard().getString()))
            {
              pw.UnSetHover();
              dragConnection.SetPort(pw.IsOutput(), null, 0);
              event.consume();
            }
          }
        }
      });

      // drop connection to port, if it is valid.
      pw.Aura.setOnDragDropped( new EventHandler<DragEvent>() {
        @Override public void handle(DragEvent event) {
          System.err.printf("Drop\n");
          event.acceptTransferModes(TransferMode.ANY);
          if (pw.IsOutput() != dragConnection.isFromOut() &&
              !blockView.GetBlock().ID.toString().equals(event.getDragboard().getString()))
          {
            setOnDragOver(null);
            setOnDragDropped(null);
            setOnDragExited(null);
            event.consume();

            pw.UnSetHover();
            dragConnection.SetPort(pw.IsOutput(), blockView.GetBlock().ID, pw.GetPortNum());
            if (ConnectConnectionView(dragConnection, false) == "")
            {
              System.err.printf("Drop on valid port\n");
              RegisterConnOnPort(pw, dragConnection, false);
              event.setDropCompleted(true);
            }
            else
            {
              System.err.printf("Drop on invalid port\n");
              RemoveDisplConn(dragConnection);
              event.setDropCompleted(false);
            }
            System.err.printf("dragConnection nulled\n");
            DeleteErrorMessage();
            dragConnection = null;
          }
        }
      });

      // detect drag action over the port and create connection folliwing the mouse
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

          dragConnection = new ConnectionView(
            SchemaPane.sceneToLocal(pw.GetTip()),
            SchemaPane.sceneToLocal(event.getSceneX(), event.getSceneY()),
            pw.IsOutput()
          );

          dragConnection.SetPort(pw.IsOutput(), blockView.GetBlock().ID, pw.GetPortNum());
          AddDisplayConn(dragConnection);

          Connection toremove = RegisterConnOnPort(pw, dragConnection, true);
          if (toremove != null)
            _schema.RemoveConnection(toremove);

          ClipboardContent content = new ClipboardContent();
          content.putString(blockView.GetBlock().ID.toString());
          startDragAndDrop(TransferMode.ANY).setContent(content);
          event.consume();
        }
      });
    }

    // if middle button is pressed over block data of block state are shown
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

    // hides block info when mouse middle button is released
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

    // set delete block action when cross button is pressed
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
}