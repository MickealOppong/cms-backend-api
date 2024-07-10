package opp.mic.cms.model;

import java.util.List;
import java.util.Map;

public record SingleProductDTO(Product product, Map<String, List<ProductVariation>> attributes) {
}
