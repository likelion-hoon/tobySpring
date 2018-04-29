package springbook.learningtest.junit;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import springbook.user.dao.UserDao;
import springbook.user.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/test-applicationContext.xml")
public class UserDaoTest {
	
	@Autowired
	private ApplicationContext context;
	
	private UserDao dao; 
	private User user1,user2,user3;
	
	@Before
	public void setUp() {
		this.dao = context.getBean("userDao",UserDao.class);
		this.user1 = new User("monki0120","이종훈","1212");
		this.user2 = new User("seolyn","안서린","3434");
		this.user3 = new User("chanjo","박찬조","5656");
	}
	
	@Test
	public void addAndGet() throws SQLException, ClassNotFoundException {

		dao.deleteAll(); 
		assertThat(dao.getCount(), is(0)); // deleteAll() 제대로 됬는지 확인
		
		dao.add(user1);
		dao.add(user2);
		assertThat(dao.getCount(), is(2)); // add() 제대로 됬는지 확인
		
		dao.add(user3);
		assertThat(dao.getCount(), is(3));
		
		User userget1 = dao.get(user1.getId());
		assertThat(userget1.getName(), is(user1.getName()));
		assertThat(userget1.getPassword(), is(user1.getPassword()));
		
		User userget2 = dao.get(user2.getId());
		assertThat(userget2.getName(), is(user2.getName()));
		assertThat(userget2.getPassword(), is(user2.getPassword()));
			
	}
}
