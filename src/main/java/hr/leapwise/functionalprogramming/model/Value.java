package hr.leapwise.functionalprogramming.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@Builder
public class Value
{

    private String        name;
    private String        description;
    private ZonedDateTime created;
    private ZonedDateTime expirationDate;
    private String        createdBy;
}
