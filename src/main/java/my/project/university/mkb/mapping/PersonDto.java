package my.project.university.mkb.mapping;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonDto {
    private List<PhoneDto> phoneDtos;
    private String inn;


    private String forAdditional;
    private String forAdditional1;

    public List<PhoneDto> getPhoneDtos() {
        if (phoneDtos == null) {
            phoneDtos = new ArrayList<>();
        }
        return phoneDtos;
    }
}
