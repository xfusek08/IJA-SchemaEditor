/**
 * @file:     SaveBlock.java
 * @package:  schemaeditor.model.safemanager
 * @author    Jaromir Franek
 * @date      23.04.2018
 */
package schemaeditor.model.safemanager;

import schemaeditor.model.safemanager.SavePort;
import schemaeditor.model.blocks.arithmetics.*;
import schemaeditor.model.blocks.complex.*;
import schemaeditor.model.blocks.conversion.*;
import schemaeditor.model.blocks.logic.*;
import schemaeditor.model.ports.*;
import schemaeditor.model.base.*;
import schemaeditor.model.base.enums.*;
import java.util.*;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * Class reprezenting one block
 */
@XmlRootElement(name = "SaveBlock")
@XmlAccessorType (XmlAccessType.FIELD)
public class SaveBlock
{
  private BlockStatus _status = null;
  private UUID ID = null;
  private List<SavePort> InputPorts = new ArrayList<>();
  private List<SavePort> OutputPorts = new ArrayList<>();
  private String DisplayName = null;
  public double X;
  public double Y;

  public void setStatus(BlockStatus _status)
  {
    this._status = _status;
  }

  public BlockStatus getStatus()
  {
    return _status;
  }

  public void setID(UUID ID)
  {
    this.ID = ID;
  }

  public UUID getID()
  {
    return ID;
  }

  public void setInputPorts(List<SavePort> InputPorts)
  {
    this.InputPorts = InputPorts;
  }

  public List<SavePort> getInputPorts()
  {
    return InputPorts;
  }

  public void setOutputPorts(List<SavePort> OutputPorts)
  {
    this.OutputPorts = OutputPorts;
  }

  public List<SavePort> getOutputPorts()
  {
    return OutputPorts;
  }

  public void setDisplayName(String DisplayName)
  {
    this.DisplayName = DisplayName;
  }

  public String getDisplayName()
  {
    return DisplayName;
  }

  public void setX(double X)
  {
    this.X = X;
  }

  public void setY(double Y)
  {
    this.Y = Y;
  }

  public double getX()
  {
    return this.X;
  }

  public double getY()
  {
    return this.Y;
  }

  public void setFromSchema(Block _block)
  {
    SavePort sPort = new SavePort();
    for(Port port : _block.InputPorts)
    {
      sPort.setFromSchema(port);
      this.InputPorts.add(sPort);
    }
    for(Port port : _block.OutputPorts)
    {
      sPort.setFromSchema(port);
      this.OutputPorts.add(sPort);
    }
    this._status = _block.GetStatus();
    this.ID = _block.ID;
    this.DisplayName = _block.DisplayName;
    this.X = _block.X;
    this.Y = _block.Y;
  }

  public Block getFromSave()
  {
    Block block;
    switch(this.DisplayName)
    {
      case "abs":
        block = new NumberBlock_Abs(this.ID);
        break;
      case "Add":
        block = new NumberBlock_Add(this.ID);
        break;
      case "Div":
        block = new NumberBlock_Div(this.ID);
        break;
      case "Mul":
        block = new NumberBlock_Mul(this.ID);
        break;
      case "Sub":
        block = new NumberBlock_Sub(this.ID);
        break;
      case "Absolute value":
        block = new CplexBlock_Abs(this.ID);
        break;
      case "Complex addition":
        block = new CplexBlock_Add(this.ID);
        break;
      case "Complementary value":
        block = new CplexBlock_Complement(this.ID);
        break;
      case "Complex division":
        block = new CplexBlock_Div(this.ID);
        break;
      case "Complex multiplication":
        block = new CplexBlock_Mul(this.ID);
        break;
      case "Complex substraction":
        block = new CplexBlock_Sub(this.ID);
        break;
      case "BoolToNumber":
        block = new ConversionBlock_BoolToNumber(this.ID);
        break;
      case "Equal":
        block = new ConversionBlock_Equal(this.ID);
        break;
      case "Greater":
        block = new ConversionBlock_Greater(this.ID);
        break;
      case "Less":
        block = new ConversionBlock_Less(this.ID);
        break;
      case "And":
        block = new LogicBlock_And(this.ID);
        break;
      case "Not":
        block = new LogicBlock_Not(this.ID);
        break;
      case "Or":
        block = new LogicBlock_Or(this.ID);
        break;
      case "Xnor":
        block = new LogicBlock_Xnor(this.ID);
        break;
      case "Xor":
        block = new LogicBlock_Xor(this.ID);
        break;
      default:
        block = new NumberBlock_Abs(this.ID);
        break;
    }
    Port sPort;
    for(SavePort port : this.InputPorts)
    {
      switch (port.getType())
      {
        case "number":
          sPort = new NumberPort();
          break;
        case "bool":
          sPort = new BoolPort();
          break;
        case "real":
          sPort = new ComplexPort();
          break;
        default:
          sPort = new NumberPort();
          break;
      }
      sPort = port.getFromSave();
      block.InputPorts.add(sPort);
    }
    for(SavePort port : this.OutputPorts)
    {
      switch (port.getType())
      {
        case "number":
          sPort = new NumberPort();
          break;
        case "bool":
          sPort = new BoolPort();
          break;
        case "real":
          sPort = new ComplexPort();
          break;
        default:
          sPort = new NumberPort();
          break;
      }
      sPort = port.getFromSave();
      block.OutputPorts.add(sPort);
    }
    block.DisplayName = this.DisplayName;
    block.X = this.X;
    block.Y = this.Y;
    return block;
  }
}