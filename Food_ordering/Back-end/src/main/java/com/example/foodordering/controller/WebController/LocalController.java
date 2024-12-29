package com.example.foodordering.controller.WebController;

import com.example.foodordering.client.MbBank.TransactionHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "")
public class LocalController {

    @Autowired
    private TransactionHistory transactionHistory;



//    @GetMapping("")
//    public ResponseEntity<Resource> getImage() throws MalformedURLException {
//        Path imagePath = Paths.get(imageUploadPath);
//
//        Resource resource = new UrlResource(imagePath.toUri() + "/" + menuItemsRepository.findAllImageUrl(11L));
//        if (resource.exists() && resource.isReadable()) {
//            return ResponseEntity.ok()
//                    .contentType(MediaType.IMAGE_JPEG) // Định dạng của ảnh
//                    .body(resource);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }



    @GetMapping("")
    public String helloF(){
//        System.out.println(transactionHistory.getRequestJson("1","haivuitin"));
        return "Say hello with my team :))";
    }


}
