/**
 * @file:     TestBlockConv.java
 * @package:  schemaeditor.tests
 * @author    Jaromir Franek
 * @date      08.04.2018
 */
package schemaeditor.tests;

import schemaeditor.app.SchemaEditor;
import java.lang.reflect.Modifier;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import schemaeditor.model.blocks.conversion.*;
import schemaeditor.model.ports.*;

/**
 * Test class
 */
public class TestBlockConv
{
    ConversionBlock_BoolToNumber btn;
    ConversionBlock_Equal equ;
    ConversionBlock_Greater gre;
    ConversionBlock_Less les;

    BoolPort boolTest;

    @Before
    public void setUp()
    {
        btn = new ConversionBlock_BoolToNumber();
        equ = new ConversionBlock_Equal();
        gre = new ConversionBlock_Greater();
        les = new ConversionBlock_Less();

        boolTest = new BoolPort();
    }

    /**
     * Zakladni test implementace ports.
     */
    @Test
    public void testPorts()
    {
        boolTest.SetValueByName("bool", 0.0);
        assertEquals(0.0, boolTest.GetValueByName("bool"), 0.0);
        boolTest.SetValueByName("bool", 1.0);
        assertEquals(1.0, boolTest.GetValueByName("bool"), 0.0);
    }

    /**
     * Zakladni test implementace BoolToNumber.
     */
    @Test
    public void BoolToNumber()
    {
        btn.InputPorts.get(0).SetValueByName("bool", 1.0);
        btn.Calculate();
        assertEquals(1.0, btn.OutputPorts.get(0).GetValueByName("number"), 0.0);
        btn.InputPorts.get(0).SetValueByName("bool", 0.0);
        btn.Calculate();
        assertEquals(0.0, btn.OutputPorts.get(0).GetValueByName("number"), 0.0);
    }

    /**
     * Zakladni test implementace Equal.
     */
    @Test
    public void Equal()
    {
        equ.InputPorts.get(0).SetValueByName("number", 2.0);
        equ.InputPorts.get(1).SetValueByName("number", 2.0);
        equ.Calculate();
        assertEquals(1.0, equ.OutputPorts.get(0).GetValueByName("bool"), 0.0);
        equ.InputPorts.get(0).SetValueByName("number", 2.0);
        equ.InputPorts.get(1).SetValueByName("number", 1.0);
        equ.Calculate();
        assertEquals(0.0, equ.OutputPorts.get(0).GetValueByName("bool"), 0.0);
        equ.InputPorts.get(0).SetValueByName("number", 2.0);
        equ.InputPorts.get(1).SetValueByName("number", -2.0);
        equ.Calculate();
        assertEquals(0.0, equ.OutputPorts.get(0).GetValueByName("bool"), 0.0);
        equ.InputPorts.get(0).SetValueByName("number", 2.5);
        equ.InputPorts.get(1).SetValueByName("number", 2.0);
        equ.Calculate();
        assertEquals(0.0, equ.OutputPorts.get(0).GetValueByName("bool"), 0.0);
        equ.InputPorts.get(0).SetValueByName("number", -2.1);
        equ.InputPorts.get(1).SetValueByName("number", -2.1);
        equ.Calculate();
        assertEquals(1.0, equ.OutputPorts.get(0).GetValueByName("bool"), 0.0);

    }

    /**
     * Zakladni test implementace Greater.
     */
    @Test
    public void Greater()
    {
        gre.InputPorts.get(0).SetValueByName("number", 2.0);
        gre.InputPorts.get(1).SetValueByName("number", 2.0);
        gre.Calculate();
        assertEquals(0.0, gre.OutputPorts.get(0).GetValueByName("bool"), 0.0);
        gre.InputPorts.get(0).SetValueByName("number", 2.0);
        gre.InputPorts.get(1).SetValueByName("number", -2.0);
        gre.Calculate();
        assertEquals(1.0, gre.OutputPorts.get(0).GetValueByName("bool"), 0.0);
        gre.InputPorts.get(0).SetValueByName("number", 2.5);
        gre.InputPorts.get(1).SetValueByName("number", 2.0);
        gre.Calculate();
        assertEquals(1.0, gre.OutputPorts.get(0).GetValueByName("bool"), 0.0);
        gre.InputPorts.get(0).SetValueByName("number", 2.0);
        gre.InputPorts.get(1).SetValueByName("number", 2.0);
        gre.Calculate();
        assertEquals(0.0, gre.OutputPorts.get(0).GetValueByName("bool"), 0.0);
        gre.InputPorts.get(0).SetValueByName("number", 2.0);
        gre.InputPorts.get(1).SetValueByName("number", 3.0);
        gre.Calculate();
        assertEquals(0.0, gre.OutputPorts.get(0).GetValueByName("bool"), 0.0);
        gre.InputPorts.get(0).SetValueByName("number", -2.3);
        gre.InputPorts.get(1).SetValueByName("number", 2.4);
        gre.Calculate();
        assertEquals(0.0, gre.OutputPorts.get(0).GetValueByName("bool"), 0.0);
    }

    /**
     * Zakladni test implementace Less.
     */
    @Test
    public void Less()
    {
        les.InputPorts.get(0).SetValueByName("number", 2.0);
        les.InputPorts.get(1).SetValueByName("number", 2.0);
        les.Calculate();
        assertEquals(0.0, les.OutputPorts.get(0).GetValueByName("bool"), 0.0);
        les.InputPorts.get(0).SetValueByName("number", 2.0);
        les.InputPorts.get(1).SetValueByName("number", -2.0);
        les.Calculate();
        assertEquals(0.0, les.OutputPorts.get(0).GetValueByName("bool"), 0.0);
        les.InputPorts.get(0).SetValueByName("number", 2.5);
        les.InputPorts.get(1).SetValueByName("number", 2.0);
        les.Calculate();
        assertEquals(0.0, les.OutputPorts.get(0).GetValueByName("bool"), 0.0);
        les.InputPorts.get(0).SetValueByName("number", -1.0);
        les.InputPorts.get(1).SetValueByName("number", 2.0);
        les.Calculate();
        assertEquals(1.0, les.OutputPorts.get(0).GetValueByName("bool"), 0.0);
        les.InputPorts.get(0).SetValueByName("number", 2.0);
        les.InputPorts.get(1).SetValueByName("number", 3.0);
        les.Calculate();
        assertEquals(1.0, les.OutputPorts.get(0).GetValueByName("bool"), 0.0);
        les.InputPorts.get(0).SetValueByName("number", -2.3);
        les.InputPorts.get(1).SetValueByName("number", 2.4);
        les.Calculate();
        assertEquals(1.0, les.OutputPorts.get(0).GetValueByName("bool"), 0.0);
    }
}