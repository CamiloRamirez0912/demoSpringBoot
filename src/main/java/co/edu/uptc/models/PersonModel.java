package co.edu.uptc.models;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import co.edu.uptc.helpers.UtilDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonModel {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("lastName")
    private String lastName;
    @JsonProperty("age")
    private Integer age;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthday;
    @JsonProperty("gender")
    private Genders gender;

    public enum Genders {
        MALE,
        FEMALE
    }

    // Setter modificado para calcular la edad usando UtilDate
    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
        if (birthday != null) {
            this.age = UtilDate.calculateAge(birthday);
        }
    }
}