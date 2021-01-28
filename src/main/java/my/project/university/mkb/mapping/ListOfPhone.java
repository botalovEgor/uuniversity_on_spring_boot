package my.project.university.mkb.mapping;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListOfPhone {
    private List<Phone> phones;

    public List<Phone> getPhones() {
        if (phones == null) {
            phones = new ArrayList<>();
        }
        return phones;

    }
}
