package opp.mic.cms.controller;

import jakarta.servlet.annotation.MultipartConfig;
import opp.mic.cms.service.AppImageDetailsService;
import opp.mic.cms.util.PhotoStorageLocation;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.zip.DataFormatException;

@RestController
@RequestMapping("/api/photos")
@MultipartConfig
public class AppImageController {

    private AppImageDetailsService appImageDetailsService;
    private PhotoStorageLocation storageLocation;

    public AppImageController(AppImageDetailsService appImageDetailsService, PhotoStorageLocation storageLocation) {
        this.appImageDetailsService = appImageDetailsService;
        this.storageLocation = storageLocation;
    }

    @PostMapping
    public void handlePhoto(@RequestParam("file") MultipartFile file){
        try {
            appImageDetailsService.uploadPhoto(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    @GetMapping("/{filename:..+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) throws DataFormatException {
        Resource image = appImageDetailsService.getResource(storageLocation.getLocation()+"/"+filename);
        if(image == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment;filename=\""+image.getFilename()+"\"").body(image);
    }





}
