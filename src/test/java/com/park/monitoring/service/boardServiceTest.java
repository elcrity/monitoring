package com.park.monitoring.service;

import com.park.monitoring.mapper.BoardMapper;
import com.park.monitoring.model.Board;
import jakarta.xml.bind.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.net.BindException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@ActiveProfiles("local")
@Sql({"classpath:testTable.sql", "classpath:testData.sql"})
public class boardServiceTest {

    @Autowired
    BoardMapper boardMapper;
    BoardService boardService;

    @BeforeEach
    void init() {
        boardService = new BoardService(boardMapper);
    }

    @Test
    @DisplayName("Test getBoardAll")
    public void GetBoardAllTest() {
        List<Board> boards = boardService.getAllBoards();

        assertThat(boards).isNotNull();
        assertEquals(5, boards.size());
    }

    @Test
    @DisplayName("Test getBoardById")
    public void GetBoardByIdTest() {
        Board board = boardService.getBoardById(1L);
        assertNotNull(board);
        assertThat((board.getTitle())).isEqualTo("제목1");
        assertThat((board.getContent())).isEqualTo("내용1");
        board = boardService.getBoardById(10L);
        assertNull(board);
    }

    @Test
    @DisplayName("Test insertBoard")
    public void InsertBoardTest() {
        //given
        Board board = new Board.Builder()
                .title("제목9")
                .content("내용9")
                .build();

        //when
        boardService.insertBoard(board);
        Board getboard = boardService.getBoardById(6L);

        //then
        assertNotNull(getboard);
        assertThat((getboard.getTitle())).isEqualTo("제목9");
        assertThat((getboard.getContent())).isEqualTo("내용9");

        //given
        board = new Board.Builder()
                .title(null)
                .content(null)
                .build();
        //when
        Board errBoard = board;
        Throwable thrown = assertThrows(DataIntegrityViolationException.class, () ->{
            boardService.insertBoard(errBoard);
        });

        assertInstanceOf(SQLIntegrityConstraintViolationException.class, thrown.getCause());
        assertTrue(thrown.getMessage().contains("'title' cannot be null"));

//        unique, key값 중복일때 테스트

//        타입 미스매치일때 테스트
    }
//
//    @Test
//    @DisplayName("Test updateBoard")
//    public void UpdateBoardTest() {
//        // Given
//        Board board = new Board.Builder()
//                .id(testId)
//                .title("수정된 제목")
//                .content("수정된 내용")
//                .build();
//
//        // When
//        boardService.updateBoard(board);
//
//        // Then
//        Board getboard = boardService.getBoardById(testId);
//        assertNotNull(getboard);
//        assertThat((getboard.getTitle())).isEqualTo("수정된 제목");
//        assertThat((getboard.getContent())).isEqualTo("수정된 내용");
//    }
//
//    @Test
//    @DisplayName("Test deleteBoard")
//    public void DeleteBoardTest() {
//        // Given
//        Long testId = 1L;
//
//        // When
//        boardService.deleteBoard(testId);
//
//        // Then
//        Board deletedBoard = boardMapper.getBoardById(testId);
//        assertNull(deletedBoard); // 삭제 후에는 null이어야 함
//    }
}