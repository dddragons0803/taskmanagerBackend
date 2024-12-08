//package com.example.devansh.Taskmanager.config;
//
//import com.example.devansh.Taskmanager.entities.Task;
//import com.example.devansh.Taskmanager.payloads.TaskDto;
//import org.modelmapper.ModelMapper;
//import org.modelmapper.PropertyMap;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class ModelMapperConfig {
//    @Bean
//    public ModelMapper modelMapper() {
//        ModelMapper modelMapper = new ModelMapper();
//
//        // Custom mappings
//        modelMapper.addMappings(new PropertyMap<Task, TaskDto>() {
//            @Override
//            protected void configure() {
//                map(source.getAssignedTo().getFirstName() + " " + source.getAssignedTo().getLastName(), destination.getAssignedToName());
//            }
//        });
//
//        return modelMapper;
//    }
//}
