package my.project.university.mkb.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface PersonMapper {

    @Mapping(source = "phoneDtos", target = "listOfPhone")
    @Mapping(source = "inn", target = "INN")
    Person fromPersonDto(PersonDto personDto);

    Phone fromPhoneDto(PhoneDto phoneDto);

    default ListOfPhone toListOfPhone(List<PhoneDto> phoneDtos) {
        ListOfPhone listOfPhone = new ListOfPhone();
        List<Phone> collect = phoneDtos.stream().map(this::fromPhoneDto).collect(Collectors.toList());

        listOfPhone.getPhones().addAll(collect);
        return listOfPhone;
    }

    default Additional additional(PersonDto personDto) {
        Additional additional = new Additional();
        additional.setAdditional(personDto.getForAdditional());
        additional.setId(personDto.getForAdditional1());
        return additional;
    }

    default Person defaultMethod(PersonDto personDto){
        Person person =  fromPersonDto(personDto);
        person.setAdditional(additional(personDto));
        return person;
    }

}
