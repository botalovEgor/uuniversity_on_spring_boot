package my.project.university.mkb.mapping;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import my.project.university.mkb.mapping.ListOfPhone;



@AllArgsConstructor
@NoArgsConstructor
public class Person {
    private String inn;
    private ListOfPhone listOfPhone;

    private Additional additional;

    public String getInn() {
        return inn;
    }

    public void setINN(String inn) {
        this.inn = inn;
    }

    public ListOfPhone getListOfPhone() {
        return listOfPhone;
    }

    public void setListOfPhone(ListOfPhone listOfPhone) {
        this.listOfPhone = listOfPhone;
    }

    @Override
    public String toString() {
        return "Person{" +
                "inn='" + inn + '\'' +
                ", listOfPhone=" + listOfPhone +
                ", additional=" + additional +
                '}';
    }

    public Additional getAdditional() {
        return additional;
    }

    public void setAdditional(Additional additional) {
        this.additional = additional;
    }
}
