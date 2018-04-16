/**
 * @file:     Port.java
 * @package:  schemaeditor.model.base
 * @author    Jaromir Franek
 * @date      08.04.2018
 */
package schemaeditor.model.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Class representing one port
 */
public abstract class Port
{
  public HashMap<String, Double> _data;
  protected List<String> _undefinedValues;

  /** Constructor */
  public Port()
  {
    _data = new HashMap<String, Double>();
    _undefinedValues = new ArrayList<String>();
  }

  /** Define value of port */
  protected void DefineValue(String valueName, double defaultValue)
  {
    _data.put(valueName, defaultValue);
  }

  /** Check if ports are compative */
  public boolean Compatible(HashMap data)
  {
    List dataKeys = new ArrayList(data.keySet());
    List _dataKeys = new ArrayList(_data.keySet());
    if(dataKeys.size() != _dataKeys.size())
      return false;
    for(int i = 0; i < dataKeys.size(); i++)
      if(dataKeys.get(i) != _dataKeys.get(i))
        return false;
    return true;
  }

  /** Return list of value names */
  public Set<String> GetValuesNames()
  {
    return _data.keySet();
  }

  /** Return value by name of type */
  public double GetValueByName(String valueName)
  {
    return _data.get(valueName);
  }

  /** Set value by name of type */
  public void SetValueByName(String valueName, Double value)
  {
    _data.replace(valueName, value);
  }

  /** Set value by connected port */
  public void SetValueFromPort(Port otherPort)
  {
    Iterable<String> names = otherPort.GetValuesNames();
    for(String s : names)
      _data.replace(s, otherPort.GetValueByName(s));
  }

  /** Check if value is defined */
  public boolean hasDefinedValue()
  {
    if(_undefinedValues.size() > 0 || _data.size() == 0)
      return false;
    return true;
  }
}