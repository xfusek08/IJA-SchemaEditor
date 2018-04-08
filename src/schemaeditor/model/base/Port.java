/**
 * @file:     Port.java
 * @package:  schemaeditor.model.base
 * @author    Jaromir Franek
 * @date      08.04.2018
 */
package schemaeditor.model.base;

import java.util.*;

/**
 * Class representing one port
 */
public abstract class Port
{
    protected HashMap data;
    protected List<java.lang.String> undefinedValues;

    /** Constructor */
    public Port() 
    {
        this.data = new HashMap();
        this.undefinedValues = new ArrayList<>();
    }

    /** Define value of port */
    protected void _DefineValue(java.lang.String valueName, double defaultValue) 
    {

    }

    /** Check if ports are compative */
    public boolean Compatipe(HashMap data)
    {
        return true;
    }

    /** Return list of value names */
    public java.lang.String[] GetValuesNames()
    {
        java.lang.String[] values = new java.lang.String[this.undefinedValues.size()];
        values = this.undefinedValues.toArray(values);
        return values;
    }

    /** Return value by name of type */
    public double GetValueByName(java.lang.String valueName)
    {
        return 1.0;
    }

    /** Set value by name of type */
    public void SetValueByName(java.lang.String valueName, double value)
    {

    }

    /** Set value by connected port */
    public void SetValueFromPort(Port otherPort)
    {
        
    }

    /** Check if value is defined */
    public boolean hasDefinedValue()
    {
        return true;
    }
}