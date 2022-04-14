package com.postgres.datadiff.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(
        name = "UploaderLock",
        uniqueConstraints =
        @UniqueConstraint(name = "UniqueRowCombo", columnNames = {"store", "saleDate"})
)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UploaderLock implements Serializable {

   public enum STATUS{ INIT,STARTED,COMPLETE};
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String store;
    private LocalDate saleDate;
    private STATUS status;
    private String performedBy;
     @Version
    private int version;
}
