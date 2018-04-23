package schemaeditor.app.scenecomposite;

import javafx.scene.Group;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import schemaeditor.model.base.Port;

class DisplayPort extends SceneItem
{
  public static final double LENGHT = 20;

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
      // aura.setCenterX(this.getEndX());
      // aura.setCenterY(this.getEndY());
    }
    else
    {
      _portLine.setStartX(_xPos - LENGHT);
      _portLine.setStartY(_yPos);
      _portLine.setEndX(_xPos);
      _portLine.setEndY(_yPos);
      // aura.setCenterX(this.getStartX());
      // aura.setCenterY(this.getStartY());
    }
  }

  @Override
  protected void RegistrerThisToGroup(Group group)
  {
    group.getChildren().add(_portLine);
  }

  @Override
  public void SetEvents()
  {
  }

  protected void CreateGeometry()
  {
    _portLine = new Line();
    _portLine.setStroke(DisplayBlock.BORDER_COLOR);
    _portLine.setStrokeWidth(DisplayBlock.BORDER_WIDTH);

    DisplayBlock parentBlock = (DisplayBlock)_parent;
    SetPosition(parentBlock.Block.X, parentBlock.Block.Y);
    // create aura
  }
}
