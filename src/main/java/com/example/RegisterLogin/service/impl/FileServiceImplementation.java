package com.example.RegisterLogin.service.impl;

import com.example.RegisterLogin.domain.AppUser;
import com.example.RegisterLogin.repository.AppUserRepository;
import com.example.RegisterLogin.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Service
public class FileServiceImplementation implements FileService {


    @Autowired
    private AppUserRepository appUserRepository;

    @Override
    public String uploadImage(String path, MultipartFile file, Long id) throws IOException {

        //File name
        String name = file.getOriginalFilename();

        String randomID = UUID.randomUUID().toString();

        String fileName1 = randomID.concat(name.substring(name.lastIndexOf(".")));
        //Full path
        String filePath = path + File.separator + fileName1;

        //Create folder if not created
        File f = new File(path);
        if(!f.exists()){
            f.mkdir();
        }
        Optional<AppUser> appUser = appUserRepository.findById(id);

        AppUser appUser1 = appUser.get();
        appUser1.setImage(fileName1);
        appUserRepository.save(appUser1);
        //Copy file in the directory
        Files.copy(file.getInputStream(), Paths.get(filePath));
        return fileName1;
    }

    @Override
    public InputStream getResource(String path, String fileName) throws FileNotFoundException {
        String fullPath = path+ File.separator+fileName;
        InputStream is = new FileInputStream(fullPath);
        return is;
    }
}
