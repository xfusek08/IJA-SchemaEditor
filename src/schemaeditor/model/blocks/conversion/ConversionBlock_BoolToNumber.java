/**
 * @file:     ConversionBlock_BoolToNumber.java
 * @package:  schemaeditor.model.blocks.conversion
 * @author    Jaromir Franek
 * @date      08.04.2018
 */
package schemaeditor.model.blocks.conversion;

import schemaeditor.model.base.Block;
import schemaeditor.model.ports.*;
import java.util.*;

/**
 * Class reprezenting one block
 */
public class ConversionBlock_BoolToNumber extends Block
{
    public static final String NAME = "BoolToNumber";

    /** Constructor */
    public ConversionBlock_BoolToNumber(UUID ID) 
    {
        super(ID, NAME);
    }

    /** Constructor */
    public ConversionBlock_BoolToNumber()
    {
        super(UUID.randomUUID(), NAME);
    }

    public void Calculate()
    {
        
    }

    protected void DefinePorts()
    {
        InputPorts.add(new BoolPort());

        OutputPorts.add(new NumberPort());
    }
}