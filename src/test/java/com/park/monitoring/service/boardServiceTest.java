package com.park.monitoring.service;

import com.park.monitoring.mapper.BoardMapper;
import com.park.monitoring.model.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@ActiveProfiles("local")
public class boardServiceTest {

//    @Autowired
//    BoardMapper boardMapper;
//    BoardService boardService;
//    Long testId;
//    @BeforeEach
//    void init(){
//        boardService = new BoardService(boardMapper);
//        testId = boardService.getNextAutoIncrementNum();
//
//        Board board1 = new Board.Builder()
//                .id(testId)
//                .title("제목1")
//                .content("내용1")
//                .build();
//        boardMapper.insertBoard(board1);
//
//        Board board2 = new Board.Builder()
//                .id(testId+1)
//                .title("제목2")
//                .content("내용2")
//                .build();
//        boardMapper.insertBoard(board2);
//    }
//
//    @Test
//    @DisplayName("Test getBoardById")
//    public void GetBoardAllTest() {
//        List<Board> boards = boardService.getAllBoards();
//
//        assertThat(boards).isNotNull();
//        assertEquals(2, boards.size());
//    }
//
//    @Test
//    @DisplayName("Test getBoardById")
//    public void GetBoardByIdTest() {
//
//        Board board = boardService.getBoardById(testId);
//        assertNotNull(board);
//        assertThat((board.getTitle())).isEqualTo("제목1");
//        assertThat((board.getContent())).isEqualTo("내용1");
//    }
//
//    @Test
//    @DisplayName("Test insertBoard")
//    public void InsertBoardTest() {
//        long testId = boardService.getNextAutoIncrementNum();
//
//        Board board = new Board.Builder()
//                .id(testId)
//                .title("제목3")
//                .content("내용3")
//                .build();
//
//        boardService.insertBoard(board);
//        Board getboard = boardService.getBoardById(testId);
//        assertNotNull(getboard);
//        assertThat((getboard.getTitle())).isEqualTo("제목3");
//        assertThat((getboard.getContent())).isEqualTo("내용3");
//    }
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