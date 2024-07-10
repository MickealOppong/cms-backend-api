package opp.mic.cms.model;

public record ProductUpdateRequest(String name, String description, String features,AttributeUpateRequest[] attributes,
                             double salePrice, Long quantity, double regularPrice) {
}

