/**
 * @file:     NumberBlock_Sub.java
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
public class NumberBlock_Sub extends Block
{
    /** Constructor */
    public NumberBlock_Sub(UUID ID) 
    {
        super(ID, "Sub");
    }

    /** Constructor */
    public NumberBlock_Sub()
    {
        super(UUID.randomUUID(), "Sub");
    }

    public void Calculate()
    {

    }

    protected void DefinePorts()
    {

    }
}