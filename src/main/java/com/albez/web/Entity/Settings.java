package com.albez.web.Entity;

import jakarta.persistence.*;


@Entity
@Table(name = "settings")
public class Settings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
    private Long id;

    @Column(name = "theme", nullable = false)
    private int theme;

    @Column(name = "dataView", nullable = false)
    private String dataView;

    @Column(name = "displayedComments", nullable = false)
    private int displayed_comments;


    @OneToOne(mappedBy = "setting", cascade = CascadeType.ALL)
    private Administrator administrator;

    public Settings(int theme, String dataView, int displayed_comments) {
        this.theme = theme;
        this.dataView = dataView;
        this.displayed_comments = displayed_comments;
    }

    public Settings() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getTheme() {
        return theme;
    }

    public void setTheme(int theme) {
        this.theme = theme;
    }

    public String getDataView() {
        return dataView;
    }

    public void setDataView(String dataView) {
        this.dataView = dataView;
    }

    public int getDisplayed_comments() {
        return displayed_comments;
    }

    public void setDisplayed_comments(int displayed_comments) {
        this.displayed_comments = displayed_comments;
    }

    public Administrator getAdministrator() {
        return administrator;
    }

    public void setAdministrator(Administrator administrator) {
        this.administrator = administrator;
    }

    @Override
    public String toString() {
        return "\nSettings{" +
                "id=" + id +
                ", theme=" + theme +
                ", dataView='" + dataView + '\'' +
                ", displayed_comments=" + displayed_comments +
                ", administrator=" + administrator +
                '}';
    }
}
