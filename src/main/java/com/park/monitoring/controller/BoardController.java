package com.park.monitoring.controller;

import com.park.monitoring.model.Board;
import com.park.monitoring.service.BoardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/board")
public class BoardController {

    private static final Logger log = LoggerFactory.getLogger(BoardController.class);
    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping()
    public String getAllBoards(Model model) {
            List<Board> boards = boardService.getAllBoards();
            model.addAttribute("boards", boards);
            return "board";
    }

    @GetMapping("/{id}")
    public String getBoardById(@PathVariable Long id, Model model) {
        Board board = boardService.getBoardById(id);
        model.addAttribute("board", board);
        return "detail";
    }
//
//    @GetMapping("/form")
//    public String showBoardForm(Model model) {
//        Board board = new Board.Builder()
//                .id(null)
//                .title("")
//                .content("")
//                .build();
//        model.addAttribute("board", board);
//        return "write";
//    }
//
//    @PostMapping("/save")
//    public String saveBoard(@ModelAttribute Board board) {
//        long nextId = boardService.getNextAutoIncrementNum();
//        board.setId(nextId);
//        boardService.insertBoard(board);
//        return "redirect:/board";
//    }
//
//    @GetMapping("/{id}/edit")
//    public String editBoard(@PathVariable Long id, Model model) {
//        Board board = boardService.getBoardById(id);
//        model.addAttribute("board", board);
//        return "write";
//    }
//
//    @PostMapping("/{id}/update")
//    public String updateBoard(@PathVariable Long id, @ModelAttribute Board board) {
//        board.setId(id);
//        boardService.updateBoard(board);
//        return "redirect:/" + id;
//    }
//
//    @PostMapping("/{id}/delete")
//    public String deleteBoard(@PathVariable Long id) {
//        boardService.deleteBoard(id);
//        return "redirect:/board";
//    }
}