package opp.mic.cms.impl;

import opp.mic.cms.exceptions.PhotoStorageException;
import opp.mic.cms.exceptions.PhotoStorageNotFoundException;
import opp.mic.cms.interfaces.PhotoStorageService;
import opp.mic.cms.util.PhotoStorageLocation;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

@Service
public class PhotoStorageServiceImpl implements PhotoStorageService {


    private Path root;

    private PhotoStorageLocation photoStorageLocation;

    public PhotoStorageServiceImpl(PhotoStorageLocation photoStorageLocation){
        this.photoStorageLocation = photoStorageLocation;

        if(photoStorageLocation.getLocation().trim().length()==0){
            throw new PhotoStorageException("Could not initialize empty folder");
        }
        root = Paths.get(photoStorageLocation.getLocation());

    }

    @Override
    public void init(){
        try{
            if (!Files.exists(root)){
                Files.createDirectories(root);
            }
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void store(MultipartFile file,String username) {
        try{
            if(file.isEmpty()){
                throw new PhotoStorageException("Failed to store empty file");
            }

            Path destination = root.resolve(Paths.get(username+"-"+file.getOriginalFilename())).normalize().toAbsolutePath();
            if(!destination.getParent().equals(this.root.toAbsolutePath())){
                throw new PhotoStorageException("File cannot be stored outside the current directory");
            }
            try(InputStream inputStream = file.getInputStream()){
                Files.copy(inputStream,destination, StandardCopyOption.REPLACE_EXISTING);
            }
        }catch (IOException e){
            throw new PhotoStorageException("Failed to store file");
        }
    }

    @Override
    public Stream<Path> loadAll(String filename){
        try {
            return Files.walk(root,1).filter(file->!file.equals(root)).map(
                    this.root::relativize);
        } catch (IOException e) {
            throw new PhotoStorageException("Failed to read files",e);
        }

    }


    @Override
    public Path toPath(String filename) {
        return Path.of(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = toPath(filename);
            Resource resource = new UrlResource(file.toUri());
            if(resource.exists() || resource.isReadable()){
                return resource;
            }else{
                throw new PhotoStorageNotFoundException("Count not read file "+filename);
            }
        } catch (MalformedURLException e) {
            throw new PhotoStorageNotFoundException("Count not read file"+filename,e);
        }
    }

    @Override
    public void delete(String file) {
        Path path = toPath(file);
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        PhotoStorageServiceImpl p = new PhotoStorageServiceImpl(new PhotoStorageLocation());
        System.out.println(p.loadAsResource("users-photo/epps-img-3.jpeg"));
    }
}
