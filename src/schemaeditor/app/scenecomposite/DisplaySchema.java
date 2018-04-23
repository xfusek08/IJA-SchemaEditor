package schemaeditor.app.scenecomposite;

import javafx.scene.Group;
import schemaeditor.model.base.Block;
import schemaeditor.model.base.Schema;

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

  public void AddBlock(Block block)
  {
    DisplayBlock newBlock = new DisplayBlock(this, block);
    AddChild(newBlock);
    if (_group != null)
      newBlock.RegistrerToGroup(_group);
  }

  protected void DefineBlocks()
  {
    _schema.GetBlocks().forEach(b -> AddBlock(b));
  }
}