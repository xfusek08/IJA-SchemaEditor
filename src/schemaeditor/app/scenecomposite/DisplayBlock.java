package schemaeditor.app.scenecomposite;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import schemaeditor.model.base.Block;
import javafx.scene.input.MouseEvent;

public class DisplayBlock extends SceneItem
{
  static final int HEIGTH = 100;
  static final int WIDTH = 60;
  static final int BORDER_WIDTH = 3;
  static final Color BORDER_COLOR = Color.BLACK;
  static final Color DEFAULT_COLOR = Color.WHITE;

  public Block Block;

  protected Rectangle _rectangle;
  protected Label _label;

  public DisplayBlock(SceneItem parent, Block block)
  {
    super(parent);
    Block = block;
    SetOffset(0, 0);
    CreateGeometry();
    DefinePorts();
  }

  @Override
  public void Draw()
  {
    _rectangle.setX(_xPos);
    _rectangle.setY(_yPos);
    _label.setLayoutX(_xPos + WIDTH / 2 - 20);
    _label.setLayoutY(_yPos + 10);
  }

  @Override
  protected void RegistrerThisToGroup(Group group)
  {
    group.getChildren().add(_rectangle);
    group.getChildren().add(_label);
  }

  @Override
  public void SetEvents()
  {
    _rectangle.setOnMousePressed(new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent t) {
          _rectangle.toFront();
          _label.toFront();
          SetOffset(_xPos - t.getX(), _yPos - t.getY());
        }
      }
    );
    _rectangle.setOnMouseReleased(new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent t) {
            Block.X = _xPos;
            Block.Y = _yPos;
        }
      }
    );
    _rectangle.setOnMouseDragged(new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent t) {
          SetPosition(t.getX(), t.getY());
        }
      }
    );
  }

  protected void CreateGeometry()
  {
    _rectangle = new Rectangle(_xPos, _yPos, WIDTH, HEIGTH);
    _rectangle.setFill(DEFAULT_COLOR);
    _rectangle.setStrokeWidth(BORDER_WIDTH);
    _rectangle.setStroke(BORDER_COLOR);
    _label = new Label(Block.DisplayName);
    _label.setFont(new Font("Arial", 20));
    SetPosition(Block.X, Block.Y);
  }

  protected void DefinePorts()
  {
    for (int i = 0; i < Block.InputPorts.size(); i++)
      AddChild(new DisplayPort(this, i, false));
    for (int i = 0; i < Block.OutputPorts.size(); i++)
      AddChild(new DisplayPort(this, i, true));
  }
}