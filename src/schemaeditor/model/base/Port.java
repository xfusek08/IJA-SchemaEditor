package schemaeditor.model.base;

import java.util.*;

public abstract class Port
{
    protected HashMap data;
    protected List<java.lang.String> undefinedValues;
    public Port() 
    {
        this.data = new HashMap();
        this.undefinedValues = new ArrayList<>();
    }

    private void DefineValue(java.lang.String valueName, double defaultValue) 
    {

    }

    public boolean Compatipe(HashMap data)
    {
        return true;
    }

    public java.lang.String[] GetValuesNames()
    {
        java.lang.String[] values = new java.lang.String[this.undefinedValues.size()];
        values = this.undefinedValues.toArray(values);
        return values;
    }

    public double GetValueByName(java.lang.String valueName)
    {
        return 1.0;
    }

    public void SetValueByName(java.lang.String valueName, double value)
    {

    }

    public void SetValueFromPort(Port otherPort)
    {
        
    }

    public boolean hasDefinedValue()
    {
        return true;
    }
}