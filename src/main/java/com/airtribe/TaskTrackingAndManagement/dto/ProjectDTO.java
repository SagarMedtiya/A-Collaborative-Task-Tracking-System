package com.airtribe.TaskTrackingAndManagement.dto;

import com.airtribe.TaskTrackingAndManagement.entity.Project;
import com.airtribe.TaskTrackingAndManagement.entity.User;
import lombok.*;

import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProjectDTO{
   private Long id;
   private String name;
   private String description;
   private Set<String> members;
   private User owner;
   public ProjectDTO(Project project){
       this.id = project.getId();
       this.name = project.getName();
       this.description = project.getDescription();
       this.members = project.getMembers()
               .stream()
               .map(User::getUsername)
               .collect(Collectors.toSet());
   }
}
