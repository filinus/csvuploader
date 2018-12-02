package us.filin.api.uploader.jpa;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@NoArgsConstructor
@Entity
@Data
@Table(
    name = "customer"
)
public class Customer {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "gen1")
    @GenericGenerator(name = "gen1", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, length = 36)
    private String id;

    private String customerName;
}
