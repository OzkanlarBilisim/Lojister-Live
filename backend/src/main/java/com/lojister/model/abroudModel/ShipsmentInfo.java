package com.lojister.model.abroudModel;

import javax.persistence.*;

@Entity
public class ShipsmentInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;
    private String plate;
    private String trailerPlate;
    private String name;
    private String tc;
    private String tel;
    private String awb;
    private String airlane;
    private String orginAirport;
    private String destination;
    private String ambar;
    private String bayrak;
    private String opsiyon;
    private String iskele;
    private String booking;
    private String seal;
    private String cotainer;
    @ManyToOne
    private AdAbroud adAbroud;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getTrailerPlate() {
        return trailerPlate;
    }

    public void setTrailerPlate(String trailerPlate) {
        this.trailerPlate = trailerPlate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTc() {
        return tc;
    }

    public void setTc(String tc) {
        this.tc = tc;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAwb() {
        return awb;
    }

    public void setAwb(String awb) {
        this.awb = awb;
    }

    public String getAirlane() {
        return airlane;
    }

    public void setAirlane(String airlane) {
        this.airlane = airlane;
    }

    public String getOrginAirport() {
        return orginAirport;
    }

    public void setOrginAirport(String orginAirport) {
        this.orginAirport = orginAirport;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getAmbar() {
        return ambar;
    }

    public void setAmbar(String ambar) {
        this.ambar = ambar;
    }

    public String getBayrak() {
        return bayrak;
    }

    public void setBayrak(String bayrak) {
        this.bayrak = bayrak;
    }

    public String getOpsiyon() {
        return opsiyon;
    }

    public void setOpsiyon(String opsiyon) {
        this.opsiyon = opsiyon;
    }

    public String getIskele() {
        return iskele;
    }

    public void setIskele(String iskele) {
        this.iskele = iskele;
    }

    public String getBooking() {
        return booking;
    }

    public void setBooking(String booking) {
        this.booking = booking;
    }

    public String getSeal() {
        return seal;
    }

    public void setSeal(String seal) {
        this.seal = seal;
    }

    public String getCotainer() {
        return cotainer;
    }

    public void setCotainer(String cotainer) {
        this.cotainer = cotainer;
    }

    public AdAbroud getAdAbroud() {
        return adAbroud;
    }

    public void setAdAbroud(AdAbroud adAbroud) {
        this.adAbroud = adAbroud;
    }
}
