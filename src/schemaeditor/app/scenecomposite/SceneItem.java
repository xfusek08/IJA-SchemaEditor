/**
 * @file:     SceneItem.java
 * @package:  schemaeditor.app.scenecomposite
 * @author    Petr Fusek
 * @date      1.05.2018
 */

package schemaeditor.app.scenecomposite;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.shape.Shape;

/**
 * Class representing scene item
 */
public abstract class SceneItem
{
  protected double _xOffset = 0;
  protected double _yOffset = 0;
  protected double _xPos = 0;
  protected double _yPos = 0;
  protected SceneItem _parent = null;
  protected List<SceneItem> _childs;

  public SceneItem(SceneItem parent)
  {
    _parent = parent;
    _childs = new ArrayList<SceneItem>();
    CreateGeometry();
  }

  public void SetOffset(double xOffset, double yOffset)
  {
    _xOffset = xOffset;
    _yOffset = yOffset;
  }

  public void SetPosition(double parentX, double parentY)
  {
    _xPos = parentX + _xOffset;
    _yPos = parentY + _yOffset;
    Draw();
    for (SceneItem item : _childs)
      item.SetPosition(_xPos, _yPos);
  }

  public void AddChild(SceneItem child)
  {
    _childs.add(child);
  }

  public void RegistrerToGroup(Group group)
  {
    SetEvents();
    RegistrerThisToGroup(group);
    for (SceneItem item : _childs)
      item.RegistrerToGroup(group);
  }

  public abstract void SetEvents();
  public abstract void Draw();
  protected abstract void RegistrerThisToGroup(Group group);
  protected abstract void CreateGeometry();
}