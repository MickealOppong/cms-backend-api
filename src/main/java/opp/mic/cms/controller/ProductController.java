package opp.mic.cms.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import opp.mic.cms.model.*;
import opp.mic.cms.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Slf4j
@RestController
@RequestMapping("/api/products")
public class ProductController {


    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public ProductsPaginationResponse list(@RequestParam(required = false) Integer page,@RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) String name){
        SearchParameters searchParameters = new SearchParameters(name,page,pageSize);
        return productService.all(searchParameters.getPage(),searchParameters.getName(), searchParameters.getPageSize());
    }

    @GetMapping("/product/{id}")
    public SingleProductDTO product(@RequestParam Long id){
        return productService.singleProduct(id);
    }
    @PostMapping("/product")
    public ResponseEntity<String> addProduct(@RequestParam String name, @RequestParam String description,
                                             @RequestParam Long quantity,@RequestParam String features,
                                              @RequestParam double regularPrice,@RequestParam String attributes,
                                             @RequestParam double salePrice,@RequestParam  String categories,
                                             @RequestParam(required = false) String vendorName,@RequestParam(required = false) String vendorEmail,
                                             @RequestParam(required = false) String vendorTelephone,@RequestParam(required = false) String vendorAddress,
                                            @RequestParam double purchasePrice,@RequestParam String invoiceId,
                                             @RequestParam(required = false) MultipartFile[] files) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        AttributeRequest[] attributeList = objectMapper.readValue(attributes,AttributeRequest[].class);

        CategoryRequest[] categoryList = objectMapper.readValue(categories,CategoryRequest[].class);

        ProductRequest productRequest =
                new ProductRequest(name,description,features,categoryList,attributeList,salePrice,quantity,
                        regularPrice,vendorName, vendorEmail,vendorTelephone,
                        vendorAddress, purchasePrice, invoiceId);
        Product product = productService.save(productRequest,files);
       if(product != null){
           return ResponseEntity.ok().body("Product created");
       }

        return ResponseEntity.ok().body("Could not create product");
    }


    @PatchMapping("/product/{id}")
    public ResponseEntity<String> updateProduct(@RequestParam Long id,@RequestParam String name, @RequestParam String description,
                                             @RequestParam Long quantity,@RequestParam String features,
                                             @RequestParam double regularPrice,
                                             @RequestParam double salePrice,
                                             @RequestParam String attributes) throws IOException {

    log.info(name,description);

        ObjectMapper objectMapper = new ObjectMapper();
        AttributeUpateRequest[] attributeList = objectMapper.readValue(attributes,AttributeUpateRequest[].class);

        ProductUpdateRequest productUpdateRequest =
                new ProductUpdateRequest(name,description,features,attributeList,salePrice,quantity,
                        regularPrice);
        Product product = productService.updateProductData(id,productUpdateRequest);
        if(product != null){
            return ResponseEntity.ok().body("Product updated");
        }

        return ResponseEntity.ok().body("Could not update product");
    }

}
