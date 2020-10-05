package hr.leapwise.functionalprogramming.domain;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@Table(name = "db_value")
@Data
@EqualsAndHashCode
public class DbValue
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column (name = "name")
    private String name;

    @Column (name = "description")
    private String description;

    @Column (name = "created")
    private ZonedDateTime created;

    @Column (name = "expirationDate")
    private ZonedDateTime expirationDate;

    @Column (name = "createdBy")
    private String createdBy;

}

