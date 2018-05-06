/**
 * @file:     BlockStatus.java
 * @package:  schemaeditor.model.base
 * @author    Jaromir Franek
 * @date      08.04.2018
 */
package schemaeditor.model.base;

import java.util.Observable;
import java.util.UUID;
import schemaeditor.model.base.enums.EState;

/**
 * Class representing status
 */
public class BlockStatus extends Observable
{
  private EState _state;
  private String _message;

  /** Constructor */
  public BlockStatus()
  {
    this._state = EState.Ready;
    this._message = "";
  }

  /**
   * Change state
   * @param state value to be set
   */
  public void setState(EState state)
  {
    _state = state;
    setChanged();
    notifyObservers();
  }

   /**
   * Get state
   * @return state
   */
  public EState getState()
  {
    return _state;
  }

  /**
   * Get message
   * @return massage
   */
  public String getMessage()
  {
    return _message;
  }

  /**
   * Set message
   * @param _message value of new message
   */
  public void setMessage(String _message)
  {
	  this._message = _message;
  }
}