package cc.aileron.dao.db.exec;

/**
 * Object以外が渡された場合
 */
public class NotFoundImplementsInterfaceError extends Error
{
	private static final long serialVersionUID = -2218509026537858166L;

	/**
	 * @param target
	 */
	public NotFoundImplementsInterfaceError(final Class<?> target)
	{
		super("Need to implememnts any interface:" + target.getName());
	}
}
