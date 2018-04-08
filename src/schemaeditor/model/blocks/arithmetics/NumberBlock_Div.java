/**
 * @file:     NumberBlock_Div.java
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
public class NumberBlock_Div extends Block
{
    public static final String NAME = "Div";

    /** Constructor */
    public NumberBlock_Div(UUID ID) 
    {
        super(ID, NAME);
    }

    /** Constructor */
    public NumberBlock_Div()
    {
        super(UUID.randomUUID(), NAME);
    }

    public void Calculate()
    {

    }

    protected void DefinePorts()
    {

    }
}