package javaproject.project;

import javaproject.project.Entity.Board;
import javaproject.project.Entity.User_T;
import javaproject.project.Repository.BoardRepository;
import javaproject.project.Repository.UserRepository;
import javaproject.project.Service.BoardService;
import javaproject.project.Service.CommentService;
import javaproject.project.Service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
class ProjectApplicationTests {

	@Autowired
	private  UserRepository userRepository;
	@Autowired
	private BoardRepository boardRepository;
	@Autowired
	private BoardService boardService;
	@Autowired
	private CommentService commentService;
@Autowired
private UserService userService;
	@Test
	public void add_entity() {

		User_T d = new User_T();
		d.setNickname("홍길똥");
		d.setPassword("123");
		d.setEmail("123@naver.com");
		d.setBusinessman_num("346g56gwe5s3d5bfg3sf");
		d.setBusinessman_site("55fsf4f5@naver.com");
		this.userRepository.save(d);

		User_T a = new User_T();
		a.setNickname("강길동");
		a.setPassword("123");
		a.setEmail("6721@naver.com");
		a.setBusinessman_num("dfw5w1fw5fd2x3");
		a.setBusinessman_site("332f23f2@naver.com");
		this.userRepository.save(a);

		User_T c = new User_T();
		c.setNickname("도레미");
		c.setPassword("123");
		c.setEmail("324@naver.com");
		c.setBusinessman_num("324154gfd22dsf");
		c.setBusinessman_site("werxsdf@naver.com");
		this.userRepository.save(c);

//		User_T e = new User_T();
//		e.setNickname("joojsn");
//		e.setPassword("123");
//		e.setEmail("45gd@naver.com");
//		e.setBusinessman_num("324154gf12d22dsf");
//		e.setBusinessman_site("wer34xsdf@naver.com");
//		this.userService.create(e);

		User_T as = new User_T();
		as.setNickname("55oosoon");
		as.setPassword("123");
		as.setEmail("3g2d@naver.com");
		as.setBusinessman_num("32554gf8182dsf");
		as.setBusinessman_site("wer45df@naver.com");
		this.userRepository.save(as);

		User_T as1 = new User_T();
		as1.setNickname("komoon");
		as1.setPassword("123");
		as1.setEmail("3g456df2d@naver.com");
		as1.setBusinessman_num("325dff8182dsf");
		as1.setBusinessman_site("we56rf@naver.com");
		this.userRepository.save(as1);

	}
	@Test
	public void dataidpirnt(){
		Optional<User_T> ou=userRepository.findById(1);
		if(ou.isPresent()) {
			User_T u = ou.get();
			System.out.println(u.getNickname());
		}
	}

	@Test
	public void add_entity_board(){
		Optional<User_T> ou=userRepository.findById(2);
		User_T u;
		if(ou.isPresent()) {
			u=ou.get();
			for(int i = 0; i<=40; i++){
				Board b = new Board();
				boardService.create(String.format("자유게시판 번째"+i+"타이틀"),String.format("자유게시판 번째"+i+"내용"),u,14);
			}
		}
	}
	@Test
	public  void add_entity_board_test(){
		Optional<User_T> ou=userRepository.findById(2);
		User_T u;
		if(ou.isPresent()){
			u=ou.get();
//			boardService.create("타이틀 테스트","내용 테스트",u);
		}
	}
	@Test
	public void allPrint(){
		List<Board> b =boardRepository.findAll();
		System.out.println(b.get(1).getTitle());

	}
	@Test
	public void add_entityComment(){
		Optional<User_T> ou=userRepository.findById(2);
		Optional<Board> ob =boardRepository.findById(258);
		User_T u;
		Board b;

		if(ou.isPresent()) {
			if(ob.isPresent()){
				b=ob.get();
				u=ou.get();
				for(int i = 0; i<=10; i++){
					commentService.create(b,String.format("댓글 번째"+i+"내용"),u);
				}

			}
		}


	}



}
