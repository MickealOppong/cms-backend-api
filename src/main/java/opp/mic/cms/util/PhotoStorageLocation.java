package opp.mic.cms.util;

import org.springframework.stereotype.Component;

@Component
public class PhotoStorageLocation {

    private String location="users-photo";

    public String getLocation(){
        return this.location;
    }
}
