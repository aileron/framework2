package cc.aileron.dao.db;

/**
 * @author Misato Takahashi<misato@speee.jp>
 *
 */
public class TargetClassNotRegistry extends Error
{

	/**
	 *
	 */
	private static final long serialVersionUID = -6320965794019155696L;

	/**
	 * @param targetClass
	 */
	public TargetClassNotRegistry(Class<?> targetClass)
	{
		super("Target Class is not Registry:" + targetClass.getName());
	}
}
