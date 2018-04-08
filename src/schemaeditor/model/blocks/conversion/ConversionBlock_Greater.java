/**
 * @file:     ConversionBlock_Greater.java
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
public class ConversionBlock_Greater extends Block
{
    /** Constructor */
    public ConversionBlock_Greater(UUID ID) 
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