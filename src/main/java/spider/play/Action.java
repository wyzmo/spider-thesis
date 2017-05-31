package spider.play;

public interface Action<T>
{
  /**
   * Applies an action to a specified target
   * and returns a new, modified, or changed
   * target.
   */
  T apply(T target);
}