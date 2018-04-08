/**
 * @file:     NumberBlock_Add.java
 * @package:  schemaeditor.model.blocks.arithmetics
 * @author    Jaromir Franek
 * @date      08.04.2018
 */
package schemaeditor.model.blocks.arithmetics;

import schemaeditor.model.base.Block;
import java.util.*;

/**
 * Class reprezenting one block
 */
public class NumberBlock_Add extends Block
{
    /** Constructor */
    public NumberBlock_Add(UUID ID) 
    {
        super(ID, "Add");
    }

    /** Constructor */
    public NumberBlock_Add()
    {
        super(UUID.randomUUID(), "Add");
    }

    public void Calculate()
    {

    }

    protected void DefinePorts()
    {

    }
}