
package schemaeditor.app;

import java.util.ArrayList;
import java.util.List;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import schemaeditor.model.base.Block;
import schemaeditor.model.base.Port;

class DisplayBlock
{
  static final int HEIGTH = 100;
  static final int WIDTH = 60;
  static final int BORDER_WIDTH = 3;
  static final Color BORDER_COLOR = Color.BLACK;
  static final Color DEFAULT_COLOR = Color.TRANSPARENT;

  protected Block _block;
  protected double X_offset = 0;
  protected double Y_offset = 0;
  protected List<PortLine> portLines;

  public DisplayBlock(Block block)
  {
    _block = block;
    portLines = new ArrayList<PortLine>();
  }

  Group GetShape()
  {
    Group result = new Group();

    Rectangle mainrect = new Rectangle(_block.X, _block.Y, WIDTH, HEIGTH);
    mainrect.setFill(DEFAULT_COLOR);
    mainrect.setStrokeWidth(BORDER_WIDTH);
    mainrect.setStroke(BORDER_COLOR);

    int portCnt = 1;
    double step = HEIGTH / (_block.InputPorts.size() + 1);
    for (Port port : _block.InputPorts)
    {
      PortLine portLine = new PortLine(_block.X, _block.Y, step * portCnt, port, false);
      portLines.add(portLine);
      portLine.registerToGroup(result);
      portCnt++;
    }

    portCnt = 1;
    step = HEIGTH / (_block.OutputPorts.size() + 1);
    for (Port port : _block.OutputPorts)
    {
      PortLine portLine = new PortLine(_block.X, _block.Y, step * portCnt, port, true);
      portLines.add(portLine);
      portLine.registerToGroup(result);
      portCnt++;
    }

    result.getChildren().add(mainrect);

    mainrect.setOnMousePressed(new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent t) {
            X_offset = _block.X - t.getX();
            Y_offset = _block.Y - t.getY();
        }
      }
    );
    mainrect.setOnMouseReleased(new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent t) {
            _block.X = mainrect.getX();
            _block.Y = mainrect.getY();
        }
      }
    );
    mainrect.setOnMouseDragged(new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent t) {
          mainrect.setX(t.getX() + X_offset);
          mainrect.setY(t.getY() + Y_offset);
          for (PortLine port : portLines)
            port.setPosition(mainrect.getX(), mainrect.getY());
        }
      }
    );

    return  result;
  }
}

class PortLine extends Line
{
  protected double x_offSet;
  protected double y_offSet;
  protected boolean isOutput;
  protected Circle aura;

  public Port Port;
  public PortLine(double blockX, double blockY, double step, Port port, boolean isOutput)
  {
    super(blockX, blockY, blockX, blockY);
    setStroke(DisplayBlock.BORDER_COLOR);
    setStrokeWidth(DisplayBlock.BORDER_WIDTH);
    y_offSet = step;
    x_offSet = 20;
    Port = port;
    this.isOutput = isOutput;
    aura = new Circle(0, 0, 10);
    setPosition(blockX, blockY);
  }

  public void setPosition(double blockX, double blockY)
  {
    if (isOutput)
    {
      this.setStartX(blockX + DisplayBlock.WIDTH);
      this.setStartY(blockY + y_offSet);
      this.setEndX(blockX + x_offSet + DisplayBlock.WIDTH);
      this.setEndY(blockY + y_offSet);
      aura.setCenterX(this.getEndX());
      aura.setCenterY(this.getEndY());
    }
    else
    {
      this.setStartX(blockX - x_offSet);
      this.setStartY(blockY + y_offSet);
      this.setEndX(blockX);
      this.setEndY(blockY + y_offSet);
      aura.setCenterX(this.getStartX());
      aura.setCenterY(this.getStartY());
    }
  }

  public void registerToGroup(Group group)
  {
    aura.setFill(Color.TRANSPARENT);
    aura.setStroke(Color.TRANSPARENT);
    aura.setStrokeWidth(2);
    aura.setOnMouseEntered(new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent t) {
          aura.setStroke(Color.SKYBLUE);
        }
      }
    );
   aura.setOnMouseExited(new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent t) {
          aura.setStroke(Color.TRANSPARENT);
        }
      }
    );
    group.getChildren().add(this);
    group.getChildren().add(aura);
  }
}
