package opp.mic.cms.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAccess {

    private String model;
    private String  text;


    public static void main(String[] args) throws JsonProcessingException {
      String json = "[{\"model\":\"John\",\"text\":30},{\"model\":\"me\",\"text\":455}]";
        ObjectMapper objectMapper = new ObjectMapper();
        UserAccess[] k = objectMapper.readValue(json, UserAccess[].class);
        System.out.println(Arrays.toString(k));
    }
}


