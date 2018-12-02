package us.filin.api.uploader.jpa;

import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.UUID;

@NoArgsConstructor
@Entity
@Data
@Table(
    name = "contact",
    indexes = {
            @Index(name = "owner", columnList = "owner", unique = false),
            @Index(name = "uuid", columnList = "uuid", unique = false),
            @Index(name = "joined_date", columnList = "joined_date", unique = false),
            @Index(name = "name", columnList = "last_name,first_name,middle_initial", unique = false)
    }
)
public class Contact {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "gen1")
    @GenericGenerator(name = "gen1", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, length = 36)
    private String id;

    @Column(name = "owner", length = 64)
    private String owner;

    @Column(name = "first_name", length = 64)
    private String firstName;
    @Column(name = "last_name", length = 64)
    private String lastName;
    @Column(name = "middle_initial", length = 1)
    private String middleInitial;
    @Column(name = "email")
    private String email;
    @Column(name = "street_address")
    private String streetAddress;
    @Column(name = "zip_code")
    private Short zipCode;
    @Column(name = "joined_date")
    private Date joinedDate;
    @Column(name = "uuid", length = 36)
    private UUID uuid;


}
