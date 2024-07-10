package opp.mic.cms.model;

import lombok.Builder;

import java.util.List;

@Builder
public record ProductsPaginationResponse(List<Product> products,int PageCount,int page,Long size,int pageSize) {
}
