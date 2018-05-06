/**
 * @file:     DisplaySchema.java
 * @package:  schemaeditor.app.scenecomposite
 * @author    Petr Fusek
 * @date      1.05.2018
 */

package schemaeditor.app.scenecomposite;

import javafx.scene.Group;
import schemaeditor.model.base.Block;
import schemaeditor.model.base.Schema;

/**
 * Class representing display schema
 */
public class DisplaySchema extends SceneItem
{
  protected Schema _schema;
  protected Group _group;
  public DisplaySchema(Schema schema)
  {
    super(null);
    _schema = schema;
    _group = null;
    DefineBlocks();
  }

  @Override
  protected void RegistrerThisToGroup(Group group)
  {
    _group = group;
  }

  @Override
  public void Draw() { }

  @Override
  public void SetEvents() { }

  @Override
  public void CreateGeometry() { }

  public void AddBlock(Block block)
  {
    DisplayBlock newBlock = new DisplayBlock(this, block);
    AddChild(newBlock);
    _schema.AddBlock(block);
    if (_group != null)
      newBlock.RegistrerToGroup(_group);
  }

  protected void DefineBlocks()
  {
    _schema.GetBlocks().forEach(b -> AddBlock(b));
  }
}