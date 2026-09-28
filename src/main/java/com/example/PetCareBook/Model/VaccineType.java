package com.example.PetCareBook.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "vaccine_types")
public class VaccineType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vaccineTypeId;

    @Column(nullable = false, length = 100)
    private String vaccineName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Integer intervalDays;


    @com.fasterxml.jackson.annotation.JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserAccount user;

    public UserAccount getUser() { return user; }
    public void setUser(UserAccount user) { this.user = user; }

    public VaccineType() {}

    public Long getVaccineTypeId() { return vaccineTypeId; }
    public void setVaccineTypeId(Long vaccineTypeId) { this.vaccineTypeId = vaccineTypeId; }
    public String getVaccineName() { return vaccineName; }
    public void setVaccineName(String vaccineName) { this.vaccineName = vaccineName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Integer getIntervalDays() { return intervalDays; }
    public void setIntervalDays(Integer intervalDays) { this.intervalDays = intervalDays; }
}
