package com.example.RegisterLogin.resource;
import com.example.RegisterLogin.loginresponse.LoginResponse;
import com.example.RegisterLogin.service.AppUserService;
import com.example.RegisterLogin.service.dto.AppUserDTO;
import com.example.RegisterLogin.service.dto.LoginDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@EnableCaching
@RequestMapping("/api")
public class AppUserResource {

    @Autowired
    private AppUserService appUserService;


    @PostMapping(path = "/save")
    public String saveUser(@RequestBody AppUserDTO appUserDTO){
        String name = appUserService.addUser(appUserDTO);
        return name;
    }
    @PostMapping(path = "/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO){
        LoginResponse loginResponse= appUserService.login(loginDTO);
        return ResponseEntity.ok(loginResponse);
    }
    @GetMapping(path = "/user-all")
    public ResponseEntity<Page<AppUserDTO>> getAllUser(Pageable pageable, @RequestParam(required = false) String searchKey,@RequestParam(required = false) Long id){
        long startTime = System.currentTimeMillis();
        Page<AppUserDTO> page = appUserService.getAllUser(pageable,searchKey,id);
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        System.out.println("API execution time: " + executionTime + " milliseconds");
       return ResponseEntity.ok().body(page);
    }

}
