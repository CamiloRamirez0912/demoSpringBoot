package co.edu.uptc.dtos;


import java.time.LocalDate;
import java.time.Period;

import co.edu.uptc.models.PersonModel;
import co.edu.uptc.models.PersonModel.Genders;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonDto {
   private Long id;
   private String name;
   private String lastName;
   private int age;
   private Genders gender;

   public static PersonDto toPersonDto(PersonModel person) {
      PersonDto personDto = new PersonDto();
      personDto.setName(person.getName());
      personDto.setLastName(person.getLastName());
      personDto.setId(person.getId());
      personDto.setGender(person.getGender());
      personDto.setAge(Period.between(person.getBirthday(), LocalDate.now()).getYears());
      return personDto;
   }
}
