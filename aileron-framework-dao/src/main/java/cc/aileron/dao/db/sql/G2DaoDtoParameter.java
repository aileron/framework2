package cc.aileron.dao.db.sql;

import java.util.List;

/**
 * @author Masahiko Ashizawa <ashizawa@speee.jp>
 */
public class G2DaoDtoParameter
{
	/**
	 * @param parameters
	 */
	public G2DaoDtoParameter(final List<Object> parameters)
	{
		this.parameters = parameters;
	}

	/**
	 * parameters
	 */
	public final List<Object> parameters ;
}
