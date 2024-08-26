package com.Ashish.ProjectManagementSystem.modal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jdk.dynalink.linker.LinkerServices;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Entity
@Data
public class Issue {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;
    private String description ;
    private String status;
    private String projectId;
    private String priority;
    private String dueDate;
    private List<String> tags = new ArrayList<>();




    @ManyToOne
    private User assignee ;

    @ManyToOne
    @JsonIgnore
    private Project project;


    @JsonIgnore
    @OneToMany(mappedBy = "issue" ,cascade = CascadeType.ALL , orphanRemoval = true)
    private  List<Comments> comments = new ArrayList<>();


}
