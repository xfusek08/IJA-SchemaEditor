/**
 * @file:     TestBlockAritm.java
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
import schemaeditor.model.blocks.arithmetics.*;
import schemaeditor.model.ports.*;

/**
 * Test class
 */
public class TestBlockAritm
{
    NumberBlock_Abs abs;
    NumberBlock_Add add;
    NumberBlock_Div div;
    NumberBlock_Mul mul;
    NumberBlock_Sub sub;

    NumberPort numberTest;

    @Before
    public void setUp()
    {
        abs = new NumberBlock_Abs();
        add = new NumberBlock_Add();
        div = new NumberBlock_Div();
        mul = new NumberBlock_Mul();
        sub = new NumberBlock_Sub();

        numberTest = new NumberPort();
    }

    /**
     * Zakladni test implementace ports.
     */
    @Test
    public void testPorts()
    {
        numberTest.SetValueByName("number", 1.0);
        assertEquals(1.0, numberTest.GetValueByName("number"), 0.0);
        numberTest.SetValueByName("number", 2.0);
        assertEquals(2.0, numberTest.GetValueByName("number"), 0.0);
        numberTest.SetValueByName("number", -5.0);
        assertEquals(-5.0, numberTest.GetValueByName("number"), 0.0);
        numberTest.SetValueByName("number", -5.4);
        assertEquals(-5.4, numberTest.GetValueByName("number"), 0.0);
        numberTest.SetValueByName("number", 1.2);
        assertEquals(1.2, numberTest.GetValueByName("number"), 0.0);
    }

    /**
     * Zakladni test implementace abs.
     */
    @Test
    public void testAbs()
    {
        abs.InputPorts.get(0).SetValueByName("number", 2.0);
        abs.Calculate();
        assertEquals(2.0, abs.OutputPorts.get(0).GetValueByName("number"), 0.0);
        abs.InputPorts.get(0).SetValueByName("number", -10.0);
        abs.Calculate();
        assertEquals(10.0, abs.OutputPorts.get(0).GetValueByName("number"), 0.0);
        abs.InputPorts.get(0).SetValueByName("number", 0.0);
        abs.Calculate();
        assertEquals(0.0, abs.OutputPorts.get(0).GetValueByName("number"), 0.0);

        abs.InputPorts.get(0).SetValueByName("number", 2.15);
        abs.Calculate();
        assertEquals(2.15, abs.OutputPorts.get(0).GetValueByName("number"), 0.0);
        abs.InputPorts.get(0).SetValueByName("number", -7.01);
        abs.Calculate();
        assertEquals(7.01, abs.OutputPorts.get(0).GetValueByName("number"), 0.0);
    }

    /**
     * Zakladni test implementace add.
     */
    @Test
    public void testAdd()
    {
        add.InputPorts.get(0).SetValueByName("number", 2.0);
        add.InputPorts.get(1).SetValueByName("number", 2.0);
        add.Calculate();
        assertEquals(4.0, add.OutputPorts.get(0).GetValueByName("number"), 0.0);
        add.InputPorts.get(0).SetValueByName("number", -10.0);
        add.InputPorts.get(1).SetValueByName("number", 10.0);
        add.Calculate();
        assertEquals(0.0, add.OutputPorts.get(0).GetValueByName("number"), 0.0);
        add.InputPorts.get(0).SetValueByName("number", -10.0);
        add.InputPorts.get(1).SetValueByName("number", -40.0);
        add.Calculate();
        assertEquals(-50.0, add.OutputPorts.get(0).GetValueByName("number"), 0.0);
        add.InputPorts.get(0).SetValueByName("number", 0.0);
        add.InputPorts.get(1).SetValueByName("number", 0.0);
        add.Calculate();
        assertEquals(0.0, add.OutputPorts.get(0).GetValueByName("number"), 0.0);

        add.InputPorts.get(0).SetValueByName("number", 2.15);
        add.InputPorts.get(1).SetValueByName("number", 2.15);
        add.Calculate();
        assertEquals(4.5, add.OutputPorts.get(0).GetValueByName("number"), 0.0);
        add.InputPorts.get(0).SetValueByName("number", -7.01);
        add.InputPorts.get(1).SetValueByName("number", -7.01);
        add.Calculate();
        assertEquals(-14.02, add.OutputPorts.get(0).GetValueByName("number"), 0.0);
        add.InputPorts.get(0).SetValueByName("number", -7.02);
        add.InputPorts.get(1).SetValueByName("number", 3.01);
        add.Calculate();
        assertEquals(-4.01, add.OutputPorts.get(0).GetValueByName("number"), 0.0);
    }

    /**
     * Zakladni test implementace div.
     */
    @Test
    public void testDiv()
    {
        div.InputPorts.get(0).SetValueByName("number", 2.0);
        div.InputPorts.get(1).SetValueByName("number", 2.0);
        div.Calculate();
        assertEquals(1.0, div.OutputPorts.get(0).GetValueByName("number"), 0.0);
        div.InputPorts.get(0).SetValueByName("number", -10.0);
        div.InputPorts.get(1).SetValueByName("number", 2.0);
        div.Calculate();
        assertEquals(-5.0, div.OutputPorts.get(0).GetValueByName("number"), 0.0);
        div.InputPorts.get(0).SetValueByName("number", 0.0);
        div.InputPorts.get(1).SetValueByName("number", 2.0);
        div.Calculate();
        assertEquals(0.0, div.OutputPorts.get(0).GetValueByName("number"), 0.0);

        div.InputPorts.get(0).SetValueByName("number", 2.5);
        div.InputPorts.get(1).SetValueByName("number", 0.5);
        div.Calculate();
        assertEquals(5, div.OutputPorts.get(0).GetValueByName("number"), 0.0);
        div.InputPorts.get(0).SetValueByName("number", -4.2);
        div.InputPorts.get(1).SetValueByName("number", 0.20);
        div.Calculate();
        assertEquals(-2.1, div.OutputPorts.get(0).GetValueByName("number"), 0.0);
    }

    /**
     * Zakladni test implementace mul.
     */
    @Test
    public void testMul()
    {
        mul.InputPorts.get(0).SetValueByName("number", 2.0);
        mul.InputPorts.get(1).SetValueByName("number", 2.0);
        mul.Calculate();
        assertEquals(4.0, mul.OutputPorts.get(0).GetValueByName("number"), 0.0);
        mul.InputPorts.get(0).SetValueByName("number", -10.0);
        mul.InputPorts.get(1).SetValueByName("number", 2.0);
        mul.Calculate();
        assertEquals(-20.0, mul.OutputPorts.get(0).GetValueByName("number"), 0.0);
        mul.InputPorts.get(0).SetValueByName("number", 0.0);
        mul.InputPorts.get(1).SetValueByName("number", 2.0);
        mul.Calculate();
        assertEquals(0.0, mul.OutputPorts.get(0).GetValueByName("number"), 0.0);

        mul.InputPorts.get(0).SetValueByName("number", 2.5);
        mul.InputPorts.get(1).SetValueByName("number", 0.5);
        mul.Calculate();
        assertEquals(1.25, mul.OutputPorts.get(0).GetValueByName("number"), 0.0);
        mul.InputPorts.get(0).SetValueByName("number", -4.2);
        mul.InputPorts.get(1).SetValueByName("number", 0.50);
        mul.Calculate();
        assertEquals(-2.1, mul.OutputPorts.get(0).GetValueByName("number"), 0.0);
    }

    /**
     * Zakladni test implementace sub.
     */
    @Test
    public void testSub()
    {
        sub.InputPorts.get(0).SetValueByName("number", 2.0);
        sub.InputPorts.get(1).SetValueByName("number", 2.0);
        sub.Calculate();
        assertEquals(0.0, sub.OutputPorts.get(0).GetValueByName("number"), 0.0);
        sub.InputPorts.get(0).SetValueByName("number", -10.0);
        sub.InputPorts.get(1).SetValueByName("number", 10.0);
        sub.Calculate();
        assertEquals(-20.0, sub.OutputPorts.get(0).GetValueByName("number"), 0.0);
        sub.InputPorts.get(0).SetValueByName("number", -10.0);
        sub.InputPorts.get(1).SetValueByName("number", -40.0);
        sub.Calculate();
        assertEquals(30.0, sub.OutputPorts.get(0).GetValueByName("number"), 0.0);
        sub.InputPorts.get(0).SetValueByName("number", 0.0);
        sub.InputPorts.get(1).SetValueByName("number", 0.0);
        sub.Calculate();
        assertEquals(0.0, sub.OutputPorts.get(0).GetValueByName("number"), 0.0);

        sub.InputPorts.get(0).SetValueByName("number", 2.15);
        sub.InputPorts.get(1).SetValueByName("number", 2.15);
        sub.Calculate();
        assertEquals(0, sub.OutputPorts.get(0).GetValueByName("number"), 0.0);
        sub.InputPorts.get(0).SetValueByName("number", -7.01);
        sub.InputPorts.get(1).SetValueByName("number", 7.01);
        sub.Calculate();
        assertEquals(-14.02, sub.OutputPorts.get(0).GetValueByName("number"), 0.0);
        sub.InputPorts.get(0).SetValueByName("number", -7.02);
        sub.InputPorts.get(1).SetValueByName("number", 3.01);
        sub.Calculate();
        assertEquals(-10.03, sub.OutputPorts.get(0).GetValueByName("number"), 0.0);
    }
}