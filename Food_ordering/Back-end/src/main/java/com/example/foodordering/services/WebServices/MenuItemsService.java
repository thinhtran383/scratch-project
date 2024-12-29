package com.example.foodordering.services.WebServices;

import com.example.foodordering.DTO.ResponseObject;
import com.example.foodordering.entity.MenuItem;
import com.example.foodordering.repositories.MenuItemsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class MenuItemsService {

    @Value("${image.upload.path}")
    private String imageUploadPath;

    @Autowired
    private MenuItemsRepository menuItemRepository;

//    public ResponseEntity<ResponseObject> addNewItem(@RequestBody MenuItem menuItem) {
//        List<MenuItem> isExist = menuItemRepository.findMenuItemByName(menuItem.getName().trim());
//
//        if (!isExist.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
//                    new ResponseObject("false", "Item is existed", ""));
//        } else {
//            return ResponseEntity.status(HttpStatus.OK).body(
//                    new ResponseObject("ok", "Query successfully", menuItemRepository.save(menuItem)));
//        }
//    }

    public boolean addNewItem(MenuItem menuItem){
        List<MenuItem> isExist = menuItemRepository.findMenuItemByName(menuItem.getName().trim());

        if(!isExist.isEmpty()) {
            return false;
        } else {
            menuItemRepository.save(menuItem);
            return true;
        }
    }

    public List<MenuItem> getAllMenu() {
        return menuItemRepository.findAll();
    }

    public Resource getImageById(Long id) throws MalformedURLException {
        Path imagePath = Paths.get(imageUploadPath);

        Resource resource = new UrlResource(imagePath.toUri() + "/" + menuItemRepository.findAllImageUrl(id));

        if(resource.exists() && resource.isReadable()){
            return resource;
        }

        return null;
    }

}
