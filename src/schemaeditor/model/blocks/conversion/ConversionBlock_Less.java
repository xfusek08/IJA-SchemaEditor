/**
 * @file:     ConversionBlock_Less.java
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
public class ConversionBlock_Less extends Block
{
    /** Constructor */
    public ConversionBlock_Less(UUID ID) 
    {
        super(ID, "Less");
    }

    public ConversionBlock_Less()
    {
        super(UUID.randomUUID(), "Less");
    }

    public void Calculate()
    {
        
    }

    protected void DefinePorts()
    {

    }
}