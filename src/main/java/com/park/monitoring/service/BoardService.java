package com.park.monitoring.service;

import com.park.monitoring.mapper.BoardMapper;
import com.park.monitoring.model.Board;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardService {

    private final BoardMapper boardMapper;

    public BoardService(BoardMapper boardMapper) {
        this.boardMapper = boardMapper;
    }

    public List<Board> getAllBoards() {
        return boardMapper.getAllBoards();
    }

    public Board getBoardById(Long id) {
        return boardMapper.getBoardById(id);
    }

    public void insertBoard(Board board) {
        boardMapper.insertBoard(board);
    }

    public void updateBoard(Board board) {
        boardMapper.updateBoard(board);
    }

    public void deleteBoard(Long id) {
        boardMapper.deleteBoard(id);
    }

    public Long getNextAutoIncrementNum() {
        return boardMapper.getNextAutoIncrementNum();
    }
}
