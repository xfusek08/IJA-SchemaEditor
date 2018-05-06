/**
 * @file:     Port.java
 * @package:  schemaeditor.model.safemanager
 * @author    Jaromir Franek
 * @date      19.04.2018
 */

package schemaeditor.model.safemanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import schemaeditor.model.ports.*;
import schemaeditor.model.base.*;

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

  /**
   * Set save method
   * @param type port type
  */
  public void setType(String type)
  {
    this.type = type;
  }

  /**
   * Get save method
   * @return port type
  */
  public String getType()
  {
    return type;
  }

  /**
   * Set save method
   * @param _data port data
  */
  public void setData(HashMap<String, Double> _data)
  {
    this._data = _data;
  }

  /**
   * Get save method
   * @return port data
  */
  public HashMap<String, Double> getData()
  {
    return _data;
  }

  /**
   * Set save method
   * @param _undefinedValues undefined values
  */
  public void setVal(List<String> _undefinedValues)
  {
    this._undefinedValues = _undefinedValues;
  }

  /**
   * Get save method
   * @return undefined values
  */
  public List<String> getVal()
  {
    return _undefinedValues;
  }

  /**
   * Set save method
   * @param port port to be set
  */
  public void setFromSchema(Port port)
  {
    this.type = port.type;
    this._data = port._data;
    this._undefinedValues = port.GetUndefinedValues();
  }

  /**
   * Get save method
   * @return port
  */
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