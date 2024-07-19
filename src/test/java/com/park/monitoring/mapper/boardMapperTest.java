//package com.park.monitoring.mapper;
//
//import com.park.monitoring.model.Board;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.sql.init.SqlInitializationAutoConfiguration;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.test.context.jdbc.Sql;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.*;
//
//@ExtendWith(SpringExtension.class)
//@MybatisTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@Transactional
//@ActiveProfiles("local")
//@Sql({"classpath:testTable.sql", "classpath:testData.sql"})
//public class boardMapperTest {
//
////    private static final Logger log = LoggerFactory.getLogger(boardMapperTest.class);
////    @Autowired
////    private BoardMapper boardMapper;
////
////    private Board board;
////    @Autowired
////    private SqlInitializationAutoConfiguration sql;
////
////    @Test
////    @DisplayName("getAllBoards test")
////    public void getAllBoardsTest() {
////        List<Board> boards = boardMapper.getAllBoards();
////
////        assertNotNull(boards);
////        assertEquals(5, boards.size());
////    }
////
////    @Test
////    @DisplayName("getBoardByID test")
////    public void getBoardTest() throws Exception {
////        Long testId = 2L;
////        log.info("=====getBoardTest==testId======={}", testId);
////
////        Board board = boardMapper.getBoardById(testId);
////        log.info("=====getBoardTest===testId======{}", board);
////        assertNotNull(board);
////        assertThat((board.getTitle())).isEqualTo("제목" + testId);
////        assertThat((board.getContent())).isEqualTo("내용" + testId);
////    }
////
////    @Test
////    @DisplayName("insertBoard Test")
////    public void insertBoardTest() throws Exception {
////        Long testId = 6L;
////        Board board = new Board.Builder()
////                .title("제목"+testId)
////                .content("내용"+testId)
////                .build();
////        boardMapper.insertBoard(board);
////
////        Board getboard = boardMapper.getBoardById(testId);
////
////        assertNotNull(getboard);
////        assertThat((getboard.getTitle())).isEqualTo("제목"+testId);
////        assertThat((board.getContent())).isEqualTo("내용"+testId);
////    }
////
////    @Test
////    public void UpdateBoardTest() {
////        Long testId = 1L;
////        Board board = new Board.Builder()
////                .id(testId)
////                .title("수정된 제목")
////                .content("수정된 내용")
////                .build();
////
////        boardMapper.updateBoard(board);
////
////        Board updatedBoard = boardMapper.getBoardById(testId);
////        assertEquals("수정된 제목", updatedBoard.getTitle());
////        assertEquals("수정된 내용", updatedBoard.getContent());
////    }
////
////    @Test
////    public void DeleteBoardTest() {
////        Long testId = 1L;
////
////        boardMapper.deleteBoard(testId);
////
////        Board deletedBoard = boardMapper.getBoardById(testId);
////        assertNull(deletedBoard); // 삭제 후에는 null이어야 함
////    }
//}