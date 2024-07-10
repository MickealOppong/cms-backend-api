package opp.mic.cms.service;

import lombok.extern.slf4j.Slf4j;
import opp.mic.cms.exceptions.ProductNotFoundException;
import opp.mic.cms.model.*;
import opp.mic.cms.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final AppImageDetailsService appImageDetailsService;
    private final CategoryService categoryService;
    private final AttributesService attributesService;
    private final VendorService vendorService;
    private final ProductVariationService productVariationService;

    public ProductService(ProductRepository productRepository,
                          AppImageDetailsService appImageDetailsService,
                          CategoryService categoryService, AttributesService attributesService,VendorService vendorService,
                          ProductVariationService productVariationService
                          ) {
        this.productRepository = productRepository;
        this.appImageDetailsService = appImageDetailsService;
        this.categoryService = categoryService;
        this.vendorService = vendorService;
        this.attributesService = attributesService;
        this.productVariationService = productVariationService;
    }

    @Transactional
    public Product save(ProductRequest productRequest, MultipartFile[] files) throws IOException {

        Product product = new Product(productRequest.name(), productRequest.description(), productRequest.features(),
                productRequest.quantity(), BigDecimal.valueOf(productRequest.regularPrice()),BigDecimal.valueOf(productRequest.salePrice()));

        Optional<Vendor> vendor = vendorService.getVendorByName(productRequest.vendorName());
        if(vendor.isPresent()){
            log.info("in");
           return doPersistData(productRequest,files,vendor.get(),product);
        }else{
            log.info("out");
            Vendor newVendor = new Vendor(productRequest.vendorName(), productRequest.vendorAddress(),
                    productRequest.vendorTelephone(), productRequest.vendorEmail());
            Vendor saveVendor = vendorService.save(newVendor);
           return doPersistData(productRequest,files,saveVendor,product);
        }
    }


    @Transactional
    public Product updateProductData(Long id,ProductUpdateRequest productUpdateRequest){
    Product savedProduct = productRepository.findById(id)
            .orElseThrow(()->new ProductNotFoundException("Could not retrieve product with "+id));

    savedProduct.setName(productUpdateRequest.name());
    savedProduct.setDescription(productUpdateRequest.description());
    savedProduct.setFeatures(productUpdateRequest.features());
    savedProduct.setQuantity(productUpdateRequest.quantity());
    savedProduct.setRegularPrice(BigDecimal.valueOf(productUpdateRequest.regularPrice()));
    savedProduct.setSalePrice(BigDecimal.valueOf(productUpdateRequest.salePrice()));
        Arrays.stream(productUpdateRequest.attributes()).forEach(s->{
            productVariationService.update(s.recId(),s.quantity(),s.price());
        });
        productRepository.save(savedProduct);
       return savedProduct;
    }

    private Product doPersistData(ProductRequest productRequest, MultipartFile[] files,Vendor vendor,Product product){
        try{
            //save product in repository
        Product savedProduct= productRepository.save(product);

        //create and save vendor or supplier if does not exist
        VendorTransaction vendorTransaction = new VendorTransaction(productRequest.invoiceId(), vendor,savedProduct, productRequest.quantity(), BigDecimal.valueOf(productRequest
                .purchasePrice()));
        vendorService.saveTransaction(vendorTransaction);

        Arrays.stream(productRequest.attributes()).forEach(s->{
            productVariationService.save(s.title(), Arrays.stream(s.list()).toList(),savedProduct);
        });
        Arrays.stream(productRequest.categories()).map(CategoryRequest::value).forEach(f->categoryService.saveCategory(f,savedProduct));

        //save product images
        appImageDetailsService.saveProductImages(savedProduct.getId(), files);

        return savedProduct;
        }catch(IOException e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public ProductsPaginationResponse all(int page,String searchItem,int pageSize){
        List<Product> productList;
        PageRequest pageRequest = PageRequest.of(page,pageSize);
        Page<Product> products = productRepository.findAll(pageRequest);
      if(searchItem == null || searchItem==""){
          productList = products.stream().toList();
      }else{
          productList = new ArrayList<>(products.stream().filter(f -> f.getName().contains(searchItem)).toList());
      }
      ProductsPaginationResponse productsPaginationResponse = ProductsPaginationResponse.builder()
              .size(products.getTotalElements())
              .PageCount(products.getTotalPages())
              .page(page)
              .products(productList)
              .pageSize(pageSize)
              .build();
      for(Product product :products){
          product.setImages(appImageDetailsService.getAll(product.getId()));
      }
      return productsPaginationResponse;
    }


    public SingleProductDTO singleProduct(Long id){
        Map<String,List<ProductVariation>> map = new HashMap<>();
       Product product= productRepository.findById(id)
               .orElseThrow(()->new ProductNotFoundException("Product does not exist"));
      List<ProductVariation> productVariationList =productVariationService.getAllByProductId(product.getId());
     for(ProductVariation variation : productVariationList){
       map.put(variation.getName(), productVariationList.stream().filter(f->f.getName().equals(variation.getName())).toList());
     }
       product.setImages(appImageDetailsService.getAll(product.getId()));
        SingleProductDTO singleProductDTO = new SingleProductDTO(product,map);
       return singleProductDTO;
    }


}
