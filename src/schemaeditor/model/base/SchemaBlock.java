/**
 * @file:     SchemaBlock.java
 * @package:  schemaeditor.model.base
 * @author    Petr Fusek
 * @date      16.04.2018
 */
package schemaeditor.model.base;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

class SchemaBlock
{
  protected Block _block;
  protected Set<UUID> _precedestors;

  public SchemaBlock(Block block)
  {
    _block = block;
    _precedestors = new HashSet<UUID>();
  }

  public Block GetBlock()
  {
    return _block;
  }
}
