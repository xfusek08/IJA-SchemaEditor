/**
 * Main entry point to application
 * @file:     SchemaEditor.java
 * @package:  schemaeditor.app
 * @author    Petr Fusek
 * @date      06.05.2018
 */

package schemaeditor.app;


import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import schemaeditor.model.safemanager.SchemaXMLLoader;
import schemaeditor.model.base.Schema;
import schemaeditor.model.blocks.arithmetics.*;
import javafx.scene.shape.Rectangle;

/**
 * Main class entry method
 */
public class SchemaEditor extends Application
{
  /**
   * Create window and add button
   * @param primaryStage .... dont know what it is
   */
  @Override
  public void start(Stage primaryStage)
  {
    BorderPane root = new BorderPane();
    try
    {
      Scene scene = new Scene(root);
      primaryStage.setScene(scene);
      primaryStage.show();
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
    root.setCenter(new MainView());
  }

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args)
  {
    launch(args);
  }

}
