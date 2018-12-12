import static org.junit.Assert.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import com.spring.board.entity.BoardEntity;
import com.spring.board.repository.BoardRepository;

@RunWith(SpringRunner.class)
public class requestTest {
	
	@Autowired
	private BoardRepository testRepo;

	@Test
	public void insertInto() {
		
		testRepo.save(BoardEntity.builder().boardUuid("test")
				.title("테스트")
				.content("name")
				.userName("성주 콘솔")
				.time(LocalDateTime.now())
				.build());
		
		
		  List<BoardEntity> boardEntityList = (List<BoardEntity>) testRepo.findAll();

        //then
		  BoardEntity BoardEntity = boardEntityList.get(0);
	      assertThat(BoardEntity.getTitle(), is("테스트 게시글"));
	      assertThat(BoardEntity.getContent(), is("테스트 본문"));
	}
	
	
		 
	
}
