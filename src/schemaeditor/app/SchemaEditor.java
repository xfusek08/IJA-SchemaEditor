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
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import safemanager.model.safemanager.SchemaXMLLoader;
import schemaeditor.app.scenecomposite.DisplaySchema;
import schemaeditor.model.base.Schema;
import schemaeditor.model.blocks.arithmetics.NumberBlock_Abs;
import schemaeditor.model.blocks.arithmetics.NumberBlock_Add;
import javafx.scene.shape.Rectangle;

/**
 * Main class testing javafx
 * @author Petr Fusek
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
    try
    {
      Schema schema = new Schema();
      schema.AddBlock(new NumberBlock_Add());

      DisplaySchema displSchema = new DisplaySchema(schema);
      // displSchema.AddBlock(new NumberBlock_Add());

      Group root = new Group();
      displSchema.RegistrerToGroup(root);
      Scene scene = new Scene(root, 800, 600);
      primaryStage.setTitle("Hello World!");
      primaryStage.setScene(scene);
      primaryStage.show();

      // schema.AddBlock(new NumberBlock_Abs());
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args)
  {
    launch(args);
  }

}
