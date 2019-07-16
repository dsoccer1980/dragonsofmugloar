package com.dsoccer1980.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ad_solution")
public class AdSolution {


    @Id
    private String adId;
    private Integer success;
    private Integer fail;

}
