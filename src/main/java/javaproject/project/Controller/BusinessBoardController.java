package javaproject.project.Controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import javaproject.project.CreateForm.BoardCreateForm;
import javaproject.project.CreateForm.CommentForm;
import javaproject.project.Entity.Board;
import javaproject.project.Entity.Comment;
import javaproject.project.Entity.User_T;
import javaproject.project.Service.BoardService;
import javaproject.project.Service.CommentService;
import javaproject.project.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

//보드맵은 게시판마다 분류하기위한 용도
//11= 유저게시판의리뷰         21 사업자 부분공사
//12 = 유저게시판의 질문       22 사업자 전체공사
//13 = 유저게시판의 팁
//14 = 유저게시판의 자유게시판
@RequestMapping("/BusinessBoard")
@RequiredArgsConstructor
@Controller
public class BusinessBoardController {

    private final BoardService boardService;
    private final CommentService commentService;
    private final UserService userService;


    // === 전체공사 게시판 시작===
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/Create_BusinessBoard_integral")
    public String showCreateBusinessBoardFormIntegral(Model model,
                                                      Principal principal,
                                                      BoardCreateForm boardCreateForm){
        //사업자인지 사용자인지를 확인하기위한 작업
        if (principal != null) {
            User_T user = this.userService.getUser(principal.getName());
            model.addAttribute("user", user);
        }
        return "/Create/Business/Create_BusinessBoard_integral";
    }


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/Create_BusinessBoard_integral")
    public String boardCreateForm(@Valid BoardCreateForm boardCreateForm,
                                  BindingResult bindingResult,
                                  Principal principal ) {
        if (bindingResult.hasErrors()) {
            return "/Create/Business/Create_BusinessBoard_integral";
        }
        User_T user = this.userService.getUser(principal.getName());
        this.boardService.create(
                boardCreateForm.getTitle(),
                boardCreateForm.getContent(),
                user,22);
        return "redirect:/BusinessBoard/integral";
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/Create_BusinessBoard_part")
//    주의; BoardCreateForm를 받는쪽에서 생성하지않으면 html에서 BoardCreateForm는 없는 객체 처리가됨 주의
    public String showCreateBusinessBoardFormpart(Model model,
                                                  Principal principal,
                                                  BoardCreateForm boardCreateForm){
        //사업자인지 사용자인지를 확인하기위한 작업
        if (principal != null) {
            User_T user = this.userService.getUser(principal.getName());
            model.addAttribute("user", user);
        }

        return "/Create/Business/Create_BusinessBoard_part";
    }


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/Create_BusinessBoard_part")
    public String boardCreateFormpart(@Valid BoardCreateForm boardCreateForm,
                                      BindingResult bindingResult,
                                      Principal principal ) {

        if (bindingResult.hasErrors()) {
            return "/Create/Business/Create_BusinessBoard_part";
        }
        User_T user = this.userService.getUser(principal.getName());
        this.boardService.create(
                boardCreateForm.getTitle(),
                boardCreateForm.getContent(),
                user,21);
        return "redirect:/BusinessBoard/part";
    }

    @GetMapping("/integral")
    public String BusinessBoardFormIntegral(Model model,
                                            Principal principal,
                                            HttpSession session,
                                            @RequestParam(value="page", defaultValue="0") int page,
                                            @RequestParam(value = "kw", defaultValue = "") String kw,
                                            @RequestParam(value = "attribute", defaultValue = "createDate") String attribute) {
        //사업자인지 사용자인지를 확인하기위한 작업
        if (principal != null) {
            User_T user = this.userService.getUser(principal.getName());
            model.addAttribute("user", user);
        }
        boolean sortAscending = true;
        Boolean previousSortAscending = (Boolean) session.getAttribute("sortAscending");

        if (previousSortAscending != null) {
            sortAscending = !previousSortAscending; // 이전 정렬 방식을 토글합니다.
        }

        Page<Board> paging = this.boardService.getList(page, kw, 22, attribute, sortAscending);

        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);
        model.addAttribute("attribute", attribute);
        model.addAttribute("sortAscending", sortAscending);
        session.setAttribute("sortAscending", sortAscending); // 세션에 현재 정렬 방식을 저장합니다.
        return "BusinessBoard/integral_board";
    }
    //게시판 api가 확고하게 사용될경우 이것은 게시판마다 만들어지거나 변수 를 주어줘 동적으로 바꿀예정
    @GetMapping("/integral_board_detail/{id}")
    public String BusinessBoard_Board_integral_Board_Detail( Model model,
                                                             Principal principal,
                                                             CommentForm commentForm,
                                                             @PathVariable("id")Integer id,
                                                             @RequestParam(value = "page",defaultValue = "0")int page){
        //사업자인지 사용자인지를 확인하기위한 작업
        if (principal != null) {
            User_T user = this.userService.getUser(principal.getName());
            model.addAttribute("user", user);
        }
        Board board = this.boardService.getBoard(id);
        Page<Comment> paging_Comment=this.commentService.getList(page,board);
//        조회수 늘리기위한 함수추가
        this.boardService.boardViewsCount(board);

        model.addAttribute("board", board);
        model.addAttribute("paging_Comment",paging_Comment);
        //댓글관련 후에 추가해야함
        return "businessBoard/integral_board_detail";
    }
    // === 전체공사 게시판 끝===




    @GetMapping("/home")
    public String business_Board_Main(Model model,
                                      Principal principal){
        //사업자인지 사용자인지를 확인하기위한 작업
        if (principal != null) {
            User_T user = this.userService.getUser(principal.getName());
            model.addAttribute("user", user);
        }
        return "businessBoard/home";
    }


    @GetMapping("/event")
    public String business_Board_Event(Model model,
                                       Principal principal){
        //사업자인지 사용자인지를 확인하기위한 작업
        if (principal != null) {
            User_T user = this.userService.getUser(principal.getName());
            model.addAttribute("user", user);
        }
        return "businessBoard/event";
    }

    // === 부분공사 게시판 시작===
    @GetMapping("/part")
    public String BusinessBoardFormPart(Model model,
                                        Principal principal,
                                        HttpSession session,
                                        @RequestParam(value="page", defaultValue="0") int page,
                                        @RequestParam(value = "kw", defaultValue = "") String kw,
                                        @RequestParam(value = "attribute", defaultValue = "createDate") String attribute) {
        //사업자인지 사용자인지를 확인하기위한 작업
        if (principal != null) {
            User_T user = this.userService.getUser(principal.getName());
            model.addAttribute("user", user);
        }
        boolean sortAscending = true;
        Boolean previousSortAscending = (Boolean) session.getAttribute("sortAscending");

        if (previousSortAscending != null) {
            sortAscending = !previousSortAscending; // 이전 정렬 방식을 토글합니다.
        }

        Page<Board> paging = this.boardService.getList(page, kw, 21, attribute, sortAscending);

        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);
        model.addAttribute("attribute", attribute);
        model.addAttribute("sortAscending", sortAscending);
        session.setAttribute("sortAscending", sortAscending); // 세션에 현재 정렬 방식을 저장합니다.
        return "BusinessBoard/part_board";
    }

    @GetMapping("/part_board_detail/{id}")
    public String BusinessBoardBoardPartBoardDetail(Model model,
                                                    Principal principal,
                                                    CommentForm commentForm,
                                                    @PathVariable("id")Integer id,
                                                    @RequestParam(value = "page",defaultValue = "0")int page){
        //사업자인지 사용자인지를 확인하기위한 작업
        if (principal != null) {
            User_T user = this.userService.getUser(principal.getName());
            model.addAttribute("user", user);
        }
        Board board = this.boardService.getBoard(id);
        Page<Comment> paging_Comment=this.commentService.getList(page,board);
//        조회수 늘리기위한 함수추가
        this.boardService.boardViewsCount(board);

        model.addAttribute("board", board);
        model.addAttribute("paging_Comment",paging_Comment);
        //댓글관련 후에 추가해야함
        return "BusinessBoard/part_board_detail";
    }

//    @GetMapping("")
//    public String business_Board_Quick_Link(){
//    return "";
//    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String BoardDelete(Principal principal, @PathVariable("id") Integer id) {
        Board board = this.boardService.getBoard(id);
        String path="";
        if (!board.getAuthor().getLoginId().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        switch (board.getBoardMap()){
            case 21 :
                path="part";
                break;
            case 22 :
                path = "integral";
                break;
        }
        this.boardService.delete(board);
        return String.format("redirect:/BusinessBoard/%s",path);
    }


    // === 부분공사 게시판 끝===
    //    추천 맵핑 추가시작
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String BoardVote(Principal principal, @PathVariable("id") Integer id){
        Board board = this.boardService.getBoard(id);
        User_T user = this.userService.getUser(principal.getName());
        String path = "";
        switch (board.getBoardMap()){
            case 21:
                path = "part_board_detail";
                break;
            case 22:
                path = "integral_board_detail";
                break;
        }
        this.boardService.vote(board,user);

        return String.format("redirect:/BusinessBoard/%s/%s", path, id);


    }
//    추천맵핑추가 끝

    // === 유저보드데이터 수정 시작

    @PreAuthorize("isAuthenticated()")
    @GetMapping("Board_Modify/{id}")
    public String modifyBoard(Model model,
                              BoardCreateForm boardCreateForm,
                              @PathVariable("id") Integer id,
                              Principal principal) {
        //사업자인지 사용자인지를 확인하기위한 작업
        if (principal != null) {
            User_T user = this.userService.getUser(principal.getName());
            model.addAttribute("user", user);
        }
        Board board = this.boardService.getBoard(id);
        String path="";
        // 수정권한확인 닉네임으로
        if (!board.getAuthor().getLoginId().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        switch (board.getBoardMap()){
            case 21 :
                path="modify_Board_Part";
                break;
            case 22 :
                path = "modify_Board_Integral";
                break;
        }
        // 수정된 내용을 board 객체에 반영
        boardCreateForm.setTitle(board.getTitle());
        boardCreateForm.setContent(board.getContent());

        return String.format("Modify/Business/%s",path) ;
    }
    @PreAuthorize("isAuthenticated()")
    @PostMapping("Board_Modify/{id}")
    public String modifyBoard(@Valid BoardCreateForm boardCreateForm, BindingResult bindingResult,
                              Principal principal, @PathVariable("id") Integer id) {
        Board board = this.boardService.getBoard(id);
        String path="";
        if (!board.getAuthor().getLoginId().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        switch (board.getBoardMap()){
            case 21:
                path = "part_board_detail";
                break;
            case 22:
                path = "integral_board_detail";
                break;

        }
        this.boardService.modify(board, boardCreateForm.getTitle(), boardCreateForm.getContent());

        return String.format("redirect:/BusinessBoard/%s/%s", path, id);
    }
// === 유저보드 데이터 수정 끝
//===== 업체링크 시작
@GetMapping("/map")
public String business_Board_Map(Model model,
                                 Principal principal) {
    //사업자인지 사용자인지를 확인하기위한 작업
    if (principal != null) {
        User_T user = this.userService.getUser(principal.getName());
        model.addAttribute("user", user);
    }
    List<User_T> userList = this.userService.getUserList();
    List<Board> boardList  = this.boardService.getBoardListByBoardMap(31);

    for (int i = 0; i < userList.size(); i++) {
        if (userList.get(i).getBusinessman_address() == null) {
            userList.remove(i);
        }
    }
    Collections.sort(boardList, new Comparator<Board>(){
        @Override
        public int compare(Board board1,Board board2){
            return board1.getAuthor().getId()-board2.getAuthor().getId();
        }
    });
    model.addAttribute("boardList", boardList);
    model.addAttribute("userList", userList);
    return "businessBoard/business_map";
}

    @PreAuthorize("isAuthenticated()")
    @GetMapping("Board_Modify_Map/{id}")
    public String modifyBoardMap(Model model,
                                 Principal principal,
                                 @PathVariable("id") Integer id,
                                 BoardCreateForm boardCreateForm) {
        //사업자인지 사용자인지를 확인하기위한 작업
        if (principal != null) {
            User_T user = this.userService.getUser(principal.getName());
            model.addAttribute("user", user);
        }
        Board board = this.boardService.getBoard(id);
        boardCreateForm.setTitle(board.getTitle());
        boardCreateForm.setContent(board.getContent());
        return "Modify/Business/modify_Board_Map";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("Board_Modify_Map/{id}")
    public String modifyBoardMap(@Valid BoardCreateForm boardCreateForm,
                                 Principal principal, @PathVariable("id") Integer id) {

        Board board = this.boardService.getBoard(id);


        this.boardService.modify(board, boardCreateForm.getTitle(), boardCreateForm.getContent());

        return "redirect:/BusinessBoard/map";
    }


//===== 업체링크 끝
}
