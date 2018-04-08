package schemaeditor.model.base;

import schemaeditor.model.base.Port;
import java.util.*;

public abstract class Block
{
    public UUID ID;
    public List<Port> InputPorts;
    public List<Port> OutputPorts;
    public java.lang.String DisplayName;
    public Block(UUID ID, java.lang.String name) 
    {
        this.ID = ID;
        this.InputPorts = new ArrayList<>();
        this.OutputPorts = new ArrayList<>();
        this.DisplayName = name;
    }

    public void CleanValues()
    {
        
    }

    public java.lang.String[] GetValuesNames()
    {
        return this.undefinedValues;
    }

    public abstract void Calculate();

    public boolean isAllInputsDefined()
    {
        return true;
    }
}