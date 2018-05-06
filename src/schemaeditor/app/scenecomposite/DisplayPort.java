/**
 * @file:     DisplayPort.java
 * @package:  schemaeditor.app.scenecomposite
 * @author    Petr Fusek
 * @date      1.05.2018
 */

package schemaeditor.app.scenecomposite;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.input.MouseEvent;
import schemaeditor.model.base.Port;

/**
 * Class representing displayed port in schema
 */
class DisplayPort extends SceneItem
{
  public static final double LENGHT = 20;
  public static final double AURA_RADIUS = 10;

  public Port Port;
  protected Line _portLine;
  protected boolean _isOutput;
  protected Circle _aura;

  public DisplayPort(DisplayBlock parentBlock, int portNum, boolean isOutput)
  {
    super(parentBlock);
    _isOutput = isOutput;
    if (_isOutput)
    {
      Port = parentBlock.Block.OutputPorts.get(portNum);
      SetOffset(
        DisplayBlock.WIDTH,
        (portNum + 1) * (DisplayBlock.HEIGTH / (parentBlock.Block.OutputPorts.size() + 1))
      );
    }
    else
    {
      Port = parentBlock.Block.InputPorts.get(portNum);
      SetOffset(
        0,
        (portNum + 1) * (DisplayBlock.HEIGTH / (parentBlock.Block.InputPorts.size() + 1))
      );
    }
    CreateGeometry();
  }

  @Override
  public void Draw()
  {
    if (_isOutput)
    {
      _portLine.setStartX(_xPos);
      _portLine.setStartY(_yPos);
      _portLine.setEndX(_xPos + LENGHT);
      _portLine.setEndY(_yPos);
      _aura.setCenterX(_portLine.getEndX());
      _aura.setCenterY(_portLine.getEndY());
    }
    else
    {
      _portLine.setStartX(_xPos - LENGHT);
      _portLine.setStartY(_yPos);
      _portLine.setEndX(_xPos);
      _portLine.setEndY(_yPos);
      _aura.setCenterX(_portLine.getStartX());
      _aura.setCenterY(_portLine.getStartY());
    }
  }

  @Override
  protected void RegistrerThisToGroup(Group group)
  {
    if (_portLine != null)
      group.getChildren().add(_portLine);
    if (_aura != null)
      group.getChildren().add(_aura);
  }

  @Override
  public void SetEvents()
  {
    _aura.setOnMouseEntered(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent t) {
        _aura.setStroke(Color.SKYBLUE);
      }
    });
    _aura.setOnMouseExited(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent t) {
        _aura.setStroke(Color.TRANSPARENT);
      }
    });
  }

  protected void CreateGeometry()
  {
    _portLine = new Line();
    _portLine.setStroke(DisplayBlock.BORDER_COLOR);
    _portLine.setStrokeWidth(DisplayBlock.BORDER_WIDTH);


    _aura = new Circle(0, 0, AURA_RADIUS);
    _aura.setFill(Color.TRANSPARENT);
    _aura.setStroke(Color.TRANSPARENT);
    _aura.setStrokeWidth(2);

    DisplayBlock parentBlock = (DisplayBlock) _parent;
    SetPosition(parentBlock.Block.X, parentBlock.Block.Y);
  }
}
