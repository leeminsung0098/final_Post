package javaproject.project.Controller;


import jakarta.validation.Valid;
import javaproject.project.CreateForm.BusinessCreateForm;
import javaproject.project.CreateForm.Email.EmailPostDto;
import javaproject.project.CreateForm.Email.LoginIdAndEmailPostDto;
import javaproject.project.CreateForm.UserCreateForm;
import javaproject.project.Entity.Board;
import javaproject.project.Entity.User_T;
import javaproject.project.Repository.UserRepository;
import javaproject.project.Service.BoardService;
import javaproject.project.Service.EmailService;
import javaproject.project.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;

//구글 api를 사용하기위한 추가 컨트롤러
//https://console.cloud.google.com/apis/credentials?hl=ko&project=evocative-tube-392908
//https://chb2005.tistory.com/182
//https://chb2005.tistory.com/173
@RequestMapping(value ="/user",produces = "application/json")
@RequiredArgsConstructor
@Controller
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final User_T user_t;
    private final BoardService boardService;



    @GetMapping("/login")
//    public ModelAndView userLogin(){
    public String userLogin(){
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("Login/login_main");
        return "Login/login_main";
    }

    @GetMapping("/id_find")
    public String findId(EmailPostDto emailPostDto){
        return "/Login/id_find";
    }

//모델방식때문에 수정해야함 userRepository는 서비스에서만 사용
//    이메일전송으로 처리함으로  하단 post는 사용하지않음
    @PostMapping("/id_find")
    public ModelAndView findId(@RequestParam String email) {
        // 이메일을 이용하여 아이디를 조회
        Optional<User_T> _user = this.userRepository.findByEmail(email);
        if (_user.isEmpty()) {
            throw new UsernameNotFoundException("사용자를 찾을수 없습니다.");
        }
        User_T user =_user.get();

        ModelAndView modelAndView = new ModelAndView();
        if (user != null) {
            modelAndView.setViewName("/Login/id-found_test"); // 아이디를 찾은 뷰 이름
            modelAndView.addObject("loginId", user.getId()); // 아이디를 뷰로 전달
        } else {
            modelAndView.setViewName("/Login/id-not-found_test"); // 아이디를 찾지 못한 경우의 뷰 이름
        }
        return modelAndView;
    }

    @GetMapping("/Password_Find")
    public String user_Password_Find(LoginIdAndEmailPostDto loginIdAndEmailPostDto){

        return "Login/Password_Find";
    }
//    회원가입 들어올때 메서드
    @GetMapping("/Initiation_General")
    public  String  user_Initiation_General(UserCreateForm userCreateForm){
        return "Login/initiation_general";
    }
    //    회원가입 Post맵핑
    @PostMapping("/Initiation_General")
    public String Initiation_GeneralPost(@Valid UserCreateForm userCreateForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "Login/initiation_general";
        }

        if (!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect",
                    "2개의 패스워드가 일치하지 않습니다.");
            return "Login/initiation_general";
        }

        userService.create(userCreateForm.getLoginId(),
                userCreateForm.getEmail(), userCreateForm.getPassword1(),userCreateForm.getNickname());

        return "redirect:/UserBoard/main";
    }

//    아이디중복체크 ajax방식으로 교환
    @RequestMapping(value ="/idCheck",method =RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> idCheck(@RequestParam Map<String, Object> map){
        String s= (String)map.get("loginId");
//        System.out.println("입력값 확인");
//        System.out.println(s);
//        System.out.println(userService.checkLoginIdDuplicate(s));
        if (userService.checkLoginIdDuplicate(s)) {
            return ResponseEntity.ok("DUPLICATED");
        } else {
            return ResponseEntity.ok("AVAILABLE");
        }
    }

    //    사업자전환 메서드 시작
    @GetMapping("/Business_General")
    public  String  user_Initiation_General2(BusinessCreateForm businessCreateForm){
        return "Login/business_conversion";
    }
    //    회원가입 Post맵핑
    @PostMapping("/Business_General")
    public String initiationGeneralPost2(@Valid BusinessCreateForm businessCreateForm, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "Login/business_conversion";
        }
        User_T user = this.userService.getUser(principal.getName());
        userService.createBusiness(businessCreateForm,user);
        boardService.create("사업자 업체명이 비어있습니다.","사업자 프로필이 현제 비어있습니다.",user,31);

        return "redirect:/UserBoard/main";
    }

    //    사업자전환 메서드 끝

    //유저성향
    @GetMapping("/tendency_1")
    public String tendency_result1(){
        return "/UserBoard/tendency_detail";
    };
    @GetMapping("/tendency_2")
    public String tendency_result2(){
        return "/UserBoard/tendency_detail1";
    };
    @GetMapping("/tendency_3")
    public String tendency_result3(){
        return "/UserBoard/tendency_detail2";
    };
    @GetMapping("/tendency_4")
    public String tendency_result4(){
        return "/UserBoard/tendency_detail3";
    };
    @GetMapping("/tendency_5")
    public String tendency_result5(){
        return "/UserBoard/tendency_detail4";
    };
    @GetMapping("/tendency_6")
    public String tendency_result6(){
        return "/UserBoard/tendency_detail5";
    };
    //유저성향끝
}



