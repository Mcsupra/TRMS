package TRMS.TRMSDao;

import java.sql.SQLException;
import java.util.List;

public interface CRUD<T> {

    public int insert(T t) throws SQLException;
		
	public T select(int t) throws SQLException;
		
	public List<T> selectAll() throws SQLException;
		
	public boolean update(T t) throws SQLException;
		
	public boolean delete(int t)throws SQLException;
    
}
