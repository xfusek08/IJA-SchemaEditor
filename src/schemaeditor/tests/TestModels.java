package schemaeditor.tests;

import schemaeditor.app.SchemaEditor;
import java.lang.reflect.Modifier;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Jaromir Franek
 */
public class TestModels
{


    @Before
    public void setUp()
    {

    }

    /**
     * Zakladni test implementace.
     */
    @Test
    public void test01()
    {
        assertEquals(5, 1);
    }

    /**
     * Zakladni test implementace.
     */
    @Test
    public void test02()
    {
        assertEquals(2, 1);
    }
}