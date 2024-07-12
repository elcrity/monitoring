package com.park.monitoring.controller;

import com.park.monitoring.model.Board;
import com.park.monitoring.service.BoardService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = boardController.class)
public class boardControllerTest {
//    @Autowired
//    MockMvc mvc;
//
//    @MockBean
//    BoardService boardService;
//
//    @Test
//    @DisplayName("게시글 전체 조회 test")
//    void getBoardAll() throws Exception {
//        // Given
//        List<Board> boards = new ArrayList<>();
//        boards.add(new Board.Builder()
//                .id(1L)
//                .title("제목1")
//                .content("내용1")
//                .build());
//
//        // Mock service method to return mock data
//        when(boardService.getAllBoards()).thenReturn(boards);
//
//        // When & Then
//        mvc.perform(MockMvcRequestBuilders.get("/board"))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(MockMvcResultMatchers.view().name("board")) // View 이름이 "board"인지 확인
//                .andExpect(MockMvcResultMatchers.model().attributeExists("boards")); // Model에 "boards" 속성이 존재하는지 확인
//    }
//
//    @Test
//    @DisplayName("게시글 상세 조회 test")
//    void getBoardById() throws Exception {
//        // Given
//        Long testId = 1L;
//        Board testBoard = new Board.Builder()
//                .id(testId)
//                .title("제목1")
//                .content("내용1")
//                .build();
//
//        // Mock BoardService의 getBoardById() 메서드가 호출될 때 정해진 testBoard 객체를 반환하도록 설정
//        when(boardService.getBoardById(testId)).thenReturn(testBoard);
//
//        // When & Then
//        mvc.perform(MockMvcRequestBuilders.get("/" + testId))
//                .andExpect(MockMvcResultMatchers.status().isOk()) // HTTP 응답 상태가 200인지 확인
//                .andExpect(MockMvcResultMatchers.view().name("detail")) // 뷰 이름이 "detail"인지 확인
//                .andExpect(MockMvcResultMatchers.model().attributeExists("board")) // "board" 모델 속성이 존재하는지 확인
//                .andExpect(MockMvcResultMatchers.model().attribute("board", testBoard)); // "board" 모델 속성의 값이 testBoard와 일치하는지 확인
//    }
//
//    @Test
//    @DisplayName("게시글 등록 폼 조회 test")
//    void showBoardForm() throws Exception {
//        Long testId = 1L;
//        Board testBoard = new Board.Builder()
//                .id(testId)
//                .title("제목1")
//                .content("내용1")
//                .build();
//
//        // Mock BoardService의 getBoardById() 메서드가 호출될 때 정해진 testBoard 객체를 반환하도록 설정
//        when(boardService.getBoardById(testId)).thenReturn(testBoard);
//
//        mvc.perform(MockMvcRequestBuilders.get("/form"))
//                .andExpect(MockMvcResultMatchers.status().isOk()) // HTTP 응답 상태가 200인지 확인
//                .andExpect(MockMvcResultMatchers.view().name("write")) // 뷰 이름이 "Form"인지 확인
//                .andExpect(MockMvcResultMatchers.model().attributeExists("board")); // "board" 모델 속성이 존재하는지 확인
//    }
//
//    @Test
//    @DisplayName("게시글 등록 test")
//    void saveBoard() throws Exception {
//        // Given
//        Board board = new Board.Builder()
//                .title("새로운 제목")
//                .content("새로운 내용")
//                .build();
//
//        // When & Then
//        mvc.perform(MockMvcRequestBuilders.post("/save")
//                        .param("title", board.getTitle())
//                        .param("content", board.getContent()))
//                .andExpect(MockMvcResultMatchers.status().is3xxRedirection()) // HTTP 응답 상태가 3xx 리다이렉션 코드인지 확인 (리다이렉션 확인)
//                .andExpect(MockMvcResultMatchers.redirectedUrl("/board")); // 리다이렉션된 URL이 "/board"인지 확인
//    }
//
//    @Test
//    @DisplayName("게시글 수정 폼 조회 test")
//    void editBoard() throws Exception {
//        // Given
//        Long testId = 1L;
//        Board testBoard = new Board.Builder()
//                .id(testId)
//                .title("제목1")
//                .content("내용1")
//                .build();
//
//        // Mock BoardService의 getBoardById() 메서드가 호출될 때 testBoard 객체를 반환하도록 설정
//        when(boardService.getBoardById(testId)).thenReturn(testBoard);
//
//        // When & Then
//        mvc.perform(MockMvcRequestBuilders.get("/" + testId + "/edit"))
//                .andExpect(MockMvcResultMatchers.status().isOk()) // HTTP 응답 상태가 200인지 확인
//                .andExpect(MockMvcResultMatchers.view().name("write")) // 뷰 이름이 "Form"인지 확인
//                .andExpect(MockMvcResultMatchers.model().attributeExists("board")) // "board" 모델 속성이 존재하는지 확인
//                .andExpect(MockMvcResultMatchers.model().attribute("board", testBoard)); // "board" 모델 속성의 값이 testBoard와 일치하는지 확인
//    }
//
//    @Test
//    @DisplayName("게시글 수정 test")
//    void updateBoard() throws Exception {
//        // Given
//        Long testId = 1L;
//        Board updatedBoard = new Board.Builder()
//                .id(testId)
//                .title("수정된 제목")
//                .content("수정된 내용")
//                .build();
//
//        // When & Then
//        mvc.perform(MockMvcRequestBuilders.post("/" + testId + "/update")
//                        .param("title", updatedBoard.getTitle())
//                        .param("content", updatedBoard.getContent()))
//                .andExpect(MockMvcResultMatchers.status().is3xxRedirection()) // HTTP 응답 상태가 3xx 리다이렉션 코드인지 확인 (리다이렉션 확인)
//                .andExpect(MockMvcResultMatchers.redirectedUrl("/" + testId)); // 리다이렉션된 URL이 "/{id}" 형태인지 확인
//
//    }
//
//    @Test
//    @DisplayName("게시글 삭제 test")
//    void deleteBoard() throws Exception {
//        // Given
//        Long testId = 1L;
//
//        // When & Then
//        mvc.perform(MockMvcRequestBuilders.post("/" + testId + "/delete"))
//                .andExpect(MockMvcResultMatchers.status().is3xxRedirection()) // HTTP 응답 상태가 3xx 리다이렉션 코드인지 확인 (리다이렉션 확인)
//                .andExpect(MockMvcResultMatchers.redirectedUrl("/board")); // 리다이렉션된 URL이 "/board"인지 확인
//
//    }
}
