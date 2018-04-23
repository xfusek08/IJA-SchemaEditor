package schemaeditor.app;

public interface ICommand
{
  public boolean CanExecute();
  public void Execute();
  public void Undo();
}