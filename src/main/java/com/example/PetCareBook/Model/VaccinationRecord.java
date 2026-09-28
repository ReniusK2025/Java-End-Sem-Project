package com.example.PetCareBook.Model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "vaccination_records")
public class VaccinationRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vaccinationId;

    @Column(nullable = false)
    private LocalDate vaccinationDate;

    @Column(nullable = false)
    private LocalDate nextDueDate;

    @Column(length = 255)
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vaccine_type_id", nullable = false)
    private VaccineType vaccineType;

    public VaccinationRecord() {}

    public Long getVaccinationId() { return vaccinationId; }
    public void setVaccinationId(Long vaccinationId) { this.vaccinationId = vaccinationId; }
    public LocalDate getVaccinationDate() { return vaccinationDate; }
    public void setVaccinationDate(LocalDate vaccinationDate) { this.vaccinationDate = vaccinationDate; }
    public LocalDate getNextDueDate() { return nextDueDate; }
    public void setNextDueDate(LocalDate nextDueDate) { this.nextDueDate = nextDueDate; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public Pet getPet() { return pet; }
    public void setPet(Pet pet) { this.pet = pet; }
    public VaccineType getVaccineType() { return vaccineType; }
    public void setVaccineType(VaccineType vaccineType) { this.vaccineType = vaccineType; }
}
