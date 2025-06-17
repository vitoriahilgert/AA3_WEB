package br.ufscar.dc.dsw.AA2.models;

import com.fasterxml.jackson.annotation.JsonSetter;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "strategies")
public class Strategy {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String examples;

    @Column(nullable = false)
    private String tips;

    @OneToMany(mappedBy = "strategies", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    @OneToMany(mappedBy = "strategies", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TestSession> testSessions = new ArrayList<>();

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setExamples(String examples) {
        this.examples = examples;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }
}
