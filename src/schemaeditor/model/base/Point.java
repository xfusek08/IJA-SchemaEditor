/**
 * @file:     Point.java
 * @package:  safemanager.model.base
 * @author    Petr Fusek
 * @date      08.04.2018
 */
package schemaeditor.model.base;

/**
 * Simple datastructure of new point on the schema grid (board).
 */
public class Point {
  public int X;
  public int Y;

  /** Constructor */
  public Point(int x, int y)
  {
    X = x;
    Y = y;
  }
}