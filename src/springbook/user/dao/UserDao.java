package springbook.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;

import springbook.user.domain.User;

public class UserDao {

	private DataSource dataSource;
	private JdbcContext jdbcContext; 

	public void setDataSource(DataSource dataSource) {
		this.jdbcContext = new JdbcContext();
		this.jdbcContext.setDataSource(dataSource);
		this.dataSource = dataSource; // 아직 JdbcContext를 적용하지 않은 메소드를 위해 저장해둔다. 
	}

	public void add(final User user) throws SQLException {

		this.jdbcContext.workWithStatementStrategy(new StatementStrategy() {

			public PreparedStatement makePreparedStatement(Connection c) throws SQLException {

				PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values (?,?,?)");

				ps.setString(1, user.getId());
				ps.setString(2, user.getName());
				ps.setString(3, user.getPassword());

				return ps;
			}
		});
	}

	public User get(String id) throws ClassNotFoundException, SQLException {

		Connection c = dataSource.getConnection();

		PreparedStatement ps = c.prepareStatement("select * from users where id = ?");
		ps.setString(1, id);

		ResultSet rs = ps.executeQuery();

		User user = null;
		if (rs.next()) {

			user = new User();
			user.setId(rs.getString("id"));
			user.setName(rs.getString("name"));
			user.setPassword(rs.getString("password"));
		}

		rs.close();
		ps.close();
		c.close();

		return user;
	}

	// deleteAll은 클라이언트 
	public void deleteAll() throws SQLException {
		this.jdbcContext.workWithStatementStrategy(new StatementStrategy() {
			public PreparedStatement makePreparedStatement(Connection c) throws SQLException {

				PreparedStatement ps = c.prepareStatement("delete from users");

				return ps;
			}
		});
	}

	public int getCount() throws SQLException {

		Connection c = dataSource.getConnection();
		PreparedStatement ps = c.prepareStatement("select count(*) from users");

		ResultSet rs = ps.executeQuery();
		rs.next();
		int count = rs.getInt(1);

		rs.close();
		ps.close();
		c.close();

		return count;
	}
}
