package hr.leapwise.functionalprogramming.domain;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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

