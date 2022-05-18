package hellojpa;

import javax.persistence.*;

// TABLE_PER_CLASS 전략은 추천하지 않음

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS) // 상속 테이블 타입을 설정
@DiscriminatorColumn   //Dtype을 생성해줌, Single Table 전략에선 자동으로 생성됨, Table Per Class 전략에서는 작동하지 않음
public abstract class Item {

    @Id @GeneratedValue
    private Long id;

    private String name;
    private int price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
