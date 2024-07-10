package opp.mic.cms.model;

public record ProductRequest(String name, String description, String features,CategoryRequest[] categories,AttributeRequest[] attributes,
                             double salePrice, Long quantity, double regularPrice, String vendorName, String vendorEmail, String vendorTelephone,
                             String vendorAddress, double purchasePrice, String invoiceId) {
}
