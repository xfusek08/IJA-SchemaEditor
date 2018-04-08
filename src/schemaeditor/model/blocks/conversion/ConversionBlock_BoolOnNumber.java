/**
 * @file:     ConversionBlock_BoolOnNumber.java
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
public class ConversionBlock_BoolOnNumber extends Block
{
    public static final String NAME = "BoolOnNumber";

    /** Constructor */
    public ConversionBlock_BoolOnNumber(UUID ID) 
    {
        super(ID, NAME);
    }

    /** Constructor */
    public ConversionBlock_BoolOnNumber()
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