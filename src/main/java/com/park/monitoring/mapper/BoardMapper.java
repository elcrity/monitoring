package com.park.monitoring.mapper;

import com.park.monitoring.model.Board;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoardMapper {

    List<Board> getAllBoards();
    Board getBoardById(Long id);
    void insertBoard(Board board);
    void updateBoard(Board board);
    void deleteBoard(Long id);
    Long getNextAutoIncrementNum();
}
