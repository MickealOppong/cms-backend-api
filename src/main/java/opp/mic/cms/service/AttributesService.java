package opp.mic.cms.service;

import opp.mic.cms.exceptions.AttributeNotFoundException;
import opp.mic.cms.exceptions.CategoryNotFoundException;
import opp.mic.cms.model.Attributes;
import opp.mic.cms.model.ProductAttributeRequest;
import opp.mic.cms.model.ProductSKU;
import opp.mic.cms.repository.AttributesRepository;
import opp.mic.cms.repository.ProductSKURepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AttributesService {

    private AttributesRepository attributesRepository;
    private ProductSKURepository productSKURepository;

    public AttributesService(AttributesRepository attributesRepository,
                             ProductSKURepository productSKURepository) {
        this.attributesRepository = attributesRepository;
        this.productSKURepository = productSKURepository;
    }

    public Page<Attributes> getAllAttributes(int page){
        PageRequest pr = PageRequest.of(page,5);
        Page<Attributes>  productAttributes = attributesRepository.findAll(pr);
        for(Attributes pa:productAttributes){
        pa.setProductSKU(productSKURepository.findAll().stream()
                .filter(f->f.getAttributes().getId().equals(pa.getId())).collect(Collectors.toList()));
         }
        return productAttributes;
    }
    public List<Attributes> getAllAttributesAsList(){
        List<Attributes>  productAttributes = attributesRepository.findAll();
        for(Attributes pa:productAttributes){
            pa.setProductSKU(productSKURepository.findAll().stream()
                    .filter(f->f.getAttributes().getId().equals(pa.getId())).toList());
        }
        return productAttributes;
    }
    @Transactional
    public Attributes save(ProductAttributeRequest productAttributeRequest){

        //check whether attribute already exist
        Optional<Attributes> savedAttribute = attributesRepository.findByName(productAttributeRequest.name());

        if(savedAttribute.isPresent()){
            //save only new value
            ProductSKU productSKU = new ProductSKU(savedAttribute.get(), productAttributeRequest.description(),
                    productAttributeRequest.value());
            productSKURepository.save(productSKU);
            return savedAttribute.get();
        }
        //attribute does not exist ,save new attribute and value
        Attributes attributes = new Attributes(productAttributeRequest.name());
        Attributes attribute= attributesRepository.save(attributes);
        ProductSKU productSKU = new ProductSKU(attribute,productAttributeRequest.description(),
                productAttributeRequest.value());
        productSKURepository.save(productSKU);
        return attribute;
    }

    public Attributes getById(Long id){
        Attributes attributes= attributesRepository.findById(id)
                .orElseThrow(()-> new AttributeNotFoundException("Attribute "+id +" does not exist"));
      List<ProductSKU> sku= productSKURepository.findAll().stream()
              .filter(a->a.getAttributes().getId().equals(attributes.getId())).collect(Collectors.toList());
      attributes.setProductSKU(sku);
      return attributes;
    }
    public Attributes getByName(String name){
        Attributes attributes= attributesRepository.findByName(name)
                .orElseThrow(()-> new AttributeNotFoundException("Attribute "+name+" does not exist"));
        List<ProductSKU> sku= productSKURepository.findAll().stream()
                .filter(a->a.getAttributes().getId().equals(attributes.getId())).collect(Collectors.toList());
        attributes.setProductSKU(sku);
        return attributes;
    }
    @Transactional
    public boolean deleteAttribute(Long id){
        Attributes attributes= attributesRepository.findById(id)
                .orElseThrow(()-> new CategoryNotFoundException("Category "+id +" does not exist"));
        List<ProductSKU> sku= productSKURepository.findAll().stream()
                .filter(a->a.getAttributes().getId().equals(attributes.getId())).toList();
            sku.forEach(p->productSKURepository.deleteById(p.getId()));
            attributesRepository.deleteById(id);
            return true;

    }

    public ProductSKU deleteSKU(Long id){
        productSKURepository.deleteById(id);
        return productSKURepository.findById(id).orElse(null);
    }

    public ProductSKU updateSKU(Long id,ProductAttributeRequest productAttributeRequest){
        Optional<ProductSKU> productSKU = productSKURepository.findById(id);
        ProductSKU updatedSKU = null;
        if(productSKU.isPresent()){
            ProductSKU sku = productSKU.get();
            sku.setSkuValue(productAttributeRequest.value());
            sku.setDescription(productAttributeRequest.description());
            updatedSKU = productSKURepository.save(productSKU.get());
        }
        return updatedSKU;
    }


    /*
    public Attributes updateProduct(String name, Product product){
      Attributes attributes =attributesRepository.findByName(name)
              .orElseThrow(()-> new AttributeNotFoundException(name+" does not exist"));
        attributes.getProducts().add(product);
      attributesRepository.save(attributes);
      return attributes;
    }


     */

    /*
    public List<Attributes> getAll(Long id){
        for(Attributes pa:productAttributes){
            pa.setProductSKU(productSKURepository.findAll().stream()
                    .filter(f->f.getAttributes().getId().equals(pa.getId())).toList());
        }
        return productAttributes;
    }


     */

}
