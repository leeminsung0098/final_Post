package javaproject.project.Controller;

import javaproject.project.Entity.Board;
import javaproject.project.Entity.User_T;
import javaproject.project.Service.BoardService;
import javaproject.project.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@RequestMapping("/Store")
@RequiredArgsConstructor
@Controller
public class StoreController {
    private final UserService userService;
    private final BoardService boardService;


    @GetMapping("/")
    public String store_Main(Model model, Principal principal) {
        List<Board> boardList = this.boardService.getBoardListByBoardMap(31);
        if (principal != null) {
            User_T user = this.userService.getUser(principal.getName());
            model.addAttribute("user", user);
        }
        Collections.sort(boardList, new Comparator<Board>() {//정렬하기위한 람다
                    @Override
                    public int compare(Board board1, Board board2) {
                        return board1.getAuthor().getId() - board2.getAuthor().getId();
                    }
                });

        model.addAttribute("boardList", boardList);
        // 전달 필요객체는 현제 보드의 내용을 가지고있어야함
        // 두번째는 유저의 사이트주소를 가지고 있어야함
        //보드에 두가지가 저장되어있으니 바로전달
        return "Store/store";
    }
}
