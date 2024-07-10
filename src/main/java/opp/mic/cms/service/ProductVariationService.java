package opp.mic.cms.service;

import lombok.extern.slf4j.Slf4j;
import opp.mic.cms.exceptions.ProductVariationNotFoundException;
import opp.mic.cms.model.Attributes;
import opp.mic.cms.model.Product;
import opp.mic.cms.model.ProductSKU;
import opp.mic.cms.model.ProductVariation;
import opp.mic.cms.repository.ProductSKURepository;
import opp.mic.cms.repository.ProductVariationsRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
public class ProductVariationService {

    private final ProductVariationsRepository variationsRepository;
    private final AttributesService attributesService;
    private final ProductSKURepository productSKURepository;


    public ProductVariationService(ProductVariationsRepository variationsRepository,
                                   AttributesService attributesService,ProductSKURepository productSKURepository) {
        this.variationsRepository = variationsRepository;
        this.attributesService = attributesService;
        this.productSKURepository = productSKURepository;
    }

    public void save(String attributeName, List<String> skuList, Product product){

            Attributes attribute = attributesService.getByName(attributeName);
            for(String sku :skuList){
                ProductSKU productSKU = productSKURepository.findBySkuValue(sku).orElse(null);
              ProductVariation  productVariation = new ProductVariation(attribute.getName(),productSKU.getDescription(), productSKU.getSkuValue(), 0L,
                        BigDecimal.valueOf(0),product);
                ProductVariation pv = variationsRepository.save(productVariation);
                log.info(pv.getRecId()+" Prod "+ product.getId());
            }
    }
    public ProductVariation update(Long id,Long quantity,Double price){


            ProductVariation  productVariation = variationsRepository.findById(id)
                    .orElseThrow(()->new ProductVariationNotFoundException("Could not find "+id));
           productVariation.setQuantity(quantity);
           productVariation.setPrice(BigDecimal.valueOf(price));
          return variationsRepository.save(productVariation);

    }


    public List<ProductVariation> getAllByProductId(Long id){
       return variationsRepository.findByProductId(id);
    }
}
