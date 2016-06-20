package com.lukaszbaran.starter.api;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PRZEWOZNIK")
public class Camera {

    @Id
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "NAME", nullable = false, length = 45)
    private String name;

    @Column(name = "DIRECTORY", nullable = false, length = 255)
    private String directory;

}
