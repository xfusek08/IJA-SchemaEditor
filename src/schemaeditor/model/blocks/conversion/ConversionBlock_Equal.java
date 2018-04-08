/**
 * @file:     ConversionBlock_Equal.java
 * @package:  schemaeditor.model.blocks.conversion
 * @author    Jaromir Franek
 * @date      08.04.2018
 */
package schemaeditor.model.blocks.conversion;

import schemaeditor.model.base.Block;
import java.util.*;

/**
 * Class reprezenting one block
 */
public class ConversionBlock_Equal extends Block
{
    public static final String NAME = "Equal";

    /** Constructor */
    public ConversionBlock_Equal(UUID ID) 
    {
        super(ID, NAME);
    }

    public ConversionBlock_Equal()
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