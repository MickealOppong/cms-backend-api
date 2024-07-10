package opp.mic.cms.model;

import lombok.Data;

@Data
public class SearchParameters {

    private String name;
    private Integer page;
    private Integer pageSize;

    public SearchParameters(){
        this("",0,10);
    }


    public SearchParameters(String name) {
        this(name,0,10);
        this.name = name;

    }

    public SearchParameters(String name, Integer page) {
        this(name,page,10);
        this.name = name;
        this.page = page;
    }

    public SearchParameters(String name, Integer page, Integer pageSize) {
        this.name = name;
        this.page = page==null?0:page;
        this.pageSize = pageSize==null?10:pageSize;
    }
}
