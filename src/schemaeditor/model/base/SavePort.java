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
import java.util.Set;
import schemaeditor.model.ports.*;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * Class representing one port
 */
@XmlRootElement(name = "SavePort")
@XmlAccessorType (XmlAccessType.FIELD)
public class SavePort
{
  @XmlElement
  private String type; 
  @XmlElement
  private HashMap<String, Double> _data;
  @XmlElement
  private List<String> _undefinedValues;

  public void setType(String type) 
  {
    this.type = type;
  }

  public String getType() 
  {
    return type;
  }

  public void setData(HashMap<String, Double> _data) 
  {
    this._data = _data;
  }

  public HashMap<String, Double> getData() 
  {
    return _data;
  }

  public void setVal(List<String> _undefinedValues) 
  {
    this._undefinedValues = _undefinedValues;
  }

  public List<String> getVal() 
  {
    return _undefinedValues;
  }

  public void setFromSchema(Port port)
  {
    this.type = port.type;
    this._data = port._data;
    this._undefinedValues = port.GetUndefinedValues();
  }

  public Port getFromSave()
  {
    Port port;
    switch (this.type) 
    {
      case "number":
        port = new NumberPort();
        break;
      case "bool":
        port = new BoolPort();
        break;
      case "real":
        port = new ComplexPort();
        break;
      default:
        port = new NumberPort();
        break;
    }
    port._data = this._data;
    port.SetUndefinedValues(this._undefinedValues);
    return port;
  }
}