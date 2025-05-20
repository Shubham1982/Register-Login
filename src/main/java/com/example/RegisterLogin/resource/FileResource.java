package com.example.RegisterLogin.resource;

import com.example.RegisterLogin.payload.FileResponse;
import com.example.RegisterLogin.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("api/file")
public class FileResource {

    @Autowired
    private FileService fileService;

    @Value("${project.image}")
    private String path;

    @PostMapping("/upload/{id}")
    public ResponseEntity<FileResponse> uploadFile(
            @RequestParam("image")MultipartFile image,@PathVariable Long id
            ){
        String fileName = null;
        System.out.println("**********************"+image.getContentType());
        try {
            fileName = fileService.uploadImage(this.path,image,id);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(new FileResponse(null,"Image is not uploaded"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new FileResponse(fileName,"Image successfully uploaded"), HttpStatus.OK);
    }

    @GetMapping("/images/{imageName}")
    public void downloadImage(
            @PathVariable("imageName")String imageName, HttpServletResponse response
    ) throws IOException {
        InputStream resourse = fileService.getResource(this.path,imageName);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resourse,response.getOutputStream());
    }
}
