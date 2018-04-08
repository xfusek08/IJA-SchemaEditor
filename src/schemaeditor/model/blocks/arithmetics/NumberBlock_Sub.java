/**
 * @file:     NumberBlock_Sub.java
 * @package:  schemaeditor.model.blocks.arithmetics
 * @author    Jaromir Franek
 * @date      08.04.2018
 */
package schemaeditor.model.blocks.arithmetics;

import schemaeditor.model.base.Block;
import schemaeditor.model.ports.*;
import java.util.*;

/**
 * Class reprezenting one block
 */
public class NumberBlock_Sub extends Block
{
    public static final String NAME = "Sub";

    /** Constructor */
    public NumberBlock_Sub(UUID ID) 
    {
        super(ID, NAME);
    }

    /** Constructor */
    public NumberBlock_Sub()
    {
        super(UUID.randomUUID(), NAME);
    }

    public void Calculate()
    {

    }

    protected void DefinePorts()
    {
        InputPorts.add(new NumberPort());
        InputPorts.add(new NumberPort());

        OutputPorts.add(new NumberPort());
    }
}