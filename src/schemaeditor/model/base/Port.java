/**
 * @file:     Port.java
 * @package:  schemaeditor.model.base
 * @author    Jaromir Franek
 * @date      19.04.2018
 */
package schemaeditor.model.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Set;

/**
 * Class representing one port
 */
public abstract class Port extends Observable
{
  public String type;
  public HashMap<String, Double> _data;
  protected List<String> _undefinedValues;
  protected boolean _isInput;
  protected int _inputNumber;

  /** Constructor */
  public Port()
  {
    _data = new HashMap<String, Double>();
    _undefinedValues = new ArrayList<String>();
  }

  /**
   * Define value of port 
   * @param valueName Name of data type
   * @param defaultValue value of data
   */
  protected void DefineValue(String valueName, double defaultValue)
  {
    _data.put(valueName, defaultValue);
  }

  /**
   * Check if ports are compative
   * @param other other port
   * @return true if the data are of same type
   */
  public boolean Compatible(Port other)
  {
    List dataKeys = new ArrayList(_data.keySet());
    List _dataKeys = new ArrayList(other._data.keySet());
    if(dataKeys.size() != _dataKeys.size())
      return false;
    for(int i = 0; i < dataKeys.size(); i++)
      if(!dataKeys.get(i).equals(_dataKeys.get(i)))
        return false;
    return true;
  }

  /**
   * Get port data
   * @return Hash map of port data
   */
  public HashMap<String, Double> GetData()
  {
    return new HashMap<String, Double>(_data);
  }

  /**
   * Set port data
   * @param data new value of data to be set
   */
  public void SetData(HashMap<String, Double> data)
  {
    this._data = data;
  }

  /**
   * Return list of value names
   * @return Set of names
   */
  public Set<String> GetValuesNames()
  {
    return _data.keySet();
  }

  /**
   * Return list of undefined values
   * @return List of undefinedValues
   */
  public List<String> GetUndefinedValues()
  {
    return _undefinedValues;
  }

  /**
   * Set list of undefined values
   */
  public void SetUndefinedValues(List<String> _undefinedValues)
  {
    this._undefinedValues = _undefinedValues;
  }

  /**
   * Return value by name of type
   * @param valueName name of searched value
   * @return value selected by name of type
   */
  public double GetValueByName(String valueName)
  {
    return _data.get(valueName);
  }

  /**
   * Set value by name of type
   * @param valueName name of searched value
   * @param value value to be set
   */
  public void SetValueByName(String valueName, Double value)
  {
    _data.replace(valueName, value);
  }

  /**
   * Set value by connected port
   * @param otherPort input port
   */
  public void SetValueFromPort(Port otherPort)
  {
    Iterable<String> names = otherPort.GetValuesNames();
    for(String s : names)
      _data.replace(s, otherPort.GetValueByName(s));
  }

  /**
   * Check if value is defined
   * @return true if all value are defined
   */
  public boolean hasDefinedValue()
  {
    if(_undefinedValues.size() > 0 || _data.size() == 0)
      return false;
    return true;
  }

  /**
   * Get value as string
   * @return string representation of port value
   */
  public String GetValueAsString()
  {
    String res = "";
    for(String s : GetValuesNames())
      if (s != "")
        res = res + "\"" + s + "\": " + String.valueOf(GetValueByName(s) + "\n");
    return res;
  }

  /**
   * Set input value of port
   * @param number input value of port
   */
  public void SetInputNumber(int number)
  {
    _inputNumber = number;
    _isInput = true;
    setChanged();
    notifyObservers();
  }

  /**
   * Unset input value of port
   */
  public void UnsetInput()
  {
    _isInput = false;
    setChanged();
    notifyObservers();
  }

  /**
   * Get isInput status
   * @return _isInput value
   */
  public boolean IsInput()
  {
    return _isInput;
  }

  /**
   * Get Input number
   * @return input number
   */
  public int GetInputNumber()
  {
    return _inputNumber;
  }
}