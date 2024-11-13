package co.edu.uptc.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.Period;

@Getter
@Setter
public class PersonModel {
    private Long id;
    private String name;
    private String lastName;
    @JsonIgnore 
    private LocalDate birthday;
    private Genders gender;
    @JsonProperty("age")
    public int getAge() {
        if (birthday != null) {
            return Period.between(birthday, LocalDate.now()).getYears();
        }
        return 0;
    }

    public enum Genders {
        MALE,
        FEMALE
    }
}
