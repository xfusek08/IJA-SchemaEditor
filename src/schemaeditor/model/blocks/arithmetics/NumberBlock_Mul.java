/**
 * @file:     NumberBlock_Mul.java
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
public class NumberBlock_Mul extends Block
{
    /** Constructor */
    public NumberBlock_Mul(UUID ID) 
    {
        super(ID, "name");
        DefinePorts();
    }

    public void Calculate()
    {

    }

    protected void DefinePorts()
    {

    }
}