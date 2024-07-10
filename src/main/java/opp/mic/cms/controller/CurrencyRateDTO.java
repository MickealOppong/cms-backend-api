package opp.mic.cms.controller;

import opp.mic.cms.model.CurrencyRate;

import java.util.List;

public record CurrencyRateDTO(List<CurrencyRate> fx,int totalPages,int size,int pageNumber) {
}
