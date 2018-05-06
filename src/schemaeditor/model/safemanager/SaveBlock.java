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

  /** 
   * Set save metod
   * @param _status blockStatus
  */
  public void setStatus(BlockStatus _status)
  {
    this._status = _status;
  }

  /** 
   * Get save metod
   * @return blockStatus
  */
  public BlockStatus getStatus()
  {
    return _status;
  }

  /** 
   * Set save metod
   * @param ID block ID
  */
  public void setID(UUID ID)
  {
    this.ID = ID;
  }

  /** 
   * Get save metod
   * @return block ID
  */
  public UUID getID()
  {
    return ID;
  }

  /** 
   * Set save metod
   * @param InputPorts Input ports
  */
  public void setInputPorts(List<SavePort> InputPorts)
  {
    this.InputPorts = InputPorts;
  }

  /** 
   * Get save metod
   * @return Input ports
  */
  public List<SavePort> getInputPorts()
  {
    return InputPorts;
  }

  /** 
   * Set save metod
   * @param OutputPorts Output ports
  */
  public void setOutputPorts(List<SavePort> OutputPorts)
  {
    this.OutputPorts = OutputPorts;
  }

  /** 
   * Get save metod
   * @return Output ports
  */
  public List<SavePort> getOutputPorts()
  {
    return OutputPorts;
  }

  /** 
   * Set save metod
   * @param DisplayName Name of block
  */
  public void setDisplayName(String DisplayName)
  {
    this.DisplayName = DisplayName;
  }

  /** 
   * Get save metod
   * @return Name of block
  */
  public String getDisplayName()
  {
    return DisplayName;
  }

  /** 
   * Set save metod
   * @param X co-ordinate x
  */
  public void setX(double X)
  {
    this.X = X;
  }

  /** 
   * Set save metod
   * @param Y co-ordinate y
  */
  public void setY(double Y)
  {
    this.Y = Y;
  }

  /** 
   * Get save metod
   * @return co-ordinate x
  */
  public double getX()
  {
    return this.X;
  }

  /** 
   * Get save metod
   * @return co-ordinate x
  */
  public double getY()
  {
    return this.Y;
  }

  /** 
   * Set save metod
   * @param _block block to be saved
  */
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

  /** 
   * Get save metod
   * @return block that was saved
  */
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
    block.DisplayName = this.DisplayName;
    block.X = this.X;
    block.Y = this.Y;
    return block;
  }
}